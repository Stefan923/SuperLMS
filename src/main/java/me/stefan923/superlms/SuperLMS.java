package me.stefan923.superlms;

import me.stefan923.superlms.commands.CommandManager;
import me.stefan923.superlms.language.LanguageManager;
import me.stefan923.superlms.settings.SettingsManager;
import me.stefan923.superlms.utils.MessageUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperLMS extends JavaPlugin implements MessageUtils {
    private static SuperLMS instance;

    private SettingsManager settingsManager;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        instance = this;

        settingsManager = SettingsManager.getInstance();
        settingsManager.setup(this);

        languageManager = LanguageManager.getInstance();
        languageManager.setup(this);

        sendLogger("&8&l> &7&m------- &8&l( &3&lSuperLMS &b&lby Stefan923 &8&l) &7&m------- &8&l<");
        sendLogger("&b   Plugin has been initialized.");
        sendLogger("&b   Version: &3v" + getDescription().getVersion());
        sendLogger("&b   Enabled listeners: &3" + enableListeners());
        sendLogger("&b   Enabled commands: &3" + enableCommands());
        sendLogger("&8&l> &7&m------- &8&l( &3&lSuperLMS &b&lby Stefan923 &8&l) &7&m------- &8&l<");
    }

    private Integer enableListeners() {
        Integer i = 0;
        FileConfiguration settings = settingsManager.getConfig();
        PluginManager pluginManager = getServer().getPluginManager();
        return i;
    }

    private Integer enableCommands() {
        CommandManager commandManager = new CommandManager(this);
        return commandManager.getCommands().size();
    }

    public static SuperLMS getInstance() {
        return instance;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public void reloadSettingManager() {
        settingsManager.reload();
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public void reloadLanguageManager() {
        languageManager.reload();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
