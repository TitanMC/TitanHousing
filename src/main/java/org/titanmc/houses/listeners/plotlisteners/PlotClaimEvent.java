package org.titanmc.houses.listeners.plotlisteners;


import com.github.intellectualsites.plotsquared.bukkit.events.PlayerClaimPlotEvent;
import com.github.intellectualsites.plotsquared.plot.util.SchematicHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.titanmc.houses.Core;
import org.titanmc.houses.events.HouseClaimEvent;
import org.titanmc.houses.manager.HousePlayer;

import java.io.File;

/**
 * Created by Jet on 06/02/2018.
 */
public class PlotClaimEvent implements Listener {

    @EventHandler
    public void onClaimEvent(PlayerClaimPlotEvent e){
        //Create, and trigger the HouseClaimCommand so others are able to have a say in what happens
        HouseClaimEvent houseClaimEvent = new HouseClaimEvent(e.getPlayer(),e.getPlot());
        Core.getInstance().getServer().getPluginManager().callEvent(houseClaimEvent);

        if(houseClaimEvent.isCancelled()) return;

        //May need to paste the schematic
        if(Core.getInstance().getProperties().isShouldPasteFirstHouseOnClaim()){
            SchematicHandler.Schematic schematic = SchematicHandler.manager.getSchematic(new File(Core.getInstance().getDataFolder(),"schematics/house1.schematic"));
            SchematicHandler.manager.paste(schematic, e.getPlot() ,Core.getInstance().getProperties().getMoveSchematicXDirection(),Core.getInstance().getProperties().getMoveSchematicYDirection(),Core.getInstance().getProperties().getMoveSchematicZDirection(),true,null);
        }

        //Getting the users UUID
        String uuid = e.getPlayer().getUniqueId().toString();
        long expiryMilliseconds = System.currentTimeMillis() + (Core.getInstance().getProperties().getGivenRentTime() * 86400000);
        //Save into DB
        Core.getInstance().getDb().setHouseValues(uuid,1,false,expiryMilliseconds);

        //Load into JVM
        HousePlayer housePlayer = new HousePlayer(e.getPlayer(),1,false,expiryMilliseconds,false);
        Core.getInstance().getPlayerManager().getHousePlayerMap().put(e.getPlayer(),housePlayer);


    }
}
