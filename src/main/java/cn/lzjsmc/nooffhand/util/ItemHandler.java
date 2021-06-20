package cn.lzjsmc.nooffhand.util;

import cn.lzjsmc.nooffhand.NoOffhand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemHandler {
    private final NoOffhand plugin;
    private final ConfigManager configManager;
    private final ItemStackReader itemStackReader;
    private boolean isLoreAllowList;

    public ItemHandler(NoOffhand plugin, ItemStackReader itemStackReader, ConfigManager configManager) {
        this.plugin = plugin;
        this.itemStackReader = itemStackReader;
        this.configManager = configManager;
    }
    public boolean canPassLore(ItemStack offhand) {
        List<String> list = configManager.getMsgList("NoOffhand.settings.lore-list");
        if(!offhand.hasItemMeta()) {
            return true;
        }
        if(!offhand.getItemMeta().hasLore()) {
            return true;
        }
        List<String> lore = offhand.getItemMeta().getLore();
        for(String s : list) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            for(String l : lore) {
                if(l.contains(s)) {
                    return isLoreAllowList;
                }
            }
        }
        return !isLoreAllowList;
    }
    public boolean canPass(ItemStack offhand) {
        boolean isWhitelist = configManager.getSetting("limit-type").equalsIgnoreCase("allowlist");
        isLoreAllowList = configManager.getSetting("lore-limit-type").equalsIgnoreCase("allowlist");
        List<String> list = configManager.getMsgList("NoOffhand.settings.list");
        if(offhand.getType()==Material.AIR) {
            return true;
        }
        boolean isBlockedItem = isWhitelist;
        for(String s : list) {
            if(!itemStackReader.isItem(s)) {
                continue;
            }
            if(s.contains(":")) {
                ItemStack item = new ItemStack(Material.matchMaterial(s.split(":")[0]), 1, Short.parseShort(s.split(":")[1]));
                if(offhand.isSimilar(item)) {
                    isBlockedItem = !isWhitelist;
                }
            } else {
                if(Material.matchMaterial(s)==offhand.getType()) {
                    isBlockedItem = !isWhitelist;
                }
            }
        }
        try {
            if (!isBlockedItem || (isLoreAllowList && offhand.getItemMeta().hasLore())) {
                return canPassLore(offhand);
            }
        } catch (NullPointerException ignored) {}
        return false;
    }
}