package zh.shawn.project.framework.boot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RandomUtils {
    public RandomUtils() {
    }

    public static String genRandomString(String mainDate, int leftLength) {
        return (new SimpleDateFormat(mainDate)).format(new Date()) + UUID.randomUUID().toString().replaceAll("[-]", "").substring(0, leftLength);
    }

    public static int genRandomNum(int maxNum) {
        return Math.abs((new Random()).nextInt() % maxNum);
    }
}
