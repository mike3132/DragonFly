package net.resolutemc.dragonfly;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;
import net.resolutemc.dragonfly.chat.ColorTranslate;
import net.resolutemc.dragonfly.commands.DragonFlyCommand;
import net.resolutemc.dragonfly.commands.AdminFly;
import net.resolutemc.dragonfly.commands.FlyCommand;
import net.resolutemc.dragonfly.config.ConfigCreator;
import net.resolutemc.dragonfly.events.*;
import net.resolutemc.dragonfly.hook.FlyFlagHandler;
import net.resolutemc.dragonfly.hook.WorldGuardFlagHook;
import net.resolutemc.dragonfly.utils.FlightManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class DragonFly extends JavaPlugin {

    private static DragonFly INSTANCE;
    private FlightManager flightManager;
    private final Set<UUID> flyingPlayers = new HashSet<>();
    private final Set<UUID> fallingPlayers = new HashSet<>();
    private final Set<UUID> freeFlyingPlayers = new HashSet<>();
    private final Set<UUID> flayingAdmins = new HashSet<>();

    public Set<UUID> getFlyingAdmins() {
        return flayingAdmins;
    }
    public Set<UUID> getFreeFlyingPlayers() {
        return freeFlyingPlayers;
    }
    public Set<UUID> getFallingPlayers() {
        return fallingPlayers;
    }
    public Set<UUID> getFlyingPlayers() {
        return flyingPlayers;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        getServer().getConsoleSender().sendMessage(ColorTranslate.chatColor("&0[&bDragon &fFly&0]" + "&2&lENABLED"));

        Bukkit.getPluginManager().registerEvents(new FallEvent(), this);
        Bukkit.getPluginManager().registerEvents(new GamemodeChangeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new WorldChangeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveEvent(), this);

        ConfigCreator.MESSAGES.create();
        saveDefaultConfig();

        registerCommands();

        this.flightManager = new FlightManager(this);

        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(FlyFlagHandler.FACTORY(), null);
    }

    @Override
    public void onLoad() {
        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        flagRegistry.register(WorldGuardFlagHook.dragonFly);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ColorTranslate.chatColor("&0[&bDragon &fFly&0]" + "&4&lDISABLED"));

        this.flightManager.disable();
    }

    public static DragonFly getInstance() {
        return INSTANCE;
    }

    private void registerCommands() {
        new FlyCommand();
        new AdminFly();
        new DragonFlyCommand();
    }

}