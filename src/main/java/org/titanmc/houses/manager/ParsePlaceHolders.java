package org.titanmc.houses.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class ParsePlaceHolders {


    private boolean isPlaceHolderAPIActive;

    public ParsePlaceHolders(boolean isPlaceHolderAPIActive){
        this.isPlaceHolderAPIActive = isPlaceHolderAPIActive;
    }

    public String addPlaceHolders(Player p, String s){
        if(isPlaceHolderAPIActive){
            s = PlaceholderAPI.setPlaceholders(p,s);
        }
        return s;
    }
}