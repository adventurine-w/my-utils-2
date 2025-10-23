package org.adv.clickerflex.utils.generic;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static int between(int from, int to) {
        if (from == to) return from;
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public static long between(long from, long to) {
        if (from == to) return from;
        long min = Math.min(from, to);
        long max = Math.max(from, to);
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
    public static double between(double from, double to) {
        if (from == to) return from;
        double min = Math.min(from, to);
        double max = Math.max(from, to);
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    public static float between(float from, float to) {
        if (from == to) return from;
        float min = Math.min(from, to);
        float max = Math.max(from, to);
        return ThreadLocalRandom.current().nextFloat(min, max);
    }
    public static boolean chanceOf(double percentage) {
        if (percentage <= 0) return false;
        if (percentage >= 100) return true;
        return ThreadLocalRandom.current().nextDouble(100) < percentage;
    }
    public static boolean chanceOfDecimal(double chance) {
        if (chance <= 0) return false;
        if (chance >= 1) return true;
        return ThreadLocalRandom.current().nextDouble() < chance;
    }
    public static <T> T ofList(List<T> list){
        if(list.isEmpty()) return null;
        return list.get(between(0, list.size()-1));
    }
}
