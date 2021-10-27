package com.shura.im.common.data.contruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Garvey
 * @Created: 2021/10/26
 * @Description: 环队列，可用于延迟任务
 */
public final class RingBufferWheel {

    private static final Logger LOGGER = LoggerFactory.getLogger(RingBufferWheel.class);

    /**
     * 默认大小
     */
    private static final int DEFAULT_SIZE = 64;

    private Object[] ringBuffer;

    private int bufferSize;

    /**
     * 业务线程池
     */
    private ExecutorService executorService;

    private volatile int size = 0;

    /**
     * 任务终止标识
     */
    private volatile boolean stop = false;

    /**
     * 任务开始标识
     */
    private volatile AtomicBoolean start = new AtomicBoolean(false);

    /**
     *
     */
    private AtomicInteger tick = new AtomicInteger();

    private AtomicInteger taskId = new AtomicInteger();

    private Map<Integer, Task> taskMap = new ConcurrentHashMap<>(16);

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public RingBufferWheel(ExecutorService executorService) {
        this.executorService = executorService;
        this.bufferSize = DEFAULT_SIZE;
        this.ringBuffer = new Object[bufferSize];
    }

    public RingBufferWheel(ExecutorService executorService, int bufferSize) {
        this.executorService = executorService;

        if (!powerOf2(bufferSize)) {
            throw new RuntimeException("bufferSize=[" + bufferSize + "] must be a power of 2.");
        }

        this.bufferSize = bufferSize;
        this.ringBuffer = new Object[bufferSize];
    }

    /**
     * 添加任务到环队列
     * @param task
     * @return
     */
    public int addTask(Task task) {
        int key = task.getKey();
        int id;

        try {
            lock.lock();
            int index = mod(key, bufferSize);
            task.setIndex(index);
            Set<Task> taskSet = get(index);

            int cycleNum = cycleNum(key, bufferSize);
            task.setCycleNum(cycleNum);
            if (taskSet != null) {
                taskSet.add(task);
            } else {
                Set<Task> sets = new HashSet<>();
                sets.add(task);
                put(key, sets);
            }

            id = taskId.incrementAndGet();
            task.setTaskId(id);
            taskMap.put(id, task);
            size++;
        } finally {
            lock.unlock();
        }

        start();
        return id;
    }

    /**
     * 取消任务
     * @param id 任务 id
     * @return
     */
    public boolean cancel(int id) {
        boolean flag = false;
        Set<Task> tempTaskSet = new HashSet<>();

        try {
            lock.lock();

            Task task = taskMap.get(id);
            if (task == null) {
                return false;
            }

            Set<Task> taskSet = get(task.getIndex());
            for (Task item : taskSet) {
                if (item.getKey() == task.getKey() && item.getCycleNum() == task.getCycleNum()) {
                    size--;
                    flag = true;
                    taskMap.remove(id);
                } else {
                    tempTaskSet.add(task);
                }
            }

            ringBuffer[task.getIndex()] = tempTaskSet;
        } finally {
            lock.unlock();
        }

        return flag;
    }

    /**
     * 返回任务队列大小，线程安全
     * @return
     */
    public int taskSize() {
        return size;
    }

    public int taskMapSize() {
        return taskMap.size();
    }

    /**
     * 启动后台线程，充当消费者定时器，会一直运行直到方法被调用
     */
    public void start() {
        if (!start.get()) {
            if (start.compareAndSet(start.get(), true)) {
                LOGGER.info("Delay task is starting.");
                Thread job = new Thread(new TriggerJob());
                job.setName("Consumer RingBuffer thread");
                job.start();
                start.set(true);
            }
        }
    }

    /**
     * 终止消费者线程
     * @param force true 将强制关闭消费者线程并丢弃所有挂起的任务，
     *              否则消费者线程在关闭之前等待所有任务完成。
     */
    public void stop(boolean force) {
        if (force) {
            LOGGER.info("Delay task is forced stop.");
            stop = true;
            executorService.shutdownNow();
        } else {
            LOGGER.info("Delay task is stopping.");
            if (taskSize() > 0) {
                try {
                    lock.lock();
                    condition.wait();
                    stop = true;
                } catch (InterruptedException e) {
                    LOGGER.error("InterruptedException", e);
                } finally {
                    lock.unlock();
                }
            }
            executorService.shutdown();
        }
    }

    private Set<Task> get(int index) {
        return (Set<Task>) ringBuffer[index];
    }

    private void put(int key, Set<Task> tasks) {
        int index = mod(key, bufferSize);
        ringBuffer[index] = tasks;
    }

    private Set<Task> remove(int key) {
        Set<Task> tempTask = new HashSet<>();
        Set<Task> result = new HashSet<>();

        Set<Task> taskSet = (Set<Task>) ringBuffer[key];
        if (taskSet == null) {
            return result;
        }

        for (Task task : taskSet) {
            if (task.getCycleNum() == 0) {
                result.add(task);

                sizeToNotify();
            } else {
                task.setCycleNum(task.getCycleNum() - 1);
                tempTask.add(task);
            }
            // 移除 task，释放内存
            taskMap.remove(task.getTaskId());
        }

        return result;
    }

    private void sizeToNotify() {
        try {
            lock.lock();
            size--;
            if (size == 0) {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean powerOf2(int target) {
        if (target < 0) {
            return false;
        }

        int value = target & (target - 1);
        return value == 0;
    }

    private int mod(int target, int mod) {
        // 等价于 target % mod
        target = target + tick.get();
        return target & (mod - 1);
    }

    private int cycleNum(int target, int mod) {
        // 等价于 target / mode
        return target >> Integer.bitCount(mod - 1);
    }

    public abstract static class Task extends Thread {
        private int index;
        private int cycleNum;
        private int key;

        /**
         * 任务唯一 id
         */
        private int taskId;

        @Override
        public void run() {
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getCycleNum() {
            return cycleNum;
        }

        public void setCycleNum(int cycleNum) {
            this.cycleNum = cycleNum;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }
    }

    private class TriggerJob implements Runnable {
        @Override
        public void run() {
            int index = 0;
            while (!stop) {
                try {
                    Set<Task> tasks = remove(index);
                    for (Task task : tasks) {
                        executorService.submit(task);
                    }

                    if (++index > bufferSize - 1) {
                        index = 0;
                    }

                    tick.incrementAndGet();
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    LOGGER.error("exception", e);
                }
            }

            LOGGER.info("Delay task has stopped");
        }
    }
}
