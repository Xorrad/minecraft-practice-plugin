package me.xorrad.practice.utils;

import org.bukkit.scheduler.BukkitTask;

import me.xorrad.practice.fight.FightLadder;

public class Request {
	
	public BukkitTask requestRunnable;
	public FightLadder ladder;
	
	public Request(FightLadder ladder) {
		this.ladder = ladder;
	}

	public FightLadder getLadder() {
		return ladder;
	}

	public void setLadder(FightLadder ladder) {
		this.ladder = ladder;
	}

	public BukkitTask getRequestRunnable() {
		return requestRunnable;
	}
	
	

}
