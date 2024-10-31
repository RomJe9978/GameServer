package com.games.xls.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.games.framework.component.xlskit.AbstractXlsManager;
import com.games.xls.bean.XlsTest;

/**
 * 该类自动生成，不可编辑，每次生成会覆盖
 */
public final class XlsTestManager extends AbstractXlsManager<XlsTest> {

    private static final XlsTestManager INSTANCE = new XlsTestManager();

    private XlsTestManager() {
    }

    public static XlsTestManager getInstance() {
        return INSTANCE;
    }

    @Override
    public String xlsName() {
        return "Test";
    }

    @Override
    public List<XlsTest> parseFrom(String jsonText) {
        JSONArray jsonArray = JSONArray.parseArray(jsonText);
        if (Objects.isNull(jsonArray) || jsonArray.isEmpty()) {
            return Collections.emptyList();
        }

        List<XlsTest> resultList = new ArrayList<>(jsonArray.size());
        for (int i = 0, iSize = jsonArray.size(); i < iSize; i++) {
            JSONObject single = jsonArray.getJSONObject(i);
            XlsTest newInstance = XlsTest.of(single);
            resultList.add(newInstance);
        }
        return resultList;
    }

}
