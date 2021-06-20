package cn.lzjsmc.nooffhand;

import cn.lzjsmc.nooffhand.Listeners.CommandListener;
import cn.lzjsmc.nooffhand.Listeners.InventoryListener;
import cn.lzjsmc.nooffhand.bStats.MetricsLite;
import cn.lzjsmc.nooffhand.util.ConfigManager;
import cn.lzjsmc.nooffhand.util.ItemHandler;
import cn.lzjsmc.nooffhand.util.ItemStackReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class NoOffhand extends JavaPlugin {
    private final String[] msgCN = {
            "┏  正在加载配置文件...",
            "┣  正在注册监听器...",
            "┣  正在注册指令...",
            "┗  插件加载完毕！",
            "  支持一下，来发个电呗~",
            "   afdian.net/@Starc"
    };
    private final String[] msgEN = {
            "┏  Initializing config files...",
            "┣  Initializing listeners...",
            "┣  Registering commands...",
            "┗  Plugin enabled!",
            " Good to use? Welcome to donate us:",
            "   www.paypal.me/starchier"
    };
    @Override
    public void onEnable() {
        // Plugin startup logic
        String[] msg;
        Locale locale = Locale.getDefault();
        if(locale.getLanguage().equalsIgnoreCase("zh")) {
            msg = msgCN;
        } else {
            msg = msgEN;
        }
        this.getLogger().info(msg[0]);
        saveDefaultConfig();
        ConfigManager configManager = new ConfigManager(this);
        ItemStackReader itemStackReader = new ItemStackReader(this, configManager);
        itemStackReader.checkItemsList();
        ItemHandler itemHandler = new ItemHandler(this, itemStackReader, configManager);
        getLogger().info(msg[1]);
        getServer().getPluginManager().registerEvents(new InventoryListener(itemHandler), this);
        getLogger().info(msg[2]);
        getCommand("nof").setExecutor(new CommandListener(this, configManager, itemStackReader));
        getCommand("nof").setTabCompleter(new CommandListener(this, configManager, itemStackReader));
        getLogger().info(msg[3]);
        getLogger().info(msg[4]);
        getLogger().info(msg[5]);
        MetricsLite metricsLite = new MetricsLite(this, 8689);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
