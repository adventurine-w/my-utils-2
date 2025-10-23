package org.adv.clickerflex.menus.annotations;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.adv.clickerflex.Clickerflex;
import org.adv.clickerflex.menus.SimpleMenu;
import org.adv.clickerflex.utils.annotations.AutoRegisterListener;
import org.adv.clickerflex.utils.generic.RuntimeChecker;
import org.adv.clickerflex.utils.mc.ItemStackUtils;
import org.adv.clickerflex.zlogging.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
@AutoRegisterListener
public class AutoRegisterMenuInit implements Listener {
    public static Set<Class<? extends SimpleMenu>> menuList = new HashSet<>();
    public AutoRegisterMenuInit(){
        if(!menuList.isEmpty()){ Log.warn("AutoRegisterMenuInit is being initiated again?"); return; }

        RuntimeChecker rtc = new RuntimeChecker("AutoRegisterMenuInit");

        Reflections reflections = new Reflections(Clickerflex.getPackageName());
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(AutoRegisterMenu.class);

        for (Class<?> clazz : annotated) {
            if(SimpleMenu.class.isAssignableFrom(clazz)) {
                menuList.add((Class<? extends SimpleMenu>) clazz);
            }
        }
        rtc.lap();
        Log.info("Initialized "+ menuList.size()+ " classes using @AutoRegisterMenu.");
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        try {
            if (isCloseMenu(e.getCurrentItem())) {
                e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                return;
            }
            InventoryHolder topHolder = e.getView().getTopInventory().getHolder(false);
            InventoryHolder clickedHolder = e.getClickedInventory() != null
                    ? e.getClickedInventory().getHolder(false)
                    : null;
            if (clickedHolder == null) return;
            if(!(topHolder instanceof SimpleMenu)) return;
            for (Class<? extends SimpleMenu> clazz : menuList) {
                if (clazz.isInstance(topHolder)) {
                    AutoRegisterMenu annotation = clazz.getAnnotation(AutoRegisterMenu.class);
                    if (annotation.menuType() == SimpleMenuType.NO_INSERT) {
                        e.setCancelled(true);
                        //if (e.getClickedInventory() == null || !clazz.isInstance(clickedHolder)) return;
                        if (e.getClickedInventory() == null) return;
                    }
                    SimpleMenu menu = (SimpleMenu) topHolder;
                    menu.handleClick(e, clickedHolder==menu);
                    return;
                }
            }
        }catch(Exception exception){
            e.setCancelled(true);
            Log.warn("An error occurred in AutoRegisterMenuInit#onInventoryClick!");
            Log.warn(exception);
        }
    }
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().getHolder(false) instanceof SimpleMenu menu) {
            try {
                InventoryHolder clickedHolder = e.getInventory().getHolder(false);
                menu.handleDrag(e, clickedHolder==menu);
            }catch(Exception exception){
                Log.warn("An error occurred in AutoRegisterMenuInit#onInventoryDrag!");
                Log.warn(exception);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        InventoryHolder invHolder = e.getView().getTopInventory().getHolder(false);
        if (invHolder == null) return;
        for (Class<? extends SimpleMenu> clazz : menuList) {
            if (clazz.isInstance(invHolder)) {
                SimpleMenu menu = (SimpleMenu) invHolder;
                menu.handleClose(e);
                return;
            }
        }
    }
    private boolean isCloseMenu(ItemStack item){
        if(!ItemStackUtils.isValid(item)) return false;
        Component name = item.displayName();
        String plain = PlainTextComponentSerializer.plainText().serialize(name);
        return !plain.isEmpty() && plain.toUpperCase().contains("CLOSE MENU");
    }
}
