package me.latifil.bunkers.team.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.profile.status.ProfileStatus;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class TeamDamageListener implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Bunkers plugin = Bunkers.getInstance();

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player target) ||
                !(event.getDamager()  instanceof Player attacker)) {
            return;
        }

        Optional<Profile> optTarget   = plugin.getProfileManager().getProfileByUuid(target.getUniqueId());
        Optional<Profile> optAttacker = plugin.getProfileManager().getProfileByUuid(attacker.getUniqueId());
        if (optTarget.isEmpty() || optAttacker.isEmpty()) {
            event.setCancelled(true);
            return;
        }

        Profile targetProfile   = optTarget.get();
        Profile attackerProfile = optAttacker.get();

        if (   targetProfile.getTeam() == null
                || attackerProfile.getTeam() == null
                || targetProfile.getProfileStatus()   != ProfileStatus.PLAYING
                || attackerProfile.getProfileStatus() != ProfileStatus.PLAYING) {
            event.setCancelled(true);
            attacker.sendMessage(MM.deserialize(
                    "<red>You cannot do this in your current state.</red>"
            ));
            return;
        }

        if (targetProfile.getTeam().equals(attackerProfile.getTeam())) {
            event.setCancelled(true);

            Team team = attackerProfile.getTeam();
            String hex = team.getDefinition().getColor().asHexString();
            String name = target.getName();

            Component msg = MM.deserialize(
                    "<yellow>You cannot hurt </yellow>" +
                            "<" + hex + ">" + name + "<" + hex + "/>" +
                            "<yellow>.</yellow>"
            );
            attacker.sendMessage(msg);
        }
    }
}