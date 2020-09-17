package me.stefan923.superlms.settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SettingsManager {

    private static SettingsManager instance = new SettingsManager();
    private FileConfiguration config;
    private File cfile;

    public static SettingsManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        cfile = new File(p.getDataFolder(), "settings.yml");
        config = YamlConfiguration.loadConfiguration(cfile);

        config.options().header("SuperLMS by Stefan923\n");
        config.addDefault("Game.Starting Counter", 60);
        config.addDefault("Game.Maximum Player Count", 30);
        config.addDefault("Game.Minimum Player Count", 15);
        config.addDefault("Game.Grace Period.Enabled", true);
        config.addDefault("Game.Grace Period.Time In Seconds", 10);
        config.addDefault("Game.Winner Rewards", Arrays.asList("give %player_name% minecraft:diamond 16", "give %player_name% minecraft:iron_ingot 32"));
        config.addDefault("Game.Blocked Commands", Arrays.asList("shop", "spawn", "ec", "enderchest", "echest"));
        config.addDefault("Arena Auto-Prepare.Enable", true);
        config.addDefault("Arena Auto-Prepare.Hours", Arrays.asList("9:00", "12:00", "15:00", "18:00", "21:00", "0:00"));
        config.addDefault("Arena Auto-Stop.Enable", true);
        config.addDefault("Arena Auto-Stop.After X Seconds", 600);
        config.options().copyDefaults(true);
        save();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void resetConfig() {
        save();
    }

    public void save() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'settings.yml' couldn't be saved!");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }
}
