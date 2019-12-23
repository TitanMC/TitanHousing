package org.titanmc.houses.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.titanmc.houses.Core;

public class JoinEvent implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Core.getInstance().getDb().loadHouseValues(e.getPlayer());

    }
}
