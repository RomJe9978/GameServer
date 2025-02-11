package com.games.framework.engine.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

/**
 * 任务处理器，管理所有线程任务的添加，提交，删除等操作
 * todo:这里的所有思路之后进行整理重构
 *
 * @author liuxuanjie
 */
public class TaskProcessor {

    /**
     * 待添加的任务队列(多生单消模型)
     */
    private ConcurrentLinkedQueue<EngineTask> preAddTaskQueue;

    /**
     * 当前服务器帧中的所有任务列表(单线程模型，仅当前类内部操作)
     */
    private List<EngineTask> allTaskList;

    /**
     * 正在处理中的任务列表(单线程模型，仅当前类内部操作)
     * 与{@link #futureList}位置一一对应，相当于两个list实现一个map
     */
    private List<EngineTask> handlingTaskList;

    /**
     * 任务执行结果(单线程模型，仅当前类内部操作)
     */
    private List<Future<?>> futureList;

    /**
     * 任务执行者(解耦，自由适配)
     */
    private ITaskExecutor<EngineTask> taskExecutor;


    private TaskProcessor() {
    }


    public TaskProcessor init(ITaskExecutor<EngineTask> taskExecutor) {
        this.preAddTaskQueue = new ConcurrentLinkedQueue<>();
        this.allTaskList = new ArrayList<>();
        this.handlingTaskList = new ArrayList<>();
        this.futureList = new ArrayList<>();
        this.taskExecutor = taskExecutor;
        return this;
    }


    /**
     * 任务处理器的处理帧
     *
     * @param interval 距离上次执行的时间间隔：ms
     */
    public void tick(int interval) {
        // 注意一定是下一帧的最开始判断上一帧是否执行完，不要同一帧判断
        this.tickFutures();

        this.tickPreAddQueue();
        this.tickTasks();
    }


    /**
     * 对外唯一添加接口（异步操作，下一帧才会真正添加成功）
     *
     * @param engineTask 任务
     */
    public void addEngineTask(EngineTask engineTask) {
        if (Objects.nonNull(engineTask)) {
            this.preAddTaskQueue.offer(engineTask);
        }
    }


    private void tickPreAddQueue() {
        while (true) {
            EngineTask engineTask = this.preAddTaskQueue.poll();
            if (Objects.isNull(engineTask)) {
                return;
            }

            this.allTaskList.add(engineTask);
        }
    }


    private void tickTasks() {
        long curTimestamp = System.currentTimeMillis();
        for (int i = 0, iSize = this.allTaskList.size(); i < iSize; i++) {
            if (this.taskExecutor.canSubmit()) {
                EngineTask engineTask = this.allTaskList.get(i);
                if (this.handlingTaskList.contains(engineTask)) {
                    continue;
                }

                if (engineTask.requireExe(curTimestamp)) {
                    Future<?> future = this.taskExecutor.submit(engineTask);
                    this.handlingTaskList.add(engineTask);
                    this.futureList.add(future);
                }
            }
        }
    }


    private void tickFutures() {
        // 涉及遍历删除，倒序
        for (int index = this.futureList.size() - 1; index >= 0; index--) {
            Future<?> future = this.futureList.get(index);
            if (future.isDone()) {
                this.handlingTaskList.remove(index);
                this.futureList.remove(index);
                this.taskExecutor.handleFuture(future);
            }
        }
    }


    static class SingletonHolder {

        static final TaskProcessor instance = new TaskProcessor();
    }


    public static TaskProcessor getInstance() {
        return SingletonHolder.instance;
    }
}
