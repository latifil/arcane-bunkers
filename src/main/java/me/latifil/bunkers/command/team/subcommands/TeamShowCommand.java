package me.latifil.bunkers.command.team.subcommands;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.koth.model.Koth;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamShowCommand implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command.");
            return true;
        }

        Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId()).orElse(null);
        if (profile == null) {
            player.sendMessage(MM.deserialize("<red>Could not find your profile."));
            return true;
        }

        Team team;

        if (args.length == 0) {
            team = profile.getTeam();
            if (team == null) {
                profile.sendMessage(MM.deserialize("<red>Usage: /" + label + " show <team>"));
                return true;
            }
        } else {
            team = plugin.getTeamManager().getTeamByString(args[0]).orElse(null);
            if (team == null) {
                profile.sendMessage(MM.deserialize("<red>No team by that name or player was found."));
                return true;
            }
        }

        getShowLines(team).forEach(profile::sendMessage);
        return true;
    }

    private List<Component> getShowLines(Team team) {
        List<Component> lines = new ArrayList<>();

        lines.add(MM.deserialize("<gray><st>-------------------------------------------------------------</st>"));

        if (!team.isPlayerTeam()) {
            lines.add(team.getFormattedName());

            if (team.getId().toLowerCase().contains("koth")) {
                Koth koth = plugin.getKothManager().getKoth();

                lines.add(MM.deserialize("<yellow>Location: <gray>0, 0"));
                lines.add(MM.deserialize("<yellow>Status: <gray>" + (koth != null ? "Contestable" : "Not contestable")));

                if (koth != null) {
                    lines.add(MM.deserialize("<yellow>Time: <gray>" + koth.getTime()));
                    lines.add(MM.deserialize("<yellow>Max Time: <gray>" + koth.getMaxCapTime()));
                }

                lines.add(Component.empty());
                lines.add(MM.deserialize("<yellow>Come to the center of the map to cap the KoTH."));
            } else {
                lines.add(MM.deserialize("<yellow>This is a system team."));
                lines.add(MM.deserialize("<yellow>Players cannot build outside team claims."));
            }

        } else {
            Location home = team.getHome();
            lines.add(MM.deserialize("<" + team.getColor().toString().toLowerCase() + ">" + team.getName() + " <gray>[" + team.getSize() + "/5]"));

            if (home != null) {
                lines.add(MM.deserialize("<yellow>Home: <gray>" + home.getBlockX() + ", " + home.getBlockZ()));
            } else {
                lines.add(MM.deserialize("<yellow>Home: <gray>None set"));
            }

            String members = String.join(", ",
                    team.getOnlinePlayers().stream().map(Player::getName).toList());

            lines.add(MM.deserialize("<yellow>Members: <gray>" + (members.isEmpty() ? "None online" : members)));

            String dtr = String.format("%.2f", team.getDtr());
            boolean raidable = team.isRaidable();

            lines.add(MM.deserialize("<yellow>DTR: " + (raidable
                    ? "<red>" + dtr + " [Raidable]"
                    : "<green>" + dtr + " [Not Raidable]")));
        }

        lines.add(MM.deserialize("<gray><st>-------------------------------------------------------------</st>"));
        return lines;
    }
}
