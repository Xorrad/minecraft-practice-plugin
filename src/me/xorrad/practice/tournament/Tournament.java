package me.xorrad.practice.tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.xorrad.practice.Practice;
import me.xorrad.practice.arena.Arena;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.utils.JsonBuilder;
import me.xorrad.practice.utils.JsonBuilder.ClickAction;
import me.xorrad.practice.utils.JsonBuilder.HoverAction;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.kits.Kits;
import me.xorrad.practice.utils.kits.SpawnItem;

public class Tournament {
	
	public static HashMap<Integer, Tournament> tournaments;
	
	static 
	{
		tournaments = new HashMap<>();
	}
	
	public HashMap<Player, Boolean> players; //Player, Qualifed
	public ArrayList<Player> specs;
	public ArrayList<Player> invited;
	public int round;
	public Integer arenaId;
	public Random ran;
	public boolean open;
	public Integer tournamentId;
	public boolean started;
	public boolean waiting;
	public boolean end;
	public BukkitTask startTask;
	public Player leader;
	
	public Player p1, p2;
	public boolean inFight;
	
	public FightLadder ladder;
	
	public Tournament(Player leader, FightLadder ladder, boolean open) 
	{
		this.players = new HashMap<>();
		this.specs = new ArrayList<>();
		this.invited = new ArrayList<>();
		this.round = 1;
		this.ladder = ladder;
		this.open = open;
		this.ran = new Random();
		this.tournamentId = leader.getEntityId();
		this.started = false;
		this.waiting = false;
		this.end = false;
		this.leader = leader;
		this.inFight = false;
		
		Arena a = Arena.getTournamentArena(ladder);
		if(a == null)
		{
			leader.sendMessage("§cNo arena found!");
			return;
		}
		this.arenaId = a.getID();
		Arena.getArenaByID(this.arenaId).setUsed(true);
		
		clearInventory(leader);
		leader.sendMessage("§eTournament succefully created!");
		User.getPlayer(leader).setTournamentId(tournamentId);
		tournaments.put(tournamentId, this);
		players.put(leader, false);
		leader.teleport(Arena.getArenaByID(this.arenaId).getLobby());
		
		if(open)
		{
			annonceTournament();
		}
	}
	
	public void invitePlayer(Player p)
	{
		this.invited.add(p);
		
		JsonBuilder message = new JsonBuilder(new String[0]).withText("[Tournament] ").withColor("§6").withText(leader.getName() + " has invited you in ")
		.withColor("§e").withText(ladder.name()).withColor("§a").withText(" tournament !").withColor("§e").withText(" JOIN").withColor("§a")
		.withClickEvent(ClickAction.RUN_COMMAND, "/tournament join " + leader.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to join");
		message.sendJson(p);
	}
	
	public void addSpec(Player p)
	{
		User.getPlayer(p).setTournamentId(tournamentId);
		specs.add(p);
		Kits.giveTournamentItem(p);
		p.teleport(Arena.getArenaByID(arenaId).getLobby());
		clearInventory(p);
		p.sendMessage("§eYou spectate §a" + leader.getName() + "'s §etournament.");
		broadcast("§a" + p.getName() + " §ehas join the tournament !");
	}
	
	public void joinTournament(Player p)
	{
		User.getPlayer(p).setTournamentId(tournamentId);
		players.put(p, false);
		Kits.giveTournamentItem(p);
		p.teleport(Arena.getArenaByID(arenaId).getLobby());
		clearInventory(p);
		p.sendMessage("§eYou have join §a" + leader.getName() + "'s §etournament.");
		broadcast("§a" + p.getName() + " §ehas join the tournament !");
	}
	
	public void quitTournament(Player p)
	{
		showPlayer(p);
		User.getPlayer(p).setTournamentId(null);
		players.remove(p);
		specs.remove(p);
		SpawnItem.giveSpawnItem(p);
		p.teleport(Practice.getInstance().getSpawn());
		p.sendMessage("§eYou have quit §a" + leader.getName() + "'s §etournament.");
		broadcast("§a" + p.getName() + " §ehas quit the tournament !");
	}
	
	@SuppressWarnings("deprecation")
	public void annonceTournament()
	{
		for(Player pls : Bukkit.getOnlinePlayers())
		{
			if(!this.players.containsKey(pls) && !this.specs.contains(pls))
			{
				JsonBuilder message = new JsonBuilder(new String[0]).withText("[Tournament] ").withColor("§6").withText(leader.getName() + " has created ")
				.withColor("§e").withText(ladder.name()).withColor("§a").withText(" tournament !").withColor("§e").withText(" JOIN").withColor("§a")
				.withClickEvent(ClickAction.RUN_COMMAND, "/tournament join " + leader.getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to join");
				message.sendJson(pls);
			}
		}
	}

	public void startTournament() 
	{
		started = true;
		broadcast("§eRound §a" + this.round + " §estart !");
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ArrayList<Player> nextPlayers = getNextPlayers();			
				if(nextPlayers.size() >= 1)
				{
					startFight(nextPlayers.get(0), nextPlayers.get(1));
				}
				
				this.cancel();
			}
		}.runTaskTimer(Practice.getInstance(), 60, 60); //3 SECONDS
	}
	
	public void nextFight(Player winner, Player loser)
	{
		inFight = false;
		broadcast("§a" + winner.getName() + " §ewin this duel!");
		
		players.put(winner, true);
		clearInventory(winner);
		
		hidePlayer(loser);
		showSpecPlayer(loser);
		loser.setHealth(20.0D);
		players.remove(loser); //SET LOSER TO SPEC
		specs.add(loser);
		Kits.giveTournamentItem(loser);
		
		winner.teleport(Arena.getArenaByID(arenaId).getLobby());
		loser.teleport(Arena.getArenaByID(arenaId).getLobby());
		
		if(isNextFight())
		{
			//NEXT FIGHT
			ArrayList<Player> nextPlayers = getNextPlayers();			
			
			if(nextPlayers.size() >= 2)
			{
				startFight(nextPlayers.get(0), nextPlayers.get(1));
			}
			else //If only 1 player left
			{
				players.put(nextPlayers.get(0), true);
				nextRound();
			}
		}
		else
		{
			nextRound();
		}
	}
	
	public void nextRound()
	{
		if(isFinishedTournament())
		{
			broadcast("§k§5§ka§9§ka§2§ka§4§ka §a" + getWinner().getName() + " §e win the tournament §k§4§ka§2§ka§9§ka§5§ka");
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					cancelTournament();
					this.cancel();
				}
			}.runTaskTimer(Practice.getInstance(), 120, 120); //6 SECONDS
		}
		else
		{
			this.round++;
			broadcast("§eRound §a" + this.round + " §estart !");
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					for(Player pls : players.keySet())
					{
						players.put(pls, false);
					}
					
					ArrayList<Player> nextPlayers = getNextPlayers();			
					startFight(nextPlayers.get(0), nextPlayers.get(1));
					
					this.cancel();
				}
			}.runTaskTimer(Practice.getInstance(), 60, 60); //3 SECONDS
		}
	}
	
	public void stopTournament()
	{
		
	}

	public void cancelTournament()
	{
		for(Player pls : players.keySet())
		{
			showPlayer(pls);
			User.getPlayer(pls).setTournamentId(null);
			SpawnItem.giveSpawnItem(pls);
			pls.teleport(Practice.getInstance().getSpawn());
		}
		for(Player pls : specs)
		{
			showPlayer(pls);
			User.getPlayer(pls).setTournamentId(null);
			SpawnItem.giveSpawnItem(pls);
			pls.teleport(Practice.getInstance().getSpawn());
		}
		Arena.getArenaByID(this.arenaId).setUsed(false);
		tournaments.remove(tournamentId);
	}
	
	public void startFight(Player p1, Player p2)
	{
		this.p1 = p1;
		this.p2 = p2;
		
		broadcast("§a" + p1.getName() + " §evs §a" + p2.getName());
		
		waiting = true;
		inFight = true;
		
		Kits.giveKitBook(p1, ladder);
		Kits.giveKitBook(p2, ladder);
		
		p1.teleport(Arena.getArenaByID(arenaId).getSpawn1());
		p2.teleport(Arena.getArenaByID(arenaId).getSpawn2());
		
		p1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, false));
		p2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, false));
		
		startTask = new BukkitRunnable() {
			
			int time = 3;
			
			@Override
			public void run() {
				if(time==0){
					p1.removePotionEffect(PotionEffectType.SLOW);
					p1.sendMessage("§aDuel starting now!");
					p2.removePotionEffect(PotionEffectType.SLOW);
					p2.sendMessage("§aDuel starting now!");
					started = true;
					waiting = false;
					this.cancel();
				} else {
					p1.sendMessage("§aStarting in §e" + time + " §aseconds!");
					p2.sendMessage("§aStarting in §e" + time + " §aseconds!");
				}
				
				time--;
			}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);
	}
	
	public boolean isFinishedTournament()
	{
		return (players.size() == 1) ? true : false; 
	}
	
	public Player getWinner()
	{
		for(Player pls : players.keySet())
		{
			return pls;
		}
		return null;
	}
	
	public boolean isNextFight()
	{
		for(boolean value : players.values())
		{
			if(!value)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Player> getNextPlayers()
	{
		ArrayList<Player> nextPlayers = new ArrayList<>();
		
		for(Player pls : players.keySet())
		{
			if(players.get(pls) == false)
			{
				nextPlayers.add(pls);
			}
		}
		
		return nextPlayers;
	}
	
	public void broadcast(String message)
	{
		for(Player pls : players.keySet())
		{
			pls.sendMessage(message);
		}
		for(Player pls : specs)
		{
			pls.sendMessage(message);
		}
	}
	
	public Player getLeader()
	{
		return this.leader;
	}
	
	public boolean isStarted()
	{
		return this.started;
	}
	
	public boolean isEnd()
	{
		return this.end;
	}
	
	public boolean isWaiting()
	{
		return this.waiting;
	}
	
	public boolean isOpen()
	{
		return this.open;
	}
	
	public void setOpen(boolean open)
	{
		this.open = open;
	}
	
	public void clearInventory(Player p)
	{
		p.setFireTicks(0);
		p.setMaximumNoDamageTicks(20);
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().clear();
		p.updateInventory();
	}
	
	public static HashMap<Integer, Tournament> getTournaments()
	{
		return tournaments;
	}
	
	public static Tournament getTournament(Integer id)
	{
		return tournaments.get(id);
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public Integer getTournamentId() {
		return tournamentId;
	}

	public void setTournamentId(Integer tournamentId) {
		this.tournamentId = tournamentId;
	}

	public Player getP1() {
		return p1;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public Player getP2() {
		return p2;
	}

	public void setP2(Player p2) {
		this.p2 = p2;
	}

	public boolean isInFight() {
		return inFight;
	}

	public void setInFight(boolean inFight) {
		this.inFight = inFight;
	}

	public FightLadder getLadder() {
		return ladder;
	}

	public void setLadder(FightLadder ladder) {
		this.ladder = ladder;
	}

	public HashMap<Player, Boolean> getPlayers() {
		return players;
	}

	public ArrayList<Player> getSpecs() {
		return specs;
	}

	public Integer getArenaId() {
		return arenaId;
	}

	public Random getRan() {
		return ran;
	}

	public BukkitTask getStartTask() {
		return startTask;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public void setLeader(Player leader) {
		this.leader = leader;
	}
	
	public boolean isInFight(Player p)
	{
		return (p == p1 || p == p2) ? true : false;
	}
	
	public void hidePlayer(Player p)
	{
		for(Player pls : players.keySet())
		{
			pls.hidePlayer(p);
		}
	}
	
	public void showSpecPlayer(Player p)
	{
		for(Player pls : specs)
		{
			pls.showPlayer(p);
		}
	}
	
	public void showPlayer(Player p)
	{
		for(Player pls : players.keySet())
		{
			pls.showPlayer(p);
		}
		for(Player pls : specs)
		{
			pls.showPlayer(p);
		}
	}
	
}
