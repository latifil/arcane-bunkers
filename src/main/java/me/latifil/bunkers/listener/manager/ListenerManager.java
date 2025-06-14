package me.latifil.bunkers.listener.manager;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.listeners.GameDamageListener;
import me.latifil.bunkers.game.listeners.GameInteractListener;
import me.latifil.bunkers.game.listeners.GameProtectionListener;
import me.latifil.bunkers.koth.listener.KothMoveListener;
import me.latifil.bunkers.listener.listeners.ChatListener;
import me.latifil.bunkers.listener.listeners.PlayerDeathListener;
import me.latifil.bunkers.listener.listeners.PlayerRespawnListener;
import me.latifil.bunkers.profile.listeners.ProfileListener;
import me.latifil.bunkers.profile.listeners.ProfileStatusListener;
import me.latifil.bunkers.region.listeners.RegionInteractListener;
import me.latifil.bunkers.region.listeners.RegionMoveListener;
import me.latifil.bunkers.region.listeners.RegionProfileListener;
import me.latifil.bunkers.team.listeners.TeamBlockListener;
import me.latifil.bunkers.team.listeners.TeamDamageListener;
import me.latifil.bunkers.team.listeners.TeamInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class ListenerManager {

    private final Bunkers plugin;

    public ListenerManager() {
        this.plugin = Bunkers.getInstance();
        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        List<Listener> listeners = List.of(
                new ProfileListener(),
                new ProfileStatusListener(),
                new ChatListener(),
                new GameInteractListener(),
                new GameDamageListener(),
                new GameProtectionListener(),
                new RegionInteractListener(),
                new RegionMoveListener(),
                new RegionProfileListener(),
                new KothMoveListener(),
                new PlayerRespawnListener(),
                new PlayerDeathListener(),
                new TeamInteractListener(),
                new TeamDamageListener(),
                new TeamBlockListener(),
                new TeamBlockListener()
        );

        listeners.forEach(listener -> pluginManager.registerEvents(listener, plugin));
    }
}
