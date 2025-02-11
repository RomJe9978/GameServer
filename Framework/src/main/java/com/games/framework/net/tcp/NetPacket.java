package com.games.framework.net.tcp;

import lombok.Getter;
import lombok.Setter;

/**
 * 网络通讯消息包
 *
 * @author liu xuan jie
 */
@Getter
@Setter
public class NetPacket {

    /**
     * 消息协议标识，不是实例对象id
     */
    private int id;

    /**
     * 消息具体内容
     */
    private byte[] body;

    public static NetPacket of(int id, byte[] body) {
        NetPacket netPacket = new NetPacket();
        netPacket.id = id;
        netPacket.body = body;
        return netPacket;
    }
}