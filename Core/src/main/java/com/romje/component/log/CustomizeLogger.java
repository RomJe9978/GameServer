package com.romje.component.log;

import org.apache.logging.log4j.util.StackLocatorUtil;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Objects;

/**
 * 底层日志组件的装饰类，扩展定制需求
 *
 * @author RomJe
 */
public class CustomizeLogger implements Logger {
    /**
     * 关联的最终具体的logger实现
     */
    private Logger realLogger;

    /**
     * 是否需要包含位置信息，默认需要
     */
    private boolean includeLocation;

    /**
     * 业务标识前缀,可以为{@code null}
     */
    private final String LOG_PREFIX;

    private CustomizeLogger(Logger logger, String logPrefix, boolean includeLocation) {
        Objects.requireNonNull(logger);
        this.realLogger = logger;
        this.LOG_PREFIX = logPrefix;
        this.includeLocation = includeLocation;
    }

    /**
     * @return A new instance
     */
    public static CustomizeLogger newInstance(Logger logger, String logPrefix, boolean includeLocation) {
        return new CustomizeLogger(logger, logPrefix, includeLocation);
    }

    /**
     * 运行时替换logger
     *
     * @param logger 不允许为{@code null}
     */
    public void setLogger(Logger logger) {
        Objects.requireNonNull(logger);
        this.realLogger = logger;
    }

    /**
     * 加工原始的日志message
     */
    private String processMsg(String msg) {
        StringBuilder result = new StringBuilder();
        appendLocation(result);
        appendPrefix(result);
        result.append(msg);
        return result.toString();
    }

    /**
     * 添加位置信息
     */
    private void appendLocation(StringBuilder result) {
        if (Objects.isNull(result)) {
            return;
        }

        if (this.includeLocation) {
            StackTraceElement stackTraceElement = StackLocatorUtil.calcLocation(this.getClass().getName());
            if (Objects.nonNull(stackTraceElement)) {
                result.append("(");
                result.append(stackTraceElement.getFileName());
                result.append(":");
                result.append(stackTraceElement.getLineNumber());
                result.append(")");
                result.append(" - ");
            }
        }
    }

    /**
     * 添加前缀信息
     */
    private void appendPrefix(StringBuilder result) {
        if (Objects.isNull(result)) {
            return;
        }

        if (Objects.nonNull(LOG_PREFIX)) {
            result.append(LOG_PREFIX);
            result.append(" ");
        }
    }

    @Override
    public String getName() {
        return realLogger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return realLogger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled()) {
            realLogger.trace(processMsg(msg));
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            realLogger.trace(processMsg(format), arg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            realLogger.trace(processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            realLogger.trace(processMsg(format), arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            realLogger.trace(processMsg(msg), t);
        }
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return realLogger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        if (realLogger.isTraceEnabled(marker)) {
            realLogger.trace(marker, processMsg(msg));
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if (realLogger.isTraceEnabled(marker)) {
            realLogger.trace(marker, processMsg(format), arg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (realLogger.isTraceEnabled(marker)) {
            realLogger.trace(marker, processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if (realLogger.isTraceEnabled(marker)) {
            realLogger.trace(marker, processMsg(format), argArray);
        }
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if (realLogger.isTraceEnabled(marker)) {
            realLogger.trace(marker, processMsg(msg), t);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return realLogger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        if (realLogger.isDebugEnabled()) {
            realLogger.debug(processMsg(msg));
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (realLogger.isDebugEnabled()) {
            realLogger.debug(processMsg(format), arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (realLogger.isDebugEnabled()) {
            realLogger.debug(processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (realLogger.isDebugEnabled()) {
            realLogger.debug(processMsg(format), arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (realLogger.isDebugEnabled()) {
            realLogger.debug(processMsg(msg), t);
        }
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return realLogger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        if (realLogger.isDebugEnabled(marker)) {
            realLogger.debug(marker, processMsg(msg));
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if (realLogger.isDebugEnabled(marker)) {
            realLogger.debug(marker, processMsg(format), arg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (realLogger.isDebugEnabled(marker)) {
            realLogger.debug(marker, processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        if (realLogger.isDebugEnabled(marker)) {
            realLogger.debug(marker, processMsg(format), arguments);
        }
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        if (realLogger.isDebugEnabled(marker)) {
            realLogger.debug(marker, processMsg(msg), t);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return realLogger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        if (realLogger.isInfoEnabled()) {
            realLogger.info(processMsg(msg));
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (realLogger.isInfoEnabled()) {
            realLogger.info(processMsg(format), arg);
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (realLogger.isInfoEnabled()) {
            realLogger.info(processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (realLogger.isInfoEnabled()) {
            realLogger.info(processMsg(format), arguments);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (realLogger.isInfoEnabled()) {
            realLogger.info(processMsg(msg), t);
        }
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return realLogger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        if (realLogger.isInfoEnabled(marker)) {
            realLogger.info(marker, processMsg(msg));
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        if (realLogger.isInfoEnabled(marker)) {
            realLogger.info(marker, processMsg(format), arg);
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if (realLogger.isInfoEnabled(marker)) {
            realLogger.info(marker, processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        if (realLogger.isInfoEnabled(marker)) {
            realLogger.info(marker, processMsg(format), arguments);
        }
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        if (realLogger.isInfoEnabled(marker)) {
            realLogger.info(marker, processMsg(msg), t);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return realLogger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        if (realLogger.isWarnEnabled()) {
            realLogger.warn(processMsg(msg));
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (realLogger.isWarnEnabled()) {
            realLogger.warn(processMsg(format), arg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (realLogger.isWarnEnabled()) {
            realLogger.warn(processMsg(format), arguments);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (realLogger.isWarnEnabled()) {
            realLogger.warn(processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (realLogger.isWarnEnabled()) {
            realLogger.warn(processMsg(msg), t);
        }
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return realLogger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        if (realLogger.isWarnEnabled(marker)) {
            realLogger.warn(marker, processMsg(msg));
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        if (realLogger.isWarnEnabled(marker)) {
            realLogger.warn(marker, processMsg(format), arg);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if (realLogger.isWarnEnabled(marker)) {
            realLogger.warn(marker, processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        if (realLogger.isWarnEnabled(marker)) {
            realLogger.warn(marker, processMsg(format), arguments);
        }
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        if (realLogger.isWarnEnabled(marker)) {
            realLogger.warn(marker, processMsg(msg), t);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return realLogger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(processMsg(msg));
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(processMsg(format), arg);
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(processMsg(format), arguments);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(processMsg(msg), t);
        }
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return realLogger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(marker, processMsg(msg));
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(marker, processMsg(format), arg);
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(marker, processMsg(format), arg1, arg2);
        }
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(marker, processMsg(format), arguments);
        }
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if (realLogger.isErrorEnabled()) {
            realLogger.error(marker, processMsg(msg), t);
        }
    }
}
