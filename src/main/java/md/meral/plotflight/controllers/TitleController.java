package md.meral.plotflight.controllers;

import md.meral.plotflight.PlotFlight;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleController {
    public static TitleController instance;

    public TitleController() {
        instance = this;
    }

    public void SendTitleToPlayer(Player player, String title, String subtitle) {
        int fadeIn = PlotFlight.instance.getConfig().getInt("Title.FadeIn");
        int stay = PlotFlight.instance.getConfig().getInt("Title.Stay");
        int fadeOut = PlotFlight.instance.getConfig().getInt("Title.FadeOut");

        title = ChatColor.translateAlternateColorCodes('&', title);
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
