package com.kevin.event;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class EventCommands implements CommandExecutor {

    private final Event main;

    public EventCommands(Event main) {
        this.main = main;
    }

    boolean StopCountdown = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("event.all")) {
            if (args.length == 1) {
                if (Objects.equals(args[0], "start")) {

                    if (main.setting) {
                        sender.sendMessage(EventMethods.getPrefix() + ChatColor.RED + "The event has already been started!");
                        return false;
                    }
                    else if (main.canRegister) {
                        sender.sendMessage(EventMethods.getPrefix() + ChatColor.RED + "The registration has already been opened. " + ChatColor.WHITE + "Run /begin to start!");
                        return false;
                    }
                    sender.sendMessage(EventMethods.getPrefix() + ChatColor.GREEN + "Registration has already been opened!");
                    main.canRegister = true;

                    if (sender instanceof Player) {
                        main.teleportPlayer((Player) sender);
                    }

                } else if (Objects.equals(args[0], "begin")) {

                    int amountOfPlayers = main.getEventWorld().getPlayers().size();
                    if (amountOfPlayers < 3) {
                        sender.sendMessage(EventMethods.getPrefix() + ChatColor.RED + "Not enough players to begin! " + amountOfPlayers + "/3");
                        return false;
                    }

                    if (main.setting) {
                        sender.sendMessage(EventMethods.getPrefix() + ChatColor.RED + "The event has already been started!");
                        return false;
                    }

                    if (sender instanceof Player) {
                        eventBegin((Player) sender);
                    }

                } else if (Objects.equals(args[0], "stop")) {

                    if (!main.setting) {
                        sender.sendMessage(EventMethods.getPrefix() + ChatColor.RED + "The event is not running!");
                        return false;
                    }

                    if (sender instanceof Player) {
                        sender.sendMessage(EventMethods.getPrefix() + ChatColor.RED + "Event Stopped");
                        eventStop();
                    }

                } else if (Objects.equals(args[0], "help")) {
                    sender.sendMessage(ChatColor.GREEN + "/" + ChatColor.WHITE + "event: start, begin, stop, regions");
                } else if (Objects.equals(args[0], "regions")) {
                    sender.sendMessage(ChatColor.GREEN + "Preset Regions");
                    sender.sendMessage(ChatColor.GREEN + "EventHole1: " + ChatColor.WHITE + "[-25, 64, -25] [25, 40, 25]");
                    sender.sendMessage(ChatColor.GREEN + "EventHole2: " + ChatColor.WHITE + "[-20, 40, -20] [20, 0, 20]");
                    sender.sendMessage(ChatColor.GREEN + "EventHole3: " + ChatColor.WHITE + "[-10, 0, -10] [10, -62 , 10]");
                }
            }
        }
        return false;
    }


    public void eventBegin(Player sender) {
        if (main.getConfig().getString("event-world") != null && main.getConfig().getString("rejoin-world") != null) {
            resetMap();
            main.canRegister = true;
            StopCountdown = false;

            Bukkit.broadcastMessage(EventMethods.getPrefix() + ChatColor.GOLD + main.getConfig().getInt("countdown-timer") + ChatColor.YELLOW + " minutes till the event begins, set up your inventories!");

            // Countdown Minutes
            new BukkitRunnable() {
                int timer = main.getConfig().getInt("countdown-timer");

                @Override
                public void run() {
                    if (StopCountdown) {
                        StopCountdown = false;
                        cancel();
                        return;
                    }
                    timer = timer - 1;
                    if (timer <= 0) {
                        cancel();
                        // Countdown Seconds
                        new BukkitRunnable() {
                            int timer = 3;

                            @Override
                            public void run() {
                                if (StopCountdown) {
                                    StopCountdown = false;
                                    cancel();
                                    return;
                                }
                                if (timer <= 0) {
                                    // START EVENT
                                    main.setting = true;
                                    int time = main.getConfig().getInt("border-time1");
                                    World world = main.getEventWorld();
                                    if (world != null) {
                                        EventMethods.startBorder(world, time);
                                    }
                                    cancel();
                                } else {
                                    if (timer == 3) {
                                        Bukkit.broadcastMessage(EventMethods.getPrefix() +  ChatColor.GREEN + "3");
                                    } else if (timer == 2) {
                                        Bukkit.broadcastMessage(EventMethods.getPrefix() +  ChatColor.YELLOW + "2");
                                    } else if (timer == 1) {
                                        Bukkit.broadcastMessage(EventMethods.getPrefix() +  ChatColor.RED + "1");
                                    }
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.playSound(p, Sound.UI_BUTTON_CLICK, 1f, 1f);
                                    }
                                    timer = timer - 1;
                                }
                            }

                        }.runTaskTimer(main, 0, 20);
                    } else {
                        String TimerText = String.valueOf(timer);
                        Bukkit.broadcastMessage(EventMethods.getPrefix() +  ChatColor.GOLD + TimerText + ChatColor.YELLOW + " Minutes till the event begins...");
                    }
                }
            }.runTaskTimer(main, 1200, 1200);
        } else {
            sender.sendMessage("event-world or rejoin-world produced a null! Check your config.yml :(");
        }
    }

    public void eventStop() {
        StopCountdown = true;
        main.canRegister = false;
        main.setting = false;
        World world = Bukkit.getWorld(Objects.requireNonNull(main.getConfig().getString("event-world")));
        if (world != null) {
            world.getWorldBorder().setSize(500);
            resetMap();
            for (Player player : world.getPlayers()) {

                player.setGameMode(GameMode.SURVIVAL);

                ItemStack AIR = new ItemStack(Material.AIR);
                player.getInventory().setItem(EquipmentSlot.OFF_HAND, AIR);
                player.getInventory().setItem(EquipmentSlot.HEAD, AIR);
                player.getInventory().setItem(EquipmentSlot.CHEST, AIR);
                player.getInventory().setItem(EquipmentSlot.LEGS, AIR);
                player.getInventory().setItem(EquipmentSlot.FEET, AIR);

                for (int i = 0; i <= 35; i++) {
                    player.getInventory().setItem(i, AIR);
                }

                player.teleport(player.getWorld().getSpawnLocation());
            }
        }
    }

    public void resetMap() {
        String BlockRespawn = main.getConfig().getString("block-respawn");
        if (!(Objects.equals(BlockRespawn, "none"))) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String cmd = "rollback rollbackregion " + BlockRespawn;
            Bukkit.dispatchCommand(console, cmd);
        }
    }

}
