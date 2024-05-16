package net.resolutemc.dragonfly.commands;

import net.resolutemc.dragonfly.DragonFly;
import net.resolutemc.dragonfly.chat.ColorTranslate;
import net.resolutemc.dragonfly.chat.ConsoleMessage;
import net.resolutemc.dragonfly.chat.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DragonFlyCommand implements CommandExecutor {

    public DragonFlyCommand() {
        DragonFly.getInstance().getCommand("DragonFly").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&0[&bDragon &fFly&0] &6Commands cannot be ran from the console");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ColorTranslate.chatColor("&0[&bDragon &fFly&0] &2Written by &5Mike3132"));
            PlayerMessage.sendMessage(player, "Help-Trigger");
            return false;
        }
        if (!player.hasPermission("dragonFly.Admin")) {
            PlayerMessage.sendMessage(player, "No-Permission");
            return false;
        }
        switch (args[0].toUpperCase()) {
            case "RELOAD":
                if (!player.hasPermission("dragonFly.Reload")) {
                    PlayerMessage.sendMessage(player, "No-Reload-Permission");
                    return false;
                }
                sender.sendMessage(ColorTranslate.chatColor("&0[&bDragon &fFly&0] " + "&6Config reloaded in &2" + String.valueOf(System.currentTimeMillis() - 1 + " &6ms")));
                DragonFly.getInstance().reloadConfig();
                break;
            case "HELP":
                PlayerMessage.sendMessage(player, "Help-Header");
                ConsoleMessage.sendMessageNoPrefix(player, "Help-A");
                ConsoleMessage.sendMessageNoPrefix(player, "Help-B");
                ConsoleMessage.sendMessageNoPrefix(player, "Help-C");
                ConsoleMessage.sendMessageNoPrefix(player, "Help-D");
                PlayerMessage.sendMessage(player, "Help-Footer");
                break;
            case "TOGGLE":
                if (!player.hasPermission("dragonFly.Toggle.Others")) {
                    PlayerMessage.sendMessage(player, "No-Toggle-Others-Permission");
                    return false;
                }
                if (args.length != 1) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        PlayerMessage.sendMessage(player, "Target-Not-Found");
                        return false;
                    }
                    if (!DragonFly.getInstance().getFlyingPlayers().contains(target.getUniqueId()) &&
                            DragonFly.getInstance().getFreeFlyingPlayers().contains(target.getUniqueId())) {
                        PlayerMessage.playerPlaceholderMessage(player, "Target-Not-Flying", target.getName());
                        return false;
                    }

                    PlayerMessage.playerPlaceholderMessage(player, "Target-Flight-Disabled", target.getName());
                    DragonFly.getInstance().getFlyingPlayers().remove(target.getUniqueId());
                    DragonFly.getInstance().getFreeFlyingPlayers().remove(target.getUniqueId());
                    target.setAllowFlight(false);
                    target.setFlying(false);
                } else {
                    player.sendMessage("Help-Trigger");
                }
                break;
            case "VERSION":
                player.sendMessage(ColorTranslate.chatColor("&0[&bDragon &fFly&0] &bCurrent plugin version: &22.0"));
                player.sendMessage(ColorTranslate.chatColor("&0[&bDragon &fFly&0] &bCurrent config version: &22.0"));
                break;
        }


        return true;
    }
}