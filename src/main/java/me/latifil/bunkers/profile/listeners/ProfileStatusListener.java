package me.latifil.bunkers.profile.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.status.ProfileStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

import java.util.UUID;

public class ProfileStatusListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();

    private boolean shouldCancel(UUID playerId) {
        return plugin.getProfileManager()
                .getProfileByUuid(playerId)
                .map(profile -> profile.getProfileStatus() != ProfileStatus.PLAYING)
                .orElse(true);
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        if (shouldCancel(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        if (shouldCancel(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent event) {
        if (shouldCancel(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleItemPickup(PlayerAttemptPickupItemEvent event) {
        if (shouldCancel(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
