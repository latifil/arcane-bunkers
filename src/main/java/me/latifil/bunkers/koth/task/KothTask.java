package me.latifil.bunkers.koth.task;

import java.util.Map;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.koth.model.Koth;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.util.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class KothTask extends BukkitRunnable {

    private static final Map<Integer, Integer> TIMES = Map.of(
            900,  300,
            1800, 240,
            2700, 180
    );

    private final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public void run() {
        Koth koth = Bunkers.getInstance().getKothManager().getKoth();
        if (koth == null) return;

        if (koth.getCapper() != null) {
            koth.setTime(koth.getTime() - 1);

            if (koth.getTime() % 30 == 0) {
                String announce =
                        "<blue>"   + koth.getName() + "</blue> " +
                                "<yellow>is being controlled.</yellow> " +
                                "<red>("   + TimeUtil.formatSeconds(koth.getTime()) + ")</red>";
                Bunkers.getInstance().getKothManager().broadcastMessage(announce);
            }
        } else {
            koth.setTime(koth.getMaxCapTime());
        }

        if (koth.getCapper() == null && !koth.getCapQueue().isEmpty()) {
            Profile next = koth.getCapQueue().poll();
            koth.setCapper(next);

            next.sendMessage(MM.deserialize(
                    "<gold>Attempting to control </gold>" +
                            "<blue>" + koth.getName() + "</blue><gold>!</gold>"
            ));

            for (Player mate : next.getTeam().getOnlinePlayers()) {
                if (!mate.getUniqueId().equals(next.getId())) {
                    mate.sendMessage(MM.deserialize(
                            "<gold>Your team has started to control </gold>" +
                                    "<blue>" + koth.getName() + "</blue><gold>!</gold>"
                    ));
                }
            }
        }

        adjustCapTime(koth);
    }

    private void adjustCapTime(Koth koth) {
        int gameTime = Bunkers.getInstance().getGameManager().getGame().getTotalTime();
        Integer newCap = TIMES.get(gameTime);
        if (newCap != null && koth.getTime() > newCap) {
            koth.setTime(newCap);
            koth.setMaxCapTime(newCap);

            Bunkers.getInstance().getKothManager().broadcastMessage(
                    "<yellow>The time has been reduced to </yellow>" +
                            "<blue>" + TimeUtil.formatSeconds(newCap) + "</blue>"
            );
        }
    }
}
