package me.xorrad.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.xorrad.practice.Practice;
import me.xorrad.practice.listeners.DuelEvent;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.gui.Gui;

public class DuelCmd implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(args.length >= 1) {
				
				Player target = Bukkit.getPlayer(args[0]);
				
				if(target == null){
					Practice.getInstance().getMessageHelper().getMessage("offline_player");
					return true;
				}
				
				if(User.getPlayer(target).getTeamId() == null && User.getPlayer(p).getTeamId() == null) {
					if(label.equalsIgnoreCase("duel")){
						if(target == p){
							p.sendMessage("§cYou can't duel yourself!");
							return true;
						}
						
						DuelEvent.targets.put(p, target);
						Gui.openDuelGui(p);
							
					} else if(label.equalsIgnoreCase("accept")){
							
						User.getPlayer(p).acceptRequest(target);
					} else if(label.equalsIgnoreCase("decline")){
							
							User.getPlayer(p).declineRequest(target);
					}
				} else if(User.getPlayer(target).getTeamId() != null && User.getPlayer(p).getTeamId() != null) { /************************************TEAM DUEL*********************************************************************/
					if(label.equalsIgnoreCase("duel")){
							
						if(target == p){
							p.sendMessage("§cYou can't duel yourself!");
							return true;
						}
						if(User.getPlayer(target).getTeamId() == null){
							p.sendMessage("§cThis player isn't in team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(target).getTeamId()).getLeader() != target){
							p.sendMessage("§cThis player isn't the leader of his team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(p).getTeamId()).getLeader() != p){
							p.sendMessage("§cYou aren't the leader of your team!");
							return true;
						}
						
						DuelEvent.targets.put(p, target);
						Gui.openDuelGui(p);
						
					} else if(label.equalsIgnoreCase("accept")){
						if(User.getPlayer(target).getTeamId() == null){
							p.sendMessage("§cThis player isn't in team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(target).getTeamId()).getLeader() != target){
							p.sendMessage("§cThis player isn't the leader of his team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(p).getTeamId()).getLeader() != p){
							p.sendMessage("§cYou aren't the leader of your team!");
							return true;
						}
						
						Team.teams.get(User.getPlayer(p).getTeamId()).acceptRequest(target);
						
					} else if(label.equalsIgnoreCase("decline")){
						
						if(User.getPlayer(target).getTeamId() == null){
							p.sendMessage("§cThis player isn't in team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(target).getTeamId()).getLeader() != target){
							p.sendMessage("§cThis player isn't the leader of his team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(p).getTeamId()).getLeader() != p){
							p.sendMessage("§cYou aren't the leader of your team!");
							return true;
						}
						
						Team.teams.get(User.getPlayer(p).getTeamId()).declineRequest(target);
					}
				} else if(User.getPlayer(target).getTeamId() != null && User.getPlayer(p).getTeamId() == null){
					p.sendMessage("§eThis player are in team!");
				} else if(User.getPlayer(target).getTeamId() == null && User.getPlayer(p).getTeamId() != null){
					p.sendMessage("§eThis player aren't in team!");
				}
			}
		}
		
		return false;
	}

}
