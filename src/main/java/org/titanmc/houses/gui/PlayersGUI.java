package org.titanmc.houses.gui;

import org.bukkit.entity.Player;
import org.titanmc.houses.Core;

/**
 * Created by Jet on 07/02/2018.
 */
public class PlayersGUI extends GUI{

    private Player p;
    private int houseLevel;
    private boolean isHouseLocked;

    /**
     *
     * @param p
     * @param houseLevel
     * @param isHouseLocked
     * @param instance
     */
    public PlayersGUI(Player p, int houseLevel, boolean isHouseLocked, Core instance){
        super(instance);
        this.p = p;
        this.houseLevel = houseLevel;
        this.isHouseLocked = isHouseLocked;
        super.openGUI(p);
    }

    /**
     *
     * @return The Player who is opening the GUI
     */
    @Override
    public Player getPlayer() {
        return p;
    }

    /**
     *
     * @return The house level of the player
     */
    @Override
    public int houseLevel() {
        return houseLevel;
    }

    /**
     *
     * @return Whether the house is locked or not
     */
    @Override
    public boolean isHouseLocked() {
        return isHouseLocked;
    }
}
