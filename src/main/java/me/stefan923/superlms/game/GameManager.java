package me.stefan923.superlms.game;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.settings.InventoryManager;
import me.stefan923.superlms.utils.MessageUtils;
import me.stefan923.superlms.utils.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.util.UUID;

public class GameManager implements MessageUtils, SerializationUtils {

    private final SuperLMS instance;

    private final FileConfiguration settings;
    private final FileConfiguration language;

    private GameStatus status;

    private int timer;
    private long startTime;
    private int currentTaskID;

    private final Sound NOTE_SOUND;

    public GameManager(SuperLMS instance) {
        this.instance = instance;

        this.settings = instance.getSettingsManager().getConfig();
        this.language = instance.getLanguageManager().getConfig();

        this.status = GameStatus.IDLE;

        // Sound names changed, make it compatible with both versions
        Sound clickSound = null;
        String[] clickSounds = {"BLOCK_NOTE_BLOCK_XYLOPHONE", "NOTE_PIANO"};
        for (String s : clickSounds) {
            try {
                clickSound = Sound.valueOf(s.toUpperCase());
                break;
            } catch (IllegalArgumentException ignored) {}
        }
        this.NOTE_SOUND = clickSound;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void waitForPlayers() {
        status = GameStatus.WAITING;
        timer = settings.getInt("Game.Starting Counter");
        currentTaskID = instance.getServer().getScheduler().scheduleAsyncRepeatingTask(instance, new BukkitRunnable() {
            private int announceTimer = 30;

            @Override
            public void run() {
                if (status.equals(GameStatus.IDLE)) {
                    cancelCurrentTask();
                }
                if (status.equals(GameStatus.WAITING) && announceTimer++ == 30) {
                    announceTimer = 0;
                    Bukkit.broadcastMessage(formatAll(instance.getLanguageManager().getConfig().getString("Game.Waiting For Players")
                            .replace("%current_count%", String.valueOf(instance.getPlayers().size()))
                            .replace("%max_count%", String.valueOf(settings.getInt("Game.Maximum Player Count")))));
                }

                if (status.equals(GameStatus.STARTING)) {
                    if (timer == 0) {
                        startGame();
                    } else if (timer <= 10) {
                        soundInGame(NOTE_SOUND);
                        Bukkit.broadcastMessage(formatAll(instance.getLanguageManager().getConfig().getString("Game.Starting In")
                                .replace("%time%", convertTime(timer * 1000, language))
                                .replace("%current_count%", String.valueOf(instance.getPlayers().size()))
                                .replace("%max_count%", String.valueOf(settings.getInt("Game.Maximum Player Count")))));
                    } else if (timer % 30 == 0) {
                        Bukkit.broadcastMessage(formatAll(instance.getLanguageManager().getConfig().getString("Game.Starting In")
                                .replace("%time%", convertTime(timer * 1000, language))
                                .replace("%current_count%", String.valueOf(instance.getPlayers().size()))
                                .replace("%max_count%", String.valueOf(settings.getInt("Game.Maximum Player Count")))));
                    }
                    --timer;
                }
            }
        }, 20L, 20L);
    }

    public void startGame() {
        cancelCurrentTask();

        status = settings.getBoolean("Game.Grace Period.Enabled") ? GameStatus.GRACE : GameStatus.STARTED;
        startTime = System.currentTimeMillis();

        try {
            Location arenaLocation = deserializeLocation(settings.getString("Game.Locations.Arena"));
            ItemStack[] contents = itemStackArrayFromBase64(settings.getString("Game.Kit.Inventory"));
            ItemStack[] armorContents = itemStackArrayFromBase64(settings.getString("Game.Kit.Armor"));

            BukkitScheduler scheduler = instance.getServer().getScheduler();
            instance.getPlayers().forEach(player -> {
                scheduler.runTask(instance, () -> player.teleport(arenaLocation));
                player.getInventory().setContents(contents);
                player.getInventory().setArmorContents(armorContents);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Started")));

        if (status.equals(GameStatus.GRACE)) {
            currentTaskID = instance.getServer().getScheduler().scheduleAsyncRepeatingTask(instance, new BukkitRunnable() {
                private int graceTimer = settings.getInt("Game.Grace Period.Time In Seconds");

                @Override
                public void run() {
                    if (status.equals(GameStatus.IDLE)) {
                        cancelCurrentTask();
                    }
                    if (graceTimer == 0) {
                        status = GameStatus.STARTED;
                        broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Grace Period Ended")));
                        cancelCurrentTask();
                    } else if (graceTimer <= 10) {
                        soundInGame(NOTE_SOUND);
                        broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Grace Period Ending")
                                .replace("%time%", convertTime(graceTimer * 1000, language))));
                        --graceTimer;
                    } else if (graceTimer % 30 == 0) {
                        broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Grace Period Ending")
                                .replace("%time%", convertTime(graceTimer * 1000, language))));
                        --graceTimer;
                    }
                }
            }, 20L, 20L);
        }
    }

    public void endGame() {
        Player winner = instance.getPlayers().get(0);

        Bukkit.broadcastMessage(formatAll(language.getString("Game.Finished")
                .replace("%winner%", winner.getName())
                .replace("%time%", convertTime(System.currentTimeMillis() - startTime, language))));

        removePlayer(winner);

        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

        for (String command : settings.getStringList("Game.Winner Rewards")) {
            Bukkit.dispatchCommand(consoleCommandSender, command.replace("%player_name%", winner.getName()));
        }
    }

    public void addPlayer(Player player) {
        instance.getPlayers().add(player);

        UUID playerUUID = player.getUniqueId();
        PlayerInventory playerInventory = player.getInventory();
        InventoryManager inventoryManager = instance.getInventoryManager();

        inventoryManager.getConfig().set(playerUUID + ".inventory", itemStackArrayToBase64(playerInventory.getContents()));
        inventoryManager.getConfig().set(playerUUID + ".armor", itemStackArrayToBase64(playerInventory.getArmorContents()));
        inventoryManager.getConfig().set(playerUUID + ".experience", (double) player.getExp());
        inventoryManager.save();

        playerInventory.clear();
        playerInventory.setHelmet(null);
        playerInventory.setChestplate(null);
        playerInventory.setLeggings(null);
        playerInventory.setBoots(null);
        player.setExp(0.0f);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        if (status.equals(GameStatus.WAITING) && instance.getPlayers().size() >= settings.getInt("Game.Minimum Player Count")) {
            status = GameStatus.STARTING;
        }

        player.teleport(deserializeLocation(settings.getString("Game.Locations.Lobby")));

        broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Waiting.Player Joined")
                .replace("%player_name%", player.getName())
                .replace("%current_count%", String.valueOf(instance.getPlayers().size()))
                .replace("%max_count%", String.valueOf(settings.getInt("Game.Maximum Player Count")))));
    }

    public void removePlayer(Player player) {
        if (!instance.getPlayers().contains(player)) {
            return;
        }

        instance.getPlayers().remove(player);
        int playerCount = instance.getPlayers().size();

        if (player.isOnline()) {
            UUID playerUUID = player.getUniqueId();
            InventoryManager inventoryManager = instance.getInventoryManager();
            FileConfiguration inventoryConfig = inventoryManager.getConfig();

            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear();
            playerInventory.setHelmet(null);
            playerInventory.setChestplate(null);
            playerInventory.setLeggings(null);
            playerInventory.setBoots(null);

            try {
                player.getInventory().setContents(itemStackArrayFromBase64(inventoryConfig.getString(playerUUID + ".inventory")));
                player.getInventory().setArmorContents(itemStackArrayFromBase64(inventoryConfig.getString(playerUUID + ".armor")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.updateInventory();
            player.setExp((float) inventoryConfig.getDouble(playerUUID + ".experience"));

            player.teleport(deserializeLocation(settings.getString("Game.Locations.Spawn")));
            inventoryConfig.set(playerUUID + ".inventory", null);
            inventoryConfig.set(playerUUID + ".armor", null);
            inventoryConfig.set(playerUUID + ".experience", null);
            inventoryConfig.set(String.valueOf(playerUUID), null);
            inventoryManager.save();
        }

        if (status.equals(GameStatus.STARTING) && playerCount < settings.getInt("Game.Minimum Player Count")) {
            status = GameStatus.WAITING;
            timer = settings.getInt("Game.Starting Counter");
        }

        if (status.equals(GameStatus.WAITING) || status.equals(GameStatus.STARTING)) {
            broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Waiting.Player Quit")
                    .replace("%player_name%", player.getName())
                    .replace("%current_count%", String.valueOf(instance.getPlayers().size()))
                    .replace("%max_count%", String.valueOf(settings.getInt("Game.Maximum Player Count")))));
            return;
        }

        if (playerCount == 1) {
            endGame();
        } else if (playerCount < 1) {
            status = GameStatus.IDLE;
        }
    }

    public void broadcastInGame(String message) {
        for (Player player : instance.getPlayers()) {
            player.sendMessage(message);
        }
    }

    private void soundInGame(Sound sound) {
        for (Player player : instance.getPlayers()) {
            player.playSound(player.getEyeLocation(), sound, 1, 1);
        }
    }

    private void cancelCurrentTask() {
        Bukkit.getScheduler().cancelTask(currentTaskID);
    }

}
