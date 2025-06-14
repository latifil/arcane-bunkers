package me.latifil.bunkers.scoreboard.model;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.scoreboard.ScoreboardWrapper;
import me.latifil.bunkers.scoreboard.api.ScoreboardProvider;
import me.latifil.bunkers.team.model.TeamDefinition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public final class PlayerScoreboard {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private final Player player;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Map<String, Team> teams = new HashMap<>();

    private static final String[] INVISIBLE_ENTRIES = {
            "§0",
            "§1",
            "§2",
            "§3",
            "§4",
            "§5",
            "§6",
            "§7",
            "§8",
            "§9",
            "§a",
            "§b",
            "§c",
            "§d",
            "§e"
    };

    public PlayerScoreboard(Player player) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.getObjective("SidebarWrapper") != null
                ? scoreboard.getObjective("SidebarWrapper")
                : scoreboard.registerNewObjective("SidebarWrapper", Criteria.DUMMY, Component.empty());

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);

        registerPlayerTeams();
        update();
    }

    private void registerPlayerTeams() {
        var teamManager = Bunkers.getInstance().getTeamManager();
        for (TeamDefinition def : teamManager.getTeamDefinitions()) {
            if (!def.isPlayerTeam()) continue;

            Team team = scoreboard.getTeam(def.getId());
            if (team == null) {
                team = scoreboard.registerNewTeam(def.getId());
            }

            team.prefix(Component.text("", def.getColor()));
            team.setAllowFriendlyFire(false);
            team.setCanSeeFriendlyInvisibles(true);
            teams.put(def.getId(), team);
        }
    }

    public void update() {
        if (!isActive()) return;

        ScoreboardProvider provider = ScoreboardWrapper.getInstance().getProvider();
        Component title = MM.deserialize(provider.getTitle(player));
        objective.displayName(title);

        List<String> rawLines = new ArrayList<>(provider.getLines(player));
        Collections.reverse(rawLines);

        List<String> oldEntries = new ArrayList<>(scoreboard.getEntries());
        if (rawLines.size() != oldEntries.size()) {
            for (String e : oldEntries) {
                scoreboard.resetScores(e);
            }
        }

        int line = 1;
        for (String rawLine : rawLines) {
            if (line > INVISIBLE_ENTRIES.length) break;
            String legacy = LEGACY.serialize(MM.deserialize(rawLine));

            if (legacy.length() > 40) {
                legacy = legacy.substring(0, 40);
            }

            String entryKey = legacy + ChatColor.RESET + getUniqueSuffix(line);
            objective.getScore(entryKey).setScore(line++);
        }
    }


    private String getUniqueSuffix(int line) {
        return ChatColor.COLOR_CHAR + Integer.toHexString(line % 16);
    }

    public void updateTeams(Iterable<? extends Player> players) {
        var manager = Bunkers.getInstance().getProfileManager();

        for (Player target : players) {
            Optional<Profile> opt = manager.getProfileByUuid(target.getUniqueId());
            if (opt.isEmpty()) continue;

            String teamId = Optional.ofNullable(opt.get().getTeam()).map(t -> t.getId()).orElse(null);

            for (Map.Entry<String, Team> entry : teams.entrySet()) {
                Team team = entry.getValue();
                boolean isCorrect = entry.getKey().equals(teamId);

                if (isCorrect && !team.hasPlayer(target)) {
                    team.addPlayer(target);
                } else if (!isCorrect) {
                    team.removePlayer(target);
                }
            }
        }
    }

    public void disable() {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        scoreboard.getEntries().forEach(scoreboard::resetScores);
        scoreboard.getObjectives().forEach(Objective::unregister);
        scoreboard.getTeams().forEach(Team::unregister);
    }

    private boolean isActive() {
        return player != null && player.isOnline() && player.getScoreboard() == scoreboard;
    }
}
