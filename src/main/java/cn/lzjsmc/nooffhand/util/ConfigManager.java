package cn.lzjsmc.nooffhand.util;

import cn.lzjsmc.nooffhand.NoOffhand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private NoOffhand plugin;
    private FileConfiguration cfg = null;
    public ConfigManager(NoOffhand plugin) {
        this.plugin = plugin;
    }
    public String getMsg(String path) {
        String msg = getCfg().getString("NoOffhand.messages."+path, null);
        if(msg==null) {
            return null;
        }
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return msg;
    }
    public String getSetting(String path) {
        return getCfg().getString("NoOffhand.settings."+path, null);
    }
    public List<String> getMsgList(String path) {
        List<String> msg = new ArrayList<>();
        for(String s : getCfg().getStringList(path)) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            msg.add(s);
        }
        return msg;
    }
    public FileConfiguration getCfg() {
        if(cfg==null) {
            plugin.reloadConfig();
            cfg = plugin.getConfig();
        }
        return cfg;
    }
    public void reloadConfig() {
        plugin.reloadConfig();
        cfg = plugin.getConfig();
    }
}
