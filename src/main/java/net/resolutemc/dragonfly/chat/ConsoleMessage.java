package net.resolutemc.dragonfly.chat;

import net.resolutemc.dragonfly.DragonFly;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConsoleMessage {

    public static void sendMessageNoPrefix(CommandSender sender, String key) {
        File messagesConfig = new File(DragonFly.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages." + key, "message");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}