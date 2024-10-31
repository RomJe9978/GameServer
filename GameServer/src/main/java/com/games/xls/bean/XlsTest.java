package com.games.xls.bean;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import com.games.framework.component.xlskit.AbstractXlsBean;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

/**
 * 该类自动生成，不可编辑，每次生成会覆盖
 */
@Getter
public final class XlsTest extends AbstractXlsBean {

    /**
     * 主键
     */
    private final int id;

    /**
     * 年龄
     */
    private final int age;

    /**
     * 成绩
     */
    private final int[] score;

    /**
     * 是否是男孩
     */
    private final boolean isBoy;

    /**
     * 是否及格
     */
    private final boolean[] isPass;


    private XlsTest(int id, int age, int[] score, boolean isBoy, boolean[] isPass) {
        this.id = id;
        this.age = age;
        this.score = score;
        this.isBoy = isBoy;
        this.isPass = isPass;
    }

    public static XlsTest of(JSONObject jsonObject) {
        return new XlsTest(
                jsonObject.getIntValue("id"),
                jsonObject.getIntValue("age"),
                jsonObject.getObject("score", int[].class),
                jsonObject.getBooleanValue("isBoy"),
                jsonObject.getObject("isPass", boolean[].class)
        );
    }
}
