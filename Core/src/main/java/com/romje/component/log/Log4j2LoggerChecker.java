package com.romje.component.log;

import com.romje.utils.EmptyUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

/**
 * @author RomJe
 */
public class Log4j2LoggerChecker implements ILoggerChecker {
    /**
     * 所有配置中存在的logger name的集合
     */
    private Set<String> nameSet;

    public static Log4j2LoggerChecker newInstance() {
        return new Log4j2LoggerChecker();
    }

    @Override
    public boolean init(String configFileName) {
        LoggerContext loggerContext = null;
        try (InputStream inputStream = Log4j2LoggerChecker.class.getClassLoader().getResourceAsStream(configFileName)) {
            if (Objects.isNull(inputStream)) {
                return false;
            }

            Configurator.initialize(null, new ConfigurationSource(inputStream));
            loggerContext = (LoggerContext) LogManager.getContext(false);
            loggerContext.start();

            Configuration configuration = loggerContext.getConfiguration();
            assert Objects.nonNull(configuration);
            this.nameSet = configuration.getLoggers().keySet();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(loggerContext)) {
                loggerContext.close();
            }
        }
    }

    /**
     * log4j2中如果指定名称不在配置中，会使用
     * {@code Root}的配置，这种情况会被任务配置不存在
     */
    @Override
    public boolean isExist(String loggerName) {
        if (isRoot(loggerName)) {
            return true;
        }

        if (Objects.isNull(loggerName)) {
            return false;
        }

        if (EmptyUtil.isEmpty(this.nameSet)) {
            return false;
        }

        return this.nameSet.contains(loggerName);
    }
}
