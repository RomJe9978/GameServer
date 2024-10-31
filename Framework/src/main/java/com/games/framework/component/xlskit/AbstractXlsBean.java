package com.games.framework.component.xlskit;

/**
 * 所有由Excel表映射的JavaBean的统一父类
 *
 * <p> 之所以做这样一个父类的，是因为Excel配置表映射的JavaBean一般都是通过各种
 * 各样的脚本自动生成的，统一父类可以作为一个标识，增强可维护性，也便于统一管理。
 *
 * <p> 这层继承关系可能永远用不到，但是希望用到的时候是有的，并且增加这么一层继承关系
 * 并不会有什么弊端。处理继承类以外，还可以做成接口，或者做成注解等等。只是此处选择了
 * 继承的方式而已。
 *
 * <p> 额外说明一下，由于目前游戏策划的配置大多通过Excel的方式进行，所以这里约定统一
 * “Xls”的前缀代表所有Excel相关的JavaBean，不选择类似“Config”,“Data”这样的单词作为前
 * 缀或后缀的原因是：这些词对于开发者已经都有了更常见的含义，希望做到“望文知意”。
 *
 * <p> 使用场景举例，某些情况下，需要扫包获取所有Excel映射的JavaBean，可以用该类做区分。
 * <p> 使用场景举例，有些情况下，需要指定泛型为Excel映射的JavaBean，可以用该类去做限制。
 *
 * @author liu xuan jie
 */
public abstract class AbstractXlsBean {

    public AbstractXlsBean() {
    }

    /**
     * @return 必须有唯一主键
     */
    public abstract int getId();
}