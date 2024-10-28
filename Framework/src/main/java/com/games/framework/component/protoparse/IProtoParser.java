package com.games.framework.component.protoparse;

import com.google.protobuf.GeneratedMessageV3;

/**
 * @author liu xuan jie
 */
public interface IProtoParser {

    GeneratedMessageV3 parseFrom(int packetId, byte[] bytes);
}