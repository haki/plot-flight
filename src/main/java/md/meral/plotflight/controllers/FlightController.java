package md.meral.plotflight.controllers;

import md.meral.plotflight.PlotFlight;
import md.meral.plotflight.utils.MessageManager;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class FlightController {
    public static FlightController instance;

    public FlightController() {
        instance = this;
    }

    private final ArrayList<UUID> playersCanFly = new ArrayList<>();
    private final ArrayList<UUID> playersCantFly = new ArrayList<>();

    public boolean CanFly(UUID uuid) {
        return playersCanFly.contains(uuid);
    }

    public boolean CantFly(UUID uuid) {
        return playersCantFly.contains(uuid);
    }

    public ArrayList<UUID> GetPlayersCanFly() {
        return playersCanFly;
    }

    public void AddPlayerToFlyList(UUID uuid) {
        playersCanFly.add(uuid);
    }

    public void AddPlayerToCantFlyList(UUID uuid) {
        playersCantFly.add(uuid);
    }

    public void RemovePlayerFromFlyList(UUID uuid) {
        for (int i = 0; i < playersCanFly.size(); i++) {
            if (playersCanFly.get(i).equals(uuid)) {
                playersCanFly.remove(i);
                break;
            }
        }
    }

    public void RemovePlayerFromCantFlyList(UUID uuid) {
        for (int i = 0; i < playersCantFly.size(); i++) {
            if (playersCantFly.get(i).equals(uuid)) {
                playersCantFly.remove(i);
                break;
            }
        }
    }

    public void AllowToFlyPlayer(Player player) {
        FlightController.instance.AddPlayerToFlyList(player.getUniqueId());

        String message = PlotFlight.instance.getConfig().getString("Settings.FlightEnabled");
        MessageManager.PlayerMessageSpecialColor(message, player);

        player.setAllowFlight(true);

        if (PlotFlight.instance.getConfig().getBoolean("Title.TitleMessage")) {
            TitleController.instance.SendTitleToPlayer(player, PlotFlight.instance.getConfig().getString("Title.EnabledMessage"), "");
        }

        String soundString = PlotFlight.instance.getConfig().getString("Settings.EnabledSoundEffect");
        try {
            Sound sound = Sound.valueOf(soundString);
            player.playSound(player.getLocation(), sound, 1F, 1F);
        } catch (IllegalArgumentException e) {
            PlotFlight.instance.getLogger().warning("Invalid sound effect in config: " + soundString);
        }
    }

    public void DontAllowToFlyPlayer(Player player, boolean withText) {
        FlightController.instance.RemovePlayerFromFlyList(player.getUniqueId());

        if (withText) {
            String message = PlotFlight.instance.getConfig().getString("Settings.FlightDisabled");
            MessageManager.PlayerMessageSpecialColor(message, player);

            if (PlotFlight.instance.getConfig().getBoolean("Title.TitleMessage")) {
                TitleController.instance.SendTitleToPlayer(player, PlotFlight.instance.getConfig().getString("Title.DisabledMessage"), "");
            }
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
