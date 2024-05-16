package net.resolutemc.dragonfly.hook;

import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;
import net.resolutemc.dragonfly.DragonFly;
import net.resolutemc.dragonfly.chat.PlayerMessage;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FlyFlagHandler extends FlagValueChangeHandler<State> {

    public static Factory FACTORY() {
        return new Factory();
    }

    public static class Factory extends Handler.Factory<FlyFlagHandler> {
        @Override
        public FlyFlagHandler create(Session session) {
            return new FlyFlagHandler(session);
        }
    }

    protected FlyFlagHandler(Session session) {
        super(session, WorldGuardFlagHook.dragonFly);
    }

    private Boolean currentValue;
    private Boolean originalFly;

    public Boolean getCurrentValue() {
        return currentValue;
    }

    @Override
    protected void onInitialValue(LocalPlayer player, ApplicableRegionSet applicableRegionSet, State value) {
        this.flyValue(player, player.getWorld(), value);
    }

    @Override
    protected boolean onSetValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, State currentValue, State lastValue, MoveType moveType) {
        this.flyValue(player, (World) to.getExtent(), currentValue);
        return true;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, State lastValue, MoveType moveType) {
        this.flyValue(player, (World) to.getExtent(), null);
        return true;
    }

    private final Set<UUID> cachedPlayers = new HashSet<>();

    private void flyValue(LocalPlayer player, World world, State state) {
        Player bukkitPlayer = ((BukkitPlayer) player).getPlayer();

        if (state != null) {
            boolean value = state == State.ALLOW;
            if (bukkitPlayer.getAllowFlight() != value) {
                if (this.originalFly == null) {
                    this.originalFly = bukkitPlayer.getAllowFlight();
                }
                bukkitPlayer.setAllowFlight(value);
                if (state == State.DENY) {
                    PlayerMessage.sendMessage(bukkitPlayer, "Region-Flight-Disabled-Message");
                    if (!DragonFly.getInstance().getFlyingPlayers().contains(bukkitPlayer.getUniqueId())) return;
                    DragonFly.getInstance().getFlyingPlayers().remove(bukkitPlayer.getUniqueId());
                    cachedPlayers.add(bukkitPlayer.getUniqueId());
                    return;
                }
                PlayerMessage.sendMessage(bukkitPlayer, "Region-Flight-Enabled-Message");

                return;
            }
            this.currentValue = value;
            if (bukkitPlayer.getAllowFlight() == value) {
                if (DragonFly.getInstance().getFlyingPlayers().contains(bukkitPlayer.getUniqueId())) {
                    DragonFly.getInstance().getFlyingPlayers().remove(bukkitPlayer.getUniqueId());
                    cachedPlayers.add(bukkitPlayer.getUniqueId());
                    PlayerMessage.sendMessage(bukkitPlayer, "Region-Flight-Fuel-Message");
                }
            }
        } else {
            if (this.originalFly != null) {
                bukkitPlayer.setAllowFlight(this.originalFly);
                this.originalFly = null;
                PlayerMessage.sendMessage(bukkitPlayer, "Region-Flight-Enabled-Leave-Message");
                if (cachedPlayers.contains(bukkitPlayer.getUniqueId())) {
                    DragonFly.getInstance().getFlyingPlayers().add(bukkitPlayer.getUniqueId());
                    cachedPlayers.remove(bukkitPlayer.getUniqueId());
                }
            }
            this.currentValue = null;
            if (!cachedPlayers.contains(bukkitPlayer.getUniqueId())) return;
            DragonFly.getInstance().getFlyingPlayers().add(bukkitPlayer.getUniqueId());
            cachedPlayers.remove(bukkitPlayer.getUniqueId());
            PlayerMessage.sendMessage(bukkitPlayer, "Region-Flight-Fuel-Leave-Message");
        }
    }

}