package me.latifil.bunkers.game.task;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public void run() {
        Game game = Bunkers.getInstance().getGameManager().getGame();
        int time = game.getCountdownTime() - 1;
        game.setCountdownTime(time);

        if (time <= 0) {
            Bunkers.getInstance().getGameManager().finallyStart();
            cancel();
            return;
        }

        if (time % 5 == 0 || time <= 5) {
            String message = "<yellow>Game starting in <green>" + time +
                    "<yellow>" + (time == 1 ? " second" : " seconds") + ".";
            Bukkit.broadcast(MM.deserialize(message));
        }
    }
}
