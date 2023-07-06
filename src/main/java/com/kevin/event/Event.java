package com.kevin.event;

import com.kevin.event.EntryCommands.JoinCommand;
import com.kevin.event.EntryCommands.LeaveCommand;
import com.kevin.event.Listeners.EventDenyListeners;
import com.kevin.event.Listeners.EventTriggerListeners;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Event extends JavaPlugin {

    public Location returnLocation = null;
    public static Event event;
    // EVENT MODE:
    // TRUE: on - FALSE: off
    public boolean setting = false;
    public boolean canRegister = false;

    @Override
    public void onEnable() {

        event = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new EventTriggerListeners(this), this);
        Bukkit.getPluginManager().registerEvents(new EventDenyListeners(this), this);
        Objects.requireNonNull(getCommand("event")).setExecutor(new EventCommands(this));
        Objects.requireNonNull(getCommand("join")).setExecutor(new JoinCommand(this));
        Objects.requireNonNull(getCommand("leave")).setExecutor(new LeaveCommand( this));

        if (getConfig().getBoolean("return-on-death")){
            String worldName = getConfig().getString("return-location.world");
            if (worldName != null) {
                World world = Bukkit.getWorld(worldName);
                int x = getConfig().getInt("return-location.x");
                int y = getConfig().getInt("return-location.y");
                int z = getConfig().getInt("return-location.z");
                if (world != null) {
                    returnLocation = new Location(world, x, y, z);
                }
            }
        }
    }

    public static Event getInstance() {
        return event;
    }

    public boolean isInEventWorld(World world) {
        return world.getName().equals(getConfig().get("event-world"));
    }

    public World getEventWorld() {
        return Bukkit.getWorld(Objects.requireNonNull(getConfig().getString("event-world")));
    }

    public void teleportPlayer(Player player) {
        if (player != null) {
            World world = Bukkit.getWorld(Objects.requireNonNull(getConfig().getString("event-world")));
            int x = getConfig().getInt("spawn-X");
            int y = getConfig().getInt("spawn-Y");
            int z = getConfig().getInt("spawn-Z");
            Location loc = new Location(world, x, y, z);
            player.teleport(loc);
            player.setGameMode(GameMode.SURVIVAL);
            EventMethods.gearPlayer(player);
        }
    }

}