package me.latifil.bunkers.koth.manager;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.koth.model.Koth;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

public class KothManager {

    private final MiniMessage MM = MiniMessage.miniMessage();
    private Koth koth;

    public KothManager() {
        this.koth = null;
    }

    public Koth getKoth() {
        return this.koth;
    }

    public void setKoth(final Koth koth) {
        this.koth = koth;
    }

    public Component getFormattedKothName() {
        String name = Bunkers.getInstance()
                .getGameManager()
                .getGame()
                .getKothName();
        return MM.deserialize("<gold>" + name + " KoTH</gold>");
    }

    public Component getKothPrefix() {
        return MM.deserialize("<gold>[King of the Hill]</gold> <yellow>");
    }

    public void broadcastMessage(final String message) {
        Component full = getKothPrefix()
                .append(MM.deserialize(message));
        Bukkit.broadcast(full);
    }
}
