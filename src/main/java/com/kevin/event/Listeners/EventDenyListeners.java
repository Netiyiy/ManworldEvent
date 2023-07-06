package com.kevin.event.Listeners;

import com.kevin.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public class EventDenyListeners implements Listener {

    private final Event main;

    public EventDenyListeners(Event main) {
        this.main = main;
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (main.isInEventWorld(e.getEntity().getWorld())) {
            if (!main.setting) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(BlockPlaceEvent e) {
        if (main.isInEventWorld(e.getPlayer().getWorld())) {
            if (!main.setting) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            if (!e.getPlayer().isOp()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(BlockBreakEvent e) {
        if (main.isInEventWorld(e.getPlayer().getWorld())) {
            if (!main.setting) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (main.isInEventWorld(e.getEntity().getWorld())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntitySpawn(BlockDropItemEvent e) {
        if (main.isInEventWorld(e.getBlock().getWorld())) {
            if (main.setting) {
                e.setCancelled(true);
            }
        }
    }
}
