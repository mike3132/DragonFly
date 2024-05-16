package net.resolutemc.dragonfly.events;

import net.resolutemc.dragonfly.DragonFly;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe) {
        Player player = pqe.getPlayer();

        if (DragonFly.getInstance().getFlyingPlayers().contains(player.getUniqueId())) {
            DragonFly.getInstance().getFlyingPlayers().remove(player.getUniqueId());
            return;
        }
        if (DragonFly.getInstance().getFlyingAdmins().contains(player.getUniqueId())) {
            DragonFly.getInstance().getFlyingAdmins().remove(player.getUniqueId());
            return;
        }
        if (DragonFly.getInstance().getFreeFlyingPlayers().contains(player.getUniqueId())) {
            DragonFly.getInstance().getFreeFlyingPlayers().remove(player.getUniqueId());
        }
    }
}