package me.xorrad.practice.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.xorrad.practice.Practice;
import me.xorrad.practice.elo.EloConfig;
import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.gui.Gui;
import me.xorrad.practice.utils.kits.Kits;
import me.xorrad.practice.utils.kits.SpawnItem;

public class QueueManager {
	
	public static QueueManager instance;
	
	public static ArrayList<Player> bow_unranked;
	public static ArrayList<Player> debuff_unranked;
	public static ArrayList<Player> nodebuff_unranked;
	public static ArrayList<Player> gapple_unranked;
	public static ArrayList<Player> axe_unranked;
	public static ArrayList<Player> soup_unranked;
	
	public static ArrayList<Player> bow_ranked;
	public static ArrayList<Player> debuff_ranked;
	public static ArrayList<Player> nodebuff_ranked;
	public static ArrayList<Player> gapple_ranked;
	public static ArrayList<Player> axe_ranked;
	public static ArrayList<Player> soup_ranked;
	
	public static ArrayList<Player> bow_2v2;
	public static ArrayList<Player> debuff_2v2;
	public static ArrayList<Player> nodebuff_2v2;
	public static ArrayList<Player> gapple_2v2;
	public static ArrayList<Player> axe_2v2;
	public static ArrayList<Player> soup_2v2;
	
	public static HashMap<Player, BukkitTask> queueRunnable;
	
	public static HashMap<Player, Queue> unranked;
	public static HashMap<Player, Queue> unranked2v2;
	public static HashMap<Player, Queue> ranked;
	
	public QueueManager() {
		instance = this;
	}
	
	static {
		bow_unranked = new ArrayList<>();
		debuff_unranked = new ArrayList<>();
		nodebuff_unranked = new ArrayList<>();
		gapple_unranked = new ArrayList<>();
		axe_unranked = new ArrayList<>();
		soup_unranked = new ArrayList<>();
		
		bow_ranked = new ArrayList<>();
		debuff_ranked = new ArrayList<>();
		nodebuff_ranked = new ArrayList<>();
		gapple_ranked = new ArrayList<>();
		axe_ranked = new ArrayList<>();
		soup_ranked = new ArrayList<>();
		
		bow_2v2 = new ArrayList<>();
		debuff_2v2 = new ArrayList<>();
		nodebuff_2v2 = new ArrayList<>();
		gapple_2v2 = new ArrayList<>();
		axe_2v2 = new ArrayList<>();
		soup_2v2 = new ArrayList<>();
		
		queueRunnable = new HashMap<>();
		
		unranked = new HashMap<>();
		unranked2v2 = new HashMap<>();
		ranked = new HashMap<>();
	}
	
	public static Player unrankedFirstPlayer(FightLadder ladder) {
		for(Queue q : unranked.values()) {
			if(q.ladder.equals(ladder)) {
				return q.p;
			}
		}
		return null;
	}
	
	public static Player unranked2v2FirstPlayer(FightLadder ladder) {
		for(Queue q : unranked2v2.values()) {
			if(q.ladder.equals(ladder)) {
				return q.p;
			}
		}
		return null;
	}
	
	public static int unrankedQueueSize(FightLadder ladder) {
		int count = 0;
		
		for(Queue q : unranked.values()) {
			if(q.ladder.equals(ladder)) {
				count++;
			}
		}
		return count;
	}
	
	public static int unranked2v2QueueSize(FightLadder ladder) {
		int count = 0;
		
		for(Queue q : unranked2v2.values()) {
			if(q.ladder.equals(ladder)) {
				count++;
			}
		}
		return count;
	}
	
	public static int rankedQueueSize(FightLadder ladder) {
		int count = 0;
		
		for(Queue q : ranked.values()) {
			if(q.ladder.equals(ladder)) {
				count++;
			}
		}
		return count;
	}
	
	public static boolean isInUnrankedQueue(Player p) {
		if(unranked.containsKey(p)) {
			return true;
		}
		return false;
	}
	
	public static boolean isInUnranked2v2Queue(Player p) {
		if(unranked2v2.containsKey(p)) {
			return true;
		}
		return false;
	}
	
	public static boolean isInRankedQueue(Player p) {
		if(ranked.containsKey(p)) {
			return true;
		}
		return false;
	}
	
	public static void addToQueue(Player p, FightLadder ladder) {
		Kits.giveQueueItem(p);
		p.sendMessage("§eAdded to the §a" + ladder.name() + " §equeue, please wait another player.");
		unranked.put(p, new Queue(p, ladder, false));
		Gui.updateUnrankedGui();
	}
	
	public static void addTo2v2Queue(Player p, FightLadder ladder) {
		Kits.giveQueueItem(p);
		p.sendMessage("§eAdded to the §a" + ladder.name() + " §equeue, please wait another player.");
		unranked2v2.put(p, new Queue(p, ladder, false));
		Gui.update2v2Gui();
	}
	
	public static void addToRankedQueue(Player p, FightLadder ladder) {
		Kits.giveQueueItem(p);
		p.sendMessage("§eAdded to the §a" + ladder.name() + " §eranked queue, please wait another player.");
		ranked.put(p, new Queue(p, ladder, true));
		Gui.updateRankedGui();
	}
	
	public static void removeFromUnrankedQueue(Player p){
		Queue q = unranked.get(p);
		
		SpawnItem.giveSpawnItem(p);
		p.sendMessage("§eRemoved from the un-ranked §a"+q.getLadder().name()+" §equeue.");
		
		q.stopTask();
		unranked.remove(p);
		
		Gui.updateUnrankedGui();
	}
	
	public static void removeFromUnranked2v2Queue(Player p){
		Queue q = unranked2v2.get(p);
		
		Kits.giveTeamLeaderItem(p);
		if(User.getPlayer(p).getTeamId() != null) {
			for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
				pls.sendMessage("§eRemoved from the 2v2 §a"+q.getLadder().name()+" §equeue.");
			}
		} else {
			p.sendMessage("§eRemoved from the 2v2 §a"+q.getLadder().name()+" §equeue.");
		}
		
		q.stopTask();
		unranked2v2.remove(p);
		
		Gui.update2v2Gui();
	}
	
	public static void removeFromRankedQueue(Player p){
		Queue q = ranked.get(p);
		
		/*SpawnItem.giveSpawnItem(p);
		p.sendMessage("§eRemoved from the ranked §a"+q.getLadder().name()+" §equeue.");*/
		
		q.stopTask();
		q.stopRankedTask();
		ranked.remove(p);
		
		Gui.updateRankedGui();
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * OLD CODE
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static void removeFromQueue(Player p) {
		
		if(bow_unranked.contains(p)) {
			Gui.updateUnrankedGui();
			p.sendMessage("§eRemoved from the un-ranked §aBow queue.");
			bow_unranked.remove(p);
		}
		if(debuff_unranked.contains(p)) {
			Gui.updateUnrankedGui();
			p.sendMessage("§eRemoved from the un-ranked §aDebuff queue.");
			debuff_unranked.remove(p);
		}
		if(nodebuff_unranked.contains(p)) {
			Gui.updateUnrankedGui();
			p.sendMessage("§eRemoved from the un-ranked §aNoDebuff queue.");
			nodebuff_unranked.remove(p);
		}
		if(gapple_unranked.contains(p)) {
			Gui.updateUnrankedGui();
			p.sendMessage("§eRemoved from the un-ranked §aGapple queue.");
			gapple_unranked.remove(p);
		}
		if(axe_unranked.contains(p)) {
			Gui.updateUnrankedGui();
			p.sendMessage("§eRemoved from the un-ranked §aAxe queue.");
			axe_unranked.remove(p);
		}
		if(soup_unranked.contains(p)) {
			Gui.updateUnrankedGui();
			p.sendMessage("§eRemoved from the un-ranked §aSoup queue.");
			soup_unranked.remove(p);
		}
		
		if(bow_2v2.contains(p)) {
			Gui.update2v2Gui();
			Kits.giveTeamLeaderItem(p);
			
			if(User.getPlayer(p).getTeamId() != null) {
				for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
					pls.sendMessage("§eRemoved from the 2v2 §aBow queue.");
				}
			} else {
				p.sendMessage("§eRemoved from the 2v2 §aBow queue.");
			}
			
			bow_2v2.remove(p);
		}
		if(debuff_2v2.contains(p)) {
			Gui.update2v2Gui();
			Kits.giveTeamLeaderItem(p);
			if(User.getPlayer(p).getTeamId() != null) {
				for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
					pls.sendMessage("§eRemoved from the 2v2 §aDebuff queue.");
				}
			} else {
				p.sendMessage("§eRemoved from the 2v2 §aDebuff queue.");
			}
			debuff_2v2.remove(p);
		}
		if(nodebuff_2v2.contains(p)) {
			Gui.update2v2Gui();
			Kits.giveTeamLeaderItem(p);
			if(User.getPlayer(p).getTeamId() != null) {
				for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
					pls.sendMessage("§eRemoved from the 2v2 §aNoDebuff queue.");
				}
			} else {
				p.sendMessage("§eRemoved from the 2v2 §aNoDebuff queue.");
			}
			nodebuff_2v2.remove(p);
		}
		if(gapple_2v2.contains(p)) {
			Gui.update2v2Gui();
			Kits.giveTeamLeaderItem(p);
			if(User.getPlayer(p).getTeamId() != null) {
				for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
					pls.sendMessage("§eRemoved from the 2v2 §aGapple queue.");
				}
			} else {
				p.sendMessage("§eRemoved from the 2v2 §aGapple queue.");
			}
			gapple_2v2.remove(p);
		}
		if(axe_2v2.contains(p)) {
			Gui.update2v2Gui();
			Kits.giveTeamLeaderItem(p);
			if(User.getPlayer(p).getTeamId() != null) {
				for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
					pls.sendMessage("§eRemoved from the 2v2 §aAxe queue.");
				}
			} else {
				p.sendMessage("§eRemoved from the 2v2 §aAxe queue.");
			}
			axe_2v2.remove(p);
		}
		if(soup_2v2.contains(p)) {
			Gui.update2v2Gui();
			Kits.giveTeamLeaderItem(p);
			if(User.getPlayer(p).getTeamId() != null) {
				for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
					pls.sendMessage("§eRemoved from the 2v2 §aSoup queue.");
				}
			} else {
				p.sendMessage("§eRemoved from the 2v2 §aSoup queue.");
			}
			soup_2v2.remove(p);
		}
		
		if(bow_ranked.contains(p)) {
			Gui.updateRankedGui();
			p.sendMessage("§eRemoved from the ranked §aBow queue.");
			bow_ranked.remove(p);
		}
		if(debuff_ranked.contains(p)) {
			Gui.updateRankedGui();
			p.sendMessage("§eRemoved from the ranked §aDebuff queue.");
			debuff_ranked.remove(p);
		}
		if(nodebuff_ranked.contains(p)) {
			Gui.updateRankedGui();
			p.sendMessage("§eRemoved from the ranked §aNoDebuff queue.");
			nodebuff_ranked.remove(p);
		}
		if(gapple_ranked.contains(p)) {
			Gui.updateRankedGui();
			p.sendMessage("§eRemoved from the ranked §aGapple queue.");
			gapple_ranked.remove(p);
		}
		if(axe_ranked.contains(p)) {
			Gui.updateRankedGui();
			p.sendMessage("§eRemoved from the ranked §aAxe queue.");
			axe_ranked.remove(p);
		}
		if(soup_ranked.contains(p)) {
			Gui.updateRankedGui();
			p.sendMessage("§eRemoved from the ranked §aSoup queue.");
			soup_ranked.remove(p);
		}
		
	}
	
	/**
	 * 
	 * 
	 * UNRANKED 2v2
	 * 
	 *
	 */
	
	public static ArrayList<Player> getBow_2v2() {
		return bow_2v2;
	}

	public static ArrayList<Player> getDebuff_2v2() {
		return debuff_2v2;
	}

	public static ArrayList<Player> getNodebuff_2v2() {
		return nodebuff_2v2;
	}
	
	public static ArrayList<Player> getGapple_2v2() {
		return gapple_2v2;
	}
	
	public static ArrayList<Player> getAxe_2v2() {
		return axe_2v2;
	}
	public static ArrayList<Player> getSoup_2v2() {
		return soup_2v2;
	}
	
	public static void addToBowQueue2v2(Player p){
		
		for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
			pls.sendMessage("§eAdded to the §aBow §equeue, please wait another player.");
		}
		
		bow_2v2.add(p);
		Gui.update2v2Gui();
	}
	
	public static void addToDebuffQueue2v2(Player p){
		
		for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
			pls.sendMessage("§eAdded to the §aDebuff §equeue, please wait another player.");
		}
		
		debuff_2v2.add(p);
		Gui.update2v2Gui();
	}
	
	public static void addToNoDebuffQueue2v2(Player p){
		
		for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
			pls.sendMessage("§eAdded to the §aNoDebuff §equeue, please wait another player.");
		}
		
		nodebuff_2v2.add(p);
		Gui.update2v2Gui();
	}
	
	public static void addToGappleQueue2v2(Player p){
		
		for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
			pls.sendMessage("§eAdded to the §aGapple §equeue, please wait another player.");
		}
		
		gapple_2v2.add(p);
		Gui.update2v2Gui();
	}
	
	public static void addToAxeQueue2v2(Player p){
		
		for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
			pls.sendMessage("§eAdded to the §aAxe §equeue, please wait another player.");
		}
		
		axe_2v2.add(p);
		Gui.update2v2Gui();
	}
	
	public static void addToSoupQueue2v2(Player p){
		
		for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
			pls.sendMessage("§eAdded to the §aSoup §equeue, please wait another player.");
		}
		
		soup_2v2.add(p);
		Gui.update2v2Gui();
	}
	
	public static void removeFromBowQueue2v2(Player p){
		bow_2v2.remove(p);
		Gui.update2v2Gui();
	}
	
	public static void removeFromDebuffQueue2v2(Player p){
		debuff_2v2.remove(p);
		Gui.update2v2Gui();
	}
	
	public static void removeFromNoDebuffQueue2v2(Player p){
		nodebuff_2v2.remove(p);
		Gui.update2v2Gui();
	}

	public static void removeFromGappleQueue2v2(Player p){
		gapple_2v2.remove(p);
		Gui.update2v2Gui();
	}

	public static void removeFromAxeQueue2v2(Player p){
		axe_2v2.remove(p);
		Gui.update2v2Gui();
	}
	
	public static void removeFromSoupQueue2v2(Player p){
		soup_2v2.remove(p);
		Gui.update2v2Gui();
	}
	
	/**
	 * 
	 * 
	 * UNRANKED
	 * 
	 *
	 */

	public static ArrayList<Player> getBow_unranked() {
		return bow_unranked;
	}

	public static ArrayList<Player> getDebuff_unranked() {
		return debuff_unranked;
	}

	public static ArrayList<Player> getNodebuff_unranked() {
		return nodebuff_unranked;
	}
	
	public static ArrayList<Player> getGapple_unranked() {
		return gapple_unranked;
	}
	
	public static ArrayList<Player> getAxe_unranked() {
		return axe_unranked;
	}
	
	public static ArrayList<Player> getSoup_unranked() {
		return soup_unranked;
	}
	
	public static int BowSize() {
		return bow_unranked.size();
	}
	
	public static int NoDebuffSize() {
		return nodebuff_unranked.size();
	}
	
	public static int DebuffSize() {
		return debuff_unranked.size();
	}
	
	public static int GappleSize() {
		return gapple_unranked.size();
	}
	
	public static int AxeSize() {
		return axe_unranked.size();
	}
	
	public static int SoupSize() {
		return soup_unranked.size();
	}

	/*UNRANKED*/
	public static void addToBowQueueUnranked(Player p){
		bow_unranked.add(p);
		Gui.updateUnrankedGui();
	}
	
	public static void addToDebuffQueueUnranked(Player p){
		debuff_unranked.add(p);
		Gui.updateUnrankedGui();
	}
	
	public static void addToNoDebuffQueueUnranked(Player p){
		nodebuff_unranked.add(p);
		Gui.updateUnrankedGui();
	}
	
	public static void addToGappleQueueUnranked(Player p){
		gapple_unranked.add(p);
		Gui.updateUnrankedGui();
	}
	
	public static void addToAxeQueueUnranked(Player p){
		axe_unranked.add(p);
		Gui.updateUnrankedGui();
	}
	
	public static void addToSoupQueueUnranked(Player p){
		soup_unranked.add(p);
		Gui.updateUnrankedGui();
	}
	
	public static void removeFromBowQueueUnranked(Player p){
		bow_unranked.remove(p);
		Gui.updateUnrankedGui();
	}
	
	public static void removeFromDebuffQueueUnranked(Player p){
		debuff_unranked.remove(p);
		Gui.updateUnrankedGui();
	}
	
	public static void removeFromNoDebuffQueueUnranked(Player p){
		nodebuff_unranked.remove(p);
		Gui.updateUnrankedGui();
	}

	public static void removeFromGappleQueueUnranked(Player p){
		gapple_unranked.remove(p);
		Gui.updateUnrankedGui();
	}

	public static void removeFromAxeQueueUnranked(Player p){
		axe_unranked.remove(p);
		Gui.updateUnrankedGui();
	}
	
	public static void removeFromSoupQueueUnranked(Player p){
		soup_unranked.remove(p);
		Gui.updateUnrankedGui();
	}
	
	/*RANKED*/
	public static ArrayList<Player> getBow_ranked() {
		return bow_ranked;
	}
	
	public static ArrayList<Player> getDebuff_ranked() {
		return debuff_ranked;
	}

	public static ArrayList<Player> getNodebuff_ranked() {
		return nodebuff_ranked;
	}
	
	public static ArrayList<Player> getGapple_ranked() {
		return gapple_ranked;
	}
	
	public static ArrayList<Player> getAxe_ranked() {
		return axe_ranked;
	}
	
	public static ArrayList<Player> getSoup_ranked() {
		return soup_ranked;
	}
	
	public static void addToBowQueueRanked(Player p){
		bow_ranked.add(p);
		Gui.updateRankedGui();
		int min=(EloConfig.getElo(p, "Bow")-50), max=(EloConfig.getElo(p, "Bow")+50);
		p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
		queueRunnable.put(p, new BukkitRunnable() {
			
			int min=(EloConfig.getElo(p, "Bow")-50), max=(EloConfig.getElo(p, "Bow")+50);
			int time = 0;
			
			@Override
			public void run() {
				
				if(time>=5){
					time=0;
					if(min >= 50){
						min -= 50;
					}
					max += 50;
					p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
				} else {
					time++;
				}
				
				for(Player p2 : bow_ranked){
					if(EloConfig.getElo(p2, "Bow") >= min && EloConfig.getElo(p2, "Bow") <= max && p != p2){
						Player p1 = p;
						
						p1.sendMessage("§aBow §eranked match found: §a" + p1.getName() + "("+EloConfig.getElo(p1, "Bow")+") §evs §a"+p2.getName()+"("+EloConfig.getElo(p2, "Bow")+")");
						p2.sendMessage("§aBow §eranked match found: §a" + p2.getName() + "("+EloConfig.getElo(p2, "Bow")+") §evs §a"+p1.getName()+"("+EloConfig.getElo(p1, "Bow")+")");
						
						new Fight(p1, p2, FightLadder.Bow, true, false);
						
						QueueManager.removeFromBowQueueRanked(p1);
						QueueManager.removeFromBowQueueRanked(p2);
						Gui.updateRankedGui();
						return;
					}
				}
				
			}
		}.runTaskTimer(Practice.getInstance(), 20, 20));
	}
	
	public static void addToDebuffQueueRanked(Player p){
		debuff_ranked.add(p);
		Gui.updateRankedGui();
		int min=(EloConfig.getElo(p, "Debuff")-50), max=(EloConfig.getElo(p, "Debuff")+50);
		p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
		queueRunnable.put(p, new BukkitRunnable() {
			
			int min=(EloConfig.getElo(p, "Debuff")-50), max=(EloConfig.getElo(p, "Debuff")+50);
			int time = 0;
			
			@Override
			public void run() {
				
				if(time>=5){
					time=0;
					if(min >= 50){
						min -= 50;
					}
					max += 50;
					p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
				} else {
					time++;
				}
				
				for(Player p2 : debuff_ranked){
					if(EloConfig.getElo(p2, "Debuff") >= min && EloConfig.getElo(p2, "Debuff") <= max && p != p2){
						Player p1 = p;
						
						p1.sendMessage("§aDebuff §eranked match found: §a" + p1.getName() + "("+EloConfig.getElo(p1, "Debuff")+") §evs §a"+p2.getName()+"("+EloConfig.getElo(p2, "Debuff")+")");
						p2.sendMessage("§aDebuff §eranked match found: §a" + p2.getName() + "("+EloConfig.getElo(p2, "Debuff")+") §evs §a"+p1.getName()+"("+EloConfig.getElo(p1, "Debuff")+")");
						
						new Fight(p1, p2, FightLadder.Debuff, true, false);
						
						QueueManager.removeFromDebuffQueueRanked(p1);
						QueueManager.removeFromDebuffQueueRanked(p2);
						Gui.updateRankedGui();
						return;
					}
				}
				
			}
		}.runTaskTimer(Practice.getInstance(), 20, 20));
	}
	
	public static void addToNoDebuffQueueRanked(Player p){
		nodebuff_ranked.add(p);
		Gui.updateRankedGui();
		int min=(EloConfig.getElo(p, "NoDebuff")-50), max=(EloConfig.getElo(p, "NoDebuff")+50);
		p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
		queueRunnable.put(p, new BukkitRunnable() {
			
			int min=(EloConfig.getElo(p, "NoDebuff")-50), max=(EloConfig.getElo(p, "NoDebuff")+50);
			int time = 0;
			
			@Override
			public void run() {
				
				if(time>=5){
					time=0;
					if(min >= 50){
						min -= 50;
					}
					max += 50;
					p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
				} else {
					time++;
				}
				
				for(Player p2 : nodebuff_ranked){
					if(EloConfig.getElo(p2, "NoDebuff") >= min && EloConfig.getElo(p2, "NoDebuff") <= max && p != p2){
						Player p1 = p;
						
						p1.sendMessage("§aNoDebuff §eranked match found: §a" + p1.getName() + "("+EloConfig.getElo(p1, "NoDebuff")+") §evs §a"+p2.getName()+"("+EloConfig.getElo(p2, "NoDebuff")+")");
						p2.sendMessage("§aNoDebuff §eranked match found: §a" + p2.getName() + "("+EloConfig.getElo(p2, "NoDebuff")+") §evs §a"+p1.getName()+"("+EloConfig.getElo(p1, "NoDebuff")+")");
						
						new Fight(p1, p2, FightLadder.NoDebuff, true, false);
						
						QueueManager.removeFromNoDebuffQueueRanked(p1);
						QueueManager.removeFromNoDebuffQueueRanked(p2);
						Gui.updateRankedGui();
						return;
					}
				}
				
			}
		}.runTaskTimer(Practice.getInstance(), 20, 20));
	}
	
	public static void addToGappleQueueRanked(Player p){
		gapple_ranked.add(p);
		Gui.updateRankedGui();
		int min=(EloConfig.getElo(p, "Gapple")-50), max=(EloConfig.getElo(p, "Gapple")+50);
		p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
		queueRunnable.put(p, new BukkitRunnable() {
			
			int min=(EloConfig.getElo(p, "Gapple")-50), max=(EloConfig.getElo(p, "Gapple")+50);
			int time = 0;
			
			@Override
			public void run() {
				
				if(time>=5){
					time=0;
					if(min >= 50){
						min -= 50;
					}
					max += 50;
					p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
				} else {
					time++;
				}
				
				for(Player p2 : gapple_ranked){
					if(EloConfig.getElo(p2, "Gapple") >= min && EloConfig.getElo(p2, "Gapple") <= max && p != p2){
						Player p1 = p;
						
						p1.sendMessage("§aGapple §eranked match found: §a" + p1.getName() + "("+EloConfig.getElo(p1, "Gapple")+") §evs §a"+p2.getName()+"("+EloConfig.getElo(p2, "Gapple")+")");
						p2.sendMessage("§aGapple §eranked match found: §a" + p2.getName() + "("+EloConfig.getElo(p2, "Gapple")+") §evs §a"+p1.getName()+"("+EloConfig.getElo(p1, "Gapple")+")");
						
						new Fight(p1, p2, FightLadder.Gapple, true, false);
						
						QueueManager.removeFromGappleQueueRanked(p1);
						QueueManager.removeFromGappleQueueRanked(p2);
						Gui.updateRankedGui();
						return;
					}
				}
				
			}
		}.runTaskTimer(Practice.getInstance(), 20, 20));
	}
	
	public static void addToAxeQueueRanked(Player p){
		axe_ranked.add(p);
		Gui.updateRankedGui();
		int min=(EloConfig.getElo(p, "Axe")-50), max=(EloConfig.getElo(p, "Axe")+50);
		p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
		queueRunnable.put(p, new BukkitRunnable() {
			
			int min=(EloConfig.getElo(p, "Axe")-50), max=(EloConfig.getElo(p, "Axe")+50);
			int time = 0;
			
			@Override
			public void run() {
				
				if(time>=5){
					time=0;
					if(min >= 50){
						min -= 50;
					}
					max += 50;
					p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
				} else {
					time++;
				}
				
				for(Player p2 : axe_ranked){
					if(EloConfig.getElo(p2, "Axe") >= min && EloConfig.getElo(p2, "Axe") <= max && p != p2){
						Player p1 = p;
						
						p1.sendMessage("§aAxe §eranked match found: §a" + p1.getName() + "("+EloConfig.getElo(p1, "Axe")+") §evs §a"+p2.getName()+"("+EloConfig.getElo(p2, "Axe")+")");
						p2.sendMessage("§aAxe §eranked match found: §a" + p2.getName() + "("+EloConfig.getElo(p2, "Axe")+") §evs §a"+p1.getName()+"("+EloConfig.getElo(p1, "Axe")+")");
						
						new Fight(p1, p2, FightLadder.Axe, true, false);
						
						QueueManager.removeFromAxeQueueRanked(p1);
						QueueManager.removeFromAxeQueueRanked(p2);
						Gui.updateRankedGui();
						return;
					}
				}
				
			}
		}.runTaskTimer(Practice.getInstance(), 20, 20));
	}
	
	public static void addToSoupQueueRanked(Player p){
		soup_ranked.add(p);
		Gui.updateRankedGui();
		int min=(EloConfig.getElo(p, "Soup")-50), max=(EloConfig.getElo(p, "Soup")+50);
		p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
		queueRunnable.put(p, new BukkitRunnable() {
			
			int min=(EloConfig.getElo(p, "Soup")-50), max=(EloConfig.getElo(p, "Soup")+50);
			int time = 0;
			
			@Override
			public void run() {
				
				if(time>=5){
					time=0;
					if(min >= 50){
						min -= 50;
					}
					max += 50;
					p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
				} else {
					time++;
				}
				
				for(Player p2 : soup_ranked){
					if(EloConfig.getElo(p2, "Soup") >= min && EloConfig.getElo(p2, "Soup") <= max && p != p2){
						Player p1 = p;
						
						p1.sendMessage("§aSoup §eranked match found: §a" + p1.getName() + "("+EloConfig.getElo(p1, "Soup")+") §evs §a"+p2.getName()+"("+EloConfig.getElo(p2, "Soup")+")");
						p2.sendMessage("§aSoup §eranked match found: §a" + p2.getName() + "("+EloConfig.getElo(p2, "Soup")+") §evs §a"+p1.getName()+"("+EloConfig.getElo(p1, "Soup")+")");
						
						new Fight(p1, p2, FightLadder.Soup, true, false);
						
						QueueManager.removeFromSoupQueueRanked(p1);
						QueueManager.removeFromSoupQueueRanked(p2);
						Gui.updateRankedGui();
						return;
					}
				}
				
			}
		}.runTaskTimer(Practice.getInstance(), 20, 20));
	}
	
	public static void removeFromBowQueueRanked(Player p){
		if(queueRunnable.get(p) != null){
			queueRunnable.get(p).cancel();
			queueRunnable.remove(p);
		}
		bow_ranked.remove(p);
		Gui.updateRankedGui();
	}
	
	public static void removeFromDebuffQueueRanked(Player p){
		if(queueRunnable.get(p) != null){
			queueRunnable.get(p).cancel();
			queueRunnable.remove(p);
		}
		debuff_ranked.remove(p);
		Gui.updateRankedGui();
	}
	
	public static void removeFromNoDebuffQueueRanked(Player p){
		if(queueRunnable.get(p) != null){
			queueRunnable.get(p).cancel();
			queueRunnable.remove(p);
		}
		nodebuff_ranked.remove(p);
		Gui.updateRankedGui();
	}
	
	public static void removeFromGappleQueueRanked(Player p){
		if(queueRunnable.get(p) != null){
			queueRunnable.get(p).cancel();
			queueRunnable.remove(p);
		}
		gapple_ranked.remove(p);
		Gui.updateRankedGui();
	}
	
	public static void removeFromAxeQueueRanked(Player p){
		if(queueRunnable.get(p) != null){
			queueRunnable.get(p).cancel();
			queueRunnable.remove(p);
		}
		axe_ranked.remove(p);
		Gui.updateRankedGui();
	}
	
	public static void removeFromSoupQueueRanked(Player p){
		if(queueRunnable.get(p) != null){
			queueRunnable.get(p).cancel();
			queueRunnable.remove(p);
		}
		soup_ranked.remove(p);
		Gui.updateRankedGui();
	}
}
