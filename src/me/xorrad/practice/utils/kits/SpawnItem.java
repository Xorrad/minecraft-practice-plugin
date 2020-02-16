package me.xorrad.practice.utils.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.xorrad.practice.utils.SpeedItem;

public class SpawnItem {
	
	public static void giveSpawnItem(Player p){
		
		p.setFireTicks(0);
		p.setMaximumNoDamageTicks(20);
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
		
		SpeedItem unranked = new SpeedItem(Material.IRON_SWORD, "§9Un-Ranked Queue");
		SpeedItem ranked = new SpeedItem(Material.DIAMOND_SWORD, "§aRanked Queue");
		
		SpeedItem team = new SpeedItem(Material.NAME_TAG, "§eCreate Team");
		
		SpeedItem edit = new SpeedItem(Material.BOOK, "§6Edit Kits");
		
		p.getInventory().setItem(0, edit);
		p.getInventory().setItem(4, team);
		p.getInventory().setItem(7, unranked);
		p.getInventory().setItem(8, ranked);
		p.updateInventory();
	}

}