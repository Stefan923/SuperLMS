package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import me.stefan923.superlms.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener, MessageUtils {

    SuperLMS instance;

    public PlayerQuitListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (instance.getPlayers().contains(player)) {
            instance.getGameManager().removePlayer(player);

            GameStatus gameStatus = instance.getGameManager().getStatus();
            if (gameStatus.equals(GameStatus.GRACE) || gameStatus.equals(GameStatus.STARTED)) {
                instance.getGameManager().broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Player Died")
                        .replace("%player_name%", player.getName())
                        .replace("%current_count%", String.valueOf(instance.getPlayers().size()))));
            }
        }

        if (instance.getSpectators().contains(player)) {
            instance.getGameManager().removeSpectator(player);
        }
    }
}
