package org.adv.clickerflex.utils.generic;

public class WordUtils {
    public static String camelToProper(String string){
        String[] split = string.split("_");
        StringBuilder sb = new StringBuilder();
        for(String s: split){
            sb.append(Character.toUpperCase(s.charAt(0)))
                    .append(
                            (s.length()>1 ? s.substring(1) : "")
                                    .toLowerCase()
                            )
                    .append(" ");
        }
        return sb.toString().trim();
    }
}
