package org.titanmc.houses.listeners;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.titanmc.houses.Core;

public class PlaceHolderRequest extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "TitanHousing";
    }

    @Override
    public String getAuthor() {
        return "OrgTitanMC";
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        if(s.equalsIgnoreCase("owns_house")){
            return String.valueOf(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player));
        }
        if(s.equalsIgnoreCase("days_left")){
            if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player)) return "0";
            Integer[] expiryDate = Math.calculateTimeLeft(Core.getInstance().getPlayerManager().getHousePlayerMap().get(player).getMillisecondsOfExpiry());
            return String.valueOf(expiryDate[0]);
        }
        if(s.equalsIgnoreCase("hours_left")){
            if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player)) return "0";
            Integer[] expiryDate = Math.calculateTimeLeft(Core.getInstance().getPlayerManager().getHousePlayerMap().get(player).getMillisecondsOfExpiry());
            return String.valueOf(expiryDate[1]);
        }

        if(s.equalsIgnoreCase("minutes_left")){
            if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player)) return "0";
            Integer[] expiryDate = Math.calculateTimeLeft(Core.getInstance().getPlayerManager().getHousePlayerMap().get(player).getMillisecondsOfExpiry());
            return String.valueOf(expiryDate[2]);
        }

        if(s.equalsIgnoreCase("seconds_left")){
            if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player)) return "0";
            Integer[] expiryDate = Math.calculateTimeLeft(Core.getInstance().getPlayerManager().getHousePlayerMap().get(player).getMillisecondsOfExpiry());
            return String.valueOf(expiryDate[3]);
        }
        if(s.equalsIgnoreCase("current_tier")){
            if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player)) return "0";
            return String.valueOf(Core.getInstance().getPlayerManager().getHousePlayerMap().get(player).getHouseLevel());
        }
        if(s.equalsIgnoreCase("lock_status")){
            if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player)) return "false";
            return String.valueOf(Core.getInstance().getPlayerManager().getHousePlayerMap().get(player).getIsHouseLocked())/*.equalsIgnoreCase("false") ? ChatColor.GREEN + "" + ChatColor.BOLD + "✔ UNLOCKED" : ChatColor.RED + "" + ChatColor.BOLD + "LOCKED ✖" */;
        }

        if(s.equalsIgnoreCase("next_upgrade_price")){
            if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(player)) return Core.getInstance().getMessages().getNextHousePricePlaceHolder().replaceAll("%NEXTHOUSEPRICE%",String.valueOf(Core.getInstance().getProperties().getFirstHousePrice()));
            int houseLevel = Core.getInstance().getPlayerManager().getHousePlayerMap().get(player).getHouseLevel();
            if(houseLevel == Core.getInstance().getProperties().getMaxHouseLevel()) return Core.getInstance().getMessages().getNextHousePricePlaceHolder();
            return Core.getInstance().getMessages().getNextHousePricePlaceHolder().replaceAll("%NEXTHOUSEPRICE%",String.valueOf(Math.calculateHousePrice(houseLevel,Core.getInstance().getProperties().getHousePriceAlgorithm())));

        }

        return null;
    }
}
