package me.xorrad.practice.editkit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.xorrad.practice.Practice;
import me.xorrad.practice.cmd.EditKitCmd;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.kits.SpawnItem;


public class onEditKitInteract implements Listener
{
    Practice pl;
    
    public onEditKitInteract() {
        this.pl = Practice.getInstance();
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType().equals(Material.ANVIL)) {
            	if(User.getPlayer(p).getFightId() != null) {
                	event.setCancelled(true);
                	return;
                }
            	
                EditKit.openGUI(p);
                event.setCancelled(true);
            }
            else {
                if (event.getClickedBlock().getType().equals(Material.WORKBENCH) || event.getClickedBlock().getType().equals(Material.FURNACE) || event.getClickedBlock().getType().equals(Material.BURNING_FURNACE)) {
                	event.setCancelled(true);
                    return;
                }
                if(User.getPlayer(p).getFightId() != null) {
                	return;
                }
                File file = new File(this.pl.getDataFolder(), "editkit.yml");
                YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                //String world = c.getString("chest.world");
                World world2 = Bukkit.getWorld(c.getString("world"));
                double x1 = c.getDouble("chest1.x");
                double y1 = c.getDouble("chest1.y");
                double z1 = c.getDouble("chest1.z");
                //String worldd = c.getString("chest.world");
                World world3 = Bukkit.getWorld(c.getString("world"));
                double x2 = c.getDouble("chest2.x");
                double y2 = c.getDouble("chest2.y");
                double z2 = c.getDouble("chest2.z");
                Location loc = new Location(world2, x1, y1, z1);
                Location loc2 = new Location(world3, x2, y2, z2);
                if (event.getClickedBlock().getLocation().equals((Object)loc)) {
                	if(EditKitGui.editbow.contains(p) || EditKitGui.editgapple.contains(p) || EditKitGui.editaxe.contains(p) || EditKitGui.editsoup.contains(p) || EditKitGui.edituhc.contains(p)) {
                		event.setCancelled(true);
                		return;
                	}
                    EditKit.openChest1(p);
                    event.setCancelled(true);
                }
                else {
                    if (!event.getClickedBlock().getLocation().equals((Object)loc2)) {
                        return;
                    }
                    if(EditKitGui.editbow.contains(p) || EditKitGui.editgapple.contains(p) || EditKitGui.editaxe.contains(p) || EditKitGui.editsoup.contains(p) || EditKitGui.edituhc.contains(p)) {
                		event.setCancelled(true);
                		return;
                	}
                    EditKit.openChest2(p);
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onInteraact(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign) {
            Player p = e.getPlayer();
            Sign sign = (Sign)e.getClickedBlock().getState();
            String[] l = sign.getLines();
            if (l[2].equalsIgnoreCase("spawn")) {
                if (EditKitGui.editdebuff.contains(p)) {
                    EditKitGui.editdebuff.remove(p);
                }
                if (EditKitGui.editnodebuff.contains(p)) {
                    EditKitGui.editnodebuff.remove(p);;
                }
                if (EditKitGui.editbow.contains(p)) {
                    EditKitGui.editbow.remove(p);;
                }
                if (EditKitGui.editgapple.contains(p)) {
                    EditKitGui.editgapple.remove(p);;
                }
                if (EditKitGui.editaxe.contains(p)) {
                    EditKitGui.editaxe.remove(p);;
                }
                if (EditKitGui.editsoup.contains(p)) {
                    EditKitGui.editsoup.remove(p);;
                }
                e.setCancelled(true);
                p.getInventory().clear();
                for (PotionEffect effect : p.getActivePotionEffects())
                    p.removePotionEffect(effect.getType());
                File file = new File(Practice.getInstance().getDataFolder(), "spawn.yml");
                YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                World world = Bukkit.getWorld(c.getString("world"));
                double x = c.getDouble("x");
                double y = c.getDouble("y");
                double z = c.getDouble("z");
                p.teleport(new Location(world, x, y, z, -90.0f, 0.0f));
                SpawnItem.giveSpawnItem(p);
                Practice.showAll(p);
            }
        }
    }
 
    
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        if (EditKitCmd.chest1.contains(p)) {
            if (event.getBlock().getType().equals(Material.CHEST)) {
                File file = new File(this.pl.getDataFolder(), "editkit.yml");
                YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
                double x = event.getBlock().getLocation().getX();
                double y = event.getBlock().getLocation().getY();
                double z = event.getBlock().getLocation().getZ();
                c.set("chest1.x", (Object)x);
                c.set("chest1.y", (Object)y);
                c.set("chest1.z", (Object)z);
                try {
                    p.sendMessage("§aThe chest1 location has ben set!");
                    EditKitCmd.chest1.remove(p);
                    c.save(file);
                }
                catch (IOException e) {
                    p.sendMessage("§cError while saving the Location!");
                    e.printStackTrace();
                }
            }
        }
        else if (EditKitCmd.chest2.contains(p) && event.getBlock().getType().equals(Material.CHEST)) {
            File file = new File(this.pl.getDataFolder(), "editkit.yml");
            YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
            double x = event.getBlock().getLocation().getX();
            double y = event.getBlock().getLocation().getY();
            double z = event.getBlock().getLocation().getZ();
            c.set("chest2.x", (Object)x);
            c.set("chest2.y", (Object)y);
            c.set("chest2.z", (Object)z);
            try {
                p.sendMessage("§aThe chest2 location has ben set!");
                EditKitCmd.chest2.remove(p);
                c.save(file);
            }
            catch (IOException e) {
                p.sendMessage("§cError while saving the Location!");
                e.printStackTrace();
            }
        }
    }
    
	public static void getKit(String kit, Player p) throws IOException {
        YamlConfiguration c = YamlConfiguration.loadConfiguration(new File(Practice.getInstance().getDataFolder() + "\\kit\\", p.getUniqueId().toString() + kit + ".yml"));
        ItemStack[] content = (ItemStack[]) ((List<?>)c.get("inventory.armor")).toArray(new ItemStack[0]);
        p.getInventory().setArmorContents(content);
        content = (ItemStack[]) ((List<?>)c.get("inventory.content")).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
    }
    
    @EventHandler
    public void onInterdaact(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign) {
            Player p = e.getPlayer();
            Sign sign = (Sign)e.getClickedBlock().getState();
            String[] l = sign.getLines();
           
            if (l[2].equalsIgnoreCase("save")) {               
                    if (EditKitGui.editdebuff.contains(p)) {
                        this.saveKit("Debuff", p);
                        p.sendMessage("§eYou have just saved your §aDebuff §ekit");
                    }
                    if (EditKitGui.editnodebuff.contains(p)) {
                        this.saveKit("NoDebuff", p);
                        p.sendMessage("§eYou have just saved your §aNoDebuff §ekit");
                    }
                    if (EditKitGui.editbow.contains(p)) {
                        this.saveKit("Bow", p);
                        p.sendMessage("§eYou have just saved your §aBow §ekit");
                    }
                    if (EditKitGui.editgapple.contains(p)) {
                        this.saveKit("Gapple", p);
                        p.sendMessage("§eYou have just saved your §aGapple §ekit");
                    }
                    if (EditKitGui.editaxe.contains(p)) {
                        this.saveKit("Axe", p);
                        p.sendMessage("§eYou have just saved your §aAxe §ekit");
                    }
                    if (EditKitGui.editsoup.contains(p)) {
                        this.saveKit("Soup", p);
                        p.sendMessage("§eYou have just saved your §aSoup §ekit");
                    }
                    if (EditKitGui.edituhc.contains(p)) {
                        this.saveKit("BuildUHC", p);
                        p.sendMessage("§eYou have just saved your §4BuildUHC §ekit");
                    }
                e.setCancelled(true);
            }
                }
            }
        
    
    @EventHandler
    public void onInterddaact(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Sign) {
            Player p = e.getPlayer();
            Sign sign = (Sign)e.getClickedBlock().getState();
            String[] l = sign.getLines();
            if (l[2].equalsIgnoreCase("delete")) {               
                    if (EditKitGui.editdebuff.contains(p)) {
                        this.deleteKit("Debuff", p);
                        p.sendMessage("§eYou have just deleted your §aDebuff §ekit");
                    }
                    if (EditKitGui.editnodebuff.contains(p)) {
                        this.deleteKit("NoDebuff", p);
                        p.sendMessage("§eYou have just deleted your §aDebuff §ekit");
                    }
                    if (EditKitGui.editbow.contains(p)) {
                        this.deleteKit("Bow", p);
                        p.sendMessage("§eYou have just deleted your §aBow §ekit");
                    }
                    if (EditKitGui.editgapple.contains(p)) {
                        this.deleteKit("Gapple", p);
                        p.sendMessage("§eYou have just deleted your §aGapple §ekit");
                    }
                    if (EditKitGui.editaxe.contains(p)) {
                        this.deleteKit("Axe", p);
                        p.sendMessage("§eYou have just deleted your §aAxe §ekit");
                    }
                    if (EditKitGui.editsoup.contains(p)) {
                        this.deleteKit("Soup", p);
                        p.sendMessage("§eYou have just deleted your §aSoup §ekit");
                    }
                    if (EditKitGui.edituhc.contains(p)) {
                        this.deleteKit("BuildUHC", p);
                        p.sendMessage("§eYou have just deleted your §4BuildUHC §ekit");
                    }
                e.setCancelled(true);
                }
            }
        }
    
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getWhoClicked();
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (!e.getCurrentItem().hasItemMeta()) {
            return;
        }
        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aSave Kit")) {         
            if (EditKitGui.editdebuff.contains(p)) {
                this.saveKit("Debuff", p);
                p.sendMessage("§eYou have just saved your §aDebuff §ekit");
            }
            if (EditKitGui.editnodebuff.contains(p)) {
                this.saveKit("NoDebuff", p);
                p.sendMessage("§eYou have just saved your §aNoDebuff §ekit");
            }
            if (EditKitGui.editbow.contains(p)) {
                this.saveKit("Bow", p);
                p.sendMessage("§eYou have just saved your §aBow §ekit");
            }
            if (EditKitGui.editgapple.contains(p)) {
                this.saveKit("Gapple", p);
                p.sendMessage("§eYou have just saved your §aGapple §ekit");
            }
            if (EditKitGui.editaxe.contains(p)) {
                this.saveKit("Axe", p);
                p.sendMessage("§eYou have just saved your §aAxe §ekit");
            }
            if (EditKitGui.editsoup.contains(p)) {
                this.saveKit("Soup", p);
                p.sendMessage("§eYou have just saved your §aSoup §ekit");
            }
            if (EditKitGui.edituhc.contains(p)) {
                this.saveKit("BuildUHC", p);
                p.sendMessage("§eYou have just saved your §4BuildUHC §ekit");
            }
            p.closeInventory();
            e.setCancelled(true);            
        }
        if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cDelete Kit")) {
        	   if (EditKitGui.editdebuff.contains(p)) {
                   this.deleteKit("Debuff", p);
                   p.sendMessage("§eYou have just deleted your §aDebuff §ekit");
               }
               if (EditKitGui.editnodebuff.contains(p)) {
                   this.deleteKit("NoDebuff", p);
                   p.sendMessage("§eYou have just deleted your §aDebuff §ekit");
               }
               if (EditKitGui.editbow.contains(p)) {
                   this.deleteKit("Bow", p);
                   p.sendMessage("§eYou have just deleted your §aBow §ekit");
               }
               if (EditKitGui.editgapple.contains(p)) {
                   this.deleteKit("Gapple", p);
                   p.sendMessage("§eYou have just deleted your §aGapple §ekit");
               }
               if (EditKitGui.editaxe.contains(p)) {
                   this.deleteKit("Axe", p);
                   p.sendMessage("§eYou have just deleted your §aAxe §ekit");
               }
               if (EditKitGui.editsoup.contains(p)) {
                   this.deleteKit("Soup", p);
                   p.sendMessage("§eYou have just deleted your §aSoup §ekit");
               }
               if (EditKitGui.edituhc.contains(p)) {
                   this.deleteKit("BuildUHC", p);
                   p.sendMessage("§eYou have just deleted your §4BuildUHC §ekit");
               }
               p.closeInventory();
               e.setCancelled(true);  
            }        
        }      
    
    private void saveKit(String kit, Player p) {
        File file = new File(this.pl.getDataFolder() + "/kit/", p.getUniqueId().toString() + kit + ".yml");
        YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                c.set("inventory.armor", (Object)p.getInventory().getArmorContents());
                c.set("inventory.content", (Object)p.getInventory().getContents());
                c.save(file);
                this.pl.getLogger().info("Creating files..");
                this.pl.getLogger().info("File created.");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            file.delete();
            c.set("inventory.armor", (Object)p.getInventory().getArmorContents());
            c.set("inventory.content", (Object)p.getInventory().getContents());
            try {
                c.save(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void deleteKit(String kit, Player p) {
        File file = new File(this.pl.getDataFolder() + "/kit/", p.getUniqueId().toString() + kit + ".yml");
        //YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            file.delete();
        }
    }
}