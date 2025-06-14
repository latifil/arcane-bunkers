package me.latifil.bunkers.profile.model;

import me.latifil.bunkers.profile.status.ChatStatus;
import me.latifil.bunkers.profile.status.ProfileStatus;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class Profile {

    private final UUID id;
    private ProfileStatus profileStatus;
    private ChatStatus chatStatus = ChatStatus.TEAM;
    private Team team;

    private int kills = 0;
    private int deaths = 0;
    private int balance = 500;
    private int respawnTime = 0;

    private boolean hidingScoreboard = false;
    private boolean bypassMode = false;

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Profile(Player player, ProfileStatus profileStatus) {
        this.id = player.getUniqueId();
        this.profileStatus = profileStatus;
    }

    public UUID getId() {
        return id;
    }

    public ProfileStatus getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(ProfileStatus profileStatus) {
        this.profileStatus = profileStatus;
    }

    public ChatStatus getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(ChatStatus chatStatus) {
        this.chatStatus = chatStatus;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public boolean isHidingScoreboard() {
        return hidingScoreboard;
    }

    public void setHidingScoreboard(boolean hidingScoreboard) {
        this.hidingScoreboard = hidingScoreboard;
    }

    public boolean isBypassMode() {
        return bypassMode;
    }

    public void setBypassMode(boolean bypassMode) {
        this.bypassMode = bypassMode;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(id));
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    public void withdraw(int amount) {
        this.balance -= amount;
    }

    public void sendMessage(String message) {
        Component component = MINI_MESSAGE.deserialize(message);
        getPlayer().ifPresent(player -> player.sendMessage(component));
    }

    public void sendMessage(Component component) {
        getPlayer().ifPresent(player -> player.sendMessage(component));
    }
}
