package me.latifil.bunkers.game.model;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.status.GameStatus;
import me.latifil.bunkers.team.model.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private GameStatus status;
    private int totalTime;
    private int countdownTime;
    private int invincibilityTime;
    private final Set<String> offline;
    private Team winner;
    private final String mapName;
    private final String kothName;
    private final Location spawnLocation;

    public Game(GameStatus status) {
        this.status = status;
        this.totalTime = 0;
        this.countdownTime = 45;
        this.invincibilityTime = 60;
        this.offline = new HashSet<>();
        this.winner = null;

        Bunkers plugin = Bunkers.getInstance();
        this.mapName = plugin.getConfig().getString("map");
        this.kothName = plugin.getConfig().getString("koth-name");

        Location configLocation = plugin.getConfig().getLocation("spawn-location");
        this.spawnLocation = (configLocation != null)
                ? configLocation
                : Bukkit.getWorlds().get(0).getSpawnLocation();
    }

    public boolean hasStarted() {
        return status == GameStatus.STARTING || status == GameStatus.INGAME;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(int countdownTime) {
        this.countdownTime = countdownTime;
    }

    public int getInvincibilityTime() {
        return invincibilityTime;
    }

    public void setInvincibilityTime(int invincibilityTime) {
        this.invincibilityTime = invincibilityTime;
    }

    public Set<String> getOffline() {
        return offline;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public String getMapName() {
        return mapName;
    }

    public String getKothName() {
        return kothName;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
