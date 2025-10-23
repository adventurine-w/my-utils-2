package org.adv.clickerflex.menus;

import de.tr7zw.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import org.adv.clickerflex.menus.annotations.AutoRegisterMenu;
import org.adv.clickerflex.menus.annotations.SimpleMenuType;
import org.adv.clickerflex.ultimate_utils.server_side.SendUtils;
import org.adv.clickerflex.utils.generic.IntList;
import org.adv.clickerflex.utils.mc.ItemCreator;
import org.adv.clickerflex.utils.mc.ItemStackUtils;
import org.adv.clickerflex.utils.mc.RGBColor;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

import static org.adv.clickerflex.utils.mc.MessageBuilder.small;

public abstract class SimpleMenu implements InventoryHolder {

    private static final HashMap<Class<? extends SimpleMenu>, Boolean> enabledDisabledMap = new HashMap<>();
    public void enableMenu(Class<? extends SimpleMenu> clazz){
        enabledDisabledMap.put(clazz, true);
    }
    public void disableMenu(Class<? extends SimpleMenu> clazz){
        enabledDisabledMap.put(clazz, false);
    }
    public boolean isEnabled(Class<? extends SimpleMenu> clazz){
        return enabledDisabledMap.getOrDefault(clazz, true);
    }



    protected final Inventory inv;
    public SimpleMenu(int size, Component title){
        this.inv = Bukkit.createInventory(this, size, title);
    }
    public SimpleMenu(InventoryType type, Component title) {
        this.inv = Bukkit.createInventory(this, type, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
    public abstract void open(Player player);
    public boolean isDisabledAndSendGenericMessage(Class<? extends SimpleMenu> clazz, Player player){
        boolean disabled = !isEnabled(clazz);
        if(disabled) SendUtils.send(player, "<red>This menu is temporarily disabled."+(player.isOp()?" (But you're OP!)":""));
        return disabled && !player.isOp();
    }

    public abstract void onInventoryClick(InventoryClickEvent e, boolean isClickOnSimpleMenu);
    public void onInventoryDrag(InventoryDragEvent e, boolean isDragOnSimpleMenu){}
    public abstract void onInventoryClose(InventoryCloseEvent e);

    public void handleClick(InventoryClickEvent e, boolean isClickOnSimpleMenu) {
        Player p = (Player) e.getWhoClicked();
        if(this.reloading){
            if(p.isOp()){
                p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            }
            return;
        }
        if(isClickOnSimpleMenu && consumerMap.containsKey(e.getSlot())){
            consumerMap.get(e.getSlot()).accept(e, p);
        }
        this.onInventoryClick(e, isClickOnSimpleMenu);
    }
    public void handleDrag(InventoryDragEvent e, boolean isDragOnSimpleMenu){
        if(this.reloading){
            Player p = (Player) e.getWhoClicked();
            if(p.isOp()){
                p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            }
            return;
        }
        if(isDragOnSimpleMenu && this.getMenuType()==SimpleMenuType.NO_INSERT){
            e.setCancelled(true);
        }
        this.onInventoryDrag(e, isDragOnSimpleMenu);
    }
    public void handleClose(InventoryCloseEvent e){
        this.onInventoryClose(e);
    }

    private static final ItemStack CLOSE_SPRITE = ItemCreator.of(Material.BARRIER, "<red><bold>CLOSE MENU").build();
    private static final ItemStack BLACK_PANE_SPRITE = ItemCreator.of(Material.BLACK_STAINED_GLASS_PANE, "").hideTooltip().build();
    private static final ItemStack GRAY_PANE_SPRITE = ItemCreator.of(Material.GRAY_STAINED_GLASS_PANE, "").hideTooltip().build();
    private static final ItemStack LIGHT_GRAY_PANE_SPRITE = ItemCreator.of(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "").hideTooltip().build();
    private static final ItemStack GREEN_PANE_SPRITE = ItemCreator.of(Material.GREEN_STAINED_GLASS_PANE, "").hideTooltip().build();
    private static final ItemStack RED_PANE_SPRITE = ItemCreator.of(Material.RED_STAINED_GLASS_PANE, "").hideTooltip().build();
    private static final ItemStack PURPLE_PANE_SPRITE = ItemCreator.of(Material.PURPLE_STAINED_GLASS_PANE, "").hideTooltip().build();

    public static ItemStack CLOSE_MENU_SPRITE(){
        return CLOSE_SPRITE.clone();
    }
    public ItemStack getCloseMenuSprite(){
        return CLOSE_MENU_SPRITE();
    }
    public ItemStack getBlackPane(){ return BLACK_PANE_SPRITE; }
    public ItemStack getGrayPane() {
        return GRAY_PANE_SPRITE;
    }
    public ItemStack getLightGrayPane() {
        return LIGHT_GRAY_PANE_SPRITE;
    }
    public ItemStack getGreenPane(){
        return GREEN_PANE_SPRITE;
    }
    public ItemStack getRedPane(){
        return RED_PANE_SPRITE;
    }
    public ItemStack getPurplePane(){
        return PURPLE_PANE_SPRITE;
    }

    public String getClickHereToX(String x){
        return getClickHereToXXX(x);
    }
    public String getClickHereToX(String x, String color, boolean small){
        return getClickHereToXXX(x, color, small);
    }
    public static String getClickHereToXXX(String x){
        return RGBColor.LL_ORANGE+"<b>"+small("click here")+"</b> "+small("to "+x+"!");
    }
    public static String getClickHereToXXX(String x, String color, boolean small){
        if(small) {
            return color + "<b>" + small("click here") + "</b> " + small("to " + x + "!");
        }else{
            return color + "<b>CLICK HERE</b> to "+x+"!";
        }
    }

    private final HashMap<Integer, BiConsumer<InventoryClickEvent, Player>> consumerMap = new HashMap<>();
    private boolean reloading = false;

    public void setReloading(boolean reloading){
        this.reloading = reloading;
    }

    public void clearConsumers(){
        consumerMap.clear();
    }
    public void setConsumerMap(SimpleMenu other){
        consumerMap.clear();
        consumerMap.putAll(other.consumerMap);
    }
    public void setItem(int slot, Pair<ItemStack, BiConsumer<InventoryClickEvent, Player>> spriteAndBiCons){
        this.setItem(slot, spriteAndBiCons.getLeft(), spriteAndBiCons.getRight());
    }
    public void setItem(int slot, ItemStack item){
        if(!ItemStackUtils.isValid(item)){
            inv.setItem(slot, ItemStack.empty());
            return;
        }
        //this.inv.setItem(slot, item);
        if(this.getMenuType()==SimpleMenuType.NO_INSERT) {
            ItemStack sprite = item.clone();
            NBT.modify(sprite, nbt -> {
                nbt.setBoolean("dupe", true);
            });
            this.inv.setItem(slot, sprite);
            // todo: remove + log;
        }else{
            this.inv.setItem(slot, item);
        }
    }
    public void setItem(int slot, ItemStack item, BiConsumer<InventoryClickEvent, Player> consumer){
        // is in simple menu, doesn't make sense if it's not
        setItem(slot, item);
        this.consumerMap.put(slot, consumer);
    }
    public void setItem(IntList slots, ItemStack item){
        for(int slot : slots.get()){
            this.inv.setItem(slot, item);
        }
    }
    public void setItem(List<Integer> slots, ItemStack item){
        for(int slot : slots){
            this.inv.setItem(slot, item);
        }
    }
    public ItemStack getBackArrow(){
        return ItemCreator.of(Material.TIPPED_ARROW).setPotionType(PotionType.HEALING).convertToGUIItem().build();
    }
    public ItemStack getFrontArrow(){
        return ItemCreator.of(Material.TIPPED_ARROW).setPotionType(PotionType.OOZING).convertToGUIItem().build();
    }
    private SimpleMenuType getMenuType(){
        return this.getClass().getAnnotation(AutoRegisterMenu.class).menuType();
    }
}
