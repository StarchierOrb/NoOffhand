package cn.lzjsmc.nooffhand.util;

import cn.lzjsmc.nooffhand.NoOffhand;
import org.bukkit.Material;

import java.util.List;

public class ItemStackReader {
    private final NoOffhand plugin;
    private ConfigManager configManager;
    public ItemStackReader(NoOffhand plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    public boolean checkItemsList() {
        List<String> list = configManager.getCfg().getStringList(plugin.getName()+".settings.list");
        int t=0;
        for(String s : list) {
            if(Material.matchMaterial((s.contains(":")?s.split(":")[0]:s))==null) {
                t++;
                plugin.getLogger().warning(configManager.getMsg("item-not-exist").replace("%s",s));
            }
        }
        return t == 0;
    }
    public boolean isItem(String s) {
        return Material.matchMaterial((s.contains(":")?s.split(":")[0]:s))!=null;
    }
}
