package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    SuperLMS instance;

    public PlayerDropItemListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityDamage(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        if (instance.getPlayers().contains(player)) {
            event.setCancelled(true);
        }
    }

}
