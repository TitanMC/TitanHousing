package org.titanmc.houses.listeners.plotlisteners;

import com.github.intellectualsites.plotsquared.bukkit.events.PlayerEnterPlotEvent;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.titanmc.houses.Core;
import org.titanmc.houses.events.HouseEnterEvent;
import org.titanmc.houses.manager.HousePlayer;
import org.titanmc.houses.utils.Locale;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Created by Jet on 28/01/2018.
 */
public class PlotEnterEvent implements Listener{

    @EventHandler
    public void onPlayerPlotEnter(PlayerEnterPlotEvent e) {

        //Create, and trigger the HouseEnterEvent so others are able to have a say in what happens
        HouseEnterEvent houseEnterEvent = new HouseEnterEvent(e.getPlayer(),e.getPlot());
        Core.getInstance().getServer().getPluginManager().callEvent(houseEnterEvent);

        if(e.getPlayer().hasPermission("house.admin.bypassentry")){
            return;
        }

        Set<UUID> plotOwner = e.getPlot().getOwners();
        if(plotOwner.size() == 0){
            return;
        }
        Player p = e.getPlayer();
        //Load the plot owners UUID into varible
        String plotOwnersUUID = plotOwner.iterator().next().toString();

        /**
         * Check to see if plot is being upgraded
         */
        //Check to see if upgrading
        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p) && Core.getInstance().getPlayerManager().getHousePlayerMap().get(e.getPlayer()).getIsHouseBeingUpgraded()){
            blockPlayerFromEnteringPlot(e.getPlot(),p);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHouseUpgrading()));
        }
        /**
         * Check to see person entering plot is plot owner, if so return
         */

        if(p.getUniqueId().toString().equalsIgnoreCase(plotOwnersUUID)) return;

        /**
         * Check to see if person entering plot is trusted, if so return
         */
        for(UUID trustedUUIDs : e.getPlot().getTrusted()){
            if(trustedUUIDs.equals(p.getUniqueId())) return;
        }
        /**
         * Check to see if user is online
         */
        Player plotPlayer = null;
        for(HousePlayer housePlayer : Core.getInstance().getPlayerManager().getHousePlayerMap().values()){
            if(housePlayer.getPlayer().getUniqueId().toString().equalsIgnoreCase(plotOwnersUUID)){
                plotPlayer = housePlayer.getPlayer();
                break;
            }
        }
        if(plotPlayer != null){
            //Check to see if locked
            if(Core.getInstance().getPlayerManager().getHousePlayerMap().get(plotPlayer).getIsHouseLocked()) {

                //House is locked, add to block list then return
                blockPlayerFromEnteringPlot(e.getPlot(), p);
            }

            //House is not locked, return
            return;
        }

        //Returns a future to whether the house is locked or not
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> task = new Callable<Boolean>() {
            public Boolean call() throws Exception {

                return Core.getInstance().getDb().isHouseLocked(plotOwnersUUID);
            }
        };
        //Execute the future
        Future<Boolean> future = executorService.submit(task);

        //Async task to wait for Future to return
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                int counter = 1;
                while (!future.isDone() && counter != 20) {
                    counter++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                //An error has occurred pretty much if this happens
                if(counter == 20){

                    future.cancel(true);
                    return;
                }
                //Must be done
                try {
                    boolean houseLocked = future.get();
                    //If house is not locked, return, players are allowed to enter
                    if(!houseLocked){

                        return;
                    }
                    //House is locked, kick players out - This must be Synchronised
                    //When accessing Bukkit, needs to be within Synchronized

                    //Could boost out plot - get pos one wait get pos 2, work out diff, reverse
                    Bukkit.getScheduler().runTask(Core.getInstance(),new Runnable(){
                        @Override
                        public void run() {

                            blockPlayerFromEnteringPlot(e.getPlot(),p);
                        }
                    });

                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();

                }

            }
        });

        //Get the plot owner, see if online, pull from database whether private is on/off

        //Will have to deny people I think (add them to plot deny list)
    }

    public void blockPlayerFromEnteringPlot(Plot plot, Player p){

        //e.getPlot().addDenied(p.getUniqueId());
        Location plotLoc = plot.getCenter();
        org.bukkit.Location plotMiddle = new org.bukkit.Location(Bukkit.getWorld(plotLoc.getWorld()),plotLoc.getX(),plotLoc.getY(),plotLoc.getZ());
        if(p.isFlying()){
            //Needs a slightly bigger booster if they are flying
            p.setVelocity(p.getLocation().toVector().subtract(plotMiddle.toVector()).divide(new Vector(10,0,10)).setY(0.5));
            if(Core.serverVersion.startsWith("v1_12")) {
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
            }
        }else{
            p.setVelocity(p.getLocation().toVector().subtract(plotMiddle.toVector()).divide(new Vector(15,0,15)).setY(0.5));
            if(Core.serverVersion.startsWith("v1_12")) {
                p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
            }
        }

        //Title
        Locale locale = Core.getInstance().getMessages();
        if(Core.serverVersion.startsWith("v1_12")) {
            p.sendTitle(locale.getDeniedTitle().equalsIgnoreCase("none") ? "": locale.getDeniedTitle(),locale.getDeniedSubTitle().equalsIgnoreCase("none") ? "": locale.getDeniedSubTitle(),30,60,10);
        }else{
            p.sendTitle(locale.getDeniedTitle().equalsIgnoreCase("none") ? "": locale.getDeniedTitle(),locale.getDeniedSubTitle().equalsIgnoreCase("none") ? "": locale.getDeniedSubTitle());
        }

        ensurePlayerIsKickedOutOfPlot(plot,p);
    }

    public void ensurePlayerIsKickedOutOfPlot(Plot plot, Player p/*, org.bukkit.Location middle*/){
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(plot.getPlayersInPlot().contains(PlotPlayer.get(p.getName()))){
                    p.teleport(new org.bukkit.Location(Bukkit.getWorld(Core.getInstance().getProperties().getPlotsWorldName()),plot.getDefaultHome().getX(),plot.getDefaultHome().getY(),plot.getDefaultHome().getZ(),plot.getDefaultHome().getYaw(),plot.getDefaultHome().getPitch()));
                }
            }
        },10L);
    }
}
