package org.adv.clickerflex.utils.mc;


import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.adv.clickerflex.utils.generic.ColorUtils;
import org.bukkit.Color;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.adv.clickerflex.utils.generic.ColorUtils.mmColored;

@Getter
public enum RGBColor {

    WHITE("#ffffff"),

    RED("#ff0000"),
    L_RED("#ff3333"),
    LL_RED("#ff6666"),
    LLL_RED("#ff9999"),
    D_RED("#990000"),

    ORANGE("#ff8000"),
    L_ORANGE("#ff9933"),
    LL_ORANGE("#ffb84d"),
    LLL_ORANGE("#ffcc99"),
    D_ORANGE("#994d00"),

    GOLD("#FFD700"),
    L_GOLD("#FFEB7F"),
    LL_GOLD("#fff2a8"),
    LLL_GOLD("#FFF8D1"),
    D_GOLD("#B8860B"),

    YELLOW("#ffff00"),
    L_YELLOW("#ffff33"),
    LL_YELLOW("#ffff99"),
    LLL_YELLOW("#ffffb3"),
    D_YELLOW("#999900"),

    GREEN("#00cc00"),
    L_GREEN("#33d633"),
    LL_GREEN("#66ff66"),
    LLL_GREEN("#aaffaa"),
    D_GREEN("#006600"),

    CYAN("#00cccc"),
    L_CYAN("#33d6d6"),
    LL_CYAN("#66ffff"),
    LLL_CYAN("#99e6e6"),
    D_CYAN("#006666"),

    AQUA("#00ffff"),
    L_AQUA("#33ffff"),
    LL_AQUA("#99ffff"),
    LLL_AQUA("#b3ffff"),
    D_AQUA("#009999"),

    BLUE("#1A1AFF"),
    L_BLUE("#4D4DFF"),
    LL_BLUE("#7F7FFF"),
    LLL_BLUE("#B2B2FF"),
    D_BLUE("#1A1AB2"),

    PINK("#ff69b4"),
    L_PINK("#ff85c3"),
    LL_PINK("#ffb6d9"),
    LLL_PINK("#ffcce6"),
    D_PINK("#993366"),

    PURPLE("#8000ff"),
    L_PURPLE("#9933ff"),
    LL_PURPLE("#c299ff"),
    LLL_PURPLE("#cc99ff"),
    D_PURPLE("#4b0082"),

    MAGENTA("#ff00ff"),
    L_MAGENTA("#ff33ff"),
    LL_MAGENTA("#ff99ff"),
    LLL_MAGENTA("#ffb3ff"),
    D_MAGENTA("#990099"),

    BROWN("#A0522D"),
    L_BROWN("#b35b32"),
    LL_BROWN("#BC7642"),
    LLL_BROWN("#D8A47F"),
    D_BROWN("#804011"),

    GRAY("#808080"),
    L_GRAY("#999999"),
    LL_GRAY("#d3d3d3"),
    LLL_GRAY("#cccccc"),
    D_GRAY("#404040"),

    RAINBOW("gradient:#FF0000:#FF7F00:#FFFF00:#00FF00:#0000FF:#4B0082:#8F00FF"),
    L_RAINBOW("gradient:#FF6666:#FFB266:#FFFF66:#66FF99:#6666FF:#9966CC:#B266FF"),
    LL_RAINBOW("gradient:#FF8080:#FFBF80:#FFFF80:#80FFBF:#8080FF:#BF80E0:#BF80FF"),
    LLL_RAINBOW("gradient:#FFCCCC:#FFE5CC:#FFFFCC:#CCFFE5:#CCCCFF:#E5CCF5:#E5CCFF"),
    D_RAINBOW("gradient:#FF0000:#FF7F00:#FFFF00:#00FF00:#0000FF:#4B0082:#8F00FF"),

    BLACK("#000000"),

    REDDISH_ORANGE("#ff4500"),
    L_REDDISH_ORANGE("#ff6a33"),
    LL_REDDISH_ORANGE("#ff8f66"),
    LLL_REDDISH_ORANGE("#ffb499"),
    D_REDDISH_ORANGE("#b33000"),


    REDDISH_BROWN("#A0522D"),
    L_REDDISH_BROWN("#B56B3A"),
    LL_REDDISH_BROWN("#C98558"),
    LLL_REDDISH_BROWN("#DEAD87"),
    D_REDDISH_BROWN("#7A3F20"),


    ;

    // X - PURE
    // L_X - SLIGHTLY DILUTED WITH WHITE
    // LL_X - DILUTED WITH WHITE (Normal Light X)
    // LLL_X - REALLY DILUTED WITH WHITE
    // DARK_X - DARKENED WITH BLACK


    private final String hexCode;
    private Color bukkitColor = null;

    RGBColor(String hexCode) {
        this.hexCode = hexCode;
    }
    public static Optional<RGBColor> fromTag(String tag) {
        try {
            return Optional.of(RGBColor.valueOf(tag.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    public TextColor getTextColor() {
        String input = this.getHexCode();

        String hex = input.trim().toLowerCase();

        if (hex.startsWith("#")) {
            hex = hex.substring(1); // remove '#'
        } else if (hex.startsWith("0x")) {
            hex = hex.substring(2); // remove '0x'
        }

        // Now hex is just the raw 6-digit string
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid Color format: " + input);
        }

        int rgb = Integer.parseInt(hex, 16);
        return TextColor.color(rgb);
    }

    @Override
    public String toString() {
        return "<" + hexCode + ">";
    }
    public Color toColor(){
        if(bukkitColor==null){
            bukkitColor = ColorUtils.fromHex(this.hexCode);
        }
        return bukkitColor;
    }

    private static final Pattern COLOR_TAG_PATTERN = Pattern.compile("<([a-zA-Z_]+)>");

    public static String RGBS(String input) {
        Matcher matcher = COLOR_TAG_PATTERN.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String tag = matcher.group(1); // e.g., "yellow"
            String replacement = RGBColor.fromTag(tag)
                    .map(RGBColor::toString)
                    .orElse("<" + tag + ">"); // fallback if not found
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }
    public static Component RGBC(String input) {
        Matcher matcher = COLOR_TAG_PATTERN.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String tag = matcher.group(1); // e.g., "yellow"
            String replacement = RGBColor.fromTag(tag)
                    .map(RGBColor::toString)
                    .orElse("<" + tag + ">"); // fallback if not found
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return mmColored(result.toString());
    }
}