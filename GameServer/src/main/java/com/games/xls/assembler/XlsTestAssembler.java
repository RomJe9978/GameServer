package com.games.xls.assembler;

import com.games.framework.component.xlskit.IXlsAssembler;

/**
 * 该类自动生成，但只会生成一次，可以编写逻辑
 */
public final class XlsTestAssembler implements IXlsAssembler {

    private static final XlsTestAssembler INSTANCE = new XlsTestAssembler();

    private XlsTestAssembler() {
    }

    public static XlsTestAssembler getInstance() {
        return INSTANCE;
    }

    @Override
    public String xlsName() {
        return "Test";
    }

    @Override
    public boolean assemble() {
        return true;
    }

    @Override
    public boolean afterAssemble() {
        return true;
    }

    @Override
    public boolean check() {
        return true;
    }

}
