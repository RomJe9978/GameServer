package com.games.business.game.scene.actor;

/**
 * 游戏内玩家逻辑实体
 * <pre>
 *     统一命名（以下命名以背包模块为例，所有其他模块强制统一命名）:
 *     1、{@code Actor}：唯一代表游戏内的玩家对象
 *     2、{@code Actor...Module}：玩家具体业务逻辑模块（ActorBagModule）
 *     3、{@code Actor...PacketHandler}：玩家具体业务针对客户端网络消息的处理类（ActorBagPacketHandler）
 *     4、{@code Actor...MessageHandler}：玩家具体业务针对服务器内部异步通讯消息处理类（ActorBagPacketHandler）
 *     5、{@code Actor...EventHandler}：玩家具体业务针对当前上下文其他模块的事件处理类（ActorBagEventHandler）
 * </pre>
 *
 * @author liu xuan jie
 */
public class Actor {

}