package me.xorrad.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.QueueManager;
import me.xorrad.practice.utils.User;

public class PlayerQuit implements Listener{

	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		
		if(User.getPlayer(p).getTeamId() != null) {
			if(Team.teams.get(User.getPlayer(p).getTeamId()).getLeader() == p) {
				Team.teams.get(User.getPlayer(p).getTeamId()).disbandTeam();
			} else {
				Team.teams.get(User.getPlayer(p).getTeamId()).leaveTeam(p);
			}	
		}
		
		QueueManager.removeFromQueue(p);
		User.getAllPPlayers().remove(p);
	}
	
}
