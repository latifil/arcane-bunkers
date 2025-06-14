package me.latifil.bunkers.scoreboard.provider;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.game.status.GameStatus;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.scoreboard.api.ScoreboardProvider;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LobbyProvider implements ScoreboardProvider {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public List<String> getLines(Player player) {
        Game game = plugin.getGameManager().getGame();
        List<String> lines = new ArrayList<>();

        Profile profile = plugin.getProfileManager().getProfileByUuid(player.getUniqueId())
                .orElse(null);
        if (profile != null && profile.getTeam() != null) {
            Team team = profile.getTeam();
            String teamName = MM.serialize(team.getFormattedName());
            lines.add("<gold>Team:</gold> " + teamName);
        } else {
            lines.add("<gold>Team:</gold> <gray>None</gray>");
        }

        lines.add("<gold>Map:</gold> <gray>" + game.getMapName() + "</gray>");



        if (game.getStatus() == GameStatus.STARTING) {
            lines.add("");
            lines.add("<Gold>Starting In:</Gold> <gray>" + game.getCountdownTime() + "</gray>");
        }

        return lines;
    }
}
