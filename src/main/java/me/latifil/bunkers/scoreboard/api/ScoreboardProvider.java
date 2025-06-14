package me.latifil.bunkers.scoreboard.api;

import me.latifil.bunkers.Bunkers;
import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardProvider {
    Bunkers plugin = Bunkers.getInstance();

    String getTitle(Player player);

    List<String> getLines(Player player);
}
