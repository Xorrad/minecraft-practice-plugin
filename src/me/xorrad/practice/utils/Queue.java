package me.xorrad.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.xorrad.practice.Practice;
import me.xorrad.practice.elo.EloConfig;
import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.fight.FightLadder;

public class Queue {

	public Player p;
	public FightLadder ladder;
	public BukkitTask task;
	public int cooldown;

	public BukkitTask rankedTask;
	public boolean ranked;
	public int min;
	public int max;
	
	public Queue(Player p, FightLadder ladder, boolean ranked) {
		
		this.p = p;
		this.ladder = ladder;
		this.ranked = ranked;
		
		startTask();
		searchPlayer();
	}
	
	public void searchPlayer() {
		if(ranked) {
			
			String ln = ladder.name();
			int min=(EloConfig.getElo(p, ln)-50), max=(EloConfig.getElo(p, ln)+50);
			
			p.sendMessage("§eSearching in elo range [§a"+String.valueOf(min) + " §e- §a" + String.valueOf(max) + "§e]");
			
			rankedTask = new BukkitRunnable() {
				
				int min=(EloConfig.getElo(p, ln)-50), max=(EloConfig.getElo(p, ln)+50);
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
					
					for(Player p2 : QueueManager.ranked.keySet()){
						if(EloConfig.getElo(p2, ln) >= min && EloConfig.getElo(p2, ln) <= max && p != p2){
							Player p1 = p;
							
							p1.sendMessage("§a"+ln+" §eranked match found: §a" + p1.getName() + "("+EloConfig.getElo(p1, ln)+") §evs §a"+p2.getName()+"("+EloConfig.getElo(p2, ln)+")");
							p2.sendMessage("§a"+ln+" §eranked match found: §a" + p2.getName() + "("+EloConfig.getElo(p2, ln)+") §evs §a"+p1.getName()+"("+EloConfig.getElo(p1, ln)+")");
							
							new Fight(p1, p2, ladder, true, false);
							
							QueueManager.removeFromRankedQueue(p1);
							QueueManager.removeFromRankedQueue(p2);
							this.cancel();
						}
					}
					
				}
			}.runTaskTimer(Practice.getInstance(), 20, 20);
			
		}
		return;
	}
	
	public void stopRankedTask() {
		if(rankedTask != null) {
			rankedTask.cancel();
		}
	}
	
	public void startTask() {
		task = new BukkitRunnable() {
			
			@Override
			public void run() {
				upCooldown();
			}
		}.runTaskTimer(Practice.getInstance(), 20L, 20L);
	}
	
	public void stopTask() {
		if(task != null && Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())) {
			task.cancel();
		}
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public FightLadder getLadder() {
		return ladder;
	}

	public void setLadder(FightLadder ladder) {
		this.ladder = ladder;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public void upCooldown() {
		cooldown++;
	}
	
	public void downCooldown() {
		cooldown--;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}
	
	public void upMin() {
		min += 50;
	}
	
	public void downMin() {
		min -= 50;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void upMax() {
		max += 50;
	}
	
	public void downMax() {
		max -= 50;
	}
	
	public boolean isRanked() {
		return ranked;
	}

	public void setRanked(boolean ranked) {
		this.ranked = ranked;
	}

	public BukkitTask getTask() {
		return task;
	}

	public BukkitTask getRankedTask() {
		return rankedTask;
	}
	
}
