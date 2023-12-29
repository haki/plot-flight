package md.meral.plotflight.listeners;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.PlayerEnterPlotEvent;
import com.plotsquared.core.events.PlayerLeavePlotEvent;
import com.plotsquared.core.plot.Plot;
import md.meral.plotflight.PlotFlight;
import md.meral.plotflight.controllers.FlightController;
import md.meral.plotflight.utils.MessageManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
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

        if (player == null || !flyPerm || bypassPerm) {
            DontAllowToFlyPlayer(player, false);
            return;
        }

        if (plot.getMembers().contains(playerUUID) || plot.getOwners().contains(playerUUID) || plot.getTrusted().contains(playerUUID)) {
            if (!FlightController.instance.CanFly(playerUUID) && !FlightController.instance.CantFly(playerUUID)) {
                AllowToFlyPlayer(player);
            }
        }
    }

    @Subscribe
    public void onPlayerLeavePlot(PlayerLeavePlotEvent event) {
        UUID playerUUID = event.getPlotPlayer().getUUID();
        Plot plot = event.getPlot();
        Player player = PlotFlight.instance.getServer().getPlayer(playerUUID);

        if (player == null) {
            return;
        }

        if (plot.getMembers().contains(playerUUID) || plot.getOwners().contains(playerUUID) || plot.getTrusted().contains(playerUUID)) {
            if (FlightController.instance.CanFly(playerUUID)) {
                DontAllowToFlyPlayer(player, true);
            }
        }
    }

    private void AllowToFlyPlayer(Player player) {
        FlightController.instance.AddPlayerToFlyList(player.getUniqueId());

        String message = PlotFlight.instance.getConfig().getString("Settings.FlightEnabled");
        MessageManager.PlayerMessageSpecialColor(message, player);

        player.setAllowFlight(true);

        String soundString = PlotFlight.instance.getConfig().getString("Settings.EnabledSoundEffect");
        try {
            Sound sound = Sound.valueOf(soundString);
            player.playSound(player.getLocation(), sound, 1F, 1F);
        } catch (IllegalArgumentException e) {
            PlotFlight.instance.getLogger().warning("Invalid sound effect in config: " + soundString);
        }
    }

    private void DontAllowToFlyPlayer(Player player, boolean withText) {
        FlightController.instance.RemovePlayerFromFlyList(player.getUniqueId());

        if (withText) {
            String message = PlotFlight.instance.getConfig().getString("Settings.FlightDisabled");
            MessageManager.PlayerMessageSpecialColor(message, player);
        }

        player.setAllowFlight(false);

        String soundString = PlotFlight.instance.getConfig().getString("Settings.DisabledSoundEffect");
        try {
            Sound sound = Sound.valueOf(soundString);
            player.playSound(player.getLocation(), sound, 1F, 1F);
        } catch (IllegalArgumentException e) {
            PlotFlight.instance.getLogger().warning("Invalid sound effect in config: " + soundString);
        }


    }
}
