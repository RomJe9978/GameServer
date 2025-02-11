package com.games.framework.component.protoparse;

import com.google.protobuf.GeneratedMessageV3;

/**
 * 协议转化器：封装协议相关的装换工作
 *
 * @author liu xuan jie
 */
public interface IProtoParser {

    /**
     * 根据“协议Id”将指定对应的协议“序列化字节流”转换成对应协议
     *
     * @param packetId 协议Id
     * @param bytes    协议Id对应的协议“序列化流”，不允许为{@code null}
     * @return 不会为{@code null}，对应最终生成的协议数据
     */
    GeneratedMessageV3 parseFrom(int packetId, byte[] bytes);
}