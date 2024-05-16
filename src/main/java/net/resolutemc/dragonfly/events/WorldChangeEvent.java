package net.resolutemc.dragonfly.events;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.SessionManager;
import net.resolutemc.dragonfly.DragonFly;
import net.resolutemc.dragonfly.chat.PlayerMessage;
import net.resolutemc.dragonfly.hook.FlyFlagHandler;
import net.resolutemc.dragonfly.hook.WorldGuardFlagHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WorldChangeEvent implements Listener {

    private final SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent wc) {
        Player player = wc.getPlayer();
        String worldName = player.getWorld().getName();
        boolean blacklistEnabled = DragonFly.getInstance().getConfig().getBoolean("World-Blacklist-Enabled");

        List<String> blacklistedWorlds = new ArrayList<>();
        for (String world : DragonFly.getInstance().getConfig().getStringList("World-Blacklist")) {
            blacklistedWorlds.add(world);
        }

        if (DragonFly.getInstance().getFreeFlyingPlayers().contains(player.getUniqueId())) {
            player.setAllowFlight(true);
            player.setFlying(true);
            if (blacklistEnabled && blacklistedWorlds.contains(worldName)) {
                DragonFly.getInstance().getFreeFlyingPlayers().remove(player.getUniqueId());
                player.setFlying(false);
                player.setAllowFlight(false);
                PlayerMessage.sendMessage(player, "Enter-Blacklisted-World");
            }
            return;
        }

        if (DragonFly.getInstance().getFlyingPlayers().contains(player.getUniqueId())) {
            player.setAllowFlight(true);
            player.setFlying(true);
            if (blacklistEnabled && blacklistedWorlds.contains(worldName)) {
                DragonFly.getInstance().getFlyingPlayers().remove(player.getUniqueId());
                player.setFlying(false);
                player.setAllowFlight(false);
                PlayerMessage.sendMessage(player, "Enter-Blacklisted-World");
            }
            return;
        }

        if (DragonFly.getInstance().getFlyingAdmins().contains(player.getUniqueId())) {
            player.setAllowFlight(true);
            player.setFlying(true);
            return;
        }

        // WorldGuard region check
        Boolean value = sessionManager.get(WorldGuardPlugin.inst().wrapPlayer(player)).getHandler(FlyFlagHandler.class).getCurrentValue();
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if (value != null) {
            player.setAllowFlight(value);
            player.setFlying(value);
            return;
        }
        if (query.testState(localPlayer.getLocation(), localPlayer, WorldGuardFlagHook.dragonFly)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }
}