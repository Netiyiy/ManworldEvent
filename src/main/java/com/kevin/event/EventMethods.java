package com.kevin.event;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventMethods {

    public static String getPrefix() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.GREEN +"Event" + ChatColor.DARK_GRAY + "]" + ChatColor.WHITE + " ";
    }

    public static void startBorder(World world, int seconds) {
        world.getWorldBorder().setSize(500);
        Bukkit.broadcastMessage(getPrefix() + ChatColor.WHITE + " Countdown has ended, " + ChatColor.GREEN + "may the tournament begin! " + ChatColor.WHITE + "Good luck and have fun!");
        world.getWorldBorder().setSize(200, seconds);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
        }
    }

    public static void addEnchant(ItemStack itemStack, Enchantment enchant, int amount) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.addEnchant(enchant, amount, true);
        itemStack.setItemMeta(meta);
    }

    public static void gearPlayer(Player p) {

        ItemStack Sword = new ItemStack(Material.NETHERITE_SWORD);
        addEnchant(Sword, Enchantment.DAMAGE_ALL, 5);
        addEnchant(Sword, Enchantment.DURABILITY, 3);
        addEnchant(Sword, Enchantment.KNOCKBACK, 1);
        addEnchant(Sword, Enchantment.MENDING, 1);
        addEnchant(Sword, Enchantment.LOOT_BONUS_MOBS, 3);

        ItemStack Pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        addEnchant(Pickaxe, Enchantment.DIG_SPEED, 5);
        addEnchant(Pickaxe, Enchantment.DURABILITY, 3);
        addEnchant(Pickaxe, Enchantment.MENDING, 1);

        ItemStack Helmet = new ItemStack(Material.NETHERITE_HELMET);
        addEnchant(Helmet, Enchantment.DURABILITY, 3);
        addEnchant(Helmet, Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        addEnchant(Helmet, Enchantment.MENDING, 1);
        addEnchant(Helmet, Enchantment.OXYGEN, 3);
        addEnchant(Helmet, Enchantment.WATER_WORKER, 1);

        ItemStack Chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        addEnchant(Chestplate, Enchantment.DURABILITY, 3);
        addEnchant(Chestplate, Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        addEnchant(Chestplate, Enchantment.MENDING, 1);

        ItemStack Leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        addEnchant(Leggings, Enchantment.DURABILITY, 3);
        addEnchant(Leggings, Enchantment.PROTECTION_EXPLOSIONS, 4);
        addEnchant(Leggings, Enchantment.MENDING, 1);

        ItemStack Boots = new ItemStack(Material.NETHERITE_BOOTS);
        addEnchant(Boots, Enchantment.DURABILITY, 3);
        addEnchant(Boots, Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        addEnchant(Boots, Enchantment.MENDING, 1);
        addEnchant(Boots, Enchantment.PROTECTION_FALL, 4);

        ItemStack Anchors = new ItemStack(Material.RESPAWN_ANCHOR, 64);
        ItemStack Glowstone = new ItemStack(Material.GLOWSTONE, 64);
        ItemStack Crystals = new ItemStack(Material.END_CRYSTAL, 64);
        ItemStack Obi = new ItemStack(Material.OBSIDIAN, 64);
        ItemStack Gapples = new ItemStack(Material.GOLDEN_APPLE, 64);
        ItemStack Pearls = new ItemStack(Material.ENDER_PEARL, 16);
        ItemStack Totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);


        p.getInventory().setItem(0, Sword);
        p.getInventory().setItem(1, Anchors);
        p.getInventory().setItem(2, Glowstone);
        p.getInventory().setItem(3, Pearls);
        p.getInventory().setItem(4, Totem);
        p.getInventory().setItem(5, Obi);
        p.getInventory().setItem(6, Crystals);
        p.getInventory().setItem(7, Gapples);
        p.getInventory().setItem(8, Pickaxe);

        p.getInventory().setItem(9, Totem);
        p.getInventory().setItem(10, Totem);
        p.getInventory().setItem(11, Totem);
        p.getInventory().setItem(12, Totem);
        p.getInventory().setItem(13, Totem);
        p.getInventory().setItem(14, Totem);
        p.getInventory().setItem(15, Totem);
        p.getInventory().setItem(16, Totem);
        p.getInventory().setItem(17, Totem);

        p.getInventory().setItem(18, Totem);
        p.getInventory().setItem(19, Totem);
        p.getInventory().setItem(20, Totem);
        p.getInventory().setItem(21, Totem);
        p.getInventory().setItem(22, Totem);
        p.getInventory().setItem(23, Obi);
        p.getInventory().setItem(24, Crystals);
        p.getInventory().setItem(25, Totem);
        p.getInventory().setItem(26, Totem);

        p.getInventory().setItem(27, Pearls);
        p.getInventory().setItem(28, Pearls);
        p.getInventory().setItem(29, Pearls);
        p.getInventory().setItem(30, Pearls);
        p.getInventory().setItem(31, Totem);
        p.getInventory().setItem(32, Obi);
        p.getInventory().setItem(33, Crystals);
        p.getInventory().setItem(34, Totem);
        p.getInventory().setItem(35, Totem);

        p.getInventory().setItem(EquipmentSlot.OFF_HAND, Totem);
        p.getInventory().setItem(EquipmentSlot.HEAD, Helmet);
        p.getInventory().setItem(EquipmentSlot.CHEST, Chestplate);
        p.getInventory().setItem(EquipmentSlot.LEGS, Leggings);
        p.getInventory().setItem(EquipmentSlot.FEET, Boots);

        p.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
    }
}
