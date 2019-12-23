package org.titanmc.houses.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.titanmc.houses.Core;

public class LeaveEvent implements Listener{

    @EventHandler
    public void onLeave(PlayerQuitEvent e){

        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(e.getPlayer())){
            Core.getInstance().getPlayerManager().getHousePlayerMap().remove(e.getPlayer());
        }

    }
}
