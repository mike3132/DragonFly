
package net.resolutemc.dragonfly.commands;

import net.resolutemc.dragonfly.DragonFly;
import net.resolutemc.dragonfly.chat.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminFly implements CommandExecutor {

    public AdminFly() {
        DragonFly.getInstance().getCommand("AdminFly").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use fly commands");
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("dragonFly.AdminFly")) {
                PlayerMessage.sendMessage(player, "No-AdminFly-Permission");
                return false;
            }
            if (!DragonFly.getInstance().getFlyingAdmins().contains(player.getUniqueId())) {
                DragonFly.getInstance().getFlyingAdmins().add(player.getUniqueId());
                player.setAllowFlight(true);
                player.setFlying(true);
                PlayerMessage.sendMessage(player, "Admin-Flight-Enabled");
                return false;
            }
            DragonFly.getInstance().getFlyingAdmins().remove(player.getUniqueId());
            player.setAllowFlight(false);
            player.setFlying(false);
            PlayerMessage.sendMessage(player, "Admin-Flight-Disabled");
        }
        return false;
    }
}
