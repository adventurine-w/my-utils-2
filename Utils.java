package org.adv.clickerflex.utils.mc;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Comparator;
import java.util.Map;

public class Utils {
    public static <T> BiMap<Integer, T> sortMap(Map<T, ? extends Number> map) {
        BiMap<Integer, T> biMap = HashBiMap.create();

        final int[] index = {1}; // start from 1
        map.entrySet().stream()
                .sorted(Comparator.comparingDouble((Map.Entry<T, ? extends Number> e) -> e.getValue().doubleValue())
                        .reversed()) // descending
                .map(Map.Entry::getKey)
                .forEachOrdered(key -> biMap.put(index[0]++, key));

        return biMap;
    }
    public static String getProgressBar(double current, double goal){
        return getProgressBar(current, goal, 20);
    }
    public static String getProgressBar(double current, double goal, int maxBars){
        if (goal <= 0) goal = 1;
        int greenBar = (int) Math.floor(current/(goal/ maxBars));
        greenBar = Math.max(0, Math.min(20, greenBar));

        StringBuilder sb = new StringBuilder("<b>");
        if(greenBar>0){
            sb.append("<a>");
        }
        sb.append("|".repeat(greenBar));
        int redBar = 20 -greenBar;
        if(redBar>0){
            sb.append("<c>");
        }
        sb.append("|".repeat(redBar));
        return sb.append("<reset>").toString();
    }
}
