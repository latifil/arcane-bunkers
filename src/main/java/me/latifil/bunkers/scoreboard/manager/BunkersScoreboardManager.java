package me.latifil.bunkers.scoreboard.manager;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.scoreboard.model.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BunkersScoreboardManager implements Listener {

    private static final Map<UUID, PlayerScoreboard> SCOREBOARDS = new ConcurrentHashMap<>();
    private final BukkitTask updateTask;

    public BunkersScoreboardManager() {
        Bukkit.getOnlinePlayers().forEach(player ->
                SCOREBOARDS.putIfAbsent(player.getUniqueId(), new PlayerScoreboard(player))
        );

        this.updateTask = Bukkit.getScheduler().runTaskTimer(
                Bunkers.getInstance(),
                new ScoreboardUpdateTask(),
                1L, 1L
        );
    }

    public void rebuildPlayerBoard(Player player) {
        PlayerScoreboard old = SCOREBOARDS.remove(player.getUniqueId());
        if (old != null) {
            old.disable();
        }

        PlayerScoreboard fresh = new PlayerScoreboard(player);
        SCOREBOARDS.put(player.getUniqueId(), fresh);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                SCOREBOARDS.putIfAbsent(player.getUniqueId(), new PlayerScoreboard(player));
            }
        }.runTaskLater(Bunkers.getInstance(), 4L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerScoreboard board = SCOREBOARDS.remove(player.getUniqueId());
        if (board != null) {
            board.disable();
        }
    }

    public void unregister() {
        SCOREBOARDS.values().forEach(PlayerScoreboard::disable);
        SCOREBOARDS.clear();
        HandlerList.unregisterAll(this);
        updateTask.cancel();
    }

    private static class ScoreboardUpdateTask implements Runnable {
        @Override
        public void run() {
            SCOREBOARDS.values().forEach(scoreboard -> {
                scoreboard.update();
                scoreboard.updateTeams(Bukkit.getOnlinePlayers());
            });
        }
    }
}
