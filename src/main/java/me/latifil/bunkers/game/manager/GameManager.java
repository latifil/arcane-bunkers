package me.latifil.bunkers.game.manager;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.status.GameStatus;
import me.latifil.bunkers.game.task.CountdownTask;
import me.latifil.bunkers.game.task.GameTask;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import me.latifil.bunkers.game.model.Game;

public class GameManager {

    private final Game game;
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public GameManager() {
        this.game = new Game(GameStatus.LOBBY);
    }

    public Game getGame() {
        return game;
    }

    public void start() {
        game.setStatus(GameStatus.STARTING);
        Bukkit.getOnlinePlayers().forEach(p -> p.getInventory().clear());
        new CountdownTask().runTaskTimerAsynchronously(Bunkers.getInstance(), 0L, 20L);
        Bukkit.broadcast(MM.deserialize("<yellow>The countdown is now starting"));
    }

    public void stop(Team winner) {
        game.setWinner(winner);
        game.setStatus(GameStatus.OVER);
        game.setTotalTime(0);
        clearAllMobs();

        Bukkit.getOnlinePlayers().forEach(player -> {
            Bukkit.getOnlinePlayers().forEach(other -> {
                player.showPlayer(Bunkers.getInstance(), other);
                other.showPlayer(Bunkers.getInstance(), player);
            });

            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setExp(0.0F);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        });

        new BukkitRunnable() {
            int time = 15;

            @Override
            public void run() {
                if (time == -2) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                    return;
                }

                if (time == 0) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.kick(Component.text("Thanks for playing Bunkers beta, join back soon!")));
                } else if (time % 2 == 0) {
                    if (winner == null) {
                        Bukkit.broadcast(MM.deserialize("<yellow>The game was forcefully stopped."));
                    } else {
                        Bukkit.broadcast(MM.deserialize("<yellow>The team <" + winner.getColor().toString().toLowerCase() + ">" + winner.getName() + " <yellow>has won the game!"));
                    }
                }

                time--;
            }
        }.runTaskTimer(Bunkers.getInstance(), 0L, 20L);
    }

    public void tryStart() {
        if (Bukkit.getOnlinePlayers().size() == 16) {
            start();
        }
    }

    public void finallyStart() {
        game.setStatus(GameStatus.INGAME);

        Bunkers.getInstance().getTeamManager().getAllPlayerTeams().forEach(team -> {
            Location home = team.getHome();
            if (!home.getChunk().isLoaded()) {
                home.getChunk().load();
            }

            team.getOnlinePlayers().forEach(player -> {
                giveStartingItems(player);
                player.setGameMode(GameMode.SURVIVAL);
                player.setAllowFlight(false);
                player.setFlying(false);
                player.teleport(home);
            });
        });

        new GameTask().runTaskTimerAsynchronously(Bunkers.getInstance(), 0L, 20L);
        Bukkit.broadcast(MM.deserialize("<yellow>The game has now started! Good luck!"));
    }

    private void giveStartingItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
        player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
    }

    private void clearAllMobs() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream()
                .filter(entity -> entity.getType() != EntityType.PLAYER)
                .forEach(entity -> entity.remove()));
    }
}
