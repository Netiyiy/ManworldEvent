package com.kevin.event.Listeners;

import com.kevin.event.Event;
import com.kevin.event.EventMethods;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class EventTriggerListeners implements Listener {
    private final Event main;

    public EventTriggerListeners(Event main) {
        this.main = main;
    }

    // GAMEMODE CHANGING

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (main.isInEventWorld(p.getWorld())) {
            if (!Objects.equals(main.getConfig().getString("rejoin-world"), "none")) {
                World world = Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("rejoin-world")));
                assert world != null;
                p.teleport(world.getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onWorldLeave(PlayerChangedWorldEvent e) {
        if (main.isInEventWorld(e.getFrom())) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    // Will allow players to win if false
    boolean AllowWin = true;
    // If true, then continue to first fall
    boolean FirstFall = true;
    // If true, then continue to second fall
    boolean SecondFall = true;
    // If true, then continue to last fall
    boolean LastFall = true;

    @EventHandler
    public void onPlayerRespawn(PlayerDeathEvent e) {

        Player p = e.getEntity();
        if (main.isInEventWorld(p.getWorld())) {
            p.setGameMode(GameMode.SPECTATOR);
            if (AllowWin) {
                // ALIVE PLAYERS
                int AlivePlayers = 0;

                for (Player player : p.getWorld().getPlayers()) {
                    if (player.getGameMode().equals(GameMode.SURVIVAL)) AlivePlayers += 1;
                }

                // WINNER: SOMEBODY
                if (AlivePlayers == 1 && !main.getConfig().getBoolean("single-player")) {
                    for (Player player : p.getWorld().getPlayers()) {
                        if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                            Bukkit.broadcastMessage(EventMethods.getPrefix() + player.getName() + ChatColor.YELLOW + " has won the tournament!");
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 3f, 1f);
                            p.getWorld().getWorldBorder().setSize(500);
                            main.setting = false;
                            main.canRegister = false;
                            AllowWin = false;
                        }
                    }
                    // WINNER: NOBODY
                } else if (AlivePlayers == 0) {
                    Bukkit.broadcastMessage(EventMethods.getPrefix() + ChatColor.GOLD + "There is no one standing." + ChatColor.YELLOW + " Nobody won the tournament!");
                    p.getWorld().getWorldBorder().setSize(500);
                    main.setting = false;
                    main.canRegister = false;
                    AllowWin = false;
                }
            } else {
                if (Event.getInstance().returnLocation != null) {
                    p.teleport(Event.getInstance().returnLocation);
                    p.setGameMode(GameMode.SURVIVAL);
                }
            }
        }
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (main.isInEventWorld(p.getWorld()) && main.setting) {

                int AlivePlayers = 0;

                // ALIVE PLAYERS / ONLINE PLAYERS
                for (Player player : p.getWorld().getPlayers()) {
                    if (player.getGameMode().equals(GameMode.SURVIVAL)) AlivePlayers += 1;
                }

                // OUTSIDE BORDER
                p.getWorld().getWorldBorder().setDamageAmount(3);

                // When border is at 200
                if (p.getWorld().getWorldBorder().getSize() == 200) {
                    // Shrink To 50
                    if (AlivePlayers <= main.getConfig().getInt("continue-shrink-pt1")) {
                        p.getWorld().getWorldBorder().setSize(50, main.getConfig().getInt("border-time2"));
                        String PlayerTxT = String.valueOf(AlivePlayers);
                        Bukkit.broadcastMessage(EventMethods.getPrefix() + "You are one of the last " + PlayerTxT + " players, the border will continue to shrink, don't get caught outside!");
                        LastFall = true;
                        SecondFall = true;
                        FirstFall = true;
                        AllowWin = true;
                    }
                }

                // When border is at 50
                else if (p.getWorld().getWorldBorder().getSize() == 50 && FirstFall) {
                    // Shrink to 40
                    if (AlivePlayers <= main.getConfig().getInt("continue-shrink-pt2")) {

                        Bukkit.broadcastMessage(EventMethods.getPrefix() + "Have fun going down! ;)");
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "rollback rollbackregion " + main.getConfig().getString("block-remove-pt1");
                        Bukkit.dispatchCommand(console, command);

                        p.getWorld().getWorldBorder().setSize(40, 60);
                        String PlayerTxT = String.valueOf(AlivePlayers);
                        Bukkit.broadcastMessage(EventMethods.getPrefix() + PlayerTxT + " players left," + ChatColor.RED + " the border will continue to shrink again" + ChatColor.WHITE + ", don't get caught outside!");

                        FirstFall = false;

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 3f, 1f);
                        }
                    }
                    // When border is at 40
                } else if (p.getWorld().getWorldBorder().getSize() == 40 && SecondFall) {
                    // Shrink to 20
                    if (AlivePlayers <= main.getConfig().getInt("continue-shrink-pt3")) {
                        Bukkit.broadcastMessage(EventMethods.getPrefix() + "Hope the previous fall didn't scare you! Have fun on the second time ;)");
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "rollback rollbackregion " + main.getConfig().getString("block-remove-pt2");
                        Bukkit.dispatchCommand(console, command);

                        p.getWorld().getWorldBorder().setSize(20, 60);
                        String PlayerTxT = String.valueOf(AlivePlayers);
                        Bukkit.broadcastMessage(EventMethods.getPrefix() + PlayerTxT + " players left," + ChatColor.RED + " the border will continue to shrink yet again!");

                        SecondFall = false;

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 3f, 1f);
                        }
                    }
                }

                // If border is at 20
                else if (p.getWorld().getWorldBorder().getSize() == 20 && LastFall) {

                    Bukkit.broadcastMessage(EventMethods.getPrefix() + "Ow, that last fall looked painful for your feet, I enjoy your pain :)");
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String command = "rollback rollbackregion " + main.getConfig().getString("block-remove-pt3");
                    Bukkit.dispatchCommand(console, command);

                    LastFall = false;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 3f, 1f);
                    }
                }
            }
        }
    }
}


