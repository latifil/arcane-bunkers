package me.latifil.bunkers.team.manager;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.config.Config;
import me.latifil.bunkers.team.model.Team;
import me.latifil.bunkers.team.model.TeamDefinition;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class TeamManager {

    private final Set<Team> teams = new HashSet<>();
    private final Map<String, TeamDefinition> definitions = new HashMap<>();

    public TeamManager() {
        loadTeamDefinitions();
        registerAllTeams();
    }

    private void loadTeamDefinitions() {
        Bunkers plugin = Bunkers.getInstance();
        plugin.saveResource("teams.yml", false);
        Config teamConfig = new Config(plugin, "teams");
        YamlConfiguration config = teamConfig.getConfiguration();

        ConfigurationSection section = config.getConfigurationSection("teams");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection teamSec = section.getConfigurationSection(key);
            if (teamSec == null) continue;

            String name = teamSec.getString("display-name", key);
            String colorStr = teamSec.getString("color", "white");
            boolean isPlayerTeam = teamSec.getBoolean("is-player-team", true);
            Material material = Material.getMaterial(teamSec.getString("material", "STONE"));

            NamedTextColor color;
            try {
                color = NamedTextColor.NAMES.value(colorStr.toLowerCase());
            } catch (IllegalArgumentException ex) {
                color = NamedTextColor.WHITE;
            }

            TeamDefinition def = new TeamDefinition(key, name, color, isPlayerTeam, material);
            definitions.put(key.toLowerCase(), def);
        }
    }

    private void registerAllTeams() {
        teams.clear();
        for (TeamDefinition def : definitions.values()) {
            teams.add(new Team(def));
        }
    }

    public Set<Team> getAllPlayerTeams() {
        return teams.stream().filter(Team::isPlayerTeam).collect(Collectors.toSet());
    }

    public Optional<Team> getTeamById(String id) {
        return teams.stream().filter(t -> t.getId().equalsIgnoreCase(id)).findFirst();
    }

    public Optional<Team> getTeamByString(String input) {
        for (Team team : teams) {
            if (team.getId().equalsIgnoreCase(input) || team.getName().equalsIgnoreCase(input)) {
                return Optional.of(team);
            }
        }

        var player = Bukkit.getPlayerExact(input);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            return teams.stream()
                    .filter(team -> team.getMembers().contains(uuid))
                    .findFirst();
        }

        return Optional.empty();
    }

    public Optional<Team> getTeamByName(String name) {
        return getAllTeams().stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst();
    }


    public Collection<Team> getAllTeams() {
        return Collections.unmodifiableSet(teams);
    }

    public Team getRandomTeam() {
        List<Team> availableTeams = getAllPlayerTeams().stream()
                .filter(team -> !team.isFull())
                .toList();

        if (availableTeams.isEmpty()) {
            return null;
        }

        return availableTeams.get(ThreadLocalRandom.current().nextInt(availableTeams.size()));
    }

    public TeamDefinition getDefinition(String id) {
        return definitions.get(id.toLowerCase());
    }

    public Collection<TeamDefinition> getTeamDefinitions() {
        return Collections.unmodifiableCollection(definitions.values());
    }
}
