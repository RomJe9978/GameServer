package com.games.framework.configuration;

import com.romje.utils.EmptyUtil;
import org.apache.commons.configuration2.ConfigurationDecoder;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 带有“优先级”性质的配置处理代理类
 * <p>非线程安全，支持多线程读，使用处自己保证写安全
 *
 * @author RomJe
 */
public class PriorityPropertyProxy implements ImmutableConfiguration {

    /**
     * 带有“优先级”的配置处理代理链
     */
    private final List<ImmutableConfiguration> proxyList;

    private PriorityPropertyProxy() {
        this.proxyList = new ArrayList<>();
    }

    /**
     * @return 一个新创建的实例，不会为{@code Null}
     */
    public static PriorityPropertyProxy newInstance() {
        return new PriorityPropertyProxy();
    }

    /**
     * 初始化配置文件
     *
     * @param fileNameList 配置文件名称列表,列表顺序决定了优先级
     *                     位置越在前面，代表优先级越高
     *                     如果参数为{@code empty},直接返回{@code false}
     * @return 任何错误或者异常返回{@code false}
     */
    public boolean initConfig(List<String> fileNameList) {
        if (EmptyUtil.isEmpty(fileNameList)) {
            return false;
        }

        for (String fileName : fileNameList) {
            try (InputStream inputStream = PriorityPropertyProxy.class.getClassLoader().getResourceAsStream(fileName)) {
                YAMLConfiguration config = new YAMLConfiguration();
                config.read(inputStream);
                this.appendProxy(config);
            } catch (ConfigurationException | IOException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 追加一个“配置代理”到优先级集合的最后
     *
     * @param configuration 不允许为{@code Null}
     */
    public void appendProxy(ImmutableConfiguration configuration) {
        if (Objects.isNull(configuration)) {
            return;
        }
        this.proxyList.add(configuration);
    }

    @Override
    public boolean containsKey(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(keyStr) && proxy.containsKey(keyStr)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T> T get(Class<T> aClass, String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.get(aClass, keyStr);
            }
        }
        return null;
    }

    @Override
    public <T> T get(Class<T> aClass, String keyStr, T defaultObject) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.get(aClass, keyStr);
            }
        }
        return defaultObject;
    }

    @Override
    public Object getArray(Class<?> aClass, String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getArray(aClass, keyStr);
            }
        }
        return null;
    }

    @Override
    public Object getArray(Class<?> aClass, String keyStr, Object defaultObject) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getArray(aClass, keyStr);
            }
        }
        return defaultObject;
    }

    @Override
    public BigDecimal getBigDecimal(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getBigDecimal(keyStr);
            }
        }
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String keyStr, BigDecimal defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getBigDecimal(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public BigInteger getBigInteger(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getBigInteger(keyStr);
            }
        }
        return null;
    }

    @Override
    public BigInteger getBigInteger(String keyStr, BigInteger defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getBigInteger(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public boolean getBoolean(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getBoolean(keyStr);
            }
        }
        return false;
    }

    @Override
    public boolean getBoolean(String keyStr, boolean b) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getBoolean(keyStr, b);
            }
        }
        return b;
    }

    @Override
    public Boolean getBoolean(String keyStr, Boolean defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getBoolean(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public byte getByte(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getByte(keyStr);
            }
        }
        return 0;
    }

    @Override
    public byte getByte(String keyStr, byte defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getByte(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Byte getByte(String keyStr, Byte defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getByte(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public <T> Collection<T> getCollection(Class<T> aClass, String keyStr, Collection<T> collection) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getCollection(aClass, keyStr, collection);
            }
        }
        return collection;
    }

    @Override
    public <T> Collection<T> getCollection(Class<T> aClass, String keyStr, Collection<T> collection, Collection<T> defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getCollection(aClass, keyStr, collection, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public double getDouble(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getDouble(keyStr);
            }
        }
        return 0;
    }

    @Override
    public double getDouble(String keyStr, double defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return getDouble(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Double getDouble(String keyStr, Double defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return getDouble(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public String getEncodedString(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getEncodedString(keyStr);
            }
        }
        return null;
    }

    @Override
    public String getEncodedString(String keyStr, ConfigurationDecoder defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return getEncodedString(keyStr, defaultValue);
            }
        }
        return null;
    }

    @Override
    public float getFloat(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getFloat(keyStr);
            }
        }
        return 0f;
    }

    @Override
    public float getFloat(String keyStr, float defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getFloat(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Float getFloat(String keyStr, Float defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getFloat(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public int getInt(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getInt(keyStr);
            }
        }
        return 0;
    }

    @Override
    public int getInt(String keyStr, int defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getInt(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Integer getInteger(String keyStr, Integer defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getInteger(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Iterator<String> getKeys() {
        Set<String> keySet = new HashSet<>();
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.isNull(proxy)) {
                continue;
            }

            Iterator<String> keyIterator = proxy.getKeys();
            if (Objects.isNull(keyIterator)) {
                continue;
            }

            while (keyIterator.hasNext()) {
                keySet.add(keyIterator.next());
            }
        }
        return keySet.iterator();
    }

    @Override
    public Iterator<String> getKeys(String prefix) {
        Set<String> keySet = new HashSet<>();
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.isNull(proxy)) {
                continue;
            }

            Iterator<String> iterator = proxy.getKeys(prefix);
            if (Objects.isNull(iterator)) {
                continue;
            }

            while (iterator.hasNext()) {
                keySet.add(iterator.next());
            }
        }
        return keySet.iterator();
    }

    @Override
    public <T> List<T> getList(Class<T> aClass, String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getList(aClass, keyStr);
            }
        }
        return null;
    }

    @Override
    public <T> List<T> getList(Class<T> aClass, String keyStr, List<T> defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getList(aClass, keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public List<Object> getList(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getList(keyStr);
            }
        }
        return null;
    }

    @Override
    public List<Object> getList(String keyStr, List<?> defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getList(keyStr, defaultValue);
            }
        }
        // unchecked
        return (List<Object>) defaultValue;
    }

    @Override
    public long getLong(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getLong(keyStr);
            }
        }
        return 0;
    }

    @Override
    public long getLong(String keyStr, long defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getLong(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Long getLong(String keyStr, Long defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getLong(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Properties getProperties(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getProperties(keyStr);
            }
        }
        return null;
    }

    @Override
    public Object getProperty(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getProperties(keyStr);
            }
        }
        return null;
    }

    @Override
    public short getShort(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getShort(keyStr);
            }
        }
        return 0;
    }

    @Override
    public short getShort(String keyStr, short defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getShort(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public Short getShort(String keyStr, Short defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getShort(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public String getString(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getString(keyStr);
            }
        }
        return null;
    }

    @Override
    public String getString(String keyStr, String defaultValue) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getString(keyStr, defaultValue);
            }
        }
        return defaultValue;
    }

    @Override
    public String[] getStringArray(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.getStringArray(keyStr);
            }
        }
        return new String[0];
    }

    @Override
    public ImmutableConfiguration immutableSubset(String keyStr) {
        for (ImmutableConfiguration proxy : this.proxyList) {
            if (Objects.nonNull(proxy) && proxy.containsKey(keyStr)) {
                return proxy.immutableSubset(keyStr);
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.size() <= 0;
    }

    @Override
    public int size() {
        Iterator<String> iterator = this.getKeys();
        if (Objects.isNull(iterator)) {
            return 0;
        }

        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}
