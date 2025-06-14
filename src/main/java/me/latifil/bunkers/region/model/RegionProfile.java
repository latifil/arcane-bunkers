package me.latifil.bunkers.region.model;

import me.latifil.bunkers.profile.model.Profile;
import org.bukkit.Location;

import java.util.UUID;

public class RegionProfile {

    private final UUID uuid;
    private final Profile profile;
    private Location cornerOne;
    private Location cornerTwo;
    private Pillar pillarOne;
    private Pillar pillarTwo;


    public RegionProfile(final Profile profile) {
        this.uuid       = profile.getId();
        this.profile    = profile;
        this.cornerOne  = null;
        this.cornerTwo  = null;
        this.pillarOne  = null;
        this.pillarTwo  = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Profile getProfile() {
        return profile;
    }

    public Location getCornerOne() {
        return cornerOne;
    }

    public void setCornerOne(final Location cornerOne) {
        this.cornerOne = cornerOne;
    }

    public Location getCornerTwo() {
        return cornerTwo;
    }

    public void setCornerTwo(final Location cornerTwo) {
        this.cornerTwo = cornerTwo;
    }

    public Pillar getPillarOne() {
        return pillarOne;
    }

    public void setPillarOne(final Pillar pillarOne) {
        this.pillarOne = pillarOne;
    }

    public Pillar getPillarTwo() {
        return pillarTwo;
    }

    public void setPillarTwo(final Pillar pillarTwo) {
        this.pillarTwo = pillarTwo;
    }

    public void clearPillars() {
        if (pillarOne != null) {
            pillarOne.remove();
            pillarOne = null;
        }
        if (pillarTwo != null) {
            pillarTwo.remove();
            pillarTwo = null;
        }
    }
}
