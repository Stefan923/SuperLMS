package me.stefan923.superlms.utils;

import me.stefan923.superlms.settings.InventoryManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.UUID;

public interface PlayerUtils extends SerializationUtils {

    default int getTotalExperience(int level) {
        int xp = 0;

        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }

    default int getTotalExperience(Player player) {
        return Math.round(player.getExp() * player.getExpToLevel()) + getTotalExperience(player.getLevel());
    }

    default void setTotalExperience(Player player, int amount) {
        int level;
        int xp;
        float a = 0;
        float b = 0;
        float c = -amount;

        if (amount > getTotalExperience(0) && amount <= getTotalExperience(15)) {
            a = 1;
            b = 6;
        } else if (amount > getTotalExperience(15) && amount <= getTotalExperience(30)) {
            a = 2.5f;
            b = -40.5f;
            c += 360;
        } else if (amount > getTotalExperience(30)) {
            a = 4.5f;
            b = -162.5f;
            c += 2220;
        }
        level = (int) Math.floor((-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a));
        xp = amount - getTotalExperience(level);
        player.setLevel(level);
        player.setExp(0);
        player.giveExp(xp);
    }

    default void resetPlayerData(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();
        playerInventory.setHelmet(null);
        playerInventory.setChestplate(null);
        playerInventory.setLeggings(null);
        playerInventory.setBoots(null);
        player.updateInventory();

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        setTotalExperience(player, 0);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
    }

    default void savePlayerData(Player player, InventoryManager inventoryManager) {
        FileConfiguration fileConfiguration = inventoryManager.getConfig();
        PlayerInventory playerInventory = player.getInventory();
        UUID playerUUID = player.getUniqueId();

        fileConfiguration.set(playerUUID + ".inventory", itemStackArrayToBase64(playerInventory.getContents()));
        fileConfiguration.set(playerUUID + ".armor", itemStackArrayToBase64(playerInventory.getArmorContents()));
        fileConfiguration.set(playerUUID + ".experience", getTotalExperience(player));
        inventoryManager.save();
    }

    default void loadPlayerData(Player player, InventoryManager inventoryManager) {
        FileConfiguration fileConfiguration = inventoryManager.getConfig();
        UUID playerUUID = player.getUniqueId();

        try {
            player.getInventory().setContents(itemStackArrayFromBase64(fileConfiguration.getString(playerUUID + ".inventory")));
            player.getInventory().setArmorContents(itemStackArrayFromBase64(fileConfiguration.getString(playerUUID + ".armor")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.updateInventory();

        setTotalExperience(player, fileConfiguration.getInt(playerUUID + ".experience"));

        fileConfiguration.set(playerUUID + ".inventory", null);
        fileConfiguration.set(playerUUID + ".armor", null);
        fileConfiguration.set(playerUUID + ".experience", null);
        fileConfiguration.set(String.valueOf(playerUUID), null);
        inventoryManager.save();
    }

}
