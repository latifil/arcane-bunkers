package me.latifil.bunkers.koth.listener;

import me.latifil.bunkers.Bunkers;
import me.latifil.bunkers.koth.model.Koth;
import me.latifil.bunkers.profile.model.Profile;
import me.latifil.bunkers.profile.status.ProfileStatus;
import me.latifil.bunkers.region.events.PlayerEnterRegionEvent;
import me.latifil.bunkers.region.events.PlayerLeaveRegionEvent;
import me.latifil.bunkers.team.model.Team;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KothMoveListener implements Listener {

    private static final String KOTH_ID = "koth";
    private final MiniMessage MM = MiniMessage.miniMessage();

    @EventHandler
    public void onPlayerEnterClaim(PlayerEnterRegionEvent event) {
        if (event.getRegion() == null) return;

        Team team = event.getRegion().getOwner();
        if (!team.getDefinition().getId().equalsIgnoreCase(KOTH_ID)) return;

        Koth koth = Bunkers.getInstance().getKothManager().getKoth();
        if (koth == null) return;

        Profile profile = Bunkers.getInstance()
                .getProfileManager()
                .getProfileByUuid(event.getPlayer().getUniqueId())
                .orElse(null);
        if (profile == null || profile.getProfileStatus() != ProfileStatus.PLAYING) return;

        koth.getCapQueue().add(profile);
    }

    @EventHandler
    public void onPlayerLeaveClaim(PlayerLeaveRegionEvent event) {
        if (event.getRegion() == null) return;

        Team team = event.getRegion().getOwner();
        if (!team.getDefinition().getId().equalsIgnoreCase(KOTH_ID)) return;

        Koth koth = Bunkers.getInstance().getKothManager().getKoth();
        if (koth == null) return;

        Profile profile = Bunkers.getInstance()
                .getProfileManager()
                .getProfileByUuid(event.getPlayer().getUniqueId())
                .orElse(null);
        if (profile == null || profile.getProfileStatus() != ProfileStatus.PLAYING) return;

        if (profile.equals(koth.getCapper())) {
            koth.setCapper(null);

            if (koth.getTime() <= 180) {
                Bunkers.getInstance().getKothManager().broadcastMessage(
                        "<red>Control of </red>" +
                                "<" + MM.deserialize("<gradient:#ff5555:#55ffff>" + koth.getName() + "</gradient>").color() + ">" +
                                "<red> has been lost!</red>"
                );
            }

            Component msg = Bunkers.getInstance()
                    .getKothManager()
                    .getKothPrefix()
                    .append(MM.deserialize("<red>You have lost control of the KoTH.</red>"));
            event.getPlayer().sendMessage(msg);
        }

        koth.getCapQueue().remove(profile);
    }
}
