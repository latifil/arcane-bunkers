package me.latifil.bunkers.command.team.subcommands;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.model.Profile;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Optional;

public class TeamLocationCommand implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Optional<Profile> optionalProfile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (optionalProfile.isEmpty() || optionalProfile.get().getTeam() == null) {
            player.sendMessage(MM.deserialize("<red>You are not on a team."));
            return true;
        }

        Profile profile = optionalProfile.get();
        Location location = player.getLocation();
        String coords = formatCoords(location);

        Component message = MM.deserialize(
                "<dark_aqua>(Team) <yellow>" + player.getName() + "</yellow>: <white>" + coords
        );

        profile.getTeam().sendMessage(message);
        return true;
    }

    private String formatCoords(Location location) {
        DecimalFormat format = new DecimalFormat("0.00");
        return "[x: " + format.format(location.getX()) +
                ", y: " + format.format(location.getY()) +
                ", z: " + format.format(location.getZ()) + "]";
    }
}
