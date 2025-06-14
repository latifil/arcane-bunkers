package me.latifil.bunkers.command.team.subcommands;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.region.manager.RegionManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TeamClaimCommand implements CommandExecutor {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Bunkers plugin = Bunkers.getInstance();
    private final RegionManager regions = plugin.getRegionManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MM.deserialize("<red>Only players can use this command.</red>"));
            return true;
        }

        if (!player.hasPermission("bunkers.admin.claim")) {
            player.sendMessage(MM.deserialize("<red>You don't have permission to do that.</red>"));
            return true;
        }

        Optional<Profile> opt = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (opt.isEmpty()) {
            player.sendMessage(MM.deserialize("<red>Could not find your profile.</red>"));
            return true;
        }
        Profile profile = opt.get();

        if (profile.getTeam() == null) {
            player.sendMessage(MM.deserialize("<red>You must be on a team to claim.</red>"));
            return true;
        }

        if (regions.getSession(profile).isPresent()) {
            player.getInventory().remove(regions.getSelectingWand());
            regions.endSession(profile);
            player.sendMessage(MM.deserialize(
                    "<gray>Claiming <red>disabled</red> for your team.</gray>"
            ));
        } else {
            player.getInventory().addItem(regions.getSelectingWand());
            regions.beginSession(profile);
            player.sendMessage(MM.deserialize(
                    "<gray>Claiming <green>enabled</green> for your team.</gray>"
            ));
        }

        return true;
    }
}
