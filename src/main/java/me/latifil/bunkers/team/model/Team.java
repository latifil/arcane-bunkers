package me.latifil.bunkers.team.model;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.region.model.Region;
import me.latifil.bunkers.profile.model.Profile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Team {
    private final TeamDefinition definition;

    private final Set<UUID> members = new HashSet<>();
    private final Set<String> offline = new HashSet<>();

    private double dtr = 1.0;
    private Location home;
    private Region region;

    public Team(TeamDefinition definition) {
        this.definition = definition;
    }


    public String getId() {
        return definition.getId();
    }

    public String getName() {
        return definition.getDisplayName();
    }

    public NamedTextColor getColor() {
        return definition.getColor();
    }

    public boolean isPlayerTeam() {
        return definition.isPlayerTeam();
    }

    public Component getFormattedName() {
        return Component.text(getName(), getColor());
    }


    public Set<UUID> getMembers() {
        return members;
    }

    public Set<String> getOffline() {
        return offline;
    }

    public List<Player> getOnlinePlayers() {
        return members.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .toList();
    }

    public void addMember(Profile profile) {
        if (profile.getTeam() != null) {
            profile.getTeam().removeMember(profile);
        }
        profile.setTeam(this);
        members.add(profile.getId());

        Player p = Bukkit.getPlayer(profile.getId());
        if (p != null) {
            Bukkit.getScheduler().runTask(
                    Bunkers.getInstance(),
                    () -> Bunkers.getInstance()
                            .getScoreboardWrapper()
                            .getScoreboardManager()
                            .rebuildPlayerBoard(p)
            );
        }
    }

    public void removeMember(Profile profile) {
        profile.setTeam(null);
        members.remove(profile.getId());

        Player p = Bukkit.getPlayer(profile.getId());
        if (p != null) {
            Bukkit.getScheduler().runTask(
                    Bunkers.getInstance(),
                    () -> Bunkers.getInstance()
                            .getScoreboardWrapper()
                            .getScoreboardManager()
                            .rebuildPlayerBoard(p)
            );
        }
    }

    public void sendMessage(Component message) {
        getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public int getSize() {
        return members.size();
    }

    public boolean isFull() {
        return getSize() >= 5;
    }

    public double getDtr() {
        return dtr;
    }

    public void setDtr(double dtr) {
        this.dtr = dtr;
    }

    public boolean isRaidable() {
        return dtr <= 0.0;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public TeamDefinition getDefinition() {
        return this.definition;
    }
}
