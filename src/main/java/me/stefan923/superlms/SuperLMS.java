package me.stefan923.superlms;

import me.stefan923.superlms.commands.CommandManager;
import me.stefan923.superlms.game.GameManager;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.language.LanguageManager;
import me.stefan923.superlms.settings.InventoryManager;
import me.stefan923.superlms.settings.SettingsManager;
import me.stefan923.superlms.utils.MessageUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SuperLMS extends JavaPlugin implements MessageUtils {
    private static SuperLMS instance;

    private SettingsManager settingsManager;
    private LanguageManager languageManager;
    private CommandManager commandManager;
    private InventoryManager inventoryManager;
    private GameManager gameManager;

    public static ArrayList<Player> players;
    public static HashMap<String, String> status;
    public static HashMap<Player, ItemStack[]> playerItems;
    public static HashMap<Player, ItemStack[]> playerArmors;
    public static HashMap<String, BukkitRunnable> tasks;
    public static HashMap<String, Integer> time;

    @Override
    public void onEnable() {
        instance = this;

        settingsManager = SettingsManager.getInstance();
        settingsManager.setup(this);

        languageManager = LanguageManager.getInstance();
        languageManager.setup(this);

        inventoryManager = InventoryManager.getInstance();
        inventoryManager.setup(this);

        commandManager = new CommandManager(this);

        players = new ArrayList<>();
        status = new HashMap<>();
        playerItems = new HashMap<>();
        playerArmors = new HashMap<>();
        tasks = new HashMap<>();
        time = new HashMap<>();

        gameManager = new GameManager(this);

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

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void timeTask() {
        new BukkitRunnable() {
            public void run() {
                if (gameManager.getStatus().equals(GameStatus.IDLE)) {
                    final Calendar C = new GregorianCalendar();
                    C.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
                    final int hour = C.get(Calendar.HOUR);
                    final int minute = C.get(Calendar.MINUTE);

                    if (hour == 9 && minute == 0) {
                        gameManager.waitForPlayers();
                    }
                    if (hour == 12 && minute == 0) {
                        gameManager.waitForPlayers();
                    }
                    if (hour == 15 && minute == 0) {
                        gameManager.waitForPlayers();
                    }
                    if (hour == 18 && minute == 0) {
                        gameManager.waitForPlayers();
                    }
                    if (hour == 21 && minute == 0) {
                        gameManager.waitForPlayers();
                    }
                    if (hour == 0 && minute == 0) {
                        gameManager.waitForPlayers();
                    }
                }
            }
        }.runTaskTimer(this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
