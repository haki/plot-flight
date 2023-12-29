package md.meral.plotflight.controllers;

import md.meral.plotflight.PlotFlight;
import md.meral.plotflight.utils.MessageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandController {
    public static CommandController instance;

    public CommandController() {
        instance = this;
    }

    public void VerifyCommand(CommandSender sender, String label, String[] args) {
        Player player = PlotFlight.instance.getServer().getPlayer(sender.getName());
        FileConfiguration pluginConfig = PlotFlight.instance.getConfig();
        if (player.hasPermission(pluginConfig.getString("Permissions.CommandPerm")) && !player.hasPermission(pluginConfig.getString("Permissions.BypassPerm"))) {
            if (label.equalsIgnoreCase("plotfly")) {
                if (args.length == 1) {
                    switch (args[0]) {
                        case "reload":
                            CommandReload(pluginConfig, player);
                            break;
                        case "enable":
                            CommandEnable(pluginConfig, player);
                            break;
                        case "disable":
                            CommandDisable(pluginConfig, player);
                            break;
                        default:
                            MessageManager.PlayerMessageSpecialColor(pluginConfig.getString("NoPermission"), player);
                            break;
                    }
                }
            }
        } else {
            MessageManager.PlayerMessageSpecialColor(pluginConfig.getString("NoPermission"), player);
        }
    }

    public void CommandReload(FileConfiguration pluginConfig, Player player) {
        if (player.hasPermission(pluginConfig.getString("Permissions.ReloadPerm"))) {
            MessageManager.PlayerMessageSpecialColor(pluginConfig.getString("Reloaded"), player);
            PlotFlight.instance.reloadConfig();
            PlotFlight.instance.saveDefaultConfig();
        } else {
            MessageManager.PlayerMessageSpecialColor(pluginConfig.getString("NoPermission"), player);
        }
    }

    public void CommandEnable(FileConfiguration pluginConfig, Player player) {
        FlightController.instance.RemovePlayerFromCantFlyList(player.getUniqueId());
        MessageManager.PlayerMessageSpecialColor(pluginConfig.getString("Settings.CommandActive"), player);
    }

    public void CommandDisable(FileConfiguration pluginConfig, Player player) {
        FlightController.instance.AddPlayerToCantFlyList(player.getUniqueId());
        MessageManager.PlayerMessageSpecialColor(pluginConfig.getString("Settings.CommandInactive"), player);
    }
}
