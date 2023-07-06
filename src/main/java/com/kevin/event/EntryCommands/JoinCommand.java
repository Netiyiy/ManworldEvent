package com.kevin.event.EntryCommands;

import com.kevin.event.Event;
import com.kevin.event.EventMethods;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class JoinCommand implements CommandExecutor {

    private final Event main;

    public JoinCommand(Event main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
                if (!main.canRegister) {
                    sender.sendMessage(EventMethods.getPrefix() + "There is no ongoing event!");
                    return false;
                }
                if (!main.isInEventWorld(p.getWorld())) {
                    World world = Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("event-world")));
                    int x = main.getConfig().getInt("spawn-X");
                    int y = main.getConfig().getInt("spawn-Y");
                    int z = main.getConfig().getInt("spawn-Z");
                    Location loc = new Location(world, x, y, z);
                    p.teleport(loc);
                    if (!main.setting) {
                        EventMethods.gearPlayer(p);
                        sender.sendMessage(EventMethods.getPrefix() + "Have fun! :)");
                    } else {
                        p.setGameMode(GameMode.SPECTATOR);
                        sender.sendMessage(EventMethods.getPrefix() + "The event has already started, you have been put in spectator mode. " + ChatColor.RED + "/leave" + ChatColor.WHITE + " to leave this world.");
                    }
                }
            }
        return false;
        }
    }
