package me.latifil.bunkers.scoreboard.provider;

import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.scoreboard.api.ScoreboardProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OverProvider implements ScoreboardProvider {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public List<String> getLines(Player player) {
        Game game = plugin.getGameManager().getGame();
        List<String> lines = new ArrayList<>();

        lines.add("<gold>Map:</gold> <gray>" + game.getMapName() + "</gray>");

        String winner = game.getWinner() == null ? "<gray>None</gray>" : MM.serialize(game.getWinner().getFormattedName());
        lines.add("<gold>Team:</gold> " + winner);

        return lines;
    }
}
