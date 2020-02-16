package me.xorrad.practice.editkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.xorrad.practice.Practice;
import me.xorrad.practice.fight.FightLadder;

public class SelectKit implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
        	
        	
        	for(FightLadder ladder : FightLadder.values()){
        		
        		if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§e" + ladder.name() + " Default Kit")) {
        			
        			try {
	        			Class<?> c = Class.forName("me.xorrad.practice.utils.kits.Kits");
	        			Method method = c.getDeclaredMethod("give"+ladder.name()+"Kit", Player.class);
	        			method.invoke(null, p);
        			} catch(Exception ex) {
        				ex.printStackTrace();
        			}
                	p.setHealth(20.0);
                }
                if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§e" + ladder.name() + " Custom Kit")) {
                    File file = new File(Practice.getInstance().getDataFolder() + "/kit/", p.getUniqueId().toString() + ladder.name() + ".yml");
                    //Bukkit.broadcastMessage(file.getPath());
                    if (!file.exists()) {
                        p.sendMessage("§cKit not found!");
                    }
                    else {
                        try {
                            getKit(p, ladder.name());
                            p.sendMessage("§fGiving you §eCustom " + ladder.name() + " kit");
                        }
                        catch (IOException e2) {
                            p.sendMessage("§cError while getting your kit !");
                            try {
        	        			Class<?> c = Class.forName("me.xorrad.practice.utils.kits.Kits");
        	        			Method method = c.getDeclaredMethod("give"+ladder.name()+"Kit", Player.class);
        	        			method.invoke(null, p);
                			} catch(Exception ex) {
                				ex.printStackTrace();
                			}
                            e2.printStackTrace();
                        }
                    }
                    p.setHealth(20.0);
                }
                
        	}
        	
            /*if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eDebuff Default Kit")) {
            	Kits.giveDebuffKit(p);
            	p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eDebuff Custom Kit")) {
                File file = new File(Practice.getInstance().getDataFolder() + "\\kit\\", String.valueOf(p.getUniqueId()) + "Debuff" + ".yml");
                if (!file.exists()) {
                	//Kits.giveDebuffKit(p);
                    p.sendMessage("§cKit not found!");
                }
                else {
                    try {
                        getKit(p, "Debuff");
                        p.sendMessage("§fGiving you §eCustom Debuff kit");
                    }
                    catch (IOException e2) {
                        p.sendMessage("§cError while getting your kit !");
                        Kits.giveDebuffKit(p);
                        e2.printStackTrace();
                    }
                }
                p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eNoDebuff Default Kit")) {
            	Kits.giveNoDebuffKit(p);
            	p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eNoDebuff Custom Kit")) {
                File file = new File(Practice.getInstance().getDataFolder() + "\\kit\\", String.valueOf(p.getUniqueId()) + "Nodebuff" + ".yml");
                if (!file.exists()) {
                	//Kits.giveNoDebuffKit(p);
                    p.sendMessage("§cKit not found!");
                }
                else {
                    try {
                        getKit(p, "Nodebuff");
                        p.sendMessage("§fGiving you §eCustom NoDebuff kit");
                    }
                    catch (IOException e2) {
                        p.sendMessage("§cError while getting your kit !");
                        Kits.giveNoDebuffKit(p);
                        e2.printStackTrace();
                    }
                }
                p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eBow Default Kit")) {
            	Kits.giveBowKit(p);
            	p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eBow Custom Kit")) {
                File file = new File(Practice.getInstance().getDataFolder() + "\\kit\\", String.valueOf(p.getUniqueId()) + "Bow" + ".yml");
                if (!file.exists()) {
                    p.sendMessage("§cKit not found!");
                }
                else {
                    try {
                        getKit(p, "Bow");
                        p.sendMessage("§fGiving you §eCustom Bow kit");
                    }
                    catch (IOException e2) {
                        p.sendMessage("§cError while getting your kit !");
                        Kits.giveBowKit(p);
                        e2.printStackTrace();
                    }
                }
                p.setHealth(20.0);
            }
            
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eGapple Default Kit")) {
            	Kits.giveGappleKit(p);
            	p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eGapple Custom Kit")) {
                File file = new File(Practice.getInstance().getDataFolder() + "\\kit\\", String.valueOf(p.getUniqueId()) + "Gapple" + ".yml");
                if (!file.exists()) {
                    p.sendMessage("§cKit not found!");
                }
                else {
                    try {
                        getKit(p, "Gapple");
                        p.sendMessage("§fGiving you §eCustom Gapple kit");
                    }
                    catch (IOException e2) {
                        p.sendMessage("§cError while getting your kit !");
                        Kits.giveGappleKit(p);
                        e2.printStackTrace();
                    }
                }
                p.setHealth(20.0);
            }
            
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eAxe Default Kit")) {
            	Kits.giveAxeKit(p);
            	p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eAxe Custom Kit")) {
                File file = new File(Practice.getInstance().getDataFolder() + "\\kit\\", String.valueOf(p.getUniqueId()) + "Axe" + ".yml");
                if (!file.exists()) {
                    p.sendMessage("§cKit not found!");
                }
                else {
                    try {
                        getKit(p, "Axe");
                        p.sendMessage("§fGiving you §eCustom Axe kit");
                    }
                    catch (IOException e2) {
                        p.sendMessage("§cError while getting your kit !");
                        Kits.giveAxeKit(p);
                        e2.printStackTrace();
                    }
                }
                p.setHealth(20.0);
            }
            
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eSoup Default Kit")) {
            	Kits.giveSoupKit(p);
            	p.setHealth(20.0);
            }
            if (e.hasItem() && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eSoup Custom Kit")) {
                File file = new File(Practice.getInstance().getDataFolder() + "\\kit\\", String.valueOf(p.getUniqueId()) + "Soup" + ".yml");
                if (!file.exists()) {
                    p.sendMessage("§cKit not found!");
                }
                else {
                    try {
                        getKit(p, "Soup");
                        p.sendMessage("§fGiving you §eCustom Soup kit");
                    }
                    catch (IOException e2) {
                        p.sendMessage("§cError while getting your kit !");
                        Kits.giveSoupKit(p);
                        e2.printStackTrace();
                    }
                }
                p.setHealth(20.0);
            }*/
        }
    }
    
    public static void getKit(Player p, String kit) throws IOException {
        YamlConfiguration c = YamlConfiguration.loadConfiguration(new File(Practice.getInstance().getDataFolder() + "/kit/", p.getUniqueId().toString() + kit + ".yml"));
        ItemStack[] content = (ItemStack[]) ((List<?>)c.get("inventory.armor")).toArray(new ItemStack[0]);
        p.getInventory().setArmorContents(content);
        content = (ItemStack[]) ((List<?>)c.get("inventory.content")).toArray(new ItemStack[0]);
        p.getInventory().setContents(content);
    }
}

