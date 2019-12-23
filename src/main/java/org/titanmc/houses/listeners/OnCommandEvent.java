package org.titanmc.houses.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.titanmc.houses.Core;

import java.io.File;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Jet on 27/01/2018.
 */
public class OnCommandEvent implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(e.getPlayer().hasPermission("house.admin.bypasscommands")) return;
        String message[] = e.getMessage().split(" ");
        String firstCommand;
        if (message.length > 0) {
            firstCommand = message[0];
        } else {
            firstCommand = e.getMessage();
        }

        for (String s : Core.getInstance().getProperties().getCommandsToBlock()) {
            if (firstCommand.equalsIgnoreCase(s)) {

                e.setCancelled(true);
                e.getPlayer().sendMessage(Core.getInstance().getMessages().getNonExistentPlotCommand());
                return;
            }

        }
    }
}
