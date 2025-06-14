package me.latifil.bunkers.command.team.subcommands;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.profile.status.ChatStatus;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TeamChatCommand implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Optional<Profile> optionalProfile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (optionalProfile.isEmpty()) {
            player.sendMessage(MM.deserialize("<red>Could not find your profile."));
            return true;
        }

        Game game = plugin.getGameManager().getGame();
        if (!game.hasStarted()) {
            player.sendMessage(MM.deserialize("<red>You can only change chat status once the game has started."));
            return true;
        }

        Profile profile = optionalProfile.get();

        if (args.length != 1) {
            profile.setChatStatus(
                    profile.getChatStatus() == ChatStatus.TEAM ? ChatStatus.PUBLIC : ChatStatus.TEAM
            );
        } else {
            ChatStatus newStatus = ChatStatus.getStatusFromString(args[0]);
            if (newStatus == null) {
                profile.sendMessage(MM.deserialize("<red>Invalid chat status. Use 'team' or 'public'."));
                return true;
            }
            profile.setChatStatus(newStatus);
        }

        profile.sendMessage(profile.getChatStatus().getStatusMessage());
        return true;
    }
}
