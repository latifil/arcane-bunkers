package me.latifil.bunkers.region.model;

import me.latifil.bunkers.team.model.Team;
import org.bukkit.Location;

public class Region {
    private final String  id;
    private final Team    owner;
    private final Location cornerOne, cornerTwo;

    public Region(String id, Team owner, Location cornerOne, Location cornerTwo) {
        this.id         = id;
        this.owner      = owner;
        this.cornerOne  = cornerOne;
        this.cornerTwo  = cornerTwo;
    }

    public String getId()          { return id; }
    public Team getOwner()         { return owner; }

    public boolean contains(Location loc) {
        int x = loc.getBlockX(), z = loc.getBlockZ();
        int minX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int maxX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
        int minZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        int maxZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }

    public Location getCornerOne() {
        return cornerOne;
    }

    public Location getCornerTwo() {
        return cornerTwo;
    }
}
