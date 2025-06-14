package me.latifil.bunkers.scoreboard.provider;

import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.koth.model.Koth;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.scoreboard.api.ScoreboardProvider;
import me.latifil.bunkers.team.model.Team;
import me.latifil.bunkers.util.TimeUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IngameProvider implements ScoreboardProvider {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public String getTitle(Player player) {
        return null;
    }

    @Override
    public List<String> getLines(Player player) {
        Optional<Profile> optProfile = plugin.getProfileManager()
                .getProfileByUuid(player.getUniqueId());
        if (optProfile.isEmpty()) {
            return Collections.singletonList("<gray>Loading profile...</gray>");
        }

        Profile profile = optProfile.get();
        Game game = plugin.getGameManager().getGame();
        Koth koth = plugin.getKothManager().getKoth();

        var lines = new ArrayList<String>();

        switch (profile.getProfileStatus()) {

            case PLAYING -> {
                lines.add("<gray>Game Time:</gray> <gold>" +
                        TimeUtil.formatSeconds(game.getTotalTime()) + "</gold>");

                Team team = profile.getTeam();
                String teamName = team != null
                        ? MM.serialize(team.getFormattedName())
                        : "<italic><gray>None</gray></italic>";
                lines.add("<gray>Team:</gray> " + teamName);

                double dtr = (team != null ? team.getDtr() : 0.0);
                lines.add("<gray>DTR:</gray> <red>" + String.format("%.2f", dtr) + "</red>");

                if (koth != null) {
                    lines.add("<gray>" + game.getKothName() + ":</gray> <gold>" +
                            TimeUtil.formatSeconds(400) + "</gold>");
                }

                lines.add("<gray>Balance:</gray> <green>" +
                        profile.getBalance() + "</green>");

                int inv = game.getInvincibilityTime();
                if (inv > 0) {
                    lines.add("<gray>Invincibility:</gray> <yellow>" +
                            TimeUtil.formatSeconds(inv) + "</yellow>");
                }
            }

            case SPECTATING -> {
                lines.add("<gray>You are spectating.</gray>");
            }

            case RESPAWNING -> {
                lines.add("<gray>You are dead.</gray>");
                lines.add("<gray>Respawn in:</gray> <gold>" +
                        profile.getRespawnTime() + "</gold>");
            }
        }

        return lines;
    }
}
