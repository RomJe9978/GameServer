package com.games.framework.component.xlskit;

import com.games.framework.utils.ScanUtil;
import com.romje.model.BoolResult;

import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * "Excel文件配置数据"加载管理类，单例
 *
 * @author liu xuan jie
 */
public enum XlsLoader {

    INSTANCE;

    /**
     * key：Excel配置表文件名(不包含后缀)，value：该文件名对应的数据Manager
     */
    private final Map<String, AbstractXlsManager<AbstractXlsBean>> managerMap = new HashMap<>();

    /**
     * key：Excel配置表文件名(不包含后缀)，value：该文件名对应的数据Assembler
     */
    private final Map<String, IXlsAssembler> assemblerMap = new HashMap<>();

    /**
     * 加载所有“Excel”配置数据，依托所有{@link AbstractXlsManager}的实现类
     *
     * @param jsonFilesPath Json文件路径
     * @param packageName   扫描所有{@link AbstractXlsManager}实现类的包名
     * @return 任何失败或者异常{@code false}，失败详情从{@link BoolResult#message()}中获取
     */
    @SuppressWarnings("unchecked")
    public BoolResult loadXlsManager(String jsonFilesPath, String packageName) {
        // 使用无泛型的列表来处理扫描结果
        List<?> nonGenericsList = ScanUtil.scanSubclassAsList(packageName, AbstractXlsManager.class);
        List<Class<? extends AbstractXlsManager<?>>> managerClassList = (List<Class<? extends AbstractXlsManager<?>>>) nonGenericsList;
        if (managerClassList.isEmpty()) {
            return BoolResult.success();
        }

        List<AbstractXlsManager<AbstractXlsBean>> instanceList = new ArrayList<>(managerClassList.size());
        for (Class<? extends AbstractXlsManager<?>> clazz : managerClassList) {
            try {
                Method method = clazz.getMethod("getInstance");
                AbstractXlsManager<AbstractXlsBean> instance = (AbstractXlsManager<AbstractXlsBean>) method.invoke(null);

                String content = this.readJson(jsonFilesPath, instance.xlsName());
                List<AbstractXlsBean> beanList = instance.parseFrom(content);
                instance.fill(beanList);
                instanceList.add(instance);
            } catch (Exception e) {
                return BoolResult.fail("Load xls exception:" + e.getMessage());
            }
        }

        // 全部通过之后，记录所有的加工组装类
        this.managerMap.clear();
        for (AbstractXlsManager<AbstractXlsBean> instance : instanceList) {
            AbstractXlsManager<AbstractXlsBean> oldValue = this.managerMap.put(instance.xlsName(), instance);
            if (Objects.nonNull(oldValue)) {
                return BoolResult.fail("Xls manager repeated:", instance.xlsName());
            }
        }

        return BoolResult.success();
    }

    /**
     * 处理所有“配置数据组装加工类”，依托所有{@link IXlsAssembler}的实现类
     *
     * @param packageName 扫描所有{@link IXlsAssembler}实现类的包名
     * @return 任何失败或者异常{@code false}，失败详情从{@link BoolResult#message()}中获取
     */
    public BoolResult loadXlsAssembler(String packageName) {
        // 扫描出所有IXlsAssembler
        List<Class<? extends IXlsAssembler>> assemblerClassList = ScanUtil.scanInterfaceAsList(packageName, IXlsAssembler.class);
        if (assemblerClassList.isEmpty()) {
            return BoolResult.success();
        }

        // 获取所有assembler的实例
        List<IXlsAssembler> assemblerImplList = new ArrayList<>(assemblerClassList.size());
        for (Class<? extends IXlsAssembler> clazz : assemblerClassList) {
            try {
                Method method = clazz.getMethod("getInstance");
                IXlsAssembler instance = (IXlsAssembler) method.invoke(null);
                assemblerImplList.add(instance);
            } catch (Exception e) {
                return BoolResult.fail("Load xls assembler exception:" + e.getMessage());
            }
        }

        // 执行assemble
        BoolResult boolResult = this.doAssemble(assemblerImplList);
        if (boolResult.isFail()) {
            return boolResult;
        }

        // 全部通过之后，记录所有的加工组装类
        this.assemblerMap.clear();
        for (IXlsAssembler assembler : assemblerImplList) {
            IXlsAssembler oldValue = this.assemblerMap.put(assembler.xlsName(), assembler);
            if (Objects.nonNull(oldValue)) {
                return BoolResult.fail("Xls assembler repeated:", assembler.xlsName());
            }
        }
        return BoolResult.success();
    }

    /**
     * 热更“Excel”配置数据，支持多表热更
     *
     * <p> 出现任何异常或者失败，都会回滚到所有数据未热更之前的状态
     *
     * <p> 热更方法执行期间存在“中间状态”。当所有manager已经是新数据了，但是所有的
     * assembler还没有热更完毕，此时会有很短时间的数据不一致，如果业务不能接受，需
     * 要上层业务去做处理，例如，停止所有业务线程(Stop the world)
     *
     * <p> 热更方法执行完毕后，无论是成功还是失败，都不会有“中间状态了”，要么一组热
     * 更全部完成，要么一组数据全部不热更
     *
     * @param jsonFilesPath  Json文件路径
     * @param reloadXlsNames 本次想要热更的Excel文件名称（不含后缀），不允许为{@code empty}
     * @return 任何失败或者异常{@code false}，失败详情从{@link BoolResult#message()}中获取
     */
    public BoolResult reload(String jsonFilesPath, List<String> reloadXlsNames) {
        if (Objects.isNull(reloadXlsNames) || reloadXlsNames.isEmpty()) {
            return BoolResult.fail("reload xls names is empty!");
        }

        // 1、加载新数据到内存，同时临时保存旧数据，用于回滚
        Map<String, List<AbstractXlsBean>> oldDataMap = new HashMap<>(reloadXlsNames.size());
        Map<String, List<AbstractXlsBean>> newDataMap = new HashMap<>(reloadXlsNames.size());
        for (String xlsName : reloadXlsNames) {
            AbstractXlsManager<AbstractXlsBean> manager = this.managerMap.get(xlsName);
            if (Objects.isNull(manager)) {
                return BoolResult.fail("reload manager is null:" + xlsName);
            }

            oldDataMap.put(xlsName, manager.list());

            try {
                String jsonText = this.readJson(jsonFilesPath, xlsName);
                newDataMap.put(xlsName, manager.parseFrom(jsonText));
            } catch (Exception e) {
                return BoolResult.fail(xlsName + " reload manager exception:" + e.getMessage());
            }
        }

        // 2、统一替换，此时能够保证所有的新数据已经加载到内存了，为了尽可能保证这一组操作的原子性
        newDataMap.forEach((xlsName, newBeanList) -> this.managerMap.get(xlsName).fill(newBeanList));

        // 3、处理二次组装加工，这一步必须放到manager热更完毕之后，因为要用manager中的新数据
        List<IXlsAssembler> reloadAssemblerList = new ArrayList<>(reloadXlsNames.size());
        for (String xlsName : reloadXlsNames) {
            IXlsAssembler assembler = this.assemblerMap.get(xlsName);
            if (Objects.nonNull(assembler)) {
                reloadAssemblerList.add(assembler);
            }
        }

        // 4、加工一旦出现错误，直接回滚，因为此时原始manager数据已经热更成新的了，需要回退
        BoolResult assembleResult = this.doAssemble(reloadAssemblerList);
        if (assembleResult.isFail()) {
            this.rollback(oldDataMap, reloadAssemblerList);
            return assembleResult;
        }

        return BoolResult.success();
    }

    /**
     * 处理数据回滚，回滚默认成功，不做额外处理了
     *
     * @param oldDataMap          K：回滚的xls文件名（不含后缀），V:对应需要回滚的数据
     * @param reloadAssemblerList 需要重新组装加工的类
     */
    private void rollback(Map<String, List<AbstractXlsBean>> oldDataMap, List<IXlsAssembler> reloadAssemblerList) {
        if (Objects.nonNull(oldDataMap) && !oldDataMap.isEmpty()) {
            oldDataMap.forEach((xlsName, oldBeanList) -> this.managerMap.get(xlsName).fill(oldBeanList));
        }

        if (Objects.nonNull(reloadAssemblerList) && !reloadAssemblerList.isEmpty()) {
            this.doAssemble(reloadAssemblerList);
        }
    }

    /**
     * 从指定路径下的指定Json文件中，读取出内容
     *
     * @param xlsName 文件名称（不含后缀）
     */
    private String readJson(String jsonFilesPath, String xlsName) throws Exception {
        String fileName = jsonFilesPath + xlsName + ".json";
        ClassLoader classLoader = XlsLoader.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (Objects.isNull(resource)) {
            return null;
        }

        Path path = Paths.get(resource.toURI());
        return new String(Files.readAllBytes(path));
    }

    /**
     * 执行一组“加工组装类”的处理流程
     */
    private BoolResult doAssemble(List<IXlsAssembler> assemblerImplList) {
        if (Objects.isNull(assemblerImplList) || assemblerImplList.isEmpty()) {
            return BoolResult.success();
        }

        // 1. 执行assemble
        for (IXlsAssembler assembler : assemblerImplList) {
            boolean assembleResult = assembler.assemble();
            if (!assembleResult) {
                return BoolResult.fail(assembler.getClass().getSimpleName() + " do assemble fail!");
            }
        }

        // 2. 执行afterAssemble
        for (IXlsAssembler assembler : assemblerImplList) {
            boolean afterAssembleResult = assembler.afterAssemble();
            if (!afterAssembleResult) {
                return BoolResult.fail(assembler.getClass().getSimpleName() + " do after assemble fail!");
            }
        }

        // 3. 执行check
        for (IXlsAssembler assembler : assemblerImplList) {
            boolean checkResult = assembler.check();
            if (!checkResult) {
                return BoolResult.fail(assembler.getClass().getSimpleName() + " do check fail!");
            }
        }

        return BoolResult.success();
    }
}