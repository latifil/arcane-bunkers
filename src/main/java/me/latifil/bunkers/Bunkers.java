package me.latifil.bunkers;

import me.latifil.bunkers.command.team.TeamCommand;
import me.latifil.bunkers.command.team.subcommands.TeamLocationCommand;
import me.latifil.bunkers.config.Config;
import me.latifil.bunkers.game.manager.GameManager;
import me.latifil.bunkers.koth.manager.KothManager;
import me.latifil.bunkers.listener.manager.ListenerManager;
import me.latifil.bunkers.profile.manager.ProfileManager;
import me.latifil.bunkers.region.manager.RegionManager;
import me.latifil.bunkers.region.model.Region;
import me.latifil.bunkers.scoreboard.ScoreboardWrapper;
import me.latifil.bunkers.scoreboard.provider.ProviderResolver;
import me.latifil.bunkers.team.manager.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bunkers extends JavaPlugin {

    private static Bunkers instance;

    private ProfileManager profileManager;
    private TeamManager teamManager;
    private GameManager gameManager;
    private KothManager kothManager;
    private RegionManager regionManager;
    private ListenerManager listenerManager;
    private ScoreboardWrapper scoreboardWrapper;
    private Config config;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        initializeManagers();
        registerCommands();
    }

    private void loadConfig() {
        this.config = new Config(this, "config");
    }

    private void initializeManagers() {
        this.profileManager = new ProfileManager();
        this.teamManager = new TeamManager();
        this.gameManager = new GameManager();
        this.kothManager = new KothManager();
        this.regionManager = new RegionManager();
        this.listenerManager = new ListenerManager();
        this.scoreboardWrapper = new ScoreboardWrapper(this, new ProviderResolver());
    }

    private void registerCommands() {
        TeamCommand teamCommand = new TeamCommand();
        TeamLocationCommand teamLocationCommand = new TeamLocationCommand();
        getCommand("team").setExecutor(teamCommand);
        getCommand("team").setTabCompleter(teamCommand);
        getCommand("teamlocation").setExecutor(teamLocationCommand);
    }

    @Override
    public void onDisable() {
    }

    public static Bunkers getInstance() {
        return instance;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public KothManager getKothManager() {
        return kothManager;
    }

    public ScoreboardWrapper getScoreboardWrapper() {
        return scoreboardWrapper;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
