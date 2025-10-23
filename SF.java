package org.adv.clickerflex.utils.generic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SF {
    /*
    when x < 10:
      1.0 -> 1
      1.5 -> 1.5
    when x > 10:
      10.0 -> 10
      10.5 -> 10
      1000 -> 1,000
     */
    public static String sf(double x){
        return sf(x, 1, 10d);
    }
    public static String sf(double x, int dp) {
        return sf(x, dp, 10d);
    }
    public static String sf(double x, double thresholdForRounding) {
        return sf(x, 1, thresholdForRounding);
    }
    public static String sf(double x, int dp, double thresholdForRounding) {
        if (x >= thresholdForRounding) {
            if(x>1000) return formatNum(x);
            return String.format("%.0f", Math.floor(x + 1e-9)); // epsilon correction
        }

        if (x == 0) return "0";

        // Determine order of magnitude
        int magnitude = (int) Math.floor(Math.log10(Math.abs(x)));
        int sigDigits = 2; // rule 1: 2 significant figures
        double factor = Math.pow(10, sigDigits - magnitude - 1);

        // Use BigDecimal to fix floating point precision
        BigDecimal bd = new BigDecimal(x * factor)
                .setScale(0, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(factor), dp, RoundingMode.HALF_UP);

        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.setMinimumFractionDigits(0);

        // rule 2: only dp decimals if > 1
        if (x > 1) df.setMaximumFractionDigits(dp);
        else df.setMaximumFractionDigits(10); // allow full precision for numbers <= 1

        if(bd.doubleValue()>1000) return formatNumD(bd.doubleValue());
        return df.format(bd);
    }
    public static int getIntLength(double x){
        return (int) Math.floor(Math.log10(x)) + 1;
    }

    public static String intToRoman(int num) {
        if(num==0){
            return "0";
        }
        final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder roman = new StringBuilder();
        int i = 0;
        while (num > 0) {
            if (num - VALUES[i] >= 0) {
                roman.append(SYMBOLS[i]);
                num -= VALUES[i];
            } else {
                i++;
            }
        }
        return roman.toString();
    }



    public static String formatNum(double n){
        return formatNum(n, 1_000_000_000);
    }
    public static String formatNum(double n, double thresholdForK) {
        if (n < thresholdForK) {
            return formatNumD(n);
        } else {
            return formatNumK(n);
        }
    }
    public static String formatNumD(double n) {
        DecimalFormat df;

        // Use one decimal place if n < 10 and has a fractional part
        if (n < 10 && n % 1 != 0) {
            df = new DecimalFormat("#,##0.0");
        } else {
            df = new DecimalFormat("#,###");
        }

        return df.format(n);
    }
    private static final String[] suffixData = {"SP,24", "SX,21", "QI,18", "QA,15", "T,12", "B,9", "M,6", "K,3"};

    public static String formatNumK(double n) {
        for (String entry : suffixData) {
            String[] parts = entry.split(",");
            String suffix = parts[0];
            int power = Integer.parseInt(parts[1]);
            double base = Math.pow(10, power);

            if (n >= base) {
                double value = n / base;
                // Use integer formatting if no fractional part
                String formatted = (value % 1 == 0)
                        ? String.valueOf((int) value)
                        : new DecimalFormat("0.##").format(value);
                return formatted + suffix;
            }
        }
        // fallback for numbers smaller than the first suffix
        return sf(n, 1, 100d);
    }
    public static double sigF(double input, int sf) {
        if (input == 0) return 0;

        final double magnitude = Math.pow(10, Math.floor(Math.log10(Math.abs(input))));
        final double scale = Math.pow(10, sf - 1) / magnitude;
        return Math.round(input * scale) / scale;
    }
    public static double autoSigF(double value) {
        int sigFigures;
        int digits = (int) Math.floor(Math.log10(value)+1);
        if(digits==1){
            return value;
        }else if(digits==2){
            sigFigures = 1;
        }else{
            sigFigures= 2;
        }
        if (value == 0) {
            return 0;
        }
        final double scale = Math.pow(10, Math.floor(Math.log10(Math.abs(value))) + 1 - sigFigures);
        return Math.round(value / scale) * scale;
    }

    public static double nearestX(double input, int number){
        double sets = input/number;
        sets = Math.round(sets);
        return sets*number;
    }
    public static float nearestX(float input, int number){
        float sets = input/number;
        sets = Math.round(sets);
        return sets*number;
    }
    public static int nearestX(int input, int number){
        double sets = (double) input /number;
        sets = Math.round(sets);
        return (int) (sets*number);
    }
}
