package md.meral.plotflight;

import md.meral.plotflight.controllers.CommandController;
import md.meral.plotflight.controllers.FlightController;
import md.meral.plotflight.controllers.TitleController;
import md.meral.plotflight.listeners.P2Listener;
import md.meral.plotflight.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class PlotFlight extends JavaPlugin {
    public static PlotFlight instance;

    @Override
    public void onEnable() {
        instance = this;
        MessageManager.ConsoleLog("Welcome to PlotFlight!");
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            // CreatePlotFlightFolder();
            CreateOrLoadConfig();

            P2Listener p2Listener = new P2Listener();
            FlightController flightController = new FlightController();
            CommandController commandController = new CommandController();
            TitleController titleController = new TitleController();
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

        // Define the path to the config.yml within the PlotFlight folder
        File configFile = new File(plotFlightFolder, "config.yml");

        if (!configFile.exists()) {
            // Config file doesn't exist, create default config
            getLogger().info("Creating default config.yml in " + pluginName + " folder");
            CreateDefaultConfig();
        } else {
            // Config file exists, load it
            getLogger().info("Loading config.yml from " + pluginName + " folder");
            LoadPlotFlightConfig(configFile);
        }
    }

    private void CreateDefaultConfig() {
        saveResource("config.yml", false);
    }

    private void LoadPlotFlightConfig(File configFile) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    }
}
