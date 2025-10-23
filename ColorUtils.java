package org.adv.clickerflex.utils.generic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.adv.clickerflex.utils.mc.RGBColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("PatternValidation")
public class ColorUtils {
    public static MiniMessage mm = null;
    private static final Map<String, NamedTextColor> mcColors = Map.ofEntries(
            Map.entry("0", NamedTextColor.BLACK), Map.entry("1", NamedTextColor.DARK_BLUE), Map.entry("2", NamedTextColor.DARK_GREEN),
            Map.entry("3", NamedTextColor.DARK_AQUA), Map.entry("4", NamedTextColor.DARK_RED), Map.entry("5", NamedTextColor.DARK_PURPLE),
            Map.entry("6", NamedTextColor.GOLD), Map.entry("7", NamedTextColor.GRAY), Map.entry("8", NamedTextColor.DARK_GRAY),
            Map.entry("9", NamedTextColor.BLUE), Map.entry("a", NamedTextColor.GREEN), Map.entry("x", NamedTextColor.AQUA),
            Map.entry("c", NamedTextColor.RED), Map.entry("d", NamedTextColor.LIGHT_PURPLE), Map.entry("e", NamedTextColor.YELLOW),
            Map.entry("f", NamedTextColor.WHITE)
    );
    private static final Map<String, TextDecoration> mcStyles = Map.ofEntries(
            Map.entry("l", TextDecoration.BOLD),
            Map.entry("o", TextDecoration.ITALIC),
            Map.entry("n", TextDecoration.UNDERLINED),
            Map.entry("m", TextDecoration.STRIKETHROUGH),
            Map.entry("k", TextDecoration.OBFUSCATED)
    );

    public static MiniMessage getMM(){
        if(mm==null){
            TagResolver.Builder builder = TagResolver.builder()
                    .resolver(StandardTags.defaults());

            for (var entry : mcColors.entrySet()) {
                NamedTextColor color = entry.getValue();
                builder.tag(entry.getKey(), Tag.styling(s -> s.color(color)));
            }
            for(var entry : mcStyles.entrySet()) {
                builder.tag(entry.getKey(), Tag.styling(s -> s.decorate(entry.getValue())));
            }
            for(var color : RGBColor.values()){
                if(color.toString().contains("gradient")) continue;
                String key = color.name();
                if((key.split("_").length==1)){
                    key = "C_"+key;
                }
                builder.tag(key.toLowerCase(), Tag.styling(s->s.color(color.getTextColor())));
            }
            mm = MiniMessage.builder()
                    .tags(builder.build())
                    .build();
        }return mm;
    }
    public static String colored(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static Component mmC(String s){
        return mmColored(s);
    }
    public static Component mmColored(String s){
        return getMM().deserialize(s).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
    public static String mmcToString(Component component){
        return MiniMessage.miniMessage().serialize(component);
    }
    public static TextColor asTextColor(String color){
        return mmC(color).color();
    }

    public static Component set(Component c, String placeholder, String replacement) {
        return set(c, placeholder, Component.text(replacement));
    }

    public static Component set(Component c, String placeholder, Component replacement) {
        // directly replace in the Component
        return c.replaceText(TextReplacementConfig.builder()
                .matchLiteral(placeholder)
                .replacement(replacement)
                .build());
    }

    // For multiple Components, avoid copying the list unless necessary
    public static List<Component> set(List<Component> lore, String placeholder, String replacement) {
        return set(lore, placeholder, Component.text(replacement));
    }

    public static List<Component> set(List<Component> lore, String placeholder, Component replacement) {
        List<Component> result = new ArrayList<>(lore.size());
        for (Component c : lore) {
            result.add(c.replaceText(TextReplacementConfig.builder()
                    .matchLiteral(placeholder)
                    .replacement(replacement)
                    .build()));
        }
        return result;
    }

    public static Color fromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Hex color must be 6 characters long");
        }
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return Color.fromRGB(r, g, b);
    }
}
