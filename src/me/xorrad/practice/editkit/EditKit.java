package me.xorrad.practice.editkit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditKit implements Listener
{
    public static void openGUI(Player p) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, "§9Kit Management");
        ItemStack save1 = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta save1M = save1.getItemMeta();
        save1M.setDisplayName("§aSave Kit");
        save1.setItemMeta(save1M);
        ItemStack save2 = new ItemStack(Material.FLINT_AND_STEEL);
        ItemMeta save2M = save2.getItemMeta();
        save2M.setDisplayName("§cDelete Kit");
        save2.setItemMeta(save2M);
        ItemStack save3 = new ItemStack(Material.PAPER);
        ItemMeta save3M = save3.getItemMeta();
        save3M.setDisplayName("§eLoad Kit");
        save3.setItemMeta(save3M);
        inv.setItem(4, save1);
        p.openInventory(inv);;
    }
    
    public static void openChest1(Player p) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, "§9Chest 1");
        
        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 64);
        ItemStack porc = new ItemStack(Material.GRILLED_PORK, 64);
        ItemStack carrote = new ItemStack(Material.GOLDEN_CARROT, 64);
        ItemStack patate = new ItemStack(Material.BAKED_POTATO, 64);
        ItemStack ender = new ItemStack(Material.ENDER_PEARL, 16);
        inv.setItem(14, porc);
        inv.setItem(16, carrote);
        inv.setItem(9, ender);
        inv.setItem(15, steak);
        inv.setItem(17, patate);
            
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
        item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack item2 = new ItemStack(Material.DIAMOND_CHESTPLATE);
        item2.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        item2.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack item3 = new ItemStack(Material.DIAMOND_LEGGINGS);
        item3.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        item3.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack item4 = new ItemStack(Material.DIAMOND_BOOTS);
        item4.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        item4.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
        item4.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        ItemStack item5 = new ItemStack(Material.DIAMOND_SWORD);
        item5.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        item5.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        item5.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        inv.setItem(0, item5);
        inv.setItem(1, item);
        inv.setItem(2, item2);
        inv.setItem(3, item3);
        inv.setItem(4, item4);
        p.openInventory(inv);
    }
    
    public static void openChest2(Player p) {
        if (EditKitGui.editnodebuff.contains(p) || EditKitGui.editdebuff.contains(p)) {
            Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§9Chest 2");
            for (int i = 0; i < 54; ++i) {
                ItemStack item = new ItemStack(Material.POTION, 1, (short)16421);
                inv.setItem(i, item);
            }
            ItemStack speed = new ItemStack(Material.POTION, 1, (short)8226);
            ItemStack fireres = new ItemStack(Material.POTION, 1, (short)8259);
            ItemStack poison = new ItemStack(Material.POTION, 1, (short)16388);
            ItemStack lenteur = new ItemStack(Material.POTION, 1, (short)16426);
            if (EditKitGui.editdebuff.contains(p)) {
                inv.setItem(7, poison);
                inv.setItem(8, speed);
                inv.setItem(16, poison);
                inv.setItem(17, speed);
                inv.setItem(25, poison);
                inv.setItem(26, speed);
                inv.setItem(34, lenteur);
                inv.setItem(35, speed);
                inv.setItem(43, lenteur);
                inv.setItem(44, fireres);
                inv.setItem(52, lenteur);
                inv.setItem(53, speed);
            }
            else {
                inv.setItem(8, speed);
                inv.setItem(17, speed);
                inv.setItem(26, speed);
                inv.setItem(35, speed);
                inv.setItem(44, fireres);
                inv.setItem(53, speed);
            }
            p.openInventory(inv);
        }
    }
}

