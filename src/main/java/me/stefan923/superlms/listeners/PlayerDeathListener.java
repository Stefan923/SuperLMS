package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener, MessageUtils {

    SuperLMS instance;

    public PlayerDeathListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity().getPlayer();

        if (instance.getPlayers().contains(player)) {
            player.spigot().respawn();
            instance.getGameManager().removePlayer(player);

            instance.getGameManager().broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Player Died").replace("%player_name%", player.getName())));
        }
    }

}
