package me.latifil.bunkers.listener.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.profile.status.ChatStatus;
import me.latifil.bunkers.profile.status.ProfileStatus;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;

import java.util.Optional;

public class ChatListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame();
        Optional<Profile> profileOpt = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());

        if (profileOpt.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        Profile profile = profileOpt.get();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if (!game.hasStarted()) {
            if (profile.getTeam() == null) {
                event.renderer((source, sourceDisplayName, msg, viewer) ->
                        MM.deserialize("<white>" + source.getName() + "</white>: " + escape(message)));
            } else {
                event.renderer((source, sourceDisplayName, msg, viewer) ->
                        MM.deserialize("<gray>[<" + profile.getTeam().getColor().asHexString() + ">" + profile.getTeam().getName() + "<gray>]<white>" + source.getName() + ": " + escape(message)));
            }
            return;
        }

        if (message.startsWith("@")) {
            Team team = profile.getTeam();
            if (team != null) {
                team.sendMessage(getTeamChatFormat(player, message.substring(1)));
            }
            event.setCancelled(true);
            return;
        }

        if (message.startsWith("!")) {
            event.renderer((source, sourceDisplayName, msg, viewer) ->
                    getGlobalChatFormat(player, profile, message.substring(1)));
            return;
        }

        if (profile.getChatStatus() == ChatStatus.TEAM && profile.getTeam() != null) {
            profile.getTeam().sendMessage(getTeamChatFormat(player, message));
            event.setCancelled(true);
        } else {
            event.renderer((source, sourceDisplayName, msg, viewer) ->
                    getGlobalChatFormat(player, profile, message));
        }
    }

    private Component getGlobalChatFormat(Player player, Profile profile, String message) {
        if (profile.getProfileStatus() == ProfileStatus.RESPAWNING) {
            return MM.deserialize("<gray>[Dead]</gray> <white>" + player.getName() + "</white>: <gray>" + escape(message));
        }

        Team team = profile.getTeam();
        String color = (team != null) ? "<" + team.getColor().asHexString() + ">" : "#FFFFFF";
        return MM.deserialize(color + player.getName() + "<" + color + "/>: <white>" + escape(message));
    }

    private Component getTeamChatFormat(Player player, String message) {
        return MM.deserialize("<dark_aqua>(Team)</dark_aqua> <yellow>" + player.getName() + "</yellow>: <white>" + escape(message));
    }

    private String escape(String input) {
        return MiniMessage.miniMessage().escapeTags(input);
    }
}
