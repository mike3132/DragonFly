
package net.resolutemc.dragonfly.chat;

import net.resolutemc.dragonfly.DragonFly;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class PlayerMessage {

    public static void sendMessage(Player player, String key) {
        File messagesConfig = new File(DragonFly.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void fuelPlaceholderMessage(Player player, String key, String fuel) {
        File messagesConfig = new File(DragonFly.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        message = message.replace("%Fuel%", fuel);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void playerPlaceholderMessage(Player player, String key, String target) {
        File messagesConfig = new File(DragonFly.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        message = message.replace("%playerName%", target);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
