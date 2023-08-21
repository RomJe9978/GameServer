package utils;

import com.romje.utils.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author RomJe
 */
public class RandomUtilTest {

    /**
     * {@link RandomUtil#randomInt(int)}
     * <p>Test result when totalCount=1100，maxvalue=10：
     * {0=90, 1=99, 2=107, 3=108, 4=86, 5=98, 6=93, 7=109, 8=94, 9=116, 10=100}
     */
    @Test
    public void testRandomIntWithMax() {
        int totalCount = 1100;
        int maxValue = 10;

        Map<Integer, Integer> randomMap = new HashMap<>();
        for (int i = 0; i < totalCount; i++) {
            int value = RandomUtil.randomInt(maxValue);
            if (randomMap.containsKey(value)) {
                int count = randomMap.get(value);
                randomMap.put(value, count + 1);
                continue;
            }
            randomMap.put(value, 1);
        }

        System.out.println(randomMap);
    }

    /**
     * {@link RandomUtil#randomInt(int, int)}
     * <p>Test result when totalCount=1100, minValue=1, maxvalue=10：
     * {1=120, 2=100, 3=118, 4=98, 5=116, 6=113, 7=119, 8=105, 9=118, 10=93}
     */
    @Test
    public void testRandomSide() {
        int totalCount = 1100;
        int minValue = 1;
        int maxValue = 10;

        Map<Integer, Integer> randomMap = new HashMap<>();
        for (int i = 0; i < totalCount; i++) {
            int value = RandomUtil.randomInt(minValue, maxValue);
            if (randomMap.containsKey(value)) {
                int count = randomMap.get(value);
                randomMap.put(value, count + 1);
                continue;
            }
            randomMap.put(value, 1);
        }

        System.out.println(randomMap);
    }

    /**
     * {@link RandomUtil#hitProbability(int, int)}
     */
    @Test
    public void testHitProbability() {
        System.out.println("总概率为0，目标概率为0：" + RandomUtil.hitProbability(0, 0));
        System.out.println("总概率为0，目标概率为5：" + RandomUtil.hitProbability(0, 5));
        System.out.println("总概率为10，目标概率为0：" + RandomUtil.hitProbability(10, 0));

        System.out.println("=========================================================");
        for (int i = 0; i < 10; i++) {
            System.out.println("总概率为10，目标概率为5：" + RandomUtil.hitProbability(10, 5));
        }
        System.out.println("=========================================================");

        System.out.println("总概率为10，目标概率为10：" + RandomUtil.hitProbability(10, 10));
        System.out.println("总概率为10，目标概率为15：" + RandomUtil.hitProbability(10, 15));
    }

    /**
     * {@link RandomUtil#selectUnique(List, int)}
     * <pre>数组为11, 22, 33, 44, 55, 66, 77，结果为：
     * count为负数:[]
     * count为0:[]
     * count为list范围内正数:[11, 66, 55]
     * count为list的长度:[22, 77, 55, 11, 44, 66, 33]
     * count为超过list的长度:[]
     */
    @Test
    public void testSelectUnique() {
        List<Integer> list = Arrays.asList(11, 22, 33, 44, 55, 66, 77);
        System.out.println("count为负数:" + RandomUtil.selectUnique(list, -1));
        System.out.println("count为0:" + RandomUtil.selectUnique(list, 0));
        System.out.println("count为list范围内正数:" + RandomUtil.selectUnique(list, 3));
        System.out.println("count为list的长度:" + RandomUtil.selectUnique(list, 7));
        System.out.println("count为超过list的长度:" + RandomUtil.selectUnique(list, 9));
    }
}
