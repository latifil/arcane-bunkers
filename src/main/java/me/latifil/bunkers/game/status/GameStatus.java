package me.latifil.bunkers.game.status;

public enum GameStatus {

    LOBBY("Lobby"),
    STARTING("Starting"),
    INGAME("Ingame"),
    OVER("Over");

    private final String motd;

    GameStatus(String motd) {
        this.motd = motd;
    }

    public String getMotd() {
        return motd;
    }
}
