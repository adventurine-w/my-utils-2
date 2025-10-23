package org.adv.clickerflex.essentials.chat;

import net.kyori.adventure.text.Component;
import org.adv.clickerflex.menus.SimpleMenu;
import org.adv.clickerflex.menus.annotations.AutoRegisterMenu;
import org.adv.clickerflex.menus.annotations.SimpleMenuType;
import org.adv.clickerflex.player.CustomPlayer;
import org.adv.clickerflex.player.data.PlayerData;
import org.adv.clickerflex.utils.generic.IntList;
import org.adv.clickerflex.utils.mc.ItemCreator;
import org.adv.clickerflex.ultimate_utils.server_side.SendUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.adv.clickerflex.utils.generic.ColorUtils.mmC;

@AutoRegisterMenu(menuType = SimpleMenuType.NO_INSERT)
public class ChatColorMenu extends SimpleMenu {
    private final CustomPlayer player;

    public ChatColorMenu(Player player) {
        super(6*9, Component.text("Chat Colors"));
        this.player = CustomPlayer.of(player);
        reload();
    }

    private static final List<Integer> slots = IntList.ofBetween(10,16).addBetween(19,25).addBetween(28,34).addBetween(37,43).get();

    private final Map<Integer, ChatColor> cacheMap = new HashMap<>();
    public void reload(){
        cacheMap.clear();
        int n = 0;
        IntList.ofBetween(0,53).doAll(x->inv.setItem(x, super.getBlackPane()));
        for(int slot : slots) inv.setItem(slot, ItemCreator.of(Material.STONE_BUTTON).hideTooltip().build());
        for(ChatColor c : ChatColor.cachedValues){
            int slot = slots.get(n);
            inv.setItem(slot, getSprite(c));
            cacheMap.put(slot, c);
            n++;
        }
        inv.setItem(49, super.getCloseMenuSprite());
    }

    private ItemStack getSprite(ChatColor c){
        boolean hasAccess = c.hasAccess(player);
        boolean selected = player.getData().getChatColor()==c;
        ItemCreator creator = ItemCreator.of((hasAccess ?Material.FLOW_BANNER_PATTERN:Material.GUSTER_BANNER_PATTERN), c.getColor()+c.getName()+" Chat Color").addLoreC(lore->{
            lore.add(mmC("<7>Preview:"));
            lore.add(new ChatFormat(player, "Hey!").setChatColor(c).build());
            lore.add(mmC(""));
            if(hasAccess){
                lore.add(mmC(selected?"<a><b>SELECTED":super.getClickHereToX("select")));
            }else{
                lore.add(mmC("<c>Haven't Unlocked Yet!"));
            }
        });
        if(selected) creator.glow();
        return creator.build();


    }

    @Override
    public void open(Player player) {
        if(isDisabledAndSendGenericMessage(this.getClass(), player)) return;
        player.openInventory(this.inv);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, boolean isClickOnSimpleMenu) {
        if(isClickOnSimpleMenu){

            Player player = (Player) e.getWhoClicked();

            if(cacheMap.get(e.getSlot())==null) return;
            ChatColor chatColor = cacheMap.get(e.getSlot());
            if(chatColor.hasAccess(CustomPlayer.of(player))){
                player.closeInventory();
                PlayerData.of(player).setChatColor(chatColor);
                SendUtils.send(player, "<a>You equipped "+chatColor.getColor()+chatColor.getName()+" <a>as your current chat chatColor!", Sound.BLOCK_COMPARATOR_CLICK);
            }
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent e) {

    }
}
