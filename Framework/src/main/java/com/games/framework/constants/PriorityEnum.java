package com.games.framework.constants;

import lombok.Getter;

/**
 * 统一优先级管理，防止业务层不可控，肆意泛滥
 *
 * @author liu xuan jie
 */
@Getter
public enum PriorityEnum {

    HIGHEST(-1, "highest"),

    COMMON(0, "common"),

    LOWEST(1, "lowest"),
    ;

    /**
     * 优先级数值，数值越小，代表优先级越高
     */
    private final int priority;

    /**
     * 优先级描述
     */
    private final String describe;

    PriorityEnum(int priority, String describe) {
        this.priority = priority;
        this.describe = describe;
    }
}