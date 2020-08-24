package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener, MessageUtils {

    SuperLMS instance;

    public PlayerRespawnListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDeath(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        if (instance.getPlayers().contains(player)) {
            instance.getGameManager().removePlayer(player);
        }
    }
}
