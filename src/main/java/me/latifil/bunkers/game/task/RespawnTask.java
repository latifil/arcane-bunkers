package me.latifil.bunkers.game.task;

import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.profile.status.ProfileStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class RespawnTask extends BukkitRunnable {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private final Profile profile;
    private final Player player;
    private final Location startingLocation;

    public RespawnTask(Profile profile, Player player, Location startingLocation) {
        this.profile = profile;
        this.player = player;
        this.startingLocation = startingLocation;
    }

    @Override
    public void run() {
        if (profile == null || player == null || !player.isOnline()) {
            cancel();
            return;
        }

        if (profile.getTeam().isRaidable()) {
            player.sendMessage(MM.deserialize("<red>Your team has gone raidable and you will not respawn.</red>"));
            cancel();
            return;
        }

        if (startingLocation.distance(player.getLocation()) > 40) {
            player.teleport(startingLocation);
            player.sendMessage(MM.deserialize("<red>You cannot move more than 40 blocks from where you died.</red>"));
        }

        if (profile.getRespawnTime() > 0) {
            profile.setRespawnTime(profile.getRespawnTime() - 1);
        } else {
            profile.setProfileStatus(ProfileStatus.PLAYING);

            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(player, profile.getTeam().getHome(), false);
            Bukkit.getPluginManager().callEvent(respawnEvent);

            player.spigot().respawn();
            player.teleport(respawnEvent.getRespawnLocation());
            Bukkit.getOnlinePlayers().forEach(other -> other.showPlayer(player));
        }
    }
}
