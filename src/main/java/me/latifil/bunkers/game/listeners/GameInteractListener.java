package me.latifil.bunkers.game.listeners;


import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class GameInteractListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();
    private final Map<Material, Team> teamMap = new EnumMap<>(Material.class);
    private final MiniMessage MM = MiniMessage.miniMessage();

    public GameInteractListener() {
        teamMap.put(Material.RED_DYE, plugin.getTeamManager().getTeamByName("Red").orElse(null));
        teamMap.put(Material.LIGHT_BLUE_DYE, plugin.getTeamManager().getTeamByName("Blue").orElse(null));
        teamMap.put(Material.LIME_DYE, plugin.getTeamManager().getTeamByName("Green").orElse(null));
        teamMap.put(Material.YELLOW_DYE, plugin.getTeamManager().getTeamByName("Yellow").orElse(null));
        teamMap.put(Material.WHITE_DYE, null); // rand
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Game game = plugin.getGameManager().getGame();
        if (game.hasStarted()) return;

        Player player = event.getPlayer();
        Optional<Profile> optionalProfile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId());
        if (optionalProfile.isEmpty()) return;

        Profile profile = optionalProfile.get();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().isAir() || item.getType() == Material.AIR) {
            event.setCancelled(true);
            return;
        }

        Material clicked = item.getType();
        if (!teamMap.containsKey(clicked)) return;

        event.setCancelled(true);

        Team selectedTeam = teamMap.get(clicked);

        if (selectedTeam == null) {
            plugin.getTeamManager().getRandomTeam().addMember(profile);
        } else {
            if (profile.getTeam() != null && profile.getTeam().equals(selectedTeam)) {
                return;
            }
            if (selectedTeam.isFull()) {
                player.sendMessage(MM.deserialize("<red>That team is currently full. Please join another team."));
                return;
            }
            selectedTeam.addMember(profile);
        }

        Team current = profile.getTeam();
        player.sendMessage(
                MM.deserialize("<gray>You have joined team ")
                        .append(current.getFormattedName())
                        .append(MM.deserialize("<gray>. (" + current.getSize() + "/4)"))
        );
    }
}
