package me.xorrad.practice.fight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import me.xorrad.practice.Practice;
import me.xorrad.practice.arena.Arena;
import me.xorrad.practice.elo.EloSys;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.InvUtils;
import me.xorrad.practice.utils.JsonBuilder;
import me.xorrad.practice.utils.JsonBuilder.ClickAction;
import me.xorrad.practice.utils.JsonBuilder.HoverAction;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.gui.Gui;
import me.xorrad.practice.utils.kits.Kits;
import me.xorrad.practice.utils.kits.SpawnItem;

public class Fight {
	
	private static HashMap<Integer, Fight> allFights;
	
	static {
		allFights = new HashMap<>();
	}
	
	private Player p1;
	private Player p2;
	private FightLadder mode;
	
	private boolean team;
	private ArrayList<Player> team1;
	private ArrayList<Player> team2;
	
	private ArrayList<Player> specs;
	
	private BukkitTask endTask;
	private BukkitTask startTask;
	private BukkitTask fightRunnable;
	private int fightTime;

	private Integer fightId;
	private Integer ArenaID;
	private boolean ranked;
	private boolean started = false;
	private boolean end = false;
	
	private boolean duel;
	private boolean ffa;
	
	public Fight(Player p1, Player p2, FightLadder ladder, boolean ranked, boolean duel){
		this.p1 = p1;
		this.p2 = p2;
		this.mode = ladder;
		this.ranked = ranked;
		this.fightTime = 0;
		
		this.specs = new ArrayList<>();
		this.fightId = p1.getEntityId();
		User.getPlayer(p1).setFightId(fightId);
		User.getPlayer(p2).setFightId(fightId);
		allFights.put(fightId, this);
		
		if(!duel) {
			if(!ranked) {
				if(isUnrankedTeam(this)) {
					Gui.update2v2Gui();
				} else {
					Gui.updateUnrankedGui();
				}
			} else {
				Gui.updateRankedGui();
			}
		}
		
		setFightTags(p1, p2);
		setFightTags(p2, p1);
		
		startFight();
	}
	
	public Fight(Player p1, Player p2, FightLadder ladder, boolean ranked, boolean duel, ArrayList<Player> team1, ArrayList<Player> team2){ //2V2 UNRANKED
		this.p1 = p1;
		this.p2 = p2;
		this.mode = ladder;
		this.fightTime = 0;
		
		this.team = true;
		this.team1 = team1;
		this.team2 = team2;
		
		this.specs = new ArrayList<>();
		this.fightId = p1.getEntityId();
		for(Player t1 : team1) {
			User.getPlayer(t1).setFightId(fightId);
		}
		for(Player t2 : team2) {
			User.getPlayer(t2).setFightId(fightId);
		}
		setFightTeamTags(team1, team2);
		allFights.put(fightId, this);
		startFight();
	}
	
	public Fight(Player p1, Player p2, FightLadder ladder, boolean duel, ArrayList<Player> team1, ArrayList<Player> team2){
		this.p1 = p1;
		this.p2 = p2;
		this.mode = ladder;
		this.fightTime = 0;
		
		this.team = true;
		this.team1 = team1;
		this.team2 = team2;
		
		this.specs = new ArrayList<>();
		this.fightId = p1.getEntityId();
		for(Player t1 : team1) {
			User.getPlayer(t1).setFightId(fightId);
		}
		for(Player t2 : team2) {
			User.getPlayer(t2).setFightId(fightId);
		}
		
		setFightTeamTags(team1, team2);
		
		allFights.put(fightId, this);	
		
		startFight();
	}
	
	public Fight(Player p1, Player p2, FightLadder ladder, boolean ffa, ArrayList<Player> players){
		this.p1 = p1;
		this.p2 = p2;
		this.mode = ladder;
		this.fightTime = 0;
		
		this.team = true;
		this.ffa = ffa;
		this.team1 = players;
		this.team2 = new ArrayList<>();
		
		this.specs = new ArrayList<>();
		this.fightId = p1.getEntityId();
		for(Player t1 : team1) {
			User.getPlayer(t1).setFightId(fightId);
			
			for(Player t11 : team1) {
				setFightTags(t1, t11);
			}
		}
		allFights.put(fightId, this);
		startFight();
	}
	
	public void setFightTags(Player p1, Player p2) {
        Scoreboard battletag = Bukkit.getScoreboardManager().getNewScoreboard();
        battletag.registerNewObjective("battle", "dummy");
        org.bukkit.scoreboard.Team red = battletag.registerNewTeam("red");
        red.setPrefix(ChatColor.RED.toString());
        org.bukkit.scoreboard.Team green = battletag.registerNewTeam("green");
        green.setPrefix(ChatColor.GREEN.toString());
        if (p1 != null && p2 != null) {
            green.addEntry(p1.getName());
            red.addEntry(p2.getName());
        }
        p1.setScoreboard(battletag);
    }
	
	public void setFightTeamTags(ArrayList<Player> team1, ArrayList<Player> team2) {
        Scoreboard battletag = Bukkit.getScoreboardManager().getNewScoreboard();
        battletag.registerNewObjective("battle", "dummy");
        org.bukkit.scoreboard.Team green = battletag.registerNewTeam("green");
        green.setPrefix(ChatColor.GREEN.toString());
        
        org.bukkit.scoreboard.Team red = battletag.registerNewTeam("red");
        red.setPrefix(ChatColor.RED.toString());
        
        for(Player pls : team1)
        {
            for(Player pls2 : team1)
            {
            	green.addEntry(pls2.getName());
            }
            for(Player pls3 : team2)
            {
            	red.addEntry(pls3.getName());
            }
            pls.setScoreboard(battletag);
        }
        
        battletag = Bukkit.getScoreboardManager().getNewScoreboard();
        battletag.registerNewObjective("battle", "dummy");
        green = battletag.registerNewTeam("green");
        green.setPrefix(ChatColor.GREEN.toString());
        
        red = battletag.registerNewTeam("red");
        red.setPrefix(ChatColor.RED.toString());
        
        for(Player pls : team2)
        {
            for(Player pls2 : team2)
            {
            	green.addEntry(pls2.getName());
            }
            for(Player pls3 : team1)
            {
            	red.addEntry(pls3.getName());
            }
            pls.setScoreboard(battletag);
        }
    }
	
	public void startFight(){
		
		if(!team) {
		
			fightRunnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					fightTime++;
				}
			}.runTaskTimer(Practice.getInstance(), 20, 20);
			
			Kits.giveKitBook(p1, mode);
			Kits.giveKitBook(p2, mode);
			
			Arena a = Arena.getArena(mode);
			
			if(a == null){
				p1.sendMessage("§cNo arena found");
				p2.sendMessage("§cNo arena found");
				
				SpawnItem.giveSpawnItem(p1);
				SpawnItem.giveSpawnItem(p2);
				
				p1.teleport(Practice.getInstance().getSpawn());
				p2.teleport(Practice.getInstance().getSpawn());
				
				cancelFight();
				return;
			}
			ArenaID = a.getID();
			getArena().setUsed(true);

			Location spawn1 = getArena().getSpawn1();
			Location spawn2 = getArena().getSpawn2();
			p1.teleport(spawn1);
			p2.teleport(spawn2);
			
			p1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, false));
			p2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, false));
			
			startTask = new BukkitRunnable() {
				
				int time = 5;
				
				@Override
				public void run() {
					if(time==0){
						p1.removePotionEffect(PotionEffectType.SLOW);
						p1.sendMessage("§aDuel starting now!");
						p2.removePotionEffect(PotionEffectType.SLOW);
						p2.sendMessage("§aDuel starting now!");
						started = true;
						this.cancel();
					} else {
						p1.sendMessage("§aStarting in §e" + time + " §aseconds!");
						p2.sendMessage("§aStarting in §e" + time + " §aseconds!");
					}
					
					time--;
				}
			}.runTaskTimer(Practice.getInstance(), 20L, 20L);
			
			fightRunnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					fightTime++;
				}
			}.runTaskTimer(Practice.getInstance(), 20L, 20L);
				
		} else if(ffa) { /********************************************FFA************************************************************************/
			
			fightRunnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					fightTime++;
				}
			}.runTaskTimer(Practice.getInstance(), 20, 20);
			
			for(Player t1 : team1) {
				Kits.giveKitBook(t1, mode);
			}
			
			Arena a = Arena.getArena(mode);
			
			if(a == null){
				for(Player t1 : team1) {
					t1.sendMessage("§cNo arena found");
					SpawnItem.giveSpawnItem(t1);
					t1.teleport(Practice.getInstance().getSpawn());
				}
				
				cancelFight();
				return;
			}
			ArenaID = a.getID();
			getArena().setUsed(true);

			Location spawn1 = getArena().getSpawn1();
			Location spawn2 = getArena().getSpawn2();
			
			Random ran = new Random();
			Location l = (ran.nextBoolean()) ? spawn1 : spawn2;
			
			for(Player t1 : team1) {
				
				/*double minX, maxX, minZ, maxZ;
				double x, z;
				
				if(spawn1.getX() <= spawn2.getX()) {
					minX = spawn1.getX();
					maxX = spawn2.getX();
				} else {
					minX = spawn2.getX();
					maxX = spawn1.getX();
				}
				
				if(spawn1.getZ() <= spawn2.getZ()) {
					minZ = spawn1.getZ();
					maxZ = spawn2.getZ();
				} else {
					minZ = spawn2.getZ();
					maxZ = spawn1.getZ();
				}
					
				x = ran.nextInt(((int)maxX - (int)minX) + 1) +(int) minX;
				z = ran.nextInt(((int)maxZ - (int)minZ) + 1) + (int)minZ;
				
				int centerx = (int) ((maxX + minX) / 2);
	            int centerz = (int) ((maxZ + minZ) / 2);
				Location center = new Location(spawn1.getWorld(), centerx, spawn1.getWorld().getHighestBlockYAt(centerx, centerz), centerz);
				
				float yaw = getAngle(new Vector(x, 0, z), center.toVector());
				Location l = new Location(spawn1.getWorld(), x, 0, z, (int)yaw, 0);
				int maxy = l.getWorld().getHighestBlockYAt(l);
				l.setY(maxy);*/
				
				t1.teleport(l);
				t1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, false));
			}
			
			startTask = new BukkitRunnable() {
				
				int time = 5;
				
				@Override
				public void run() {
					if(time==0){
						for(Player t1 : team1) {
							if(isInTeamInTheFight(t1)) {
								t1.removePotionEffect(PotionEffectType.SLOW);
								t1.sendMessage("§aDuel starting now!");
							}
						}
						for(Player t1 : team1) {
							for(Player t2 : team1) {
								t1.showPlayer(t2);
							}
						}
						started = true;
						this.cancel();
					} else {
						for(Player t1 : team1) {
							if(isInTeamInTheFight(t1)) {
								t1.sendMessage("§aStarting in §e" + time + " §aseconds!");
							}
						}
					}
					
					time--;
				}
			}.runTaskTimer(Practice.getInstance(), 20L, 20L);
			
			fightRunnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					fightTime++;
				}
			}.runTaskTimer(Practice.getInstance(), 20L, 20L);
			
		} else { /********************************************TEAM************************************************************************/
			
			fightRunnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					fightTime++;
				}
			}.runTaskTimer(Practice.getInstance(), 20, 20);
			
			for(Player t1 : team1) {
				Kits.giveKitBook(t1, mode);
			}
			for(Player t2 : team2) {
				Kits.giveKitBook(t2, mode);
			}
			
			Arena a = Arena.getArena(mode);
			
			if(a == null){
				for(Player t1 : team1) {
					t1.sendMessage("§cNo arena found");
					SpawnItem.giveSpawnItem(t1);
					t1.teleport(Practice.getInstance().getSpawn());
				}
				for(Player t2 : team2) {
					t2.sendMessage("§cNo arena found");
					SpawnItem.giveSpawnItem(t2);
					t2.teleport(Practice.getInstance().getSpawn());
				}
				
				cancelFight();
				return;
			}
			ArenaID = a.getID();
			getArena().setUsed(true);

			Location spawn1 = getArena().getSpawn1();
			Location spawn2 = getArena().getSpawn2();
			for(Player t1 : team1) {
				t1.teleport(spawn1);
				t1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, false));
			}
			for(Player t2 : team2) {
				t2.teleport(spawn2);
				t2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, false));
			}
			
			startTask = new BukkitRunnable() {
				
				int time = 5;
				
				@Override
				public void run() {
					if(time==0){
						for(Player t1 : team1) {
							if(isInTeamInTheFight(t1)) {
								t1.removePotionEffect(PotionEffectType.SLOW);
								t1.sendMessage("§aDuel starting now!");
							}
						}
						for(Player t2 : team2) {
							if(isInTeamInTheFight(t2)) {
								t2.removePotionEffect(PotionEffectType.SLOW);
								t2.sendMessage("§aDuel starting now!");
							}
						}
						for(Player t1 : team1) {
							for(Player t2 : team2) {
								t1.showPlayer(t2);
							}
						}
						for(Player t2 : team2) {
							for(Player t1 : team1) {
								t2.showPlayer(t1);
							}
						}
						started = true;
						this.cancel();
					} else {
						for(Player t1 : team1) {
							if(isInTeamInTheFight(t1)) {
								t1.sendMessage("§aStarting in §e" + time + " §aseconds!");
							}
						}
						for(Player t2 : team2) {
							if(isInTeamInTheFight(t2)) {
								t2.sendMessage("§aStarting in §e" + time + " §aseconds!");
							}
						}
					}
					
					time--;
				}
			}.runTaskTimer(Practice.getInstance(), 20L, 20L);
			
			fightRunnable = new BukkitRunnable() {
				
				@Override
				public void run() {
					fightTime++;
				}
			}.runTaskTimer(Practice.getInstance(), 20L, 20L);
			
		}
		
	} 
	
	public void stopFight(Player winner, Player loser, boolean quit){
		end = true;
		
		if(!team) {
			InvUtils.saveInv(winner);
	        InvUtils.saveInv(loser);
	        if(!started){
				startTask.cancel();
				end = true;
			}
			if(quit){
		        winner.setMaximumNoDamageTicks(20);
		        
				/*winner.getInventory().setHelmet(new ItemStack(Material.AIR));
				winner.getInventory().setChestplate(new ItemStack(Material.AIR));
				winner.getInventory().setLeggings(new ItemStack(Material.AIR));
				winner.getInventory().setBoots(new ItemStack(Material.AIR));
				winner.getInventory().clear();
				winner.updateInventory();*/
				
				for(Player spec : specs){
		        	if(spec != null){
			        	spec.sendMessage("§eWinner: §a" + winner.getName());
			        	removeSpec(spec, true);
		        	}
		        }
		        specs.clear();
		        
		        winner.sendMessage("§eWinner: §a" + winner.getName());
		        
		        if(ranked){
		        	EloSys.replaceElo(winner, loser, mode.name());
		        }
		        
		        JsonBuilder message = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.GOLD).withText(winner.getName() + ", ").withColor(ChatColor.YELLOW).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/inv " + winner.getName()).withText(loser.getName()).withColor(ChatColor.YELLOW).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/inv " + loser.getName());
		        message.sendJson(winner);
				
				endTask = new BukkitRunnable() {
					
					@Override
					public void run() {
						winner.showPlayer(loser);
						winner.setExp(0.0f);
				        winner.setLevel(0);
				        winner.setHealth(20.0);
				        winner.setFoodLevel(20);
				        winner.setSaturation(20.0f);
				        
				        if(!ranked && User.getPlayer(winner).getUnrankedLeft() > 0) {
					        User.getPlayer(winner).downUnrakedLeft();
					        Practice.getInstance().player_infos.set(winner.getUniqueId().toString() + "." + "Unranked-Left",  User.getPlayer(winner).getUnrankedLeft());
					        try {
								Practice.getInstance().player_infos.save(Practice.getInstance().player_infos_file);
							} catch (IOException e) {
								e.printStackTrace();
							}
				        }
				        
				        if(User.getPlayer(winner).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(winner).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(winner).getEnderPearlTimer().cancel();
				        }
				        User.getPlayer(winner).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
				        Practice.getInstance().clearEffect(winner);
				        SpawnItem.giveSpawnItem(winner);
				        
				        cancelFight();
				        this.cancel();
					}
				}.runTaskTimer(Practice.getInstance(), 60, 0);
		        
			} else {
		        winner.setMaximumNoDamageTicks(20);
				loser.setMaximumNoDamageTicks(20);
		        
				/*loser.setHealth(20);
				loser.setFoodLevel(20);
				loser.setExp(0.0f);
				loser.setFoodLevel(20);
				loser.setSaturation(20.0f);
				Practice.getInstance().clearEffect(loser);
				loser.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 1));
				loser.setGameMode(GameMode.CREATIVE);*/
				
				loser.getInventory().setHelmet(new ItemStack(Material.AIR));
				loser.getInventory().setChestplate(new ItemStack(Material.AIR));
				loser.getInventory().setLeggings(new ItemStack(Material.AIR));
				loser.getInventory().setBoots(new ItemStack(Material.AIR));
				loser.getInventory().clear();
				loser.updateInventory();
				
				winner.hidePlayer(loser);
				/*winner.getInventory().setHelmet(new ItemStack(Material.AIR));
				winner.getInventory().setChestplate(new ItemStack(Material.AIR));
				winner.getInventory().setLeggings(new ItemStack(Material.AIR));
				winner.getInventory().setBoots(new ItemStack(Material.AIR));
				winner.getInventory().clear();
				winner.updateInventory();*/
				
				Practice.getInstance().clearEffect(loser);
		        loser.setGameMode(GameMode.SURVIVAL);
		        loser.setFireTicks(0);
		        
				for(Player spec : specs){
		        	if(spec != null){
			        	spec.sendMessage("§eWinner: §a" + winner.getName());
			        	removeSpec(spec, true);
		        	}
		        }
		        specs.clear();
		        
		        winner.sendMessage("§eWinner: §a" + winner.getName());
	        	loser.sendMessage("§eWinner: §a" + winner.getName());
		        
		        if(ranked){
		        	 EloSys.replaceElo(winner, loser, mode.name());
		        }
		        
		        
		        JsonBuilder message = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.GOLD).withText(winner.getName() + ", ").withColor(ChatColor.YELLOW).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/inv " + winner.getName()).withText(loser.getName()).withColor(ChatColor.YELLOW).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/inv " + loser.getName());
		        message.sendJson(winner);
		        
		        JsonBuilder message2 = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.GOLD).withText(winner.getName() + ", ").withColor(ChatColor.YELLOW).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/inv " + winner.getName()).withText(loser.getName()).withColor(ChatColor.YELLOW).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/inv " + loser.getName());
		        message2.sendJson(loser);
				
				endTask = new BukkitRunnable() {
					
					@Override
					public void run() {
						winner.showPlayer(loser);
						winner.setExp(0.0f);
				        winner.setLevel(0);
				        winner.setHealth(20.0);
				        winner.setFoodLevel(20);
				        winner.setSaturation(20.0f);
				        
				        if(!ranked &&  User.getPlayer(winner).getUnrankedLeft() > 0) {
					        User.getPlayer(winner).downUnrakedLeft();
					        Practice.getInstance().player_infos.set(winner.getUniqueId().toString() + "." + "Unranked-Left",  User.getPlayer(winner).getUnrankedLeft());
					        try {
								Practice.getInstance().player_infos.save(Practice.getInstance().player_infos_file);
							} catch (IOException e) {
								e.printStackTrace();
							}
				        }
				        
				        if(!ranked &&  User.getPlayer(loser).getUnrankedLeft() > 0) {
					        User.getPlayer(loser).downUnrakedLeft();
					        Practice.getInstance().player_infos.set(loser.getUniqueId().toString() + "." + "Unranked-Left",  User.getPlayer(loser).getUnrankedLeft());
					        try {
								Practice.getInstance().player_infos.save(Practice.getInstance().player_infos_file);
							} catch (IOException e) {
								e.printStackTrace();
							}
				        }
				        
				        if(User.getPlayer(winner).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(winner).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(winner).getEnderPearlTimer().cancel();
				        }
				        if(User.getPlayer(loser).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(loser).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(loser).getEnderPearlTimer().cancel();
				        }
				        
				        User.getPlayer(winner).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
				        User.getPlayer(loser).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
				        Practice.getInstance().clearEffect(winner);
				        
				        cancelFight();
				        this.cancel();
					}
				}.runTaskTimer(Practice.getInstance(), 60, 0);
			}
		}  
	}
	
	@SuppressWarnings({ "deprecation" })
	public void stopFight(Player winner, Player loser){
		end = true;
		
		if(!started){
			startTask.cancel();
		}
		
		if(!ffa) {
			if(team1.contains(winner)) { //TEAM1 IS THE WINNER
				
				for(Player t1 : team1) {
					for(Player t2 : team2) {
						t1.hidePlayer(t2);
					}
					if(!InvUtils.invs.containsKey(t1.getName())){
						InvUtils.saveInv(t1);
					}
					/*if(isInTeamInTheFight(t1)) {
						t1.setMaximumNoDamageTicks(20);
						t1.getInventory().setHelmet(new ItemStack(Material.AIR));
						t1.getInventory().setChestplate(new ItemStack(Material.AIR));
						t1.getInventory().setLeggings(new ItemStack(Material.AIR));
						t1.getInventory().setBoots(new ItemStack(Material.AIR));
						t1.getInventory().clear();
						t1.updateInventory();
					}*/
				}
				
				for(Player t2 : team2) {
					if(!InvUtils.invs.containsKey(t2.getName())){
						InvUtils.saveInv(t2);
					}
					if(isInTeamInTheFight(t2)) {
						t2.setFireTicks(0);
						t2.setMaximumNoDamageTicks(20);
						t2.setHealth(20);
						t2.setFoodLevel(20);
						t2.setExp(0.0f);
						t2.setFoodLevel(20);
						t2.setSaturation(20.0f);
						Practice.getInstance().clearEffect(t2);
						//t2.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 1));
						t2.setGameMode(GameMode.CREATIVE);
						
						t2.getInventory().setHelmet(new ItemStack(Material.AIR));
						t2.getInventory().setChestplate(new ItemStack(Material.AIR));
						t2.getInventory().setLeggings(new ItemStack(Material.AIR));
						t2.getInventory().setBoots(new ItemStack(Material.AIR));
						t2.getInventory().clear();
						t2.updateInventory();
					}
				}
				
				for(Player t1 : team1) {
					if(isInTeamInTheFight(t1)) {
						t1.setFireTicks(0);
						t1.setExp(0.0f);
						t1.setLevel(0);
						t1.setHealth(20.0);
						t1.setFoodLevel(20);
						t1.setSaturation(20.0f);
						
						if(User.getPlayer(t1).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(t1).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(t1).getEnderPearlTimer().cancel();
				        }
						
						User.getPlayer(t1).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
						Practice.getInstance().clearEffect(t1);
						
						t1.sendMessage("§eWinner: §a" + team1.get(0).getName() + "'s team");
					}
				}
				
				for(Player t2 : team2) {
					if(isInTeamInTheFight(t2)) {
						Practice.getInstance().clearEffect(t2);
						t2.setGameMode(GameMode.SURVIVAL);
						
				        if(User.getPlayer(t2).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(t2).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(t2).getEnderPearlTimer().cancel();
				        }
				        
				        User.getPlayer(t2).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
				        
			        	t2.sendMessage("§eWinner: §a" + team1.get(0).getName() + "'s team");
					}
				}
				
				for(Player t1 : team1) {
					JsonBuilder message = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.YELLOW);
					
					for(Player t11 : team1) {
						message.withText(t11.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t11.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
					}
					int c = 1;
					for(Player t22 : team2) {
						if(c==team2.size()) {
							message.withText(t22.getName()).withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t22.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						} else {
							message.withText(t22.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t22.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						}
						c++;
					}
					
					if(isInTeamInTheFight(t1)) {
						message.sendJson(t1);
					}
				}
				for(Player t2 : team2) {
					JsonBuilder message = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.YELLOW);
					
					for(Player t11 : team1) {
						message.withText(t11.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t11.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
					}
					int c = 1;
					for(Player t22 : team2) {
						if(c==team2.size()) {
							message.withText(t22.getName()).withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t22.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						} else {
							message.withText(t22.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t22.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						}
						c++;
					}
					
					if(isInTeamInTheFight(t2)) {
						message.sendJson(t2);
					}
				}
				
		        
		        for(Player spec : specs){
		        	if(spec != null){
			        	spec.sendMessage("§eWinner: §a" + team1.get(0).getName() + "'s team");
			        	removeSpec(spec, true);
		        	}
		        }
		        specs.clear();
				
				endTask = new BukkitRunnable() {
					
					@Override
					public void run() {
						
						for(Player t1 : team1) {
							for(Player t2 : team2) {
								t1.showPlayer(t2);
							}
							for(Player t11 : team1) {
								t1.showPlayer(t11);
							}
						}
						
				        
				        cancelFight();
				        this.cancel();
					}
				}.runTaskTimer(Practice.getInstance(), 60, 0);
			} else { /**********************************************TEAM2 IS THE WINNER**********************************************************************************************/
				for(Player t2 : team2) {
					for(Player t1 : team1) {
						t2.hidePlayer(t1);
					}
					if(!InvUtils.invs.containsKey(t2.getName())){
						InvUtils.saveInv(t2);
					}
					/*if(isInTeamInTheFight(t2)) {
						t2.setFireTicks(0);
						t2.setMaximumNoDamageTicks(20);
						t2.getInventory().setHelmet(new ItemStack(Material.AIR));
						t2.getInventory().setChestplate(new ItemStack(Material.AIR));
						t2.getInventory().setLeggings(new ItemStack(Material.AIR));
						t2.getInventory().setBoots(new ItemStack(Material.AIR));
						t2.getInventory().clear();
						t2.updateInventory();
					}*/
				}
				
				for(Player t1 : team1) {
					if(!InvUtils.invs.containsKey(t1.getName())){
						InvUtils.saveInv(t1);
					}
					if(isInTeamInTheFight(t1)) {
						t1.setFireTicks(0);
						t1.setMaximumNoDamageTicks(20);
						t1.setHealth(20);
						t1.setFoodLevel(20);
						t1.setExp(0.0f);
						t1.setFoodLevel(20);
						t1.setSaturation(20.0f);
						Practice.getInstance().clearEffect(t1);
						//t1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 1));
						t1.setGameMode(GameMode.CREATIVE);
						
						t1.getInventory().setHelmet(new ItemStack(Material.AIR));
						t1.getInventory().setChestplate(new ItemStack(Material.AIR));
						t1.getInventory().setLeggings(new ItemStack(Material.AIR));
						t1.getInventory().setBoots(new ItemStack(Material.AIR));
						t1.getInventory().clear();
						t1.updateInventory();
					}
				}
				
				for(Player t2 : team2) {
					
					if(isInTeamInTheFight(t2)) {
						t2.setExp(0.0f);
						t2.setLevel(0);
						t2.setHealth(20.0);
						t2.setFoodLevel(20);
						t2.setSaturation(20.0f);
						
						if(User.getPlayer(t2).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(t2).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(t2).getEnderPearlTimer().cancel();
				        }
						User.getPlayer(t2).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
						
						t2.sendMessage("§eWinner: §a" + team2.get(0).getName() + "'s team");
					}
				}
				
				for(Player t1 : team1) {
					if(isInTeamInTheFight(t1)) {
						Practice.getInstance().clearEffect(t1);
						t1.setGameMode(GameMode.SURVIVAL);
						
						if(User.getPlayer(t1).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(t1).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(t1).getEnderPearlTimer().cancel();
				        }
				        
				        User.getPlayer(t1).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
				        Practice.getInstance().clearEffect(t1);
				        
			        	t1.sendMessage("§eWinner: §a" + team2.get(0).getName() + "'s team");
					}
				}
				
				//SEND THE INVENTORY MESSAGE
				for(Player t1 : team1) {
					JsonBuilder message = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.YELLOW);
					
					for(Player t2 : team2) {
						message.withText(t2.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t2.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
					}
					int c = 1;
					for(Player t11 : team1) {
						if(c==team1.size()) {
							message.withText(t11.getName()).withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t11.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						} else {
							message.withText(t11.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t11.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						}
						c++;
					}
					if(isInTeamInTheFight(t1)) {
						message.sendJson(t1);
					}
				}
				for(Player t2 : team2) {
					JsonBuilder message = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.YELLOW);
					
					for(Player t22 : team2) {
						message.withText(t22.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t22.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
					}
					int c = 1;
					for(Player t11 : team1) {
						if(c==team1.size()) {
							message.withText(t11.getName()).withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t11.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						} else {
							message.withText(t11.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t11.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
						}
						c++;
					}
					
					if(isInTeamInTheFight(t2)) {
						message.sendJson(t2);
					}
				}
		        
		        for(Player spec : specs){
		        	if(spec != null){
			        	spec.sendMessage("§eWinner: §a" + team2.get(0).getName() + "'s team");
			        	removeSpec(spec, true);
		        	}
		        }
		        specs.clear();
				
				endTask = new BukkitRunnable() {
					
					@Override
					public void run() {
						
						for(Player t2 : team2) {
							for(Player t1 : team1) {
								t2.showPlayer(t1);
							}
							for(Player t22 : team2) {
								t2.showPlayer(t22);
							}
						}
				        
				        cancelFight();
				        this.cancel();
					}
				}.runTaskTimer(Practice.getInstance(), 60, 0);
			}
		} else {
								
			InvUtils.saveInv(winner);
			/*winner.setMaximumNoDamageTicks(20);
			winner.getInventory().setHelmet(new ItemStack(Material.AIR));
			winner.getInventory().setChestplate(new ItemStack(Material.AIR));
			winner.getInventory().setLeggings(new ItemStack(Material.AIR));
			winner.getInventory().setBoots(new ItemStack(Material.AIR));
			winner.getInventory().clear();
			winner.updateInventory();
			winner.setHealth(20.0D);
			winner.setFoodLevel(20);
			winner.setFireTicks(0);*/
			
			for(Player t1 : team1) {
				if(t1 != winner) {
					winner.hidePlayer(t1);
					if(!InvUtils.invs.containsKey(t1.getName())){
						InvUtils.saveInv(t1);
					}
					if(isInTeamInTheFight(t1)) {
						Practice.getInstance().clearEffect(t1);
						
						t1.getInventory().setHelmet(new ItemStack(Material.AIR));
						t1.getInventory().setChestplate(new ItemStack(Material.AIR));
						t1.getInventory().setLeggings(new ItemStack(Material.AIR));
						t1.getInventory().setBoots(new ItemStack(Material.AIR));
						t1.getInventory().clear();
						t1.updateInventory();
						
						t1.setExp(0.0f);
						t1.setLevel(0);
						t1.setHealth(20.0);
						t1.setFoodLevel(20);
						t1.setSaturation(20.0f);
						t1.setFireTicks(0);
						t1.setGameMode(GameMode.SURVIVAL);
						
						if(User.getPlayer(t1).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(t1).getEnderPearlTimer().getTaskId())){
				        	User.getPlayer(t1).getEnderPearlTimer().cancel();
				        }
						
						User.getPlayer(t1).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
						Practice.getInstance().clearEffect(t1);
					}
				}
				
				t1.sendMessage("§eWinner: §a" + winner.getName());
			}
			
			for(Player t1 : team1) {
				JsonBuilder message = new JsonBuilder(new String[0]).withText("Inventories (click to view): ").withColor(ChatColor.YELLOW);
				
				for(Player t11 : team1) {
					message.withText(t11.getName() + ", ").withClickEvent(ClickAction.RUN_COMMAND, "/inv " + t11.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to view");
				}
				
				if(isInTeamInTheFight(t1)) {
					message.sendJson(t1);
				}
			}
			
	        
	        for(Player spec : specs){
	        	if(spec != null){
		        	spec.sendMessage("§eWinner: §a" + team1.get(0).getName() + "'s team");
		        	removeSpec(spec, true);
	        	}
	        }
	        specs.clear();
	        
			endTask = new BukkitRunnable() {
				
				@Override
				public void run() {
					
					for(Player t1 : team1) {
						winner.showPlayer(t1);
					}
			        
			        cancelFight();
			        this.cancel();
				}
			}.runTaskTimer(Practice.getInstance(), 60, 0);
		}
	}
	
	public void cancelFight() {
		if(ArenaID != null)
		{
			if(mode.equals(FightLadder.BuildUHC))
			{
				Arena.getArenaByID(ArenaID).deleteBlocks();
			}
		}
		
		if(!team) {
			if(!p1.getWorld().getName().equals(Practice.getInstance().getSpawn().getWorld().getName()) || p1.getWorld().getName().equals(Practice.getInstance().getSpawn().getWorld().getName()) && p1.getLocation().distanceSquared(Practice.getInstance().getSpawn()) >= 10)
			{
				User.getPlayer(p1).setFightId(null);
				SpawnItem.giveSpawnItem(p1);
				Practice.getInstance().clearEffect(p1);
				p1.teleport(Practice.getInstance().getSpawn());
				p1.setFireTicks(0);
				User.getPlayer(p1).removeFightTag();
			}
			
			if(!p2.getWorld().getName().equals(Practice.getInstance().getSpawn().getWorld().getName()) || p2.getWorld().getName().equals(Practice.getInstance().getSpawn().getWorld().getName()) && p2.getLocation().distanceSquared(Practice.getInstance().getSpawn()) >= 10)
			{
				User.getPlayer(p2).setFightId(null);
				SpawnItem.giveSpawnItem(p2);
				Practice.getInstance().clearEffect(p2);
				p2.teleport(Practice.getInstance().getSpawn());
				p2.setFireTicks(0);
				User.getPlayer(p2).removeFightTag();
			}
			
			if(ArenaID != null){
				//Arena.getArenaID(ArenaID).deleteBlock();
				Arena.getArenaByID(ArenaID).setUsed(false);
			}
			
			fightRunnable.cancel();
			removeFight();
			
			if(ranked) {
				Gui.updateRankedGui();
			} else if(!ranked && !duel) {
				Gui.updateUnrankedGui();
			}
			
		} else if(ffa) { /***************************************FFA********************************************************************/
			for(Player t1 : team1) {
				if(isInTeamInTheFight(t1)) {
					User.getPlayer(t1).setFightId(null);
					t1.setAllowFlight(false);
					t1.setFlying(false);
					
					User.getPlayer(t1).removeFightTag();
	
					if(User.getPlayer(t1).getTeamId() == null) {
						SpawnItem.giveSpawnItem(t1);
					} else {
						if(Team.teams.get(User.getPlayer(t1).getTeamId()).getLeader() == t1) {
							Kits.giveTeamLeaderItem(t1);
						} else {
							Kits.giveTeamItem(t1);
						}
					}
					
					Practice.getInstance().clearEffect(t1);
					t1.teleport(Practice.getInstance().getSpawn());
					t1.setFireTicks(0);
					User.getPlayer(t1).setDeath(false);
				}
			}
			
			for(Player t1 : team1) {
				for(Player t2 : team1) {
					t1.showPlayer(t2);
				}
			}
			
			if(ArenaID != null){
				//Arena.getArenaID(ArenaID).deleteBlock();
				Arena.getArenaByID(ArenaID).setUsed(false);
			}
			
			fightRunnable.cancel();
			removeFight();
		} else {  /***************************************TEAM********************************************************************/
			for(Player t1 : team1) {
				if(isInTeamInTheFight(t1)) {
					User.getPlayer(t1).setFightId(null);
					t1.setAllowFlight(false);
					t1.setFlying(false);
					
					User.getPlayer(t1).removeFightTag();
	
					if(User.getPlayer(t1).getTeamId() == null) {
						SpawnItem.giveSpawnItem(t1);
					} else {
						if(Team.teams.get(User.getPlayer(t1).getTeamId()).getLeader() == t1) {
							Kits.giveTeamLeaderItem(t1);
						} else {
							Kits.giveTeamItem(t1);
						}
					}
					
					Practice.getInstance().clearEffect(t1);
					t1.teleport(Practice.getInstance().getSpawn());
					t1.setFireTicks(0);
					User.getPlayer(t1).setDeath(false);
				}
			}
			for(Player t2 : team2) {
				if(isInTeamInTheFight(t2)) {
					User.getPlayer(t2).setFightId(null);
					t2.setAllowFlight(false);
					t2.setFlying(false);
					
					User.getPlayer(t2).removeFightTag();
					
					if(User.getPlayer(t2).getTeamId() == null) {
						SpawnItem.giveSpawnItem(t2);
					} else {
						if(Team.teams.get(User.getPlayer(t2).getTeamId()).getLeader() == t2) {
							Kits.giveTeamLeaderItem(t2);
						} else {
							Kits.giveTeamItem(t2);
						}
					}
					
					Practice.getInstance().clearEffect(t2);
					t2.teleport(Practice.getInstance().getSpawn());
					t2.setFireTicks(0);
					User.getPlayer(t2).setDeath(false);
				}
			}
			
			for(Player t1 : team1) {
				for(Player t2 : team2) {
					t1.showPlayer(t2);
				}
			}
			
			for(Player t2 : team2) {
				for(Player t1 : team1) {
					t2.showPlayer(t1);
				}
			}
			
			if(ArenaID != null){
				//Arena.getArenaID(ArenaID).deleteBlock();
				Arena.getArenaByID(ArenaID).setUsed(false);
			}
			
			fightRunnable.cancel();
			removeFight();
		}
	}
	
	public boolean is1v1Fight() {
		if(!team && !ffa) {
			return true;
		}
		return false;
	}
	
	public static boolean isUnrankedTeam(Fight f) {
		
		if((Boolean)f.ranked != null && f.team && !f.duel) {
			return true;
		}
		
		return false;
	}
	
	public boolean isTeam1(Player p) {
		if(team1.contains(p)) {
			return true;
		}
		return false;
	}
	public boolean isTeam2(Player p) {
		if(team2.contains(p)) {
			return true;
		}
		return false;
	}

	public void removeFight(){
		Fight.allFights.remove(fightId);
	}
	
	public boolean isInTeamInTheFight(Player p) {
		
		if(User.getPlayer(p).getTeamId() != null && team1.contains(Team.getTeams().get(User.getPlayer(p).getTeamId()).getLeader()) || User.getPlayer(p).getTeamId() != null && team2.contains(Team.getTeams().get(User.getPlayer(p).getTeamId()).getLeader())) {
			return true;
		}
		
		return false;
	}
	
	public boolean isEndFFA() {
		int c = 0;
		for(Player pls : team1) {
			if(!User.getPlayer(pls).isDeath() && isInTeamInTheFight(pls)) {
				c++;
			}
		}
		
		return (c<=1) ? true : false;
	}
	
	public Player getWinner() {
		for(Player pls : team1) {
			if(!User.getPlayer(pls).isDeath() && isInTeamInTheFight(pls)) {
				return pls;
			}
		}
		return null;
	}
	
	public Arena getArena(){
		return Arena.getArenaByID(this.ArenaID);
	}
	
	public static HashMap<Integer, Fight> getAllFights() {
		return allFights;
	}

	public Player getP1() {
		return p1;
	}

	public Player getP2() {
		return p2;
	}

	public FightLadder getMode() {
		return mode;
	}
	
	public BukkitTask getEndTask() {
		return endTask;
	}

	public BukkitTask getStartTask() {
		return startTask;
	}

	public Integer getArenaID() {
		return ArenaID;
	}

	public boolean isRanked() {
		return ranked;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public BukkitTask getFightRunnable() {
		return fightRunnable;
	}

	public int getFightTime() {
		return fightTime;
	}

	public void setFightTime(int fightTime) {
		this.fightTime = fightTime;
	}
	
	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
	
	public boolean isTeam() {
		return team;
	}

	public void setTeam(boolean team) {
		this.team = team;
	}
	
	public boolean isTeam1Defeated() {
		if(!team2.isEmpty()) {
			for(Player t1 : team1) {
				if(!User.getPlayer(t1).isDeath()){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isTeam2Defeated() {
		if(!team2.isEmpty()) {
			for(Player t2 : team2) {
				if(!User.getPlayer(t2).isDeath()){
					return false;
				}
			}
		}
		return true;
	}

	public ArrayList<Player> getTeam1() {
		return team1;
	}

	public ArrayList<Player> getTeam2() {
		return team2;
	}
	
	public static int getRankedFights(FightLadder ladder) {
		int count = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(ladder) && f.ranked && !f.duel){
				count++;
			}
		}
		return count;
	}
	
	public static int get2v2Fights(FightLadder ladder) {
		int count = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(ladder) && isUnrankedTeam(f)){
				count++;
			}
		}
		return count;
	}
	
	public static int getFights(FightLadder ladder) {
		int count = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(ladder) && !f.ranked && !f.duel){
				count++;
			}
		}
		return count;
	}

	public static int getBowFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Bow) && !f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getDebuffUnrankedTeamFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Debuff) && isUnrankedTeam(f)){
				number++;
			}
		}
		return number;
	}
	
	public static int getNoDebuffUnrankedTeamFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.NoDebuff) && isUnrankedTeam(f)){
				number++;
			}
		}
		return number;
	}
	
	public static int getBowUnrankedTeamFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Bow) && isUnrankedTeam(f)){
				number++;
			}
		}
		return number;
	}
	
	public static int getGappleUnrankedTeamFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Gapple) && isUnrankedTeam(f)){
				number++;
			}
		}
		return number;
	}
	
	public static int getAxeUnrankedTeamFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Axe) && isUnrankedTeam(f)){
				number++;
			}
		}
		return number;
	}
	
	public static int getSoupUnrankedTeamFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Soup) && isUnrankedTeam(f)){
				number++;
			}
		}
		return number;
	}
	
	public static int getDebuffFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Debuff) && !f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getNoDebuffFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.NoDebuff) && !f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getGappleFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Gapple) && !f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getAxeFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Axe) && !f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getSoupFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Soup) && !f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getRankedBowFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Bow) && f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getRankedDebuffFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Debuff) && f.ranked && !f.duel){
				number++;
			}
		}
		return number;
	}
	
	public static int getRankedNoDebuffFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.NoDebuff) && f.ranked){
				number++;
			}
		}
		return number;
	}
	
	public static int getRankedGappleFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Gapple) && f.ranked){
				number++;
			}
		}
		return number;
	}
	
	public static int getRankedAxeFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Axe) && f.ranked){
				number++;
			}
		}
		return number;
	}
	
	public static int getRankedSoupFights(){
		int number = 0;
		
		for(Fight f : allFights.values()){
			if(f.mode.equals(FightLadder.Soup) && f.ranked){
				number++;
			}
		}
		return number;
	}

	public boolean isDuel() {
		return duel;
	}

	public void setDuel(boolean duel) {
		this.duel = duel;
	}

	public ArrayList<Player> getSpecs() {
		return specs;
	}
	
	public void addSpec(Player spec){
		specs.add(spec);
		User.getPlayer(spec).setSpectate(true);
		
		p1.hidePlayer(spec);
		p2.hidePlayer(spec);
		
		spec.teleport(p1.getLocation());
		spec.sendMessage("§eYou spectate §a" + p1.getName() + " §evs §a" + p2.getName());
		spec.setGameMode(GameMode.CREATIVE);
		spec.setAllowFlight(true);
		spec.setFlying(true);
		Kits.giveSpecItem(spec);
	}
	
	public void removeSpec(Player spec, boolean end){
		
		if(!end){
			specs.remove(spec);
		}
		User.getPlayer(spec).setSpectate(false);
		
		p1.showPlayer(spec);
		p2.showPlayer(spec);
		
		if(Practice.getInstance().getSpawn() != null){
        	spec.teleport(Practice.getInstance().getSpawn());
        }
		if(end){
			spec.sendMessage("§eIt the end of the fight !");
		} else {
			spec.sendMessage("§eYou stop watching §a" + p1.getName() + " §evs §a" + p2.getName());
		}
		spec.setAllowFlight(false);
		spec.setFlying(false);
		spec.setGameMode(GameMode.SURVIVAL);
		SpawnItem.giveSpawnItem(spec);
	}
	
	public static Integer getFightWatching(Player spec){
		for(Integer id : allFights.keySet()){
			if(id != null && allFights.get(id).specs.contains(spec)){
				return id;
			}
		}
		
		return null;
	}

	public boolean isFFa() {
		return ffa;
	}

	public void setFFa(boolean ffa) {
		this.ffa = ffa;
	}
	
	@SuppressWarnings("unused")
	private float getAngle(Vector point1, Vector point2) {
        double dx = point2.getX() - point1.getX();
        double dz = point2.getZ() - point1.getZ();
        float angle = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
        if (angle < 0) {
            angle += 360.0F;
        }
        return angle;
	}

	public Integer getFightId() {
		return fightId;
	}

	public void setFightId(Integer fightId) {
		this.fightId = fightId;
	}
}