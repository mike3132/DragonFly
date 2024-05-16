package net.resolutemc.dragonfly.utils;

import net.resolutemc.dragonfly.DragonFly;
import net.resolutemc.dragonfly.chat.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class FlightManager {

    private final BukkitTask flyTask;

    public FlightManager(DragonFly dragonFly) {
        int fuelTime = DragonFly.getInstance().getConfig().getInt("Fuel-Time");
        this.flyTask = Bukkit.getScheduler().runTaskTimer(dragonFly, this::getPlayers, 20L, fuelTime);
    }

    private void getPlayers() {
        String fuel = DragonFly.getInstance().getConfig().getString("Fuel");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!DragonFly.getInstance().getFlyingPlayers().contains(player.getUniqueId())) return;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null && itemStack.getType().equals(Material.valueOf(fuel))) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                    player.updateInventory();
                    break;
                }
                if (!player.getInventory().contains(Material.valueOf(fuel))) {
                    PlayerMessage.sendMessage(player, "Out-Of-Fuel");
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    DragonFly.getInstance().getFlyingPlayers().remove(player.getUniqueId());
                    if (player.getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid()) return;
                    PlayerMessage.sendMessage(player, "Free-Falling-Message");
                    DragonFly.getInstance().getFallingPlayers().add(player.getUniqueId());
                    break;
                }
            }
        }
    }

    public void disable() {
        this.flyTask.cancel();
    }


}