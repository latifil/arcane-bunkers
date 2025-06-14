package me.latifil.bunkers.scoreboard;

import me.latifil.bunkers.scoreboard.manager.BunkersScoreboardManager;
import me.latifil.bunkers.scoreboard.api.ScoreboardProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreboardWrapper {

    private static ScoreboardWrapper instance;

    private final JavaPlugin plugin;
    private final ScoreboardProvider provider;
    private final BunkersScoreboardManager bunkersScoreboardManager;

    public ScoreboardWrapper(JavaPlugin plugin, ScoreboardProvider provider) {
        instance = this;
        this.plugin = plugin;
        this.provider = provider;
        this.bunkersScoreboardManager = new BunkersScoreboardManager();

        Bukkit.getPluginManager().registerEvents(bunkersScoreboardManager, plugin);
    }

    public static ScoreboardWrapper getInstance() {
        return instance;
    }

    public ScoreboardProvider getProvider() {
        return provider;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public BunkersScoreboardManager getScoreboardManager() {
        return bunkersScoreboardManager;
    }
}
