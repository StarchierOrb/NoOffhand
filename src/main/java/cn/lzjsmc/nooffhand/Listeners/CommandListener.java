package cn.lzjsmc.nooffhand.Listeners;

import cn.lzjsmc.nooffhand.NoOffhand;
import cn.lzjsmc.nooffhand.util.ConfigManager;
import cn.lzjsmc.nooffhand.util.ItemStackReader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListener implements TabExecutor {
    private final NoOffhand plugin;
    private final ConfigManager configManager;
    private final ItemStackReader itemStackReader;
    public CommandListener(NoOffhand plugin, ConfigManager configManager, ItemStackReader itemStackReader) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.itemStackReader = itemStackReader;
    }
    private final String[] subCmd = {"reload"};
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("nof")){
            if(!sender.hasPermission("nooffhand.admin") && sender instanceof Player) {
                sender.sendMessage(configManager.getMsg("no-permission"));
                return true;
            }
            if(args.length<1) {
                List<String> help = configManager.getMsgList("NoOffhand.messages.help");
                for(String s : help) {
                    sender.sendMessage(s);
                }
                return true;
            }
            switch (args[0]) {
                default: {
                    sender.sendMessage(configManager.getMsg("command-usage"));
                    return true;
                }
                case "reload": {
                    plugin.getLogger().info("Reloading config...");
                    configManager.reloadConfig();
                    if(!itemStackReader.checkItemsList()) {
                        sender.sendMessage(configManager.getMsg("reload-failed"));
                    }
                    sender.sendMessage(configManager.getMsg("reload-completed"));
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if(sender instanceof Player && !sender.hasPermission("nooffhand.admin")) {
            return new ArrayList<>();
        }
        if(args.length<2) {
            return Arrays.stream(subCmd).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
