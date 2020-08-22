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
        config.addDefault("Game.Blocked Commands", Arrays.asList("shop", "spawn", "ec", "enderchest", "echest"));
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
