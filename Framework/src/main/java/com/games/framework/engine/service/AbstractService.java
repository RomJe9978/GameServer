package com.games.framework.engine.service;

import com.games.framework.component.messagekit.MessageDispatcher;
import com.games.framework.engine.context.FrameworkContext;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 关于service最底层的抽象
 *
 * @author liu xuan jie
 */
public abstract class AbstractService {

    @Getter
    protected final long serviceId;

    /**
     * service上的异步消息队列(多生单消模型)
     */
    private ConcurrentLinkedQueue<AbstractServiceMessage> messageQueue;

    public AbstractService(int serviceId) {
        this.serviceId = serviceId;
    }

    public abstract boolean init();

    /**
     * 驱动Service自身的相关处理
     *
     * @param millisTimestamp 本次驱动帧的时间戳：毫秒
     */
    public void tick(long millisTimestamp) {
        this.tickMessage();
        this.tickLogic(millisTimestamp);
    }

    /**
     * 驱动当前Service的消息处理
     */
    private void tickMessage() {
        int handleCount = FrameworkContext.INSTANCE.getServiceHandleMessageCountPreTick();
        for (int i = 0; i < handleCount; i++) {
            AbstractServiceMessage serviceMessage = messageQueue.poll();
            if (Objects.isNull(serviceMessage)) {
                break;
            }

            // 派发给指定的消息处理方法即可，当前线程驱动
            int messageMark = serviceMessage.getMessageMark();
            MessageDispatcher.INSTANCE.dispatch(messageMark, this, serviceMessage);
        }
    }

    /**
     * 驱动当前Service的逻辑处理
     *
     * @param millisTimestamp 本次驱动帧的时间戳：毫秒
     */
    public abstract void tickLogic(long millisTimestamp);

    /**
     * 向其他Service发送消息
     *
     * @param targetService 不允许为{@code null}
     * @param message       不允许为{@code null}
     */
    public void sendMessage(@NonNull AbstractService targetService, @NonNull AbstractServiceMessage message) {
        message.setSource(this);
        message.setTarget(targetService);
        targetService.receivedMessage(this, message);
    }

    /**
     * 当前Service接收消息
     *
     * @param sourceService 消息发送者，可以为{@code null}
     * @param message       不允许为{@code null}
     */
    public void receivedMessage(AbstractService sourceService, @NonNull AbstractServiceMessage message) {
        message.setSource(sourceService);
        message.setTarget(this);
        this.messageQueue.offer(message);
    }
}