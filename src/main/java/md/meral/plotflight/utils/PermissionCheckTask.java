package md.meral.plotflight.utils;

import md.meral.plotflight.PlotFlight;
import md.meral.plotflight.controllers.FlightController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PermissionCheckTask extends BukkitRunnable {
    private final PlotFlight plotFlight;

    public PermissionCheckTask(PlotFlight plotFlight) {
        this.plotFlight = plotFlight;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            CheckPlayerPermission(player);
        }
    }

    private void CheckPlayerPermission(Player player) {
        if (player.hasPermission(plotFlight.getConfig().getString("Permissions.BypassPerm"))) {
            if (!player.getAllowFlight()) {
                FlightController.instance.AllowToFlyPlayer(player);
            }
        } else {
            if (player.getAllowFlight()) {
                FlightController.instance.DontAllowToFlyPlayer(player, true);
            }
        }
    }
}
