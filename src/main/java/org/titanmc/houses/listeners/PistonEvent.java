package org.titanmc.houses.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.titanmc.houses.Core;

public class PistonEvent implements Listener {

    @EventHandler
    public void onPiston(BlockPistonExtendEvent e){
        if(e.getBlock().getWorld().getName().equals(Core.getInstance().getProperties().getPlotsWorldName())){
            for(Block block : e.getBlocks()){
                if(block.getY() >= Core.getInstance().getProperties().getGetMaxBuildHeight()){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
