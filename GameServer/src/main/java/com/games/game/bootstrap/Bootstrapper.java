package com.games.game.bootstrap;

import com.games.framework.component.checker.CleanUpChecker;
import com.games.framework.component.eventkit.EventDispatcher;
import com.games.framework.utils.ScanUtil;
import com.games.game.component.Log4j2LoggerChecker;
import com.games.game.component.LoggerRepository;
import com.games.log.Log;
import com.romje.component.checker.constcheck.ConstChecker;
import com.romje.component.checker.constcheck.ConstUnique;
import com.romje.component.checker.enumcheck.EnumChecker;
import com.romje.component.checker.enumcheck.EnumUnique;
import com.romje.component.manager.enumlookup.EnumLookup;
import com.romje.component.pool.object.Reusable;
import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 启动器。不允许实例化
 *
 * @author liu xuan jie
 */
public final class Bootstrapper {

    private Bootstrapper() {
    }

    public static void boot(String[] args) {
        exitOnFail(registerEnums(), 1);
        exitOnFail(checkEnumFieldRepeat(), 1);
        exitOnFail(checkConstFieldRepeat(), 1);
        exitOnFail(checkLoggerConfig(), 1);
        exitOnFail(checkReusable(), 1);
        exitOnFail(registerEventListener(), 1);
    }

    private static void exitOnFail(boolean result, int code) {
        if (!result) {
            System.exit(code);
        }
    }

    private static boolean registerEnums() {
        List<Class<? extends Enum<?>>> enumClassList =
                ScanUtil.scanEnumsAsList(BootParameters.SCAN_ENUM_MANAGE_PACKAGE_NAME);

        try {
            BoolResult boolResult = EnumLookup.INSTANCE.registerEnums(enumClassList);
            if (boolResult.isFail()) {
                Log.LOGIC.error("[Boot] Register enum manage fail:{}!", boolResult.message());
                return false;
            }

            Log.LOGIC.info("[Boot] Register enum manage success!");
            return true;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Log.LOGIC.error("[Boot] Register enum manage exception!", e);
            return false;
        }
    }

    private static boolean checkEnumFieldRepeat() {
        List<Class<? extends Enum<?>>> enumClassList =
                ScanUtil.scanEnumsAsList(BootParameters.ENUM_CHECKED_PACKAGE_NAME);

        try {
            BoolResult boolResult = EnumChecker.checkFieldUnique(enumClassList, EnumUnique.class);
            if (boolResult.isFail()) {
                Log.LOGIC.error("[Boot] Check enum field repeat fail:{}!", boolResult.message());
                return false;
            }

            Log.LOGIC.info("[Boot] Check enum field repeat success!");
            return true;
        } catch (Exception e) {
            Log.LOGIC.error("[Boot] Check enum field repeat exception!", e);
            return false;
        }
    }

    private static boolean checkConstFieldRepeat() {
        try {
            List<Class<?>> classList =
                    ScanUtil.scanAnnotationAsList(BootParameters.CONST_CHECK_UNIQUE_PACKAGE_NAME, ConstUnique.class);

            BoolResult boolResult = ConstChecker.checkFieldUnique(classList);
            if (boolResult.isFail()) {
                Log.LOGIC.error("[Boot] Check const field repeat fail:{}!", boolResult.message());
                return false;
            }

            Log.LOGIC.info("[Boot] Check const field repeat success!");
            return true;
        } catch (IllegalAccessException e) {
            Log.LOGIC.error("[Boot] Check const field repeat exception!", e);
            return false;
        }
    }

    private static boolean checkLoggerConfig() {
        Set<String> configNameSet = Log4j2LoggerChecker.listConfigLoggerNames();

        List<Class<?>> classList =
                ScanUtil.scanAnnotationAsList(BootParameters.SCAN_LOGGER_PACKAGE_NAME, LoggerRepository.class);
        BoolResult boolResult = Log4j2LoggerChecker.checkLoggerConfigured(classList, configNameSet);
        if (boolResult.isFail()) {
            Log.LOGIC.error("[Boot] Check logger config fail:{}!", boolResult.message());
            return false;
        }

        Log.LOGIC.info("[Boot] All logger checked finish! Non error!");
        return true;
    }

    private static boolean checkReusable() {
        List<Class<? extends Reusable>> reusableList = ScanUtil.scanInterfaceAsList(BootParameters.SCAN_REUSABLE_PACKAGE_NAME, Reusable.class);
        if (EmptyUtil.isEmpty(reusableList)) {
            Log.LOGIC.info("[Boot] check reusable list is empty!");
            return true;
        }

        List<Class<?>> clazzList = new ArrayList<>(reusableList.size());
        clazzList.addAll(reusableList);

        try {
            BoolResult boolResult = CleanUpChecker.checkCleanUp(clazzList, "clear");
            if (boolResult.isFail()) {
                Log.LOGIC.error("[Boot] check clean up error! {}", boolResult.message());
                return false;
            }
        } catch (IOException e) {
            Log.LOGIC.error("[Boot] check clean up exception!", e);
            return false;
        }

        Log.LOGIC.info("[Boot] check clean up finish! Package name:{}", BootParameters.SCAN_REUSABLE_PACKAGE_NAME);
        return true;
    }

    private static boolean registerEventListener() {
        BoolResult boolResult = EventDispatcher.INSTANCE.registerEventListener(BootParameters.SCAN_EVENT_LISTENER_PACKAGE_NAME);
        if (boolResult.isFail()) {
            Log.LOGIC.error("[Boot] Register event listener fail, message:{}!", boolResult.message());
            return false;
        }
        return true;
    }
}