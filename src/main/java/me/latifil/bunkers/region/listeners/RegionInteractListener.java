package me.latifil.bunkers.region.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.region.manager.RegionManager;
import me.latifil.bunkers.region.model.Region;
import me.latifil.bunkers.region.model.RegionProfile;
import me.latifil.bunkers.region.model.Pillar;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class RegionInteractListener implements Listener {
    private final Bunkers plugin       = Bunkers.getInstance();
    private final RegionManager regions = plugin.getRegionManager();
    private final MiniMessage mm       = MiniMessage.miniMessage();

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.NETHERITE_HOE) return;

        event.setCancelled(true);

        Optional<Profile> opt = plugin.getProfileManager()
                .getProfileByUuid(player.getUniqueId());
        if (opt.isEmpty()) return;
        Profile profile = opt.get();

        Optional<RegionProfile> sessionOpt = regions.getSession(profile);
        if (sessionOpt.isEmpty()) return;
        RegionProfile session = sessionOpt.get();

        Team team = profile.getTeam();
        if (team == null) {
            player.sendMessage(mm.deserialize("<red>Please join a team first.</red>"));
            return;
        }
        Material marker = team.getDefinition().getMaterial();
        if (marker == null) {
            player.sendMessage(mm.deserialize("<red>Your team cannot claim regions.</red>"));
            return;
        }

        switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK -> {
                Location loc = event.getClickedBlock().getLocation();
                if (session.getPillarOne() != null) {
                    session.getPillarOne().remove();
                    session.setPillarOne(null);
                }
                session.setCornerOne(loc);
                Pillar p = new Pillar(player, loc, marker);
                session.setPillarOne(p);
                p.display();

                player.sendMessage(Component.empty()
                        .append(mm.deserialize("<gray>Corner 1 set for </gray>"))
                        .append(team.getFormattedName())
                        .append(mm.deserialize("<gray>.</gray>"))
                );
            }

            case LEFT_CLICK_BLOCK -> {
                Location loc = event.getClickedBlock().getLocation();
                if (session.getPillarTwo() != null) {
                    session.getPillarTwo().remove();
                    session.setPillarTwo(null);
                }
                session.setCornerTwo(loc);
                Pillar p2 = new Pillar(player, loc, marker);
                session.setPillarTwo(p2);
                p2.display();

                player.sendMessage(Component.empty()
                        .append(mm.deserialize("<gray>Corner 2 set for </gray>"))
                        .append(team.getFormattedName())
                        .append(mm.deserialize("<gray>.</gray>"))
                );
            }

            case LEFT_CLICK_AIR -> {
                if (!player.isSneaking()) return;
                session.clearPillars();
                session.setCornerOne(null);
                session.setCornerTwo(null);
                player.sendMessage(mm.deserialize("<green>Selection cleared.</green>"));
            }

            case RIGHT_CLICK_AIR -> {
                if (!player.isSneaking()) return;
                if (session.getCornerOne() == null || session.getCornerTwo() == null) {
                    player.sendMessage(mm.deserialize("<red>Select both corners first.</red>"));
                    return;
                }

                String regionId = team.getId() + "_" + System.currentTimeMillis();
                Region region = new Region(
                        regionId,
                        team,
                        session.getCornerOne(),
                        session.getCornerTwo()
                );
                regions.addRegion(region);

                String base = "regions." + regionId + ".";
                plugin.getConfig().set(base + "team",             team.getId());
                plugin.getConfig().set(base + "corner-one",      session.getCornerOne());
                plugin.getConfig().set(base + "corner-two",      session.getCornerTwo());
                plugin.saveConfig();
                plugin.reloadConfig();

                session.clearPillars();
                regions.endSession(profile);

                player.getInventory().removeItem(regions.getSelectingWand());

                player.sendMessage(Component.empty()
                        .append(mm.deserialize("<gray>Region claimed for </gray>"))
                        .append(team.getFormattedName())
                        .append(mm.deserialize("<gray>!</gray>"))
                );
            }

            default -> {
            }
        }
    }
}
