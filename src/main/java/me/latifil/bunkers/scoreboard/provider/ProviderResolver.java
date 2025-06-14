package me.latifil.bunkers.scoreboard.provider;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.status.GameStatus;
import me.latifil.bunkers.scoreboard.api.ScoreboardProvider;
import org.bukkit.entity.Player;

import java.util.*;

public class ProviderResolver implements ScoreboardProvider {

    private final Bunkers plugin;
    private final Map<GameStatus, ScoreboardProvider> providers = new EnumMap<>(GameStatus.class);

    public ProviderResolver() {
        this.plugin = Bunkers.getInstance();

        LobbyProvider lobbyProvider = new LobbyProvider();
        providers.put(GameStatus.LOBBY, lobbyProvider);
        providers.put(GameStatus.STARTING, lobbyProvider);
        providers.put(GameStatus.INGAME, new IngameProvider());
        providers.put(GameStatus.OVER, new OverProvider());
    }

    @Override
    public String getTitle(Player player) {
        return "<bold><dark_purple>Arcane</dark_purple></bold> <white>❘ Bunkers";
    }

    @Override
    public List<String> getLines(Player player) {
        String LINE = "<gray><st>------------------------</st>";
        GameStatus status = plugin.getGameManager().getGame().getStatus();
        ScoreboardProvider provider = providers.get(status);

        if (provider == null) return Collections.emptyList();

        List<String> lines = new ArrayList<>();
        lines.add(LINE);
        lines.addAll(provider.getLines(player));
        lines.add(LINE);

        return lines;
    }
}