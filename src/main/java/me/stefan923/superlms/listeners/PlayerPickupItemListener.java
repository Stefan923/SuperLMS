package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItemListener implements Listener {

    SuperLMS instance;

    public PlayerPickupItemListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();

        if (instance.getPlayers().contains(player) || instance.getSpectators().contains(player)) {
            event.setCancelled(true);
        }
    }

}
