/**
 * <p>
 *
 * @(#)HasRepeatEnumTest.java, 07月18, 2024.
 * <p>
 * Copyright 2024 yuanfudao.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * </p>
 */
package component.checker;

/**
 * @author liuxuanjie@kanyun.com
 * <p>
 * Description
 */
public enum HasRepeatEnumTest {

    ONE(1),

    TWO(2),

    THREE(2);

    private final int code;

    HasRepeatEnumTest(int code) {
        this.code = code;
    }
}
