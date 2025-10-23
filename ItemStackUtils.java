package org.adv.clickerflex.utils.mc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.adv.clickerflex.utils.generic.ColorUtils;
import org.adv.clickerflex.utils.generic.WordUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemStackUtils {
    public static boolean isValid(ItemStack item){
        return !(item==null || item.isEmpty());
    }
    public static boolean isLeather(ItemStack item){
        Material mat = item.getType();
        return (mat==Material.LEATHER_HELMET || mat==Material.LEATHER_CHESTPLATE || mat==Material.LEATHER_LEGGINGS || mat==Material.LEATHER_BOOTS);
    }

    public static ItemStack dye(ItemStack item, Color color){
        ItemMeta meta = item.getItemMeta();
        if(meta instanceof LeatherArmorMeta lmeta) {
            lmeta.setColor(color);
            item.setItemMeta(lmeta);
        }
        return item;
    }
    public static ItemStack dye(Material material, Color color){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if(meta instanceof LeatherArmorMeta lmeta) {
            lmeta.setColor(color);
            item.setItemMeta(lmeta);
        }
        return item;
    }
    public static ItemStack trim(ItemStack item, TrimMaterial trimMaterial, TrimPattern trimPattern){
        ItemMeta meta = item.getItemMeta();
        if(meta instanceof ArmorMeta ameta){
            ameta.setTrim(new ArmorTrim(trimMaterial, trimPattern));
            item.setItemMeta(ameta);
        }
        return item;
    }
    public static ItemStack addLore(ItemStack item, String... lore){
        List<Component> itemLore = item.lore();

        if(itemLore==null) itemLore = new ArrayList<>();
        if(isValid(item)) {
            itemLore.addAll(Arrays.stream(lore).map(ColorUtils::mmColored).toList());
            item.lore(itemLore);
        }
        return item;
    }
    public static ItemStack addLore(ItemStack item, Component lore){
        List<Component> itemLore = item.lore();
        if(itemLore==null) itemLore = new ArrayList<>();
        if(isValid(item)) {
            itemLore.add(lore);
            item.lore(itemLore);
        }
        return item;
    }
    public static ItemStack addLore(ItemStack item, List<String> lore){
        List<Component> itemLore = item.lore();
        if(itemLore==null) itemLore = new ArrayList<>();
        if(isValid(item)) {
            itemLore.addAll(lore.stream().map(ColorUtils::mmColored).toList());
            item.lore(itemLore);
        }
        return item;
    }
    public static ItemStack removeLore(ItemStack item, int amountOfLines){
        List<Component> itemLore = item.lore();
        if(itemLore==null || itemLore.isEmpty()) return item;
        if(amountOfLines>itemLore.size()) {
            amountOfLines = itemLore.size();
        }
        for(int i=0;i<amountOfLines;i++){
            itemLore.removeLast();
        }
        item.lore(itemLore);
        return item;

    }
    public static ItemStack glow(ItemStack item){
        try {
            ItemMeta meta = item.getItemMeta();
            meta.setEnchantmentGlintOverride(true);
            item.setItemMeta(meta);
        }catch(Exception ignored){}
        return item;
    }
    /*
    function CNN(item: item) :: text:
	if name of {_item} is not set:
		set {_return} to type of {_item}
	else:
		set {_return} to name of {_item}
	set {_return} to "%{_return}%" in proper case
	return "&f%{_return}%"
     */
    public static Component CNN(ItemStack item){
        if(!isValid(item)) return Component.text("");
        Component name;
        var cache = item.getItemMeta();
        if (item.hasItemMeta() && cache.hasDisplayName()) {
            name = cache.displayName();
        } else {
            name = Component.text(WordUtils.camelToProper(item.getType().name())).color(NamedTextColor.WHITE);
        }
        return name;
    }
    public static List<ItemStack> setAmount(ItemStack item, int amt){
        int maxStackSize = item.getMaxStackSize();
        List<ItemStack> items = new ArrayList<>();
        if(amt<=maxStackSize){
            ItemStack clone = item.clone();
            clone.setAmount(amt);
            items.add(clone);
            return items;
        }
        int stacks = (int) Math.floor((double) amt / maxStackSize);
        int leftOver = amt % maxStackSize;
        if(stacks>=1) {
            for (int i = 0; i < stacks; i++) {
                ItemStack clone = item.clone();
                clone.setAmount(maxStackSize);
                items.add(clone);
            }
        }
        if(leftOver>=1) {
            ItemStack clone = item.clone();
            clone.setAmount(leftOver);
            items.add(clone);
        }
        return items;
    }
    public static void removeOne(ItemStack item){
        item.setAmount(item.getAmount()-1);
    }
    public static void removeX(ItemStack item, int x){
        item.setAmount(item.getAmount()-x);
    }

    public static ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player); // works for both online & offline players
            head.setItemMeta(meta);
        }
        return head;
    }
}
