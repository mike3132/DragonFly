package net.resolutemc.dragonfly.commands;

import net.resolutemc.dragonfly.DragonFly;
import net.resolutemc.dragonfly.chat.PlayerMessage;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor {

    public FlyCommand() {
        DragonFly.getInstance().getCommand("Fly").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use fly commands");
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("dragonFly.Use")) {
                PlayerMessage.sendMessage(player, "No-Permission");
                return false;
            }
            if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
                PlayerMessage.sendMessage(player, "Not-In-Survival");
                return false;
            }
            boolean blacklistEnabled = DragonFly.getInstance().getConfig().getBoolean("World-Blacklist-Enabled");
            String worldName = player.getWorld().getName();
            List<String> blacklistedWorlds = new ArrayList<>();
            for (String world : DragonFly.getInstance().getConfig().getStringList("World-Blacklist")) {
                blacklistedWorlds.add(world);
            }
            if (player.hasPermission("dragonFly.NoFuel")) {
                if (!DragonFly.getInstance().getFreeFlyingPlayers().contains(player.getUniqueId())) {
                    if (blacklistEnabled && blacklistedWorlds.contains(worldName)) {
                        PlayerMessage.sendMessage(player, "Fly-Enabled-Blacklisted-World");
                        return true;
                    }
                    DragonFly.getInstance().getFreeFlyingPlayers().add(player.getUniqueId());
                    PlayerMessage.sendMessage(player, "Flight-Enabled");
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    return false;
                }

                DragonFly.getInstance().getFreeFlyingPlayers().remove(player.getUniqueId());
                PlayerMessage.sendMessage(player, "Flight-Disabled");
                player.setFlying(false);
                player.setAllowFlight(false);
                return false;
            }
            if (player.hasPermission("dragonFly.Fuel")) {
                String fuel = DragonFly.getInstance().getConfig().getString("Fuel");
                if (!player.getInventory().contains(Material.valueOf(fuel))) {
                    PlayerMessage.fuelPlaceholderMessage(player, "No-Fuel-In-Inventory", fuel);
                    return false;
                }
                if (!DragonFly.getInstance().getFlyingPlayers().contains(player.getUniqueId())) {
                    if (blacklistEnabled && blacklistedWorlds.contains(worldName)) {
                        PlayerMessage.sendMessage(player, "Fly-Enabled-Blacklisted-World");
                        return true;
                    }
                    DragonFly.getInstance().getFlyingPlayers().add(player.getUniqueId());
                    PlayerMessage.sendMessage(player, "Flight-Enabled");
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    return false;
                }
                DragonFly.getInstance().getFlyingPlayers().remove(player.getUniqueId());
                PlayerMessage.sendMessage(player, "Flight-Disabled");
                player.setFlying(false);
                player.setAllowFlight(false);
                return false;
            }
            PlayerMessage.sendMessage(player, "No-Fly-Extra-Permission");
        }
        return false;
    }
}