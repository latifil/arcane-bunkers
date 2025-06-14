package me.latifil.bunkers.region.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.region.manager.RegionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class RegionProfileListener implements Listener {
    private final RegionManager regionManager = Bunkers.getInstance().getRegionManager();

    private void cleanupSession(final PlayerEvent event) {
        Optional<Profile> optProfile = Bunkers.getInstance()
                .getProfileManager()
                .getProfileByUuid(event.getPlayer().getUniqueId());
        optProfile.ifPresent(profile ->
                regionManager.getSession(profile)
                        .ifPresent(session ->
                                regionManager.endSession(profile)
                        )
        );
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        cleanupSession(event);
    }

    @EventHandler
    public void onKick(final PlayerKickEvent event) {
        cleanupSession(event);
    }
}
