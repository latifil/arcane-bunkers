package me.latifil.bunkers.listener.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.profile.status.ProfileStatus;
import me.latifil.bunkers.game.task.RespawnTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        Bunkers.getInstance()
                .getProfileManager()
                .getProfileByUuid(dead.getUniqueId())
                .ifPresent(profile -> {
                    if (profile.getTeam() == null) return;


                    Player killer = dead.getKiller();
                    if (killer != null) {
                        Bunkers.getInstance()
                                .getProfileManager()
                                .getProfileByUuid(killer.getUniqueId())
                                .ifPresent(kp -> {
                                    kp.setKills(kp.getKills() + 1);
                                    kp.deposit(250);
                                    Component msg = MM.deserialize(
                                            "<yellow>You have earned <green>$250</green> for killing " +
                                                    "<" + profile.getTeam().getColor().asHexString() + ">" +
                                                    dead.getName() +
                                                    "<" + profile.getTeam().getColor().asHexString() + "/>"
                                    );
                                    killer.sendMessage(msg);
                                });
                    }
                    reset(dead);

                    profile.setDeaths(profile.getDeaths() + 1);
                    if (profile.getTeam().isRaidable()) {
                        profile.setProfileStatus(ProfileStatus.SPECTATING);
                        return;
                    }
                    profile.setRespawnTime(30);
                    profile.setProfileStatus(ProfileStatus.RESPAWNING);
                    new RespawnTask(profile, dead, dead.getLocation())
                            .runTaskTimer(Bunkers.getInstance(), 0L, 20L);
                });
    }

    private void reset(Player p) {
        Bukkit.getOnlinePlayers().stream()
                .filter(other -> !other.getUniqueId().equals(p.getUniqueId()))
                .forEach(other -> other.hidePlayer(Bunkers.getInstance(), p));

        p.setGameMode(GameMode.CREATIVE);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);
        p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setVelocity(p.getVelocity().setY(1.1));
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
    }
}
