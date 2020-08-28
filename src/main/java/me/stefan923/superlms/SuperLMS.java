package me.stefan923.superlms;

import me.stefan923.superlms.commands.CommandManager;
import me.stefan923.superlms.game.GameManager;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.language.LanguageManager;
import me.stefan923.superlms.listeners.*;
import me.stefan923.superlms.settings.InventoryManager;
import me.stefan923.superlms.settings.SettingsManager;
import me.stefan923.superlms.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SuperLMS extends JavaPlugin implements MessageUtils {
    private static SuperLMS instance;

    private SettingsManager settingsManager;
    private LanguageManager languageManager;
    private InventoryManager inventoryManager;
    private GameManager gameManager;

    public static ArrayList<Player> players;
    public static ArrayList<Player> spectators;

    @Override
    public void onEnable() {
        instance = this;

        settingsManager = SettingsManager.getInstance();
        settingsManager.setup(this);

        languageManager = LanguageManager.getInstance();
        languageManager.setup(this);

        inventoryManager = InventoryManager.getInstance();
        inventoryManager.setup(this);

        gameManager = new GameManager(this);

        players = new ArrayList<>();
        spectators = new ArrayList<>();

        sendLogger("&8&l> &7&m------- &8&l( &3&lSuperLMS &b&lby Stefan923 &8&l) &7&m------- &8&l<");
        sendLogger("&b   Plugin has been initialized.");
        sendLogger("&b   Version: &3v" + getDescription().getVersion());
        sendLogger("&b   Enabled listeners: &3" + enableListeners());
        sendLogger("&b   Enabled commands: &3" + enableCommands());
        sendLogger("&8&l> &7&m------- &8&l( &3&lSuperLMS &b&lby Stefan923 &8&l) &7&m------- &8&l<");

        if (settingsManager.getConfig().getBoolean("Arena Auto-Prepare.Enable")) {
            timeTask();
        }
    }

    private Integer enableListeners() {
        Integer i = 9;
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EntityDamageByEntityListener(this), this);
        pluginManager.registerEvents(new EntityDamageListener(this), this);
        pluginManager.registerEvents(new FoodLevelChangeListener(this), this);
        pluginManager.registerEvents(new PlayerCommandPreprocessListener(this), this);
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new PlayerDropItemListener(this), this);
        pluginManager.registerEvents(new PlayerPickupItemListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerRespawnListener(this), this);
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

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public void timeTask() {
        long nextGame = 0;
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
        calendar.set(Calendar.SECOND, 0);

        while (nextGame == 0) {
            for (String time : settingsManager.getConfig().getStringList("Arena Auto-Prepare.Hours")) {
                String[] tempTime = time.split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tempTime[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(tempTime[1]));

                long duration = calendar.getTimeInMillis() - System.currentTimeMillis();
                if (duration > 0 && (nextGame == 0 || duration < nextGame)) {
                    nextGame = duration;
                    System.out.println(nextGame);
                }
            }
            calendar.add(Calendar.MILLISECOND, 86400000); // adding 1 day
        }

        nextGame = ((nextGame / 1000) + 1) * 20; // converting to ticks

        Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                if (gameManager.getStatus().equals(GameStatus.IDLE)) {
                    gameManager.waitForPlayers();
                }

                timeTask();
            }
        }, nextGame);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
