package net.resolutemc.dragonfly.events;

import net.resolutemc.dragonfly.DragonFly;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class GamemodeChangeEvent implements Listener {


    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent gmc) {
        Player player = gmc.getPlayer();
        String worldName = player.getWorld().getName();
        boolean creativeCheckEnabled = DragonFly.getInstance().getConfig().getBoolean("Creative-Worlds-Enabled");

        List<String> creativeWorlds = new ArrayList<>();
        for (String worlds : DragonFly.getInstance().getConfig().getStringList("Creative-Worlds")) {
            creativeWorlds.add(worlds);
        }
        if (gmc.getNewGameMode() != GameMode.CREATIVE) return;
        if (!creativeCheckEnabled) return;
        if (creativeWorlds.contains(worldName)) {
            if (!DragonFly.getInstance().getFlyingPlayers().contains(player.getUniqueId())) {
                DragonFly.getInstance().getFlyingPlayers().remove(player.getUniqueId());
                return;
            }
            if (!DragonFly.getInstance().getFreeFlyingPlayers().contains(player.getUniqueId())) {
                DragonFly.getInstance().getFreeFlyingPlayers().remove(player.getUniqueId());
            }
        }

    }


}