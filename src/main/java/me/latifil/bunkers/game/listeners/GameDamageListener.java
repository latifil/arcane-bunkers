package me.latifil.bunkers.game.listeners;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class GameDamageListener implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onAnyDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player p)) return;

        Game game = Bunkers.getInstance().getGameManager().getGame();
        if (game.getInvincibilityTime() > 0) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) ||
                !(event.getDamager() instanceof Player damager)) return;

        Game game = Bunkers.getInstance().getGameManager().getGame();
        if (game.getInvincibilityTime() > 0) {
            event.setCancelled(true);
            Component msg = MM.deserialize("<red>The invincibility timer is still on.</red>");
            damager.sendMessage(msg);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        Game game = Bunkers.getInstance().getGameManager().getGame();
        if (game.getInvincibilityTime() > 0) {
            event.setCancelled(true);
        }
    }
}
