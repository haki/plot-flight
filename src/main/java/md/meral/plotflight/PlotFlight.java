package md.meral.plotflight;

import md.meral.plotflight.controllers.CommandController;
import md.meral.plotflight.controllers.FlightController;
import md.meral.plotflight.listeners.P2Listener;
import md.meral.plotflight.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public final class PlotFlight extends JavaPlugin {
    public static PlotFlight instance;

    @Override
    public void onEnable() {
        instance = this;
        MessageManager.ConsoleLog("Welcome to PlotFlight!");
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            CreatePlotFlightFolder();
            CreateOrLoadConfig();

            P2Listener p2Listener = new P2Listener();
            FlightController flightController = new FlightController();
            CommandController commandController = new CommandController();
        } else {
            MessageManager.ConsoleLogError("PlotSquared plugin missing, PlotFlight cannot start!");
        }
    }

    @Override
    public void onDisable() {
        MessageManager.ConsoleLog("PlotFlight will miss you :(");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandController.instance.VerifyCommand(sender, label, args);
        return super.onCommand(sender, command, label, args);
    }

    private void CreateOrLoadConfig() {
        String pluginName = getDescription().getName();
        File plotFlightFolder = new File(getDataFolder(), pluginName);
        if (!plotFlightFolder.exists()) {
            plotFlightFolder.mkdirs(); // Create the folder if it doesn't exist
        }

        // Define the path to the config.yml within the PlotFlight folder
        File configFile = new File(plotFlightFolder, "config.yml");

        if (!configFile.exists()) {
            // Config file doesn't exist, create default config
            getLogger().info("Creating default config.yml in " + pluginName + " folder");
            CreateDefaultConfig(configFile);
        } else {
            // Config file exists, load it
            getLogger().info("Loading config.yml from " + pluginName + " folder");
            LoadPlotFlightConfig(configFile);
        }
    }

    private void CreatePlotFlightFolder() {
        String pluginName = getDescription().getName();
        File plotFlightFolder = new File(getDataFolder(), pluginName);
        if (!plotFlightFolder.exists()) {
            getLogger().info("Creating " + pluginName + " folder");
            plotFlightFolder.mkdirs();
        }
    }

    private void CreateDefaultConfig(File configFile) {
        try {
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            writer.write("#   _________________________________________________________\n" +
                    "#  |   __   _   ____   _   _   _____   _     _   _    ___    |\n" +
                    "#  |  |  \\ | | |  __| | | / | |  _  | | |   | | | |  / _ \\   |\n" +
                    "#  |  | | \\| | | |_   | |/ /  | | | | | |   | |_| | | |_| |  |\n" +
                    "#  |  | |\\ | | |  _|  |   <   | | | | | |    \\   /  |  _  |  |\n" +
                    "#  |  | | \\  | | |__  | |\\ \\  | |_| | | |__   | |   | | | |  |\n" +
                    "#  |  |_|  |_| |____| |_| \\_| |_____| |_____| |_|   |_| |_|  |\n" +
                    "#  |_________________________________________________________|\n" +
                    "#\n" +
                    "Reloaded: \"&aConfig reloaded.\"\n" +
                    "NoPermission: \"&cYou do not have permission to execute this command.\"\n" +
                    "\n" +
                    "Permissions:\n" +
                    "  FlyPerm: plotfly.fly # Allows the player to fly on plots they have permissions.\n" +
                    "  BypassPerm: plotfly.bypass # If there is someone with this permission, flights will not be opened/closed when entering or exiting the plot.\n" +
                    "  CommandPerm: plotfly.command # Allows the player to use the /plotfly command. It is recommended that this permission not be given to the player.\n" +
                    "  ReloadPerm: plotfly.reload # Allows the player to use the /plotfly reload command. It is recommended that this permission not be given to the player.\n" +
                    "\n" +
                    "Settings:\n" +
                    "  FlightEnabled: \"&8[&aPlotFly&8] &7Flight Mode: &aEnabled\"\n" +
                    "  EnabledSoundEffect: \"ENTITY_ENDER_DRAGON_FLAP\" # Sound effect given when the player enters the plot.\n" +
                    "  FlightDisabled: \"&8[&aPlotFly&8] &7Flight Mode: &cDisabled\"\n" +
                    "  DisabledSoundEffect: \"ENTITY_ENDER_DRAGON_FLAP\" # Sound effect when the player exits the plot\n" +
                    "  CommandActive: \"&8[&aPlotFly&8] &7Now your flight mode will be active or inactive when you enter the plot.\" # /plotfly enable message\n" +
                    "  CommandInactive: \"&8[&aPlotFly&8] &7Your flight mode will no longer be active or inactive when you enter the plot.\" # /plotfly disable message\n" +
                    "\n" +
                    "Title:\n" +
                    "  TitleMessage: false # Is the title message on or off?\n" +
                    "  EnabledMessage: \"&8Flight mode &2enabled\"\n" +
                    "  DisabledMessage: \"&8Flight mode &4disabled\"\n" +
                    "  FadeIn: 2 # How long do you want it to take to appear on screen?\n" +
                    "  Stay: 10 # How long do you want it to take to stay on screen?\n" +
                    "  FadeOut: 2 # How many seconds do you want it to take for the text to disappear from the screen?");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadPlotFlightConfig(File configFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    }
}
