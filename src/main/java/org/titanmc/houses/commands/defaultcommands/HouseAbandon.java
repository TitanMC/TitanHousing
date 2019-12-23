package org.titanmc.houses.commands.defaultcommands;


import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import com.github.intellectualsites.plotsquared.plot.util.SchematicHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.titanmc.houses.Core;
import org.titanmc.houses.commands.CommandExecutor;
import org.titanmc.houses.events.HouseUnclaimEvent;
import org.titanmc.houses.utils.Locale;
import org.titanmc.houses.utils.UnclaimReason;

import java.io.File;
import java.util.HashSet;
import java.util.UUID;

public class HouseAbandon extends CommandExecutor {

    /**
     * House Abandon Command
     */

    public HouseAbandon() {
        setCommand("trust");
        setPermission("house.player.abandon");
        setLength(1);
        setPlayer();
        setUsage("/house abandon");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        Locale locale = Core.getInstance().getMessages();

        //Check if they have any plots
        if (plotPlayer.getPlots().size() == 0) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseAbandonNoHome()));
            return;
        }

        //They have a plot so get it
        Plot plot = plotPlayer.getPlots().iterator().next();
        HouseUnclaimEvent houseUnclaimEvent = new HouseUnclaimEvent(p, plot, UnclaimReason.LEFT);
        Core.getInstance().getServer().getPluginManager().callEvent(houseUnclaimEvent);

        plot.deletePlot(null);

        if(!Core.getInstance().getProperties().getSchematicToPasteonExpiry().equalsIgnoreCase("none")){
            SchematicHandler.Schematic schematic = SchematicHandler.manager.getSchematic(new File(Core.getInstance().getDataFolder(),"schematics/"+Core.getInstance().getProperties().getSchematicToPasteonExpiry()));
            SchematicHandler.manager.paste(schematic, plot,Core.getInstance().getProperties().getMoveExpirySchematicXDirection(),Core.getInstance().getProperties().getMoveExpirySchematicYDirection(),Core.getInstance().getProperties().getMoveExpirySchematicZDirection(),true,null);
        }

        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p)){
            Core.getInstance().getPlayerManager().getHousePlayerMap().remove(p);
        }

        Core.getInstance().getDb().deleteRecord(p.getUniqueId().toString());

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseAbandonSuccess()));



    }

}