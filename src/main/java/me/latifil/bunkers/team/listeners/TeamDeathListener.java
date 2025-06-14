package me.latifil.bunkers.team.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TeamDeathListener implements Listener {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Bunkers.getInstance()
                .getProfileManager()
                .getProfileByUuid(event.getEntity().getUniqueId())
                .ifPresent(profile -> {
                    Team team = profile.getTeam();
                    if (team == null) return;

                    team.setDtr(team.getDtr() - 1);

                    profile.getPlayer().ifPresent(player -> {
                        String name = player.getName();
                        Component deathMsg = MINI.deserialize(
                                "<red>Member Death:</red> <white>" + name + "</white>"
                        );
                        Component dtrMsg = MINI.deserialize(
                                "<red>DTR:</red> <white>" + team.getDtr() + "</white>"
                        );
                        team.sendMessage(deathMsg);
                        team.sendMessage(dtrMsg);
                    });
                });
    }
}
