package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    SuperLMS instance;

    public EntityDamageListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        GameStatus status = instance.getGameManager().getStatus();

        final Player player = (Player) entity;
        if (instance.getPlayers().contains(player) && (status.equals(GameStatus.WAITING) || status.equals(GameStatus.STARTING) || status.equals(GameStatus.GRACE))) {
            event.setCancelled(true);
        }
    }

}
