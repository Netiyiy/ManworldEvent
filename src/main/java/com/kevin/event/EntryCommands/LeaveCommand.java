package com.kevin.event.EntryCommands;

import com.kevin.event.Event;
import com.kevin.event.EventMethods;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LeaveCommand implements CommandExecutor {

    private final Event main;

    public LeaveCommand(Event main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (main.isInEventWorld(p.getWorld())) {
                if (!Objects.equals(main.getConfig().getString("rejoin-world"), "none")) {
                    World world = Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("rejoin-world")));
                    assert world != null;
                    p.teleport(world.getSpawnLocation());
                    sender.sendMessage(EventMethods.getPrefix() + "Aw bye.. :(");
                } else {
                    sender.sendMessage(EventMethods.getPrefix() + "There is no set return world...");
                }
            }
        }
        return false;
    }
}


