package org.titanmc.houses.events;

import com.github.intellectualsites.plotsquared.plot.object.Plot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HouseEnterEvent extends Event {
    /**
     * Called when a user enters a house area
     */

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Plot plot;


    public HouseEnterEvent(Player player,Plot plot){
        this.player = player;
        this.plot = plot;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    /**
     * @return Returns the Player who entered the house
     */
    public Player getPlayer() {
        return player;
    }
    /**
     * @return Returns the Plot (house) the user has entered
     */
    public Plot getPlot() {
        return plot;
    }
}