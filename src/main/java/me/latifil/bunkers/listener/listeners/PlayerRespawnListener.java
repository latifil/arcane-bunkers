package me.latifil.bunkers.listener.listeners;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.SURVIVAL);
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH)
                .getValue();
        player.setHealth(maxHealth);
        player.setFoodLevel(20);
        player.setSaturation(5f);
        player.setFireTicks(0);
        player.getActivePotionEffects().stream()
                .map(PotionEffect::getType)
                .forEach(player::removePotionEffect);
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
