package me.latifil.bunkers.region.model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class Pillar {
    private final Player player;
    private final Location corner;
    private final BlockData pillarData;

    public Pillar(final Player player, final Location corner, final Material material) {
        this.player = player;
        this.corner = corner.clone();
        if (material.isBlock()) {
            this.pillarData = material.createBlockData();
        } else {
            this.pillarData = Material.GLASS.createBlockData();
        }
    }

    public void display() {
        int startY = corner.getBlockY();
        int maxY = corner.getWorld().getMaxHeight();
        for (int y = startY; y < maxY; y++) {
            Location loc = new Location(
                    corner.getWorld(),
                    corner.getBlockX(),
                    y,
                    corner.getBlockZ()
            );

            if (loc.getBlock().getType() == Material.AIR) {
                player.sendBlockChange(loc, pillarData);
            }
        }
    }

    public void remove() {
        int startY = corner.getBlockY();
        int maxY = corner.getWorld().getMaxHeight();
        for (int y = startY; y < maxY; y++) {
            Location loc = new Location(
                    corner.getWorld(),
                    corner.getBlockX(),
                    y,
                    corner.getBlockZ()
            );
            BlockData real = loc.getBlock().getBlockData();
            player.sendBlockChange(loc, real);
        }
    }
}
