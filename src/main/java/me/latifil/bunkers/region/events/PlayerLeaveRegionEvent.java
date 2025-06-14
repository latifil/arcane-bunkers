package me.latifil.bunkers.region.events;

import me.latifil.bunkers.event.BunkersEvent;
import me.latifil.bunkers.region.model.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerLeaveRegionEvent extends BunkersEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Region region;

    public PlayerLeaveRegionEvent(final Player player, final Region region) {
        this.player = player;
        this.region = region;
    }

    public Player getPlayer()  { return this.player; }
    public Region  getRegion()  { return this.region; }

    @Override public HandlerList getHandlers()      { return HANDLERS; }
    public    static HandlerList getHandlerList()   { return HANDLERS; }
}