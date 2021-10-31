package com.shura.im.client.component;

import com.shura.im.client.service.InnerCommand;
import com.shura.im.client.service.impl.command.PrintAllCommand;
import com.shura.im.client.util.SpringBeanFactory;
import com.shura.im.common.enums.SystemCommandEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
@Component
public class InnerCommandContext {

    private final static Logger LOGGER = LoggerFactory.getLogger(InnerCommandContext.class);

    /**
     * 获取执行器实例
     *
     * @param command 执行器实例
     * @return
     */
    public InnerCommand getInstance(String command) {

        Map<String, String> allClazz = SystemCommandEnum.getAllClazz();

        //兼容需要命令后接参数的数据 :q shura
        String[] trim = command.trim().split(" ");
        String clazz = allClazz.get(trim[0]);
        InnerCommand innerCommand = null;
        try {
            if (StringUtils.isBlank(clazz)) {
                clazz = PrintAllCommand.class.getName();
            }
            innerCommand = (InnerCommand) SpringBeanFactory.getBean(Class.forName(clazz));
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }

        return innerCommand;
    }
}
