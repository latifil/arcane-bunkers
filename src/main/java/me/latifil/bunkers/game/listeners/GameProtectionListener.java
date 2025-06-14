package me.latifil.bunkers.game.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class GameProtectionListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();

    private boolean shouldProtect(Player player) {
        Game game = plugin.getGameManager().getGame();
        return !game.hasStarted() && !player.isOp();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (shouldProtect(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (shouldProtect(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (shouldProtect(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (shouldProtect(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (!plugin.getGameManager().getGame().hasStarted()) {
            event.setCancelled(true);
        }
    }
}
