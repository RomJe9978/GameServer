package com.games.game.bootstrap;

import com.games.framework.component.eventkit.EventHandler;
import com.games.framework.component.eventkit.EventListener;
import com.games.game.constant.EventKey;

/**
 * @author liu xuan jie
 */
@EventHandler
public class Test {

    @EventListener({EventKey.TEST_EVENT, EventKey.TEST_EVENT2})
    public static void onTestInt(Integer param) {
        System.out.println("Test on test int param:" + param);
    }

    @EventListener({EventKey.TEST_EVENT, EventKey.TEST_EVENT2})
    public static void onTestString(String param) {
        System.out.println("Test on test string param:" + param);
    }

    @EventListener({EventKey.TEST_EVENT, EventKey.TEST_EVENT2})
    public static void onTestObject(Object param) {
        System.out.println("Test on test object param:" + param);
    }

    @EventListener({EventKey.TEST_EVENT, EventKey.TEST_EVENT2})
    public static void onTestTest(Test param) {
        System.out.println("Test on test test param:" + param);
    }

    @EventListener({EventKey.TEST_EVENT, EventKey.TEST_EVENT2})
    public static void onTestBoot(Bootstrapper param) {
        System.out.println("Test on test boot param:" + param);
    }

    @EventListener({EventKey.TEST_EVENT, EventKey.TEST_EVENT2})
    public static void onTestParameters(BootParameters param) {
        System.out.println("Test on test boot param:" + param);
    }
}

