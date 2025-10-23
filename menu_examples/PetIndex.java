package org.adv.clickerflex.menus.guide;

import net.kyori.adventure.text.Component;
import org.adv.clickerflex.item.CustomItem;
import org.adv.clickerflex.item.tracker.ItemTracker;
import org.adv.clickerflex.menus.SimpleMenu;
import org.adv.clickerflex.menus.annotations.AutoRegisterMenu;
import org.adv.clickerflex.menus.annotations.SimpleMenuType;
import org.adv.clickerflex.pets.actual_code.PetConfig;
import org.adv.clickerflex.pets.actual_code.PetData;
import org.adv.clickerflex.pets.actual_code.PetLevel;
import org.adv.clickerflex.pets.actual_code.menus.PetsMenu;
import org.adv.clickerflex.pets.pet_index.Pet;
import org.adv.clickerflex.ultimate_utils.ListRotation;
import org.adv.clickerflex.utils.generic.IntList;
import org.adv.clickerflex.zlogging.Log;
import org.adv.clickerflex.utils.mc.ItemCreator;
import org.adv.clickerflex.utils.mc.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@AutoRegisterMenu(menuType= SimpleMenuType.NO_INSERT)
public class PetIndex extends SimpleMenu {

    private static final HashMap<Integer, PetIndex> cache = new HashMap<>();

    private static final ListRotation<Integer> rotation = new ListRotation<>(List.of(1, 50, 100));
    public static void open(Player player, int lvl){
        if(lvl==0 || lvl > PetConfig.MAX_LEVEL){
            Log.warn("Tried opening a Pet Index for "+player.getName()+", failed due invalid lvl (Lvl "+lvl+")");
        }
        if(!cache.containsKey(lvl)) cache.put(lvl, new PetIndex(lvl));
        cache.get(lvl).open(player);
    }

    private final int lvl;
    public PetIndex(int lvl) {
        super(5*9, Component.text("Pet Index - Lvl "+lvl+" (Preview)"));
        this.lvl=lvl;
        reload();
    }

    private static final int SWAP_SLOT = 42;
    private static final int GO_BACK_SLOT = 43;

    private static final List<Integer> slots = IntList.ofBetween(10,16).addBetween(19,25).addBetween(28,34).get();
    public void reload(){
        IntList.ofBetween(0,44).doAll(x->inv.setItem(x, super.getBlackPane()));
        int n = 0;
        super.setItem(slots, ItemStack.empty());
        for(Pet pet : Pet.cachedValues){
            if(n >= slots.size()) break;
            if(pet==Pet.CORRUPTED) continue;
            inv.setItem(slots.get(n), getSprite(pet));
            n++;
        }
        inv.setItem(40, super.getCloseMenuSprite());
        inv.setItem(SWAP_SLOT, ItemCreator.of(Material.EMERALD_BLOCK, "<a>Preview Other Levels!").addLore(lore->{
            lore.add("<7>Click here to swap between");
            lore.add("<7>and preview multiple levels!");
            lore.add("");
            lore.add(rotation.toString(this.lvl));
            lore.add("");
            lore.add(super.getClickHereToX("swap", "<a>", true));
        }).build());
        inv.setItem(GO_BACK_SLOT, ItemCreator.of(Material.BONE, "<6>Pets").addLore(lore->{
            lore.add("");
            lore.add("<7>Click here to view");
            lore.add("<7>your pets menu.");
            lore.add("");
            lore.add(super.getClickHereToX("view"));
        }).build());
    }

    private ItemStack getSprite(Pet pet){
        PetData data = new PetData(pet, ItemTracker.create());
        data.setTotalEXP(PetLevel.of(pet.getRarity(), lvl).getExpRequirement());
        CustomItem cItem = data.toItem();
        if(cItem==null) return ItemCreator.of(Material.BARRIER, "<c><b>INVALID SPRITE!").addLore("<c>Pet: "+pet.getName()).build();
        ItemStack sprite = cItem.getItemReference().clone();
        ItemStackUtils.addLore(sprite, List.of(
                "<8><st>                                                   ",
                "<7><b>THIS IS A PREVIEW!"
        ));
        return sprite;
    }

    @Override
    public void open(Player player) {
        if(isDisabledAndSendGenericMessage(this.getClass(), player)) return;
        player.openInventory(this.inv);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e, boolean isClickOnSimpleMenu) {
        if(!isClickOnSimpleMenu) return;
        Player player = (Player) e.getWhoClicked();
        if(e.getSlot()==SWAP_SLOT){
            int nextRotationLvl = rotation.getNext(this.lvl);
            open(player, nextRotationLvl);
            player.playSound(player, Sound.BLOCK_COMPARATOR_CLICK, 1f, 1f);
        }else if(e.getSlot()==GO_BACK_SLOT){
            new PetsMenu(player).open(player);
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent e) {

    }
}
