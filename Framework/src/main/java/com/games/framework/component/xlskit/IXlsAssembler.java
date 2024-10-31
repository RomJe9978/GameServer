package com.games.framework.component.xlskit;

/**
 * 基于Excel配置表原始配置数据的二次组装接口。
 *
 * <p> 当前类可以对原始配置数据进行组装等预处理，避免业务层重复处理,也可以将一些
 * 耗时，耗性能的操作提前进行静态处理，避免出现在业务层面等等，实现类做统一管理
 *
 * <pre>
 *     1.接口顺序{@link #assemble()}{@link #afterAssemble()}{@link #check()}
 *     2.该接口下的所有实现类，不允许外部进行任何实例化，这一项最好有检测
 * </pre>
 *
 * @author liu xuan jie
 */
public interface IXlsAssembler {

    /**
     * @return 当前组装加工类所对应的Excel表名称，不包含后缀
     */
    String xlsName();

    /**
     * 对指定一个Excel表原始数据进行二次加工
     *
     * <p>该接口的调用时机，保证了此时所有Excel原始数据已经全部加载到内存中了
     * <p>该接口仅针对一张配置表自己内部的数据二次加工，封装和组装
     * <p>该接口内部可以在组装数据的同时进行一些配置表自身内部数据的基础规则检查
     * <p>该接口内可以使用其他配置表的初始数据，但是不可以使用其他配置表的二次加工数据
     *
     * @return 任何失败和异常，返回{@code false}
     */
    boolean assemble();

    /**
     * 当前Excel表如果关联了其他配置表，可以在该接口做多表关联数据的二次加工和封装
     *
     * <p>如果有需要组装使用其他配置表“二次加工”后的数据，在此接口实现即可
     * <p>该接口的调用时机，保证了在所有配置表都进行第一次{@link #assemble()}之后进行
     *
     * @return 任何失败和异常，返回{@code false}
     */
    boolean afterAssemble();

    /**
     * 对当前Excel配置表数据的所有规则的检查，越详细越好
     *
     * <p>该接口内可以使用所有配置表的“初始”和“二次加工”数据
     * <p>该接口的调用时机，保证了所有配置表的“assemble”和“afterAssemble”都已经完成
     *
     * @return 任何失败和异常，返回{@code false}
     */
    boolean check();
}