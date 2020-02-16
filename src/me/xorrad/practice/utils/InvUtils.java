package me.xorrad.practice.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.xorrad.practice.Practice;

public class InvUtils implements Listener{
	
	public static HashMap<String, Inventory> invs;
	
	static {
		invs = new HashMap<>();
	}
	
	public static void saveInv(Player p){
		String name = p.getName();
		
		Inventory inv = Bukkit.createInventory(null, 6*9, "§7" + p.getName() + "'s Inventory");
		
		/*for(ItemStack i : p.getInventory().getContents()){
			if(i==null){
				inv.addItem(new ItemStack(Material.AIR));
			} else {
				inv.addItem(i);
			}
		}*/
		for(int i=0; i<p.getInventory().getSize(); i++){
			ItemStack item = p.getInventory().getItem(i);
			if(item!=null){
				inv.setItem(i, item);
			}
		}
		
		Damageable pd = p;
		String health = String.format("%1$,.0f", pd.getHealth());
		SpeedItem heal = null;
		if(pd.getHealth() == 0){
			heal = new SpeedItem(Material.SKULL_ITEM, "§cDead");
		} else {
			heal = new SpeedItem(Material.MELON, "§c" + health + "♥");
		}
		inv.setItem(48, heal);
		
		SpeedItem food = new SpeedItem(Material.COOKED_BEEF, "§6Food: " + String.valueOf(p.getFoodLevel()));
		inv.setItem(49, food);
		
		ArrayList<String> lores = new ArrayList<>();
		
		for(PotionEffect pe : p.getActivePotionEffects()){
			switch(pe.getType().getName()){
				case "SPEED":
					lores.add("§bSpeed: §7" + getDurationString(pe.getDuration()));
				break;
				case "FIRE_RESISTANCE":
					lores.add("§6Fire Resistance: §7" + getDurationString(pe.getDuration()));
				break;
				case "POISON":
					lores.add("§2Poison: §7" + getDurationString(pe.getDuration()));
				break;
				case "SLOWNESS":
					lores.add("§7Slowness: §7" + getDurationString(pe.getDuration()));
				break;
				case "INCREASE_DAMAGE":
					lores.add("§cStrength: §7" + getDurationString(pe.getDuration()));
				break;
				case "REGENERATION":
					lores.add("§dRegeneration: §7" + getDurationString(pe.getDuration()));
				break;
				case "ABSORPTION":
					lores.add("§eAbsortion: §7" + getDurationString(pe.getDuration()));
				break;
			}
		}
		
		SpeedItem effects = new SpeedItem(Material.POTION, "§3Potion Effects: ", lores);
		inv.setItem(50, effects);
		
		invs.put(name, inv);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				invs.remove(name);
			}
		}.runTaskTimer(Practice.getInstance(), 4200, 4200);
	}
	
	public static void openInv(Player p, String inv){
		
		if(invs.containsKey(inv)){
			p.openInventory(invs.get(inv));
		} else {
			p.sendMessage("§cUnknown Inventory!");
		}
		
	}
	
	@SuppressWarnings("unused")
	public static String getDurationString(int value) {

		int seconds = value/20;
	    int hours = seconds / 3600;
	    int minutes = (seconds % 3600) / 60;
	    seconds = seconds % 60;

	    return twoDigitString(minutes) + ":" + twoDigitString(seconds);
	}
	
	public static String twoDigitString(int number) {

	    if (number == 0) {
	        return "00";
	    }

	    if (number / 10 == 0) {
	        return "0" + number;
	    }

	    return String.valueOf(number);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e){		
		if(e.getInventory().getName().contains("'s Inventory")){
			e.setCancelled(true);
		}
	}

}
