package md.meral.plotflight.listeners;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.PlayerEnterPlotEvent;
import com.plotsquared.core.events.PlayerLeavePlotEvent;
import com.plotsquared.core.plot.Plot;
import md.meral.plotflight.PlotFlight;
import md.meral.plotflight.controllers.CommandController;
import md.meral.plotflight.controllers.FlightController;
import org.bukkit.entity.Player;

import java.util.UUID;

public class P2Listener {
    public static P2Listener instance;

    public P2Listener(PlotAPI api) {
        api.registerListener(this);
        instance = this;
    }

    public P2Listener() {
        PlotAPI api = new PlotAPI();
        api.registerListener(this);
        instance = this;
    }

    @Subscribe
    public void onPlayerEnterPlot(PlayerEnterPlotEvent event) {
        UUID playerUUID = event.getPlotPlayer().getUUID();
        Plot plot = event.getPlot();
        Player player = PlotFlight.instance.getServer().getPlayer(playerUUID);

        boolean flyPerm = player.hasPermission(PlotFlight.instance.getConfig().getString("Permissions.FlyPerm"));
        boolean bypassPerm = player.hasPermission(PlotFlight.instance.getConfig().getString("Permissions.BypassPerm"));

        if (!bypassPerm && player.getAllowFlight()) {
            FlightController.instance.DontAllowToFlyPlayer(player, false);
        }

        if (player == null || (!CommandController.instance.CheckOpStatus(player) && !flyPerm)) {
            FlightController.instance.DontAllowToFlyPlayer(player, false);
            return;
        }

        if (bypassPerm || plot.getMembers().contains(playerUUID) || plot.getOwners().contains(playerUUID) || plot.getTrusted().contains(playerUUID)) {
            if (!FlightController.instance.CanFly(playerUUID) && !FlightController.instance.CantFly(playerUUID)) {
                FlightController.instance.AllowToFlyPlayer(player);
            }
        } else {
            FlightController.instance.DontAllowToFlyPlayer(player, false);
        }
    }

    @Subscribe
    public void onPlayerLeavePlot(PlayerLeavePlotEvent event) {
        UUID playerUUID = event.getPlotPlayer().getUUID();
        Plot plot = event.getPlot();
        Player player = PlotFlight.instance.getServer().getPlayer(playerUUID);

        boolean bypassPerm = player.hasPermission(PlotFlight.instance.getConfig().getString("Permissions.BypassPerm"));

        if (player == null || bypassPerm) {
            return;
        }

        if (plot.getMembers().contains(playerUUID) || plot.getOwners().contains(playerUUID) || plot.getTrusted().contains(playerUUID)) {
            if (FlightController.instance.CanFly(playerUUID)) {
                FlightController.instance.DontAllowToFlyPlayer(player, true);
            }
        }
    }
}
