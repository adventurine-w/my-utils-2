package org.adv.clickerflex.utils.mc;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.adv.clickerflex.utils.generic.ColorUtils.mmColored;


// recommended use in gui only or item sprites
public class ItemCreator implements Cloneable {
    private String name;

    protected final List<Component> lore = new ArrayList<>();

    private ItemMeta meta;
    @Setter
    private ItemStack sprite;

    private ItemCreator(ItemStack sprite, String name){
        this.sprite=sprite;
        this.name = name;
    }
    public ItemCreator clone() {
        ItemStack clonedSprite = sprite.clone();
        ItemCreator clone = new ItemCreator(clonedSprite, this.name);
        if (this.meta != null) {
            clone.meta = clonedSprite.getItemMeta();
        }
        clone.lore.addAll(this.lore);
        return clone;
    }
    public static ItemCreator of(Material material){
        return ItemCreator.of(material, null);
    }
    public static ItemCreator of(Material material, String name){
        return new ItemCreator(new ItemStack(material), name);
    }
    public static ItemCreator of(ItemStack item){
        return ItemCreator.of(item, null);
    }
    public static ItemCreator of(ItemStack item, String name){
        return new ItemCreator(item.clone(), name);
    }
    public static @NotNull ItemCreator ofSkull(String texture){
        return ofSkull(texture, null);
    }
    public static ItemCreator ofSkull(String texture, String name){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
        playerProfile.setProperty(new ProfileProperty("textures", texture));
        meta.setPlayerProfile(playerProfile);
        item.setItemMeta(meta);
        return new ItemCreator(item, name);
    }
    private ItemMeta getMeta(){
        if(meta==null) {
            this.meta = sprite.getItemMeta();
        }
        return this.meta;
    }
    public ItemCreator setColor(Color color){
        if(getMeta() instanceof LeatherArmorMeta ameta){
            ameta.setColor(color);
        }else if(getMeta() instanceof PotionMeta pmeta){
            pmeta.setColor(color);
        }else if(getMeta() instanceof FireworkEffectMeta fmeta){
            fmeta.setEffect(FireworkEffect.builder().withColor(Color.RED).build());
        }
        return this;
    }
    public ItemCreator setPotionType(PotionType potionType){
        if(getMeta() instanceof PotionMeta pmeta) {
            pmeta.setBasePotionType(potionType);
        }
        return this;
    }
    public ItemCreator setTrim(TrimMaterial material, TrimPattern pattern){
        if(getMeta() instanceof ArmorMeta ameta){
            ameta.setTrim(new ArmorTrim(material, pattern));
        }
        return this;
    }
    public ItemCreator addLore(Consumer<List<String>> consumer){
       return addLoreWP("", consumer);
    }
    public ItemCreator addLoreC(Consumer<List<Component>> consumer){
        List<Component> list = new ArrayList<>();
        consumer.accept(list);
        lore.addAll(list);
        return this;
    }
    public ItemCreator addLoreWP(String lorePrefix, Consumer<List<String>> consumer){
        List<String> list = new ArrayList<>();
        consumer.accept(list);
        return addLoreWP(lorePrefix, list.toArray(String[]::new));
    }
    public ItemCreator addLore(String... lore){
        return addLoreWP("", lore);
    }
    public ItemCreator addLoreWP(String lorePrefix, String... lore){
        for(String line : lore){
            if(line.trim().isEmpty()){
                this.lore.add(Component.text(""));
            }else{
                this.lore.add(mmColored(lorePrefix+line));
            }
        }
        return this;
    }

    public ItemCreator addLoreCWP(String lorePrefix, Consumer<List<Component>> consumer){
        List<Component> list = new ArrayList<>();
        consumer.accept(list);
        for(var x : list){
            lore.add(mmColored(lorePrefix).append(x));
        }
        return this;
    }
    public ItemCreator setName(String name){
        this.name=name;
        return this;
    }
    public ItemCreator setAmount(int amt){
        getMeta().setMaxStackSize(99);
        this.sprite.setAmount(amt);
        return this;
    }
    public ItemCreator hideTooltip(){
        getMeta().setHideTooltip(true);
        return this;
    }
    public ItemCreator convertToGUIItem(){
        ItemMeta meta = getMeta();
        meta.setAttributeModifiers(sprite.getType().getDefaultAttributeModifiers());
        meta.addItemFlags(ItemFlag.values());
        meta.setUnbreakable(true);
        return this;
    }
    public ItemCreator setGlow(){ return glow(); }
    public ItemCreator setGlow(boolean glow){ return glow(glow); }
    public ItemCreator glow(){
        getMeta().setEnchantmentGlintOverride(true);
        return this;
    }
    public ItemCreator glow(boolean glow){
        getMeta().setEnchantmentGlintOverride(glow);
        return this;
    }
    protected ItemStack build(List<Component> lore){
        ItemMeta meta = getMeta();
        if(name!=null) meta.displayName(mmColored(name));
        meta.lore(lore);
        sprite.setItemMeta(meta);
        return sprite;
    }
    public ItemCreatorLore migrate(){
        return new ItemCreatorLore(this);
    }
    public ItemStack build(){
        return build(this.lore);
    }
}