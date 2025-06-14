package me.latifil.bunkers.region.manager;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.region.model.Region;
import me.latifil.bunkers.region.model.RegionProfile;
import me.latifil.bunkers.team.model.Team;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;

public class RegionManager {
    private final Bunkers plugin = Bunkers.getInstance();
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private final Map<String, Region> regions = new HashMap<>();
    private final Map<UUID, RegionProfile> sessions = new HashMap<>();

    public RegionManager() {
        ConfigurationSection teamsSection = plugin.getConfig().getConfigurationSection("teams");
        if (teamsSection != null) {
            for (String teamId : teamsSection.getKeys(false)) {
                Location c1 = plugin.getConfig().getLocation("teams." + teamId + ".cornerOne");
                Location c2 = plugin.getConfig().getLocation("teams." + teamId + ".cornerTwo");
                if (c1 == null || c2 == null) continue;

                plugin.getTeamManager()
                        .getTeamById(teamId)
                        .ifPresent(owner -> {
                            Region region = new Region(teamId, owner, c1, c2);
                            regions.put(teamId.toLowerCase(), region);
                        });
            }
        }
    }

    public void beginSession(Profile profile) {
        sessions.put(profile.getId(), new RegionProfile(profile));
    }

    public Optional<RegionProfile> getSession(Profile profile) {
        return Optional.ofNullable(sessions.get(profile.getId()));
    }

    public void endSession(Profile profile) {
        sessions.remove(profile.getId());
    }

    public void addRegion(Region region) {
        String id = region.getId().toLowerCase();
        regions.put(id, region);
        plugin.getConfig().set("teams." + id + ".cornerOne", region.getCornerOne());
        plugin.getConfig().set("teams." + id + ".cornerTwo", region.getCornerTwo());
        plugin.saveConfig();
    }

    public Optional<Region> getRegionAt(Location loc) {
        return regions.values().stream()
                .filter(r -> r.contains(loc))
                .findFirst();
    }

    public Optional<Team> getTeamAt(Location loc) {
        return getRegionAt(loc).map(Region::getOwner);
    }

    public ItemStack getSelectingWand() {
        ItemStack wand = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta meta = wand.getItemMeta();
        if (meta != null) {
            meta.displayName(MINI.deserialize("<green><bold>Region Wand</bold></green>"));
            wand.setItemMeta(meta);
        }
        return wand;
    }
}
