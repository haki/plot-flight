package md.meral.plotflight.utils;

import md.meral.plotflight.PlotFlight;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {
    public static void ConsoleLog(String message) {
        PlotFlight.instance.getServer().getConsoleSender().sendMessage(message);
    }

    public static void ConsoleLogError(String message) {
        PlotFlight.instance.getServer().getConsoleSender().sendMessage(ChatColor.RED + message);
    }

    public static void PlayerMessage(String message, Player player) {
        player.sendMessage(message);
    }

    public static void PlayerMessageSpecialColor(String message, Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
