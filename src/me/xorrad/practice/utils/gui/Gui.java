package me.xorrad.practice.utils.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.QueueManager;
import me.xorrad.practice.utils.SpeedItem;

public class Gui {
	
	public static Inventory unranked;
	public static Inventory ranked;
	public static Inventory unranked_2v2;
	
	static {
		unranked = Bukkit.createInventory(null, 9, "§9Select a Un-Ranked Queue");
		ranked = Bukkit.createInventory(null, 9, "§9Select a Ranked Queue");
		unranked_2v2 = Bukkit.createInventory(null, 9, "§9Select a 2v2 Queue");
		updateUnrankedGui();
		updateRankedGui();
		update2v2Gui();
	}
	
	public static void openUnrankedTeamGui(Player p){
		p.openInventory(unranked_2v2);
	}
	
	public static void openUnrankedGui(Player p){
		p.openInventory(unranked);
	}
	
	public static void openRankedGui(Player p){
		p.openInventory(ranked);
	}
	
	public static void updateUnrankedGui(){
		// UNRANKED GUI
		unranked.clear();
		
		ArrayList<String> ndb_lore = new ArrayList<>();
		ndb_lore.add("§eIn queue: §a" + QueueManager.nodebuff_unranked.size());
		ndb_lore.add("§eIn fights: §a" + Fight.getNoDebuffFights());
		
		ArrayList<String> db_lore = new ArrayList<>();
		db_lore.add("§eIn queue: §a" + QueueManager.debuff_unranked.size());
		db_lore.add("§eIn fights: §a" + Fight.getDebuffFights());
		
		ArrayList<String> bow_lore = new ArrayList<>();
		bow_lore.add("§eIn queue: §a" + QueueManager.bow_unranked.size());
		bow_lore.add("§eIn fights: §a" + Fight.getBowFights());
		
		ArrayList<String> gapple_lore = new ArrayList<>();
		gapple_lore.add("§eIn queue: §a" + QueueManager.gapple_unranked.size());
		gapple_lore.add("§eIn fights: §a" + Fight.getGappleFights());
		
		ArrayList<String> axe_lore = new ArrayList<>();
		axe_lore.add("§eIn queue: §a" + QueueManager.axe_unranked.size());
		axe_lore.add("§eIn fights: §a" + Fight.getAxeFights());
		
		ArrayList<String> soup_lore = new ArrayList<>();
		soup_lore.add("§eIn queue: §a" + QueueManager.soup_unranked.size());
		soup_lore.add("§eIn fights: §a" + Fight.getSoupFights());
		
		/*SpeedItem nodebuff = new SpeedItem(Material.POTION, "§cNoDebuff", ndb_lore);
		//nodebuff.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		nodebuff.setDurability((short)16421);
		nodebuff.setAmount(Fight.getNoDebuffFights());
		
		SpeedItem debuff = new SpeedItem(Material.POTION, "§2Debuff", db_lore);
		debuff.setDurability((short)16426);
		debuff.setAmount(Fight.getDebuffFights());
		
		SpeedItem bow = new SpeedItem(Material.BOW, "§6Bow", bow_lore);
		bow.setAmount(Fight.getBowFights());
		
		SpeedItem gapple = new SpeedItem(Material.GOLDEN_APPLE, "§6Gapple", gapple_lore);
		gapple.setDurability((short)1);
		gapple.setAmount(Fight.getGappleFights());
		
		SpeedItem axe = new SpeedItem(Material.IRON_AXE, "§7Axe", axe_lore);
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		axe.setAmount(Fight.getAxeFights());
		
		SpeedItem soup = new SpeedItem(Material.MUSHROOM_SOUP, "§dSoup", soup_lore);*/
		
		for(FightLadder ladder : FightLadder.values()) {
			
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§eIn queue: §a" + QueueManager.unrankedQueueSize(ladder));
			lore.add("§eIn fights: §a" + Fight.getFights(ladder));
			
			SpeedItem ladderItem = new SpeedItem(ladder.getGuiItem(), ladder.getGuiString(), lore);
			if(ladder.getGuiEnchant() != null) {
				ladderItem.addEnchantment(ladder.getGuiEnchant(), 1);
			}
			ladderItem.setDurability((short)ladder.getGuiDurability());
			ladderItem.setAmount(Fight.getFights(ladder));
			unranked.addItem(ladderItem);
		}
		
		/*unranked.addItem(nodebuff);
		unranked.addItem(debuff);
		unranked.addItem(bow);
		unranked.addItem(gapple);
		unranked.addItem(axe);
		unranked.addItem(soup);*/
	}
	
	public static void updateRankedGui(){
		// RANKED GUI
		ranked.clear();
		
		/*ArrayList<String> ndb_lore = new ArrayList<>();
		ndb_lore.add("§eIn queue: §a" + QueueManager.nodebuff_ranked.size());
		ndb_lore.add("§eIn fights: §a" + Fight.getRankedNoDebuffFights());
		
		ArrayList<String> db_lore = new ArrayList<>();
		db_lore.add("§eIn queue: §a" + QueueManager.debuff_ranked.size());
		db_lore.add("§eIn fights: §a" + Fight.getRankedDebuffFights());
		
		ArrayList<String> bow_lore = new ArrayList<>();
		bow_lore.add("§eIn queue: §a" + QueueManager.bow_ranked.size());
		bow_lore.add("§eIn fights: §a" + Fight.getRankedBowFights());
		
		ArrayList<String> gapple_lore = new ArrayList<>();
		gapple_lore.add("§eIn queue: §a" + QueueManager.gapple_ranked.size());
		gapple_lore.add("§eIn fights: §a" + Fight.getRankedGappleFights());
		
		ArrayList<String> axe_lore = new ArrayList<>();
		axe_lore.add("§eIn queue: §a" + QueueManager.axe_ranked.size());
		axe_lore.add("§eIn fights: §a" + Fight.getRankedAxeFights());
		
		ArrayList<String> soup_lore = new ArrayList<>();
		soup_lore.add("§eIn queue: §a" + QueueManager.soup_ranked.size());
		soup_lore.add("§eIn fights: §a" + Fight.getRankedSoupFights());
		
		SpeedItem nodebuff = new SpeedItem(Material.POTION, "§cNoDebuff", ndb_lore);
		//nodebuff.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		nodebuff.setDurability((short)16421);
		nodebuff.setAmount(Fight.getRankedNoDebuffFights());
		
		SpeedItem debuff = new SpeedItem(Material.POTION, "§2Debuff", db_lore);
		debuff.setDurability((short)16426);
		debuff.setAmount(Fight.getRankedDebuffFights());
		
		SpeedItem bow = new SpeedItem(Material.BOW, "§6Bow", bow_lore);
		bow.setAmount(Fight.getRankedBowFights());
		
		SpeedItem gapple = new SpeedItem(Material.GOLDEN_APPLE, "§6Gapple", gapple_lore);
		gapple.setDurability((short)1);
		gapple.setAmount(Fight.getRankedGappleFights());
		
		SpeedItem axe = new SpeedItem(Material.IRON_AXE, "§7Axe", axe_lore);
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		axe.setAmount(Fight.getRankedAxeFights());
		
		SpeedItem soup = new SpeedItem(Material.MUSHROOM_SOUP, "§dSoup", soup_lore);
		
		ranked.addItem(nodebuff);
		ranked.addItem(debuff);
		ranked.addItem(bow);
		ranked.addItem(gapple);
		ranked.addItem(axe);
		ranked.addItem(soup);*/
		
		for(FightLadder ladder : FightLadder.values()) {
			
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§eIn queue: §a" + QueueManager.rankedQueueSize(ladder));
			lore.add("§eIn fights: §a" + Fight.getRankedFights(ladder));
			
			SpeedItem ladderItem = new SpeedItem(ladder.getGuiItem(), ladder.getGuiString(), lore);
			if(ladder.getGuiEnchant() != null) {
				ladderItem.addEnchantment(ladder.getGuiEnchant(), 1);
			}
			ladderItem.setDurability((short)ladder.getGuiDurability());
			ladderItem.setAmount(Fight.getRankedFights(ladder));
			ranked.addItem(ladderItem);
		}
	}
	
	public static void update2v2Gui(){
		// RANKED GUI
		unranked_2v2.clear();
		
		/*ArrayList<String> ndb_lore = new ArrayList<>();
		ndb_lore.add("§eIn queue: §a" + QueueManager.nodebuff_2v2.size());
		ndb_lore.add("§eIn fights: §a" + Fight.getNoDebuffUnrankedTeamFights());
		
		ArrayList<String> db_lore = new ArrayList<>();
		db_lore.add("§eIn queue: §a" + QueueManager.debuff_2v2.size());
		db_lore.add("§eIn fights: §a" + Fight.getDebuffUnrankedTeamFights());
		
		ArrayList<String> bow_lore = new ArrayList<>();
		bow_lore.add("§eIn queue: §a" + QueueManager.bow_2v2.size());
		bow_lore.add("§eIn fights: §a" + Fight.getBowUnrankedTeamFights());
		
		ArrayList<String> gapple_lore = new ArrayList<>();
		gapple_lore.add("§eIn queue: §a" + QueueManager.gapple_2v2.size());
		gapple_lore.add("§eIn fights: §a" + Fight.getGappleUnrankedTeamFights());
		
		ArrayList<String> axe_lore = new ArrayList<>();
		axe_lore.add("§eIn queue: §a" + QueueManager.axe_2v2.size());
		axe_lore.add("§eIn fights: §a" + Fight.getAxeUnrankedTeamFights());
		
		ArrayList<String> soup_lore = new ArrayList<>();
		soup_lore.add("§eIn queue: §a" + QueueManager.soup_2v2.size());
		soup_lore.add("§eIn fights: §a" + Fight.getSoupUnrankedTeamFights());
		
		SpeedItem nodebuff = new SpeedItem(Material.POTION, "§cNoDebuff", ndb_lore);
		//nodebuff.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		nodebuff.setDurability((short)16421);
		nodebuff.setAmount(Fight.getNoDebuffUnrankedTeamFights());
		
		SpeedItem debuff = new SpeedItem(Material.POTION, "§2Debuff", db_lore);
		debuff.setDurability((short)16426);
		debuff.setAmount(Fight.getDebuffUnrankedTeamFights());
		
		SpeedItem bow = new SpeedItem(Material.BOW, "§6Bow", bow_lore);
		bow.setAmount(Fight.getBowUnrankedTeamFights());
		
		SpeedItem gapple = new SpeedItem(Material.GOLDEN_APPLE, "§6Gapple", gapple_lore);
		gapple.setDurability((short)1);
		gapple.setAmount(Fight.getGappleUnrankedTeamFights());
		
		SpeedItem axe = new SpeedItem(Material.IRON_AXE, "§7Axe", axe_lore);
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		axe.setAmount(Fight.getAxeUnrankedTeamFights());
		
		SpeedItem soup = new SpeedItem(Material.MUSHROOM_SOUP, "§dSoup", soup_lore);
		
		unranked_2v2.addItem(nodebuff);
		unranked_2v2.addItem(debuff);
		unranked_2v2.addItem(bow);
		unranked_2v2.addItem(gapple);
		unranked_2v2.addItem(axe);
		unranked_2v2.addItem(soup);*/
		
		for(FightLadder ladder : FightLadder.values()) {
			
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§eIn queue: §a" + QueueManager.unranked2v2QueueSize(ladder));
			lore.add("§eIn fights: §a" + Fight.get2v2Fights(ladder));
			
			SpeedItem ladderItem = new SpeedItem(ladder.getGuiItem(), ladder.getGuiString(), lore);
			if(ladder.getGuiEnchant() != null) {
				ladderItem.addEnchantment(ladder.getGuiEnchant(), 1);
			}
			ladderItem.setDurability((short)ladder.getGuiDurability());
			ladderItem.setAmount(Fight.get2v2Fights(ladder));
			unranked_2v2.addItem(ladderItem);
		}
	}
	
	public static void openDuelGui(Player p){
		Inventory inv = Bukkit.createInventory(null, 9, "§8Select PvP Style");
		
		/*SpeedItem nodebuff = new SpeedItem(Material.POTION, "§cNoDebuff");
		nodebuff.setDurability((short)16421);
		
		SpeedItem debuff = new SpeedItem(Material.POTION, "§2Debuff");
		debuff.setDurability((short)16426);
		
		SpeedItem bow = new SpeedItem(Material.BOW, "§6Bow");
		
		SpeedItem gapple = new SpeedItem(Material.GOLDEN_APPLE, "§6Gapple");
		gapple.setDurability((short)1);
		
		SpeedItem axe = new SpeedItem(Material.IRON_AXE, "§7Axe");
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		
		SpeedItem soup = new SpeedItem(Material.MUSHROOM_SOUP, "§dSoup");
		
		inv.addItem(nodebuff);
		inv.addItem(debuff);
		inv.addItem(bow);
		inv.addItem(gapple);
		inv.addItem(axe);
		inv.addItem(soup);*/
		
		for(FightLadder ladder : FightLadder.values()) {
			SpeedItem ladderItem = new SpeedItem(ladder.getGuiItem(), ladder.getGuiString());
			if(ladder.getGuiEnchant() != null) {
				ladderItem.addEnchantment(ladder.getGuiEnchant(), 1);
			}
			ladderItem.setDurability((short)ladder.getGuiDurability());
			inv.addItem(ladderItem);
		}
		
		p.openInventory(inv);
	}
	
	public static void openTeamFight(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, "§9Menu of Team Fight");
		
		SpeedItem ffa = new SpeedItem(Material.GOLD_SWORD, "§6FFA");
		SpeedItem split = new SpeedItem(Material.IRON_SWORD, "§8Team-Fight");
		
		inv.setItem(3, ffa);
		inv.setItem(5, split);
		
		p.openInventory(inv);
	}
	
	public static void openTeamFightDuelGui(Player p){
		Inventory inv = Bukkit.createInventory(null, 9, "§8Team-Fight");
		
		/*SpeedItem nodebuff = new SpeedItem(Material.POTION, "§cNoDebuff");
		nodebuff.setDurability((short)16421);
		
		SpeedItem debuff = new SpeedItem(Material.POTION, "§2Debuff");
		debuff.setDurability((short)16426);
		
		SpeedItem bow = new SpeedItem(Material.BOW, "§6Bow");
		
		SpeedItem gapple = new SpeedItem(Material.GOLDEN_APPLE, "§6Gapple");
		gapple.setDurability((short)1);
		
		SpeedItem axe = new SpeedItem(Material.IRON_AXE, "§7Axe");
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		
		SpeedItem soup = new SpeedItem(Material.MUSHROOM_SOUP, "§dSoup");
		
		inv.addItem(nodebuff);
		inv.addItem(debuff);
		inv.addItem(bow);
		inv.addItem(gapple);
		inv.addItem(axe);
		inv.addItem(soup);*/
		
		for(FightLadder ladder : FightLadder.values()) {
			SpeedItem ladderItem = new SpeedItem(ladder.getGuiItem(), ladder.getGuiString());
			if(ladder.getGuiEnchant() != null) {
				ladderItem.addEnchantment(ladder.getGuiEnchant(), 1);
			}
			ladderItem.setDurability((short)ladder.getGuiDurability());
			inv.addItem(ladderItem);
		}
		
		p.openInventory(inv);
	}
	public static void openTeamFFADuelGui(Player p){
		Inventory inv = Bukkit.createInventory(null, 9, "§6FFA");
		
		/*SpeedItem nodebuff = new SpeedItem(Material.POTION, "§cNoDebuff");
		nodebuff.setDurability((short)16421);
		
		SpeedItem debuff = new SpeedItem(Material.POTION, "§2Debuff");
		debuff.setDurability((short)16426);
		
		SpeedItem bow = new SpeedItem(Material.BOW, "§6Bow");
		
		SpeedItem gapple = new SpeedItem(Material.GOLDEN_APPLE, "§6Gapple");
		gapple.setDurability((short)1);
		
		SpeedItem axe = new SpeedItem(Material.IRON_AXE, "§7Axe");
		axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		
	SpeedItem soup = new SpeedItem(Material.MUSHROOM_SOUP, "§dSoup");
		
		inv.addItem(nodebuff);
		inv.addItem(debuff);
		inv.addItem(bow);
		inv.addItem(gapple);
		inv.addItem(axe);
		inv.addItem(soup);*/
		
		for(FightLadder ladder : FightLadder.values()) {
			SpeedItem ladderItem = new SpeedItem(ladder.getGuiItem(), ladder.getGuiString());
			if(ladder.getGuiEnchant() != null) {
				ladderItem.addEnchantment(ladder.getGuiEnchant(), 1);
			}
			ladderItem.setDurability((short)ladder.getGuiDurability());
			inv.addItem(ladderItem);
		}
		
		p.openInventory(inv);
	}
	
	public static void openOtherTeamDuelGui(Player p){
		Inventory inv = Bukkit.createInventory(null, 6*9, "§7Team List");

		if(Team.getTeams() != null) {
			for(Team t : Team.getTeams().values()) {
	
				/*if(!t.isInFight()) {
					ArrayList<String> lores = new ArrayList<>();
					for(Player pls : t.getPlayers()) {
						lores.add("§e- " + pls.getName());
					}
					
					SpeedItem item = new SpeedItem(Material.SKULL_ITEM, "§a" + t.getLeader().getName() + "'s team", lores);
					item.setDurability((short)3);
					inv.addItem(item);
				}*/
				
				ArrayList<String> lores = new ArrayList<>();
				for(Player pls : t.getPlayers()) {
					lores.add("§e- " + pls.getName());
				}
				
				SpeedItem item = new SpeedItem(Material.SKULL_ITEM, "§a" + t.getLeader().getName() + "'s team", lores);
				
				if(t.isInFight()) {
					item.setDurability((short)1);
				} else {
					item.setDurability((short)3);
				}
				
				inv.addItem(item);
			}
		}
		
		p.openInventory(inv);
	}
	
	public static void openCreateTournamentGui(Player p)
	{
		Inventory inv = Bukkit.createInventory(null, 9, "§7Create Tournament");
		
		for(FightLadder ladder : FightLadder.values()) {
			if(ladder.isTournamentLadder())
			{
				SpeedItem ladderItem = new SpeedItem(ladder.getGuiItem(), ladder.getGuiString());
				if(ladder.getGuiEnchant() != null) {
					ladderItem.addEnchantment(ladder.getGuiEnchant(), 1);
				}
				ladderItem.setDurability((short)ladder.getGuiDurability());
				inv.addItem(ladderItem);
			}
		}
		
		p.openInventory(inv);
	}
}
