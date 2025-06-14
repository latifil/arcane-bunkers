package me.latifil.bunkers.command.team.subcommands;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.region.manager.RegionManager;
import me.latifil.bunkers.team.manager.TeamManager;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TeamSetHomeCommand implements CommandExecutor {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Bunkers plugin = Bunkers.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MM.deserialize("<red>Only players can use this command.</red>"));
            return true;
        }
        if (!player.hasPermission("bunkers.admin.sethome")) {
            player.sendMessage(MM.deserialize("<red>You don't have permission to do that.</red>"));
            return true;
        }
        if (args.length != 1) {
            player.sendMessage(MM.deserialize("<yellow>Usage:</yellow> <white>/" + label + " sethome <team></white>"));
            return true;
        }

        TeamManager teamMgr = plugin.getTeamManager();
        Team target = teamMgr.getTeamByString(args[0]).orElse(null);
        if (target == null) {
            player.sendMessage(MM.deserialize("<red>No team found by that name or player.</red>"));
            return true;
        }

        RegionManager regions = plugin.getRegionManager();
        Optional<Team> hereOpt = regions.getTeamAt(player.getLocation());
        if (hereOpt.isEmpty() || ! hereOpt.get().getId().equalsIgnoreCase(target.getId())) {
            player.sendMessage(MM.deserialize(
                    "<red>You must be inside <" +
                            target.getDefinition().getColor().asHexString() + ">" +
                            target.getName() +
                            "</" + target.getDefinition().getColor().asHexString() + "> territory to set its home.</red>"
            ));
            return true;
        }

        Location loc = player.getLocation();
        target.setHome(loc);
        String ser = String.join(",",
                loc.getWorld().getName(),
                String.valueOf(loc.getX()),
                String.valueOf(loc.getY()),
                String.valueOf(loc.getZ()),
                String.valueOf(loc.getYaw()),
                String.valueOf(loc.getPitch())
        );
        plugin.getConfig().set("teams." + target.getId() + ".home", ser);
        plugin.saveConfig();

        player.sendMessage(MM.deserialize(
                "<green>Home for team </green>" +
                        "<" + target.getDefinition().getColor().asHexString() + ">" +
                        target.getName() +
                        "<" + target.getDefinition().getColor().asHexString() + "/> <green>set to your current location.</green>"
        ));
        return true;
    }
}
