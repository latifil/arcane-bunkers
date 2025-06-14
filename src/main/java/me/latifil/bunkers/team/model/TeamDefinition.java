package me.latifil.bunkers.team.model;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class TeamDefinition {

    private final String id;
    private final String displayName;
    private final NamedTextColor color;
    private final boolean playerTeam;
    private final Material material;

    public TeamDefinition(String id, String displayName, NamedTextColor color, boolean playerTeam, Material material) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.playerTeam = playerTeam;
        this.material = material;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NamedTextColor getColor() {
        return color;
    }

    public boolean isPlayerTeam() {
        return playerTeam;
    }

    public Material getMaterial() {
        return material;
    }
}