package me.latifil.bunkers.profile.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.profile.status.ProfileStatus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ProfileListener implements Listener {

    private final Bunkers plugin = Bunkers.getInstance();
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame();
        Inventory inventory = player.getInventory();
        inventory.clear();

        Profile profile;
        if (game.hasStarted()) {
            profile = new Profile(player, ProfileStatus.SPECTATING);
            player.teleport(game.getSpawnLocation());
            event.joinMessage(null);
        } else {
            profile = new Profile(player, ProfileStatus.PLAYING);
            player.teleport(game.getSpawnLocation());

            inventory.setItem(2, createTeamItem(Material.RED_DYE, "<red>Red Team"));
            inventory.setItem(3, createTeamItem(Material.YELLOW_DYE, "<yellow>Yellow Team"));
            inventory.setItem(4, createTeamItem(Material.WHITE_DYE, "<gradient:#FF5555:#FFFF55:#55FF55:#55FFFF>Random Team</gradient>"));
            inventory.setItem(5, createTeamItem(Material.LIME_DYE, "<green>Green Team"));
            inventory.setItem(6, createTeamItem(Material.LIGHT_BLUE_DYE, "<aqua>Blue Team"));
            player.updateInventory();

            int playerCount = Bukkit.getOnlinePlayers().size();
            event.joinMessage(MM.deserialize("<white>" + player.getName() + " <gray>has joined. (" + playerCount + "/16)"));
            plugin.getGameManager().tryStart();
        }

        plugin.getProfileManager().addProfile(profile);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        removeProfile(event);
        event.quitMessage(null);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removeProfile(event);
        event.leaveMessage(null);
    }

    private void removeProfile(PlayerEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.getGameManager().getGame();

        plugin.getProfileManager().getProfileByUuid(player.getUniqueId()).ifPresent(profile -> {
            if (profile.getTeam() != null) {
                if (game.hasStarted()) {
                    game.getOffline().add(player.getName());
                    profile.getTeam().getOffline().add(player.getName());
                }
                profile.getTeam().removeMember(profile);
            }
            plugin.getProfileManager().removeProfile(profile);
        });
    }

    private ItemStack createTeamItem(Material material, String displayNameMiniMessage) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        Component name = MM.deserialize(displayNameMiniMessage);
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
