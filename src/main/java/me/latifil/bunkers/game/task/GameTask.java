package me.latifil.bunkers.game.task;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.game.model.Game;
import me.latifil.bunkers.game.status.GameStatus;
import me.latifil.bunkers.koth.model.Koth;
import me.latifil.bunkers.koth.task.KothTask;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public void run() {
        Game game = Bunkers.getInstance().getGameManager().getGame();
        int totalTime = game.getTotalTime() + 1;
        game.setTotalTime(totalTime);

        if (game.getInvincibilityTime() > 0) {
            game.setInvincibilityTime(game.getInvincibilityTime() - 1);
        } else if (game.getInvincibilityTime() == 0 && totalTime == 60) {
            Bukkit.broadcast(MM.deserialize("<red>Invincibility is now disabled and PvP has now been enabled!"));
        }

        if (totalTime == 30) {
            String kothName = game.getKothName() != null ? game.getKothName() : game.getMapName();
            Koth koth = new Koth(kothName, 360);
            Bunkers.getInstance().getKothManager().setKoth(koth);
            new KothTask().runTaskTimerAsynchronously(Bunkers.getInstance(), 0L, 20L);

            Bunkers.getInstance().getKothManager().broadcastMessage("<blue>" + koth.getName() + " <yellow>can now be contested.");
        }

        if (totalTime % 3 == 0) {
            Bunkers.getInstance().getProfileManager().getAllPlayingProfiles().forEach(
                    profile -> profile.setBalance(profile.getBalance() + 3)
            );
        }

        if (game.getStatus() == GameStatus.OVER) {
            cancel();
        }
    }
}
