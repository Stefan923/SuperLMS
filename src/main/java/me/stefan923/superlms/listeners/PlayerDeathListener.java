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
            event.setKeepInventory(true);
            event.setKeepLevel(true);

            player.spigot().respawn();
            event.setDeathMessage(null);

            instance.getGameManager().broadcastInGame(formatAll(instance.getLanguageManager().getConfig().getString("Game.Player Died")
                    .replace("%player_name%", player.getName())
                    .replace("%current_count%", String.valueOf(instance.getPlayers().size()))));
        }
    }

}
