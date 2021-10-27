package com.shura.im.gateway.kit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
public class NetAddressIsReachable {

    /**
     * 校验 ip 地址和端口是否可达
     *
     * @param address
     * @param port
     * @param timeout
     * @return True if connection successful
     */
    public static boolean checkAddressReachable(String address, int port, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, port), timeout);
            return true;
        } catch (IOException exception) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                return false;
            }
        }
    }
}
