package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener, MessageUtils {

    SuperLMS instance;

    public PlayerCommandPreprocessListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();

        if (!instance.getPlayers().contains(player) && !instance.getSpectators().contains(player)) {
            return;
        }

        if (instance.getPlayers().contains(player) || instance.getSpectators().contains(player)) {
            for (final String command : instance.getSettingsManager().getConfig().getStringList("Game.Command Whitelist")) {
                if (event.getMessage().contains("/" + command) || command.equalsIgnoreCase("*")) {
                    return;
                }
            }
        }

        player.sendMessage(formatAll(instance.getLanguageManager().getConfig().getString("General.Blocked Command")));
        event.setCancelled(true);
    }

}
