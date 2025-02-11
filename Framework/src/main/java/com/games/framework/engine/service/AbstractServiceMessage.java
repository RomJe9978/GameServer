package com.games.framework.engine.service;

import lombok.Getter;
import lombok.Setter;

/**
 * Service之间的通讯的消息封装
 *
 * @author liu xuan jie
 */
@Getter
public abstract class AbstractServiceMessage {

    /**
     * 消息标识，代表一种消息，不是对象实例标识
     * 建议业务层使用常量类或者枚举类进行统一管理，维护
     */
    private final int messageMark;

    /**
     * 消息发送者
     */
    @Setter
    private AbstractService source;

    /**
     * 消息接收者
     */
    @Setter
    private AbstractService target;

    public AbstractServiceMessage(int messageMark) {
        this.messageMark = messageMark;
    }
}