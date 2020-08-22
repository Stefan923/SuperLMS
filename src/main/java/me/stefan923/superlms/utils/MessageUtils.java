package me.stefan923.superlms.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public interface MessageUtils {
    default String formatAll(String string) {
        return string.replace("&a", "§a")
                .replace("&b", "§b")
                .replace("&c", "§c")
                .replace("&d", "§d")
                .replace("&e", "§e")
                .replace("&f", "§f")
                .replace("&0", "§0")
                .replace("&1", "§1")
                .replace("&2", "§2")
                .replace("&3", "§3")
                .replace("&4", "§4")
                .replace("&5", "§5")
                .replace("&6", "§6")
                .replace("&7", "§7")
                .replace("&8", "§8")
                .replace("&9", "§9")
                .replace("&o", "§o")
                .replace("&l", "§l")
                .replace("&m", "§m")
                .replace("&n", "§n")
                .replace("&k", "§k")
                .replace("&r", "§r");
    }

    default void sendLogger(final String string) {
        Bukkit.getConsoleSender().sendMessage(formatAll(string));
    }

    default void sendCenteredMessage(CommandSender player, String message) {
        if (message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontSize dFI = FontSize.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontSize.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    default String convertTime(final long time, final FileConfiguration language) {
        StringBuilder stringBuilder = new StringBuilder();

        int years = (int) (time / 1000 / 60 / 60 / 24 / 30 / 12);
        if (years != 0)
            stringBuilder.append(years).append(" ").append(years == 1 ? language.getString("General.Word Year") : language.getString("General.Word Years"));
        int months = (int) ((time / 1000 / 60 / 60 / 24 / 30) % 12);
        if (months != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(months).append(" ").append(months == 1 ? language.getString("General.Word Month") : language.getString("General.Word Months"));
        int days = (int) ((time / 1000 / 60 / 60 / 24) % 30);
        if (days != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(days).append(" ").append(days == 1 ? language.getString("General.Word Day") : language.getString("General.Word Days"));
        int hours = (int) ((time / 1000 / 60 / 60) % 24);
        if (hours != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(hours).append(" ").append(hours == 1 ? language.getString("General.Word Hour") : language.getString("General.Word Hours"));
        int minutes = (int) ((time / 1000 / 60) % 60);
        if (minutes != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(minutes).append(" ").append(minutes == 1 ? language.getString("General.Word Minute") : language.getString("General.Word Minutes"));
        int seconds = (int) ((time / 1000) % 60);
        if (seconds != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(seconds).append(" ").append(seconds == 1 ? language.getString("General.Word Second") : language.getString("General.Word Seconds"));

        return stringBuilder.toString();
    }
}
