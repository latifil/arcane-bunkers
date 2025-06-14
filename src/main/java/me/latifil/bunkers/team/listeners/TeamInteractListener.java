package me.latifil.bunkers.team.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.game.status.GameStatus;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class TeamInteractListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();
    private final MiniMessage mm = MiniMessage.miniMessage();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Game game = plugin.getGameManager().getGame();
        if (game.getStatus() != GameStatus.INGAME) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Optional<Profile> optProfile = plugin.getProfileManager()
                .getProfileByUuid(event.getPlayer().getUniqueId());
        if (optProfile.isEmpty()) return;
        Profile profile = optProfile.get();

        if (profile.isBypassMode()) {
            event.setCancelled(false);
            return;
        }
        if (profile.getTeam() == null) {
            event.setCancelled(true);
            return;
        }

        Material mat = event.getClickedBlock().getType();
        if (!(mat.name().contains("FENCE")
                || mat.name().contains("DOOR")
                || mat.name().contains("CHEST")
                || mat.name().contains("PLATE"))) {
            return;
        }

        Location loc = event.getClickedBlock().getLocation();
        Optional<Team> territoryOpt = plugin.getRegionManager().getTeamAt(loc);
        if (territoryOpt.isEmpty()) return;
        Team territory = territoryOpt.get();

        if (territory.isRaidable()) {
            event.setCancelled(false);
            return;
        }

        if (!territory.getId().equalsIgnoreCase(profile.getTeam().getId())) {
            event.setCancelled(true);
            profile.sendMessage(mm.deserialize(
                    "<yellow>You cannot do that in the territory of </yellow>"
                            + "<"   + territory.getDefinition().getColor().asHexString() + ">"
                            + territory.getDefinition().getDisplayName()
                            + "<"   + territory.getDefinition().getColor().asHexString() + "/>"
            ));
        }
    }
}
