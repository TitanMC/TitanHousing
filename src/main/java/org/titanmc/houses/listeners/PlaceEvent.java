package org.titanmc.houses.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.titanmc.houses.Core;

public class PlaceEvent  implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){


        if(e.getBlockPlaced().getLocation().getWorld().getName().equalsIgnoreCase(Core.getInstance().getProperties().getPlotsWorldName())){
            if(e.getBlockPlaced().getLocation().getBlockY() >= Core.getInstance().getProperties().getGetMaxBuildHeight()){
                if(e.getPlayer().hasPermission("house.admin.build")) return;
                //If block being placed is a sign and the properties allow it
                if((e.getBlockPlaced().getType() == Material.SIGN_POST || e.getBlockPlaced().getType() == Material.WALL_SIGN) && Core.getInstance().getProperties().isAllowSignsInHouse()){
                    if(e.getBlockAgainst().getType() == Material.CHEST){
                        return;
                    }else{
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getSignNotOnChest()));
                    }
                }else{
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getBlockBrokenInHouse()));
                }
            }
        }
    }

    //Prevents liquid
    @EventHandler
    public void onLiquidPlace(PlayerBucketEmptyEvent e){

        if(e.getBlockClicked().getLocation().getWorld().getName().equalsIgnoreCase(Core.getInstance().getProperties().getPlotsWorldName())){
            if(e.getBlockClicked().getLocation().getBlockY() >= Core.getInstance().getProperties().getGetMaxBuildHeight()){
                if(e.getPlayer().hasPermission("house.admin.build")) return;
                //If block being placed is a sign and the properties allow it
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getBlockBrokenInHouse()));

            }
        }
    }
}
