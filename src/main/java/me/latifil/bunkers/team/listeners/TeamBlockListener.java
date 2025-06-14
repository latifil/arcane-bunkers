package me.latifil.bunkers.team.listeners;

import java.util.Optional;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.team.model.Team;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class TeamBlockListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Game game = plugin.getGameManager().getGame();
        if (!game.hasStarted()) return;

        Location loc = event.getBlock().getLocation();
        Optional<Profile> profileOpt = plugin.getProfileManager()
                .getProfileByUuid(event.getPlayer().getUniqueId());
        if (profileOpt.isEmpty()) return;

        Profile profile = profileOpt.get();
        if (profile.isBypassMode()) {
            event.setCancelled(false);
            return;
        }
        if (profile.getTeam() == null) {
            event.setCancelled(true);
            return;
        }

        Optional<Team> teamOpt = plugin.getRegionManager().getTeamAt(loc);
        if (teamOpt.isEmpty()) return;

        Team territory = teamOpt.get();
        if (territory.isRaidable()) {
            event.setCancelled(false);
            return;
        }

        if (territory != profile.getTeam()) {
            event.setCancelled(true);
            String hex = territory.getDefinition().getColor().asHexString();
            String name = territory.getDefinition().getDisplayName();
            profile.sendMessage(MM.deserialize(
                    "<yellow>You cannot do that in the territory of </yellow>" +
                            "<" + hex + ">" + name + "<" + hex + "/>" +
                            "<yellow>.</yellow>"
            ));
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Game game = plugin.getGameManager().getGame();
        if (!game.hasStarted()) return;

        Location loc = event.getBlock().getLocation();
        Optional<Profile> profileOpt = plugin.getProfileManager()
                .getProfileByUuid(event.getPlayer().getUniqueId());
        if (profileOpt.isEmpty()) return;

        Profile profile = profileOpt.get();
        if (profile.isBypassMode()) {
            event.setCancelled(false);
            return;
        }
        if (profile.getTeam() == null) {
            event.setCancelled(true);
            return;
        }

        Optional<Team> teamOpt = plugin.getRegionManager().getTeamAt(loc);
        if (teamOpt.isEmpty()) return;

        Team territory = teamOpt.get();
        if (territory.isRaidable()) {
            event.setCancelled(false);
            return;
        }

        if (territory != profile.getTeam()) {
            event.setCancelled(true);
            String hex = territory.getDefinition().getColor().asHexString();
            String name = territory.getDefinition().getDisplayName();
            profile.sendMessage(MM.deserialize(
                    "<yellow>You cannot do that in the territory of </yellow>" +
                            "<" + hex + ">" + name + "<" + hex + "/>" +
                            "<yellow>.</yellow>"
            ));
        }
    }
}
