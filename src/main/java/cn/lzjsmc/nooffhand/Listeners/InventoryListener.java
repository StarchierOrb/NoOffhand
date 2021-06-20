package cn.lzjsmc.nooffhand.Listeners;

import cn.lzjsmc.nooffhand.util.ItemHandler;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private final ItemHandler itemHandler;
    //private final NoOffhand plugin;
    public InventoryListener(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent evt) {
        if(evt.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if(hasPermission((Player) evt.getWhoClicked())) {
            return;
        }
        if(evt.getSlot()!=40 && !(evt.getAction() == InventoryAction.HOTBAR_SWAP && !evt.getClick().isKeyboardClick())) {
            return;
        }
        if(evt.getClickedInventory().getType() != InventoryType.PLAYER) {
            return;
        }
        ItemStack cursor;
        if(evt.getAction() == InventoryAction.HOTBAR_SWAP) {
            if(!evt.getClick().isKeyboardClick()) {
                if(evt.getClickedInventory().getItem(evt.getSlot())==null) {
                    return;
                }
                cursor = evt.getClickedInventory().getItem(evt.getSlot());
            } else {
                if(evt.getClickedInventory().getItem(evt.getHotbarButton())==null) {
                    return;
                }
                cursor = evt.getClickedInventory().getItem(evt.getHotbarButton());
            }
        } else {
            cursor = evt.getCursor();
        }
        if(cursor.getType()==Material.AIR) {
            return;
        }
        if(hasSubPermission(((Player) evt.getWhoClicked()), cursor.getType())) {
            return;
        }
        if(itemHandler.canPass(cursor)) {
            return;
        }
        evt.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent evt) {
        if(evt.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if(hasPermission((Player) evt.getWhoClicked())) {
            return;
        }
        if(!evt.getInventorySlots().contains(40)) {
            return;
        }
        if(evt.getInventory().getType()!=InventoryType.CRAFTING) {
            return;
        }
        ItemStack cursor = evt.getOldCursor();
        if(hasSubPermission((Player) evt.getWhoClicked(), cursor.getType())) {
            return;
        }
        if(itemHandler.canPass(cursor)) {
            return;
        }
        evt.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent evt) {
        if(hasPermission((Player) evt.getPlayer())) {
            return;
        }
        ItemStack offhand = evt.getPlayer().getInventory().getItemInOffHand();
        if(offhand.getType()==Material.AIR) {
            return;
        }
        if(hasSubPermission((Player) evt.getPlayer(), offhand.getType())) {
            return;
        }
        if(itemHandler.canPass(offhand)) {
            return;
        }
        evt.getPlayer().getInventory().setItemInOffHand(null);
        evt.getPlayer().getWorld().dropItem(evt.getPlayer().getLocation(), offhand);
    }
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent evt) {
        if(hasPermission(evt.getPlayer())) {
            return;
        }
        if(evt.getOffHandItem().getType() == Material.AIR) {
            return;
        }
        if(hasSubPermission(evt.getPlayer(), evt.getOffHandItem().getType())) {
            return;
        }
        if(itemHandler.canPass(evt.getOffHandItem())) {
            return;
        }
        evt.setCancelled(true);
    }
    public boolean hasPermission(Player p) {
        return p.hasPermission("nooffhand.bypass");
    }
    public boolean hasSubPermission(Player p, Material item) {
        return p.hasPermission("nooffhand.bypass." + item.name());
    }
}
