package com.verang.verangchatformatter;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VerangChatFormatter extends JavaPlugin implements Listener {
    private LuckPerms luckPerms;
    private String chatFormat;

    @Override
    public void onEnable() {
        this.luckPerms = LuckPermsProvider.get();
        this.saveDefaultConfig();
        this.chatFormat = getConfig().getString("chat-format", "§8➛§f");
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("reloadconfig").setExecutor(this);

        // DEBUG - IGNORE THIS
        System.out.println("Verang Chat Formatter - Loaded chat format: " + this.chatFormat);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reloadconfig")) {
            reloadConfig();
            chatFormat = getConfig().getString("chat-format", "§8➛§f");
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully.");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        User user = luckPerms.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }

        String prefix = user.getCachedData().getMetaData().getPrefix();
        prefix = prefix != null ? ChatColorUtil.translateHexColorCodes(ChatColor.translateAlternateColorCodes('&', prefix)) : "";

        String playerName = ChatColorUtil.translateHexColorCodes(event.getPlayer().getName());
        String message = event.getMessage();

        message = "§f" + ChatColorUtil.translateHexColorCodes(message);

        String format = String.format("%s %s %s %s", prefix, playerName, this.chatFormat, message);
        event.setFormat(format);
    }
}
