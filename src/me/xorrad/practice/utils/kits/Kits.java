package me.xorrad.practice.utils.kits;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.SpeedItem;
import me.xorrad.practice.utils.User;

public class Kits {
	
	public static void giveTeamLeaderItem(Player p){
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
		p.updateInventory();
		
		SpeedItem leave = new SpeedItem(Material.FIRE, "§cDisband Team");
		p.getInventory().setItem(3, leave);
		
		SpeedItem unrankedFight = new SpeedItem(Material.DIAMOND_SWORD, "§7Unranked 2v2");
		p.getInventory().setItem(7, unrankedFight);
		
		SpeedItem teamFight = new SpeedItem(Material.IRON_SWORD, "§7Team Fight");
		p.getInventory().setItem(8, teamFight);
		
		/*SpeedItem ffa = new SpeedItem(Material.GOLD_SWORD, "§7FFA");
		p.getInventory().setItem(8, ffa);*/
		
		SpeedItem other = new SpeedItem(Material.EYE_OF_ENDER, "§7Other Teams");
		p.getInventory().setItem(5, other);
		
		SpeedItem leader = new SpeedItem(Material.SKULL_ITEM, "§a"+p.getName()+"§9's Team");
		leader.setDurability((short)3);
		p.getInventory().setItem(1, leader);
		
		SpeedItem info = new SpeedItem(Material.NETHER_STAR, "§bYour team");
		p.getInventory().setItem(0, info);
		
		p.updateInventory();
	}
	
	public static void giveTeamItem(Player p){
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
		p.updateInventory();
		
		SpeedItem other = new SpeedItem(Material.EYE_OF_ENDER, "§7Other Teams");
		p.getInventory().setItem(5, other);
		
		SpeedItem leave = new SpeedItem(Material.FIRE, "§cLeave Team");
		p.getInventory().setItem(3, leave);
		
		SpeedItem leader = new SpeedItem(Material.SKULL_ITEM, "§a"+Team.getTeams().get(User.getPlayer(p).getTeamId()).getLeader().getName()+"§9's Team");
		leader.setDurability((short)3);
		p.getInventory().setItem(1, leader);
		
		SpeedItem info = new SpeedItem(Material.NETHER_STAR, "§bYour team");
		p.getInventory().setItem(0, info);
		
		p.updateInventory();
	}
	
	public static void giveQueueItem(Player p){
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
		p.updateInventory();
		
		SpeedItem leave = new SpeedItem(Material.REDSTONE, "§cRight click to leave the queue");
		p.getInventory().setItem(0, leave);
		
		p.updateInventory();
	}
	
	public static void giveTournamentItem(Player p){
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
		p.updateInventory();
		
		SpeedItem leave = new SpeedItem(Material.REDSTONE, "§cRight click to leave the tournament");
		p.getInventory().setItem(4, leave);
		
		p.updateInventory();
	}
	
	public static void giveSpecItem(Player p){
		
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
		p.updateInventory();
		
		SpeedItem leave = new SpeedItem(Material.REDSTONE, "§cRight click to stop spectate");
		p.getInventory().setItem(4, leave);
		
		p.updateInventory();
		
		p.setFireTicks(0);
	}
	
	public static void giveKitBook(Player p, FightLadder ladder){
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
		
		p.setGameMode(GameMode.SURVIVAL);
		p.setMaximumNoDamageTicks(ladder.getHitDelay());
		
		if(!ladder.equals(FightLadder.Sumo))
		{
			SpeedItem default_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§e" + ladder.name() + " Default Kit");
			SpeedItem edited_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§e" + ladder.name() + " Custom Kit");
			p.getInventory().setItem(0, default_kit);
			p.getInventory().setItem(2, edited_kit);
		}
		p.updateInventory();
		
		/*switch(ladder){
			case Bow:
				SpeedItem bow_default_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eBow Default Kit");
				SpeedItem bow_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eBow Custom Kit");
				
				p.getInventory().setItem(0, bow_default_kit);
				p.getInventory().setItem(2, bow_kit);
				p.updateInventory();
				break;
			case Debuff:
				SpeedItem debuff_default_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eDebuff Default Kit");
				SpeedItem debuff_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eDebuff Custom Kit");
				
				p.getInventory().setItem(0, debuff_default_kit);
				p.getInventory().setItem(2, debuff_kit);
				p.updateInventory();
				break;
			case NoDebuff:
				SpeedItem ndb_default_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eNoDebuff Default Kit");
				SpeedItem ndb_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eNoDebuff Custom Kit");
				
				p.getInventory().setItem(0, ndb_default_kit);
				p.getInventory().setItem(2, ndb_kit);
				p.updateInventory();
				break;
			case Gapple:
				SpeedItem gapple_default_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eGapple Default Kit");
				SpeedItem gapple_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eGapple Custom Kit");
				
				p.getInventory().setItem(0, gapple_default_kit);
				p.getInventory().setItem(2, gapple_kit);
				p.updateInventory();
				break;
			case Axe:
				SpeedItem axe_default_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eAxe Default Kit");
				SpeedItem axe_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eAxe Custom Kit");
				
				p.getInventory().setItem(0, axe_default_kit);
				p.getInventory().setItem(2, axe_kit);
				p.updateInventory();
				break;
			case Soup:
				SpeedItem soup_default_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eSoup Default Kit");
				SpeedItem soup_kit = new SpeedItem(Material.ENCHANTED_BOOK, "§eSoup Custom Kit");
				
				p.getInventory().setItem(0, soup_default_kit);
				p.getInventory().setItem(2, soup_kit);
				p.updateInventory();
				break;
			default:
				break;
		}*/
		
		p.updateInventory();
	}
	
	public static void giveBowKit(Player p){
		p.getInventory().clear();
	     	
	    ItemStack bow = new ItemStack(Material.BOW);
	    bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
	     		
	    ItemStack ender = new ItemStack(Material.ENDER_PEARL);
		ender.setAmount(3);
	     	    
	    ItemStack arrow = new ItemStack(Material.ARROW);
	    arrow.setAmount(64);
	    
	    ItemStack steak = new ItemStack(Material.COOKED_BEEF);
	    steak.setAmount(64);
	     			
	    ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
	    helmet.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
	     		
	    ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
	    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
	     		
	    ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
	    leggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
	     		
	    ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
	    boots.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
	     		
	    p.getInventory().setItem(0, bow);
	    p.getInventory().setItem(1, ender);
		p.getInventory().setItem(9, arrow);
		p.getInventory().setItem(10, steak);
		
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chestplate);
		p.getInventory().setLeggings(leggins);
		p.getInventory().setBoots(boots);
		 
		p.updateInventory();
		
	}
	
	public static void giveSumoKit(Player p){
		p.getInventory().clear();
		
		 
		p.updateInventory();
		
	}
	
	public static void giveSoupKit(Player p) {
		
		p.getInventory().clear();
		p.updateInventory();
		
		SpeedItem sword = new SpeedItem(Material.DIAMOND_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		
		ItemStack HELMET = new ItemStack(Material.IRON_HELMET);
		p.getInventory().setHelmet(new ItemStack(HELMET));
		 
		ItemStack CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
		p.getInventory().setChestplate(new ItemStack(CHESTPLATE));
		 
		ItemStack LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
		p.getInventory().setLeggings(new ItemStack(LEGGINGS));
		 
		ItemStack BOOTS = new ItemStack(Material.IRON_BOOTS);
		p.getInventory().setBoots(new ItemStack(BOOTS));
		
		ItemStack speed_item = new ItemStack(Material.POTION);
		speed_item.setDurability((short)8226);
		
		ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP);
		
		/*ItemStack strength = new ItemStack(Material.POTION);
		strength.setDurability((short)8233);*/
		
		for(int i=0; i<p.getInventory().getSize(); i++) {
			p.getInventory().addItem(soup);
		}
		
		p.getInventory().setItem(0, sword);
		p.getInventory().setItem(8, speed_item);
		//p.getInventory().setItem(7, strength);
		p.getInventory().setItem(35, speed_item);
		p.getInventory().setItem(26, speed_item);
		p.getInventory().setItem(17, speed_item);
		
		p.updateInventory();	
	}
	
	public static void giveAxeKit(Player p) {
		p.getInventory().clear();
		p.updateInventory();
		
		SpeedItem sword = new SpeedItem(Material.IRON_AXE);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		sword.addEnchantment(Enchantment.DURABILITY, 3);
		
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE);
		gapple.setAmount(15);
		
		ItemStack HELMET = new ItemStack(Material.IRON_HELMET);
		HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		HELMET.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setHelmet(new ItemStack(HELMET));
		 
		ItemStack CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE);
		CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setChestplate(new ItemStack(CHESTPLATE));
		 
		ItemStack LEGGINGS = new ItemStack(Material.IRON_LEGGINGS);
		LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setLeggings(new ItemStack(LEGGINGS));
		 
		ItemStack BOOTS = new ItemStack(Material.IRON_BOOTS);
		BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		BOOTS.addEnchantment(Enchantment.DURABILITY, 3);
		BOOTS.addEnchantment(Enchantment.PROTECTION_FALL, 4);
		p.getInventory().setBoots(new ItemStack(BOOTS));
		
		ItemStack speed_item = new ItemStack(Material.POTION);
		speed_item.setDurability((short)8226);
		
		ItemStack heal = new ItemStack(Material.POTION);
		heal.setDurability((short)16421);
		
		p.getInventory().addItem(sword);
		p.getInventory().setItem(1, speed_item);
		p.getInventory().setItem(8, gapple);
		p.getInventory().setItem(27, speed_item);
		p.getInventory().setItem(28, heal);
		
		for(int i=0; i<6; i++) {
			p.getInventory().addItem(heal);
		}
		
		p.updateInventory();
		p.updateInventory();
	}
	
	public static void giveComboKit(Player p) {
		p.getInventory().clear();
		p.updateInventory();
		
		SpeedItem sword = new SpeedItem(Material.DIAMOND_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
		
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE);
		gapple.setDurability((short)1);
		gapple.setAmount(64);
		
		ItemStack HELMET = new ItemStack(Material.DIAMOND_HELMET);
		HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		HELMET.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setHelmet(new ItemStack(HELMET));
		 
		ItemStack CHESTPLATE = new ItemStack(Material.DIAMOND_CHESTPLATE);
		CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setChestplate(new ItemStack(CHESTPLATE));
		 
		ItemStack LEGGINGS = new ItemStack(Material.DIAMOND_LEGGINGS);
		LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setLeggings(new ItemStack(LEGGINGS));
		 
		ItemStack BOOTS = new ItemStack(Material.DIAMOND_BOOTS);
		BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		BOOTS.addEnchantment(Enchantment.DURABILITY, 3);
		BOOTS.addEnchantment(Enchantment.PROTECTION_FALL, 4);
		p.getInventory().setBoots(new ItemStack(BOOTS));
		
		Potion strength = new Potion(PotionType.STRENGTH);
		strength.setLevel(2);
		strength.setHasExtendedDuration(true);
		
		Potion speed = new Potion(PotionType.SPEED);
		speed.setLevel(2);
		speed.setHasExtendedDuration(true);
		
		ItemStack speed_item = speed.toItemStack(1);
		
		p.getInventory().addItem(sword);
		p.getInventory().addItem(gapple);
		p.getInventory().addItem(HELMET);
		p.getInventory().addItem(CHESTPLATE);
		p.getInventory().addItem(LEGGINGS);
		p.getInventory().addItem(BOOTS);
		p.getInventory().setItem(7, speed_item);
		p.getInventory().setItem(34, speed_item);
		
		p.updateInventory();
	}
	
	
	public static void giveGappleKit(Player p) {
		p.getInventory().clear();
		p.updateInventory();
		
		SpeedItem sword = new SpeedItem(Material.DIAMOND_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
		
		ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE);
		gapple.setDurability((short)1);
		gapple.setAmount(64);
		
		ItemStack HELMET = new ItemStack(Material.DIAMOND_HELMET);
		HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		HELMET.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setHelmet(new ItemStack(HELMET));
		 
		ItemStack CHESTPLATE = new ItemStack(Material.DIAMOND_CHESTPLATE);
		CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setChestplate(new ItemStack(CHESTPLATE));
		 
		ItemStack LEGGINGS = new ItemStack(Material.DIAMOND_LEGGINGS);
		LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setLeggings(new ItemStack(LEGGINGS));
		 
		ItemStack BOOTS = new ItemStack(Material.DIAMOND_BOOTS);
		BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
		BOOTS.addEnchantment(Enchantment.DURABILITY, 3);
		BOOTS.addEnchantment(Enchantment.PROTECTION_FALL, 4);
		p.getInventory().setBoots(new ItemStack(BOOTS));
		
		Potion strength = new Potion(PotionType.STRENGTH);
		strength.setLevel(2);
		strength.setHasExtendedDuration(true);
		
		ItemStack force_item = strength.toItemStack(1);
		
		Potion speed = new Potion(PotionType.SPEED);
		speed.setLevel(2);
		speed.setHasExtendedDuration(true);
		
		ItemStack speed_item = speed.toItemStack(1);
		
		p.getInventory().addItem(sword);
		p.getInventory().addItem(gapple);
		p.getInventory().addItem(HELMET);
		p.getInventory().addItem(CHESTPLATE);
		p.getInventory().addItem(LEGGINGS);
		p.getInventory().addItem(BOOTS);
		p.getInventory().setItem(7, speed_item);
		p.getInventory().setItem(8, force_item);
		p.getInventory().setItem(34, speed_item);
		p.getInventory().setItem(35, force_item);
		
		p.updateInventory();
		p.updateInventory();
	}
	
	public static void giveDebuffKit(Player p){
		p.getInventory().clear();
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        p.getInventory().setItem(0, sword);
      	
        ItemStack HELMET = new ItemStack(Material.DIAMOND_HELMET);
		HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		HELMET.addEnchantment(Enchantment.DURABILITY, 3);
		 p.getInventory().setHelmet(new ItemStack(HELMET));
		 
		ItemStack CHESTPLATE = new ItemStack(Material.DIAMOND_CHESTPLATE);
		CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setChestplate(new ItemStack(CHESTPLATE));
		 
		ItemStack LEGGINGS = new ItemStack(Material.DIAMOND_LEGGINGS);
		LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setLeggings(new ItemStack(LEGGINGS));
		 
		ItemStack BOOTS = new ItemStack(Material.DIAMOND_BOOTS);
		BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		BOOTS.addEnchantment(Enchantment.DURABILITY, 3);
		BOOTS.addEnchantment(Enchantment.PROTECTION_FALL, 4);
		p.getInventory().setBoots(new ItemStack(BOOTS));
		 
		ItemStack heal = new ItemStack(Material.POTION);
		heal.setDurability((short)16421);
		
		ItemStack speed = new ItemStack(Material.POTION);
		speed.setDurability((short)8226);
		
		ItemStack fire = new ItemStack(Material.POTION);
		fire.setDurability((short)8259);
		
		ItemStack slow = new ItemStack(Material.POTION);
		slow.setDurability((short)16388);
		
		ItemStack poison = new ItemStack(Material.POTION);
		poison.setDurability((short)16426);
		
		ItemStack steak = new ItemStack(Material.COOKED_BEEF);
		steak.setAmount(64);
		
		ItemStack ender = new ItemStack(Material.ENDER_PEARL);
		ender.setAmount(16);
		
		p.getInventory().setItem(8, speed);
		p.getInventory().setItem(9, speed);
		p.getInventory().setItem(18, speed);
		p.getInventory().setItem(27, speed);
		
		p.getInventory().setItem(17, slow);
		p.getInventory().setItem(16, poison);
		p.getInventory().setItem(26, slow);
		p.getInventory().setItem(25, poison);
		p.getInventory().setItem(35, slow);
		p.getInventory().setItem(34, poison);
		
		p.getInventory().setItem(7, fire);
		p.getInventory().setItem(2, steak);
		p.getInventory().setItem(1, ender);
		p.getInventory().setItem(3, heal);
		p.getInventory().setItem(4, heal);
		p.getInventory().setItem(5, heal);
		p.getInventory().setItem(6, heal);
		p.getInventory().setItem(10, heal);
		p.getInventory().setItem(11, heal);
		p.getInventory().setItem(12, heal);
		p.getInventory().setItem(13, heal);
		p.getInventory().setItem(14, heal);
		p.getInventory().setItem(15, heal);
		p.getInventory().setItem(19, heal);
		p.getInventory().setItem(20, heal);
		p.getInventory().setItem(21, heal);
		p.getInventory().setItem(22, heal);
		p.getInventory().setItem(23, heal);
		p.getInventory().setItem(24, heal);
		p.getInventory().setItem(28, heal);
		p.getInventory().setItem(29, heal);
		p.getInventory().setItem(30, heal);
		p.getInventory().setItem(31, heal);
		p.getInventory().setItem(32, heal);
		p.getInventory().setItem(33, heal);
		 
		p.updateInventory();
		
	}
	
	public static void giveBuildUHCKit(Player p){
		p.getInventory().clear();
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
	     		
	    ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
	     		
	    ItemStack slovel = new ItemStack(Material.DIAMOND_SPADE);
	     		
	    ItemStack rod = new ItemStack(Material.FISHING_ROD);
	     	
	    ItemStack bow = new ItemStack(Material.BOW);
	    bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
	     		
	    ItemStack apple = new ItemStack(Material.GOLDEN_APPLE);
	    apple.setAmount(6);
	     		
	    ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
	    goldenHead.setAmount(3);
	    ItemMeta gMeta = goldenHead.getItemMeta();
	    gMeta.setDisplayName("§6Golden Head");
	    goldenHead.setItemMeta(gMeta);
	     	    
	    ItemStack arrow = new ItemStack(Material.ARROW);
	    arrow.setAmount(64);
	     		
	    ItemStack cobble = new ItemStack(Material.COBBLESTONE);
	    cobble.setAmount(64);
	     		
	    ItemStack lava = new ItemStack(Material.LAVA_BUCKET);
	     		
	    ItemStack water = new ItemStack(Material.WATER_BUCKET);
	     		
	    ItemStack steak = new ItemStack(Material.COOKED_BEEF);
	    steak.setAmount(64);
	     			
	    ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
	    helmet.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
	     		
	    ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
	    chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
	     		
	    ItemStack leggins = new ItemStack(Material.DIAMOND_LEGGINGS);
	    leggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
	     		
	    ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
	    boots.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
	     		
	    p.getInventory().setItem(0, sword);
	    p.getInventory().setItem(1, rod);
	    p.getInventory().setItem(2, bow);
	    p.getInventory().setItem(5, goldenHead);
	    p.getInventory().setItem(4, apple);
	    p.getInventory().setItem(3, water);
		p.getInventory().setItem(6, lava);
		p.getInventory().setItem(7, lava);
		p.getInventory().setItem(8, cobble);
		p.getInventory().setItem(9, arrow);
		p.getInventory().setItem(10, steak);
		p.getInventory().setItem(11, pickaxe);
		p.getInventory().setItem(12, slovel);
		
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chestplate);
		p.getInventory().setLeggings(leggins);
		p.getInventory().setBoots(boots);
		 
		p.updateInventory();	
	}
	
	public static void giveNoDebuffKit(Player p){
		p.getInventory().clear();
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        p.getInventory().setItem(0, sword);
  	
        ItemStack HELMET = new ItemStack(Material.DIAMOND_HELMET);
		HELMET.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		HELMET.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setHelmet(new ItemStack(HELMET));
		
		ItemStack CHESTPLATE = new ItemStack(Material.DIAMOND_CHESTPLATE);
		CHESTPLATE.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		CHESTPLATE.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setChestplate(new ItemStack(CHESTPLATE));
		
		ItemStack LEGGINGS = new ItemStack(Material.DIAMOND_LEGGINGS);
		LEGGINGS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		LEGGINGS.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().setLeggings(new ItemStack(LEGGINGS));
		
		ItemStack BOOTS = new ItemStack(Material.DIAMOND_BOOTS);
		BOOTS.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		BOOTS.addEnchantment(Enchantment.DURABILITY, 3);
		BOOTS.addEnchantment(Enchantment.PROTECTION_FALL, 4);
		p.getInventory().setBoots(new ItemStack(BOOTS));
		
		ItemStack heal = new ItemStack(Material.POTION);
		heal.setDurability((short)16421);
		
		ItemStack speed = new ItemStack(Material.POTION);
		speed.setDurability((short)8226);
		
		ItemStack fire = new ItemStack(Material.POTION);
		fire.setDurability((short)8259);		 
		
		ItemStack steak = new ItemStack(Material.COOKED_BEEF);
		steak.setAmount(64);
		
		ItemStack ender = new ItemStack(Material.ENDER_PEARL);
		ender.setAmount(16);
		
		p.getInventory().setItem(8, speed);
		p.getInventory().setItem(17, speed);
		p.getInventory().setItem(26, speed);
		p.getInventory().setItem(35, speed);
		p.getInventory().setItem(7, fire);
		p.getInventory().setItem(2, steak);
		p.getInventory().setItem(1, ender);
		p.getInventory().setItem(3, heal);
		p.getInventory().setItem(4, heal);
		p.getInventory().setItem(5, heal);
		p.getInventory().setItem(6, heal);
		p.getInventory().setItem(9, heal);
		p.getInventory().setItem(10, heal);
		p.getInventory().setItem(11, heal);
		p.getInventory().setItem(12, heal);
		p.getInventory().setItem(13, heal);
		p.getInventory().setItem(14, heal);
		p.getInventory().setItem(15, heal);
		p.getInventory().setItem(16, heal);
		p.getInventory().setItem(18, heal);
		p.getInventory().setItem(19, heal);
		p.getInventory().setItem(20, heal);
		p.getInventory().setItem(21, heal);
		p.getInventory().setItem(22, heal);
		p.getInventory().setItem(23, heal);
		p.getInventory().setItem(24, heal);
		p.getInventory().setItem(25, heal);
		p.getInventory().setItem(27, heal);
		p.getInventory().setItem(28, heal);
		p.getInventory().setItem(29, heal);
		p.getInventory().setItem(30, heal);
		p.getInventory().setItem(31, heal);
		p.getInventory().setItem(32, heal);
		p.getInventory().setItem(33, heal);
		p.getInventory().setItem(34, heal);
		
		p.updateInventory();
	}
}
