package net.resolutemc.dragonfly.events;

import net.resolutemc.dragonfly.DragonFly;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallEvent implements Listener {


    @EventHandler
    public void onPlayerFall(EntityDamageEvent ede) {
        if (!(ede.getEntity() instanceof Player)) return;
        Player player = (Player) ede.getEntity();

        if (ede.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (DragonFly.getInstance().getFallingPlayers().contains(player.getUniqueId())) {
            ede.setCancelled(true);
            DragonFly.getInstance().getFallingPlayers().remove(player.getUniqueId());
        }
    }

}