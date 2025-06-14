package me.latifil.bunkers.region.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.manager.GameManager;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.game.status.GameStatus;
import me.latifil.bunkers.region.events.PlayerEnterRegionEvent;
import me.latifil.bunkers.region.events.PlayerLeaveRegionEvent;
import me.latifil.bunkers.region.manager.RegionManager;
import me.latifil.bunkers.region.model.Region;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionMoveListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();
    private final GameManager gameManager = plugin.getGameManager();
    private final RegionManager regionManager = plugin.getRegionManager();
    private final MiniMessage mm = MiniMessage.miniMessage();

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        Game game = gameManager.getGame();
        if (game.getStatus() != GameStatus.INGAME) return;

        var from = event.getFrom();
        var to   = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;

        var player   = event.getPlayer();
        Team wilderness = plugin.getTeamManager()
                .getTeamById("wilderness")
                .orElseThrow();

        Team fromTeam = regionManager.getTeamAt(from).orElse(wilderness);
        Team toTeam   = regionManager.getTeamAt(to  ).orElse(wilderness);
        if (fromTeam == toTeam) return;

        Region fromClaim = fromTeam.getRegion();
        Region   toClaim = toTeam.getRegion();
        Bukkit.getPluginManager().callEvent(new PlayerLeaveRegionEvent(player, fromClaim));
        Bukkit.getPluginManager().callEvent(new PlayerEnterRegionEvent(player, toClaim));
        String fromId = fromTeam.getDefinition().getId();
        String   toId = toTeam  .getDefinition().getId();
        if ("KOTH_CAP".equalsIgnoreCase(fromId) || "KOTH_CAP".equalsIgnoreCase(toId)) {
            return;
        }

        player.sendMessage(mm.deserialize(
                "<yellow>Leaving:</yellow> <" + fromTeam.getDefinition().getColor().asHexString() + ">"
                        + fromTeam.getDefinition().getDisplayName() + "</" + fromTeam.getDefinition().getColor().asHexString() + ">"
        ));
        player.sendMessage(mm.deserialize(
                "<yellow>Entering:</yellow> <" + toTeam.getDefinition().getColor().asHexString() + ">"
                        + toTeam.getDefinition().getDisplayName() + "</" + fromTeam.getDefinition().getColor().asHexString() + ">"
        ));
    }
}
