package com.games.framework.component.xlskit;

import java.util.*;

/**
 * 所有由Excel表数据映射到内存数据的管理类的统一父类
 *
 * <p> 将所有数据的管理与每一个由Excel结构生成的JavaBean职责分离，此处是统一管理数据
 * <p> 每一个实现类都会对应具体的一张配置表，所有实现类可以采用脚本等方式自动生成
 *
 * <p> 内部通过{@link #parseFrom(String)}方法将JSON数据转成内存数据集合，然后通过
 * {@link #fill(List)}方法将数据集合填充到当前管理类。拆成两步骤这一点是设计过的，除
 * 了一些代码编写层面的良好习惯外，主要考虑到配置表的热更，涉及对多表热更原子性的考虑。
 *
 * <p> 例如：现在要同时热更A，B，C三个表，这三个表之间互相有依赖，现在A热更成功了，但是
 * B没有热更成功，此时从热更的角度应该做到撤销A的热更，要做到这一点儿有两种思路，一种
 * 是依次热更ABC，如果中间出现失败，则全部撤回，这种方式需要临时保存热更前的数据，以便
 * 做数据回退。而另一种方式是，先对ABC的热更产生的新数据临时保存，只有所有热更完毕，再
 * 统一替换。从设计角度，第二种其实更好一些，因为尽可能地减少中间状态，就可以避免很多问
 * 题（虽然热更不一定采用第二种方式，但是这样做没有什么弊端，并且天然支持第二种方式）。
 *
 * @author liu xuan jie
 */
public abstract class AbstractXlsManager<T extends AbstractXlsBean> {

    /**
     * 当前Excel配置中所有有效数据列表
     */
    private List<T> list;

    /**
     * 当前Excel配置中所有有效数据索引列表
     * key：单条数据的主键Id，value：单条数据信息
     */
    private Map<Integer, T> map;

    /**
     * @return 当前管理类所对应的Excel表名称，不包含后缀
     */
    public abstract String xlsName();

    /**
     * 子类负责自行实现将Json格式的数据，映射成数据集合
     * 该部分内容的实现无需开发者操作，采用脚本等自动化方式即可
     *
     * @param jsonText 当前Excel表内所有有效数据的Json格式，调用处保证正确
     * @return 不要返回{@code null}
     */
    public abstract List<T> parseFrom(String jsonText);

    /**
     * 将所有生成的数据集合填充到当前数据管理集合中
     *
     * @param tempList 所有配置条目的数据集合
     */
    public void fill(List<T> tempList) {
        if (Objects.isNull(tempList) || tempList.isEmpty()) {
            this.list = Collections.emptyList();
            this.map = Collections.emptyMap();
            return;
        }

        Map<Integer, T> tempMap = buildMap(tempList);
        this.list = tempList;
        this.map = tempMap;
    }

    /**
     * @return 不会为{@code null}
     */
    public List<T> list() {
        return Objects.isNull(this.list) ? Collections.emptyList() : this.list;
    }

    /**
     * @param key 配置中单条数据的唯一索引Id
     * @return 不会为{@code null}
     */
    public T get(int key) {
        return Objects.isNull(this.map) ? null : this.map.get(key);
    }

    private Map<Integer, T> buildMap(List<T> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Integer, T> newMap = new HashMap<>(list.size());
        for (T single : list) {
            newMap.put(single.getId(), single);
        }
        return newMap;
    }
}