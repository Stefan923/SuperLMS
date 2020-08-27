package me.stefan923.superlms.listeners;

import me.stefan923.superlms.SuperLMS;
import me.stefan923.superlms.game.GameStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    SuperLMS instance;

    public FoodLevelChangeListener(SuperLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        final Player player = (Player) entity;
        GameStatus gameStatus = instance.getGameManager().getStatus();
        if ((instance.getPlayers().contains(player) && (gameStatus.equals(GameStatus.WAITING) || gameStatus.equals(GameStatus.STARTING) || gameStatus.equals(GameStatus.GRACE))) || instance.getSpectators().contains(player)) {
            player.setFoodLevel(20);
            event.setCancelled(true);
        }
    }

}
