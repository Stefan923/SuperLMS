package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class EntityDamageByEntityListener implements Listener {

    SuperLMS instance;

    public EntityDamageByEntityListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity entity = event.getDamager();
        if (!(entity instanceof Player)) {
            return;
        }

        final Player player = (Player) entity;
        if (instance.getSpectators().contains(player)) {
            event.setCancelled(true);
        }
    }

}
