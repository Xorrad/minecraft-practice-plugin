package me.xorrad.practice.editkit;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.xorrad.practice.Practice;
import me.xorrad.practice.utils.SpeedItem;
import me.xorrad.practice.utils.kits.Kits;

public class EditKitGui implements Listener
{
    public static ArrayList<Player> editnodebuff;
    public static ArrayList<Player> editdebuff;
    public static ArrayList<Player> editbow;
    public static ArrayList<Player> editgapple;
    public static ArrayList<Player> editaxe;
    public static ArrayList<Player> editsoup;
    public static ArrayList<Player> edituhc;
    
    static {
        EditKitGui.editnodebuff = new ArrayList<Player>();
        EditKitGui.editdebuff = new ArrayList<Player>();
        EditKitGui.editbow = new ArrayList<Player>();
        EditKitGui.editgapple = new ArrayList<Player>();
        EditKitGui.editaxe = new ArrayList<Player>();
        EditKitGui.editsoup = new ArrayList<Player>();
        EditKitGui.edituhc = new ArrayList<Player>();
    }
    
    public static void stuffGui(Player p) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, "§9Select Kit");
        ItemStack item = new ItemStack(Material.POTION, 1, (short)16421);
        ItemMeta itemm = item.getItemMeta();
        itemm.setDisplayName("§cNoDebuff");
        item.setItemMeta(itemm);
        ItemStack item2 = new ItemStack(Material.POTION, 1, (short)16420);
        ItemMeta itemm2 = item2.getItemMeta();
        itemm2.setDisplayName("§2Debuff");
        item2.setItemMeta(itemm2);
        
        ItemStack item4 = new ItemStack(Material.BOW);
        ItemMeta itemm4 = item4.getItemMeta();
        itemm4.setDisplayName("§6Bow");
        item4.setItemMeta(itemm4);
        
        SpeedItem gapple = new SpeedItem(Material.GOLDEN_APPLE, "§6Gapple");
		gapple.setDurability((short)1);
		
		SpeedItem axe = new SpeedItem(Material.IRON_AXE, "§7Axe");
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		
		SpeedItem soup = new SpeedItem(Material.MUSHROOM_SOUP, "§dSoup");
		
		SpeedItem uhc = new SpeedItem(Material.LAVA_BUCKET, "§4BuildUHC");
        
        inv.setItem(0, item);
        inv.setItem(1, item2);
        inv.setItem(2, item4);
        inv.setItem(3, gapple);
        inv.setItem(4, axe);
        inv.setItem(5, soup);
        inv.setItem(6, uhc);
        p.openInventory(inv);
    }
    
    @EventHandler
    public void onKitsClick(InventoryClickEvent e) {
        if (e.getInventory().getName().equalsIgnoreCase("§9Select Kit")) {
            e.setCancelled(true);
            if (e.getWhoClicked() instanceof Player) {
                Player p = (Player)e.getWhoClicked();
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cNoDebuff")) {
                        p.closeInventory();
                        p.getInventory().clear();
                        File file = new File(Practice.getInstance().getDataFolder(), "editkit.yml");
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(c.getString("world"));
                        double x = c.getDouble("x");
                        double y = c.getDouble("y");
                        double z = c.getDouble("z");
                        float yaw = (float)c.getDouble("yaw");
                        float pitch = (float)c.getDouble("pitch");
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        EditKitGui.editnodebuff.add(p);
                        Practice.hideAll(p);
                        Kits.giveNoDebuffKit(p);
                        p.sendMessage("§eYou are editing §aNoDebuff");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§2Debuff")) {
                        p.closeInventory();
                        p.getInventory().clear();
                        File file = new File(Practice.getInstance().getDataFolder(), "editkit.yml");
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(c.getString("world"));
                        double x = c.getDouble("x");
                        double y = c.getDouble("y");
                        double z = c.getDouble("z");
                        float yaw = (float)c.getDouble("yaw");
                        float pitch = (float)c.getDouble("pitch");
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        EditKitGui.editdebuff.add(p);
                        Practice.hideAll(p);
                        Kits.giveDebuffKit(p);
                        p.sendMessage("§eYou are editing §aDebuff");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Bow")) {
                        p.closeInventory();
                        p.getInventory().clear();
                        File file = new File(Practice.getInstance().getDataFolder(), "editkit.yml");
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(c.getString("world"));
                        double x = c.getDouble("x");
                        double y = c.getDouble("y");
                        double z = c.getDouble("z");
                        float yaw = (float)c.getDouble("yaw");
                        float pitch = (float)c.getDouble("pitch");
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        EditKitGui.editbow.add(p);
                        Practice.hideAll(p);
                        Kits.giveBowKit(p);
                        p.sendMessage("§eYou are editing §aBow");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§6Gapple")) {
                        p.closeInventory();
                        p.getInventory().clear();
                        File file = new File(Practice.getInstance().getDataFolder(), "editkit.yml");
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(c.getString("world"));
                        double x = c.getDouble("x");
                        double y = c.getDouble("y");
                        double z = c.getDouble("z");
                        float yaw = (float)c.getDouble("yaw");
                        float pitch = (float)c.getDouble("pitch");
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        EditKitGui.editgapple.add(p);
                        Practice.hideAll(p);
                        Kits.giveGappleKit(p);
                        p.sendMessage("§eYou are editing §aGapple");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7Axe")) {
                        p.closeInventory();
                        p.getInventory().clear();
                        File file = new File(Practice.getInstance().getDataFolder(), "editkit.yml");
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(c.getString("world"));
                        double x = c.getDouble("x");
                        double y = c.getDouble("y");
                        double z = c.getDouble("z");
                        float yaw = (float)c.getDouble("yaw");
                        float pitch = (float)c.getDouble("pitch");
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        EditKitGui.editaxe.add(p);
                        Practice.hideAll(p);
                        Kits.giveAxeKit(p);
                        p.sendMessage("§eYou are editing §aAxe");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§dSoup")) {
                        p.closeInventory();
                        p.getInventory().clear();
                        File file = new File(Practice.getInstance().getDataFolder(), "editkit.yml");
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(c.getString("world"));
                        double x = c.getDouble("x");
                        double y = c.getDouble("y");
                        double z = c.getDouble("z");
                        float yaw = (float)c.getDouble("yaw");
                        float pitch = (float)c.getDouble("pitch");
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        EditKitGui.editsoup.add(p);
                        Practice.hideAll(p);
                        Kits.giveSoupKit(p);
                        p.sendMessage("§eYou are editing §aSoup");
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4BuildUHC")) {
                        p.closeInventory();
                        p.getInventory().clear();
                        File file = new File(Practice.getInstance().getDataFolder(), "editkit.yml");
                        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                        World world = Bukkit.getWorld(c.getString("world"));
                        double x = c.getDouble("x");
                        double y = c.getDouble("y");
                        double z = c.getDouble("z");
                        float yaw = (float)c.getDouble("yaw");
                        float pitch = (float)c.getDouble("pitch");
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        EditKitGui.edituhc.add(p);
                        Practice.hideAll(p);
                        Kits.giveBuildUHCKit(p);
                        p.sendMessage("§eYou are editing §4BuildUHC");
                    }
                }
            }
        }
    }
}

