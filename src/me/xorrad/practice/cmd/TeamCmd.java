package me.xorrad.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.User;

public class TeamCmd implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(label.equalsIgnoreCase("team")){
				if(args.length == 1){
					
					if(args[0].equalsIgnoreCase("create")) {
						
						if(User.getPlayer(p).getFightId() != null){
							p.sendMessage("§cYou can't create team when your are in fight!");
							return true;
						}
						if(User.getPlayer(p).getTeamId() != null){
							p.sendMessage("§cYou are already in team");
							return true;
						}
						
						new Team(p, true);
						p.sendMessage("§eTeam has been created!");
						
					} else if(args[0].equalsIgnoreCase("info")) {
						
						if(User.getPlayer(p).getTeamId() == null){
							p.sendMessage("§cYou aren't in team");
							return true;
						}
						
						Team t = Team.teams.get(User.getPlayer(p).getTeamId());
						p.sendMessage("§eTeam of " + t.getLeader().getName());
						
						for(Player pls : t.getPlayers()) {
							p.sendMessage("- " + pls.getName());
						}
						
					} else if(args[0].equalsIgnoreCase("leave")) {
						
						if(User.getPlayer(p).getTeamId() == null){
							p.sendMessage("§cYou aren't in team");
							return true;
						}
						
						Team.teams.get(User.getPlayer(p).getTeamId()).leaveTeam(p);
						
					} else if(args[0].equalsIgnoreCase("disband")) {
						
						if(User.getPlayer(p).getTeamId() == null){
							p.sendMessage("§cYou aren't in team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(p).getTeamId()).getLeader() != p){
							p.sendMessage("§cYou aren't the leader of the team!");
							return true;
						}
						Team.teams.get(User.getPlayer(p).getTeamId()).disbandTeam();
					} else if(args[0].equalsIgnoreCase("public")) {
						
						if(User.getPlayer(p).getTeamId() == null){
							p.sendMessage("§cYou aren't in team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(p).getTeamId()).getLeader() != p){
							p.sendMessage("§cYou aren't the leader of the team!");
							return true;
						}
						if(!Team.teams.get(User.getPlayer(p).getTeamId()).isPrivateTeam()) {
							p.sendMessage("§cYour team is already public!");
							return true;
						}
						
						Team.teams.get(User.getPlayer(p).getTeamId()).setPrivateTeam(false);
						p.sendMessage("§eYour team is now public!");
						
					} else if(args[0].equalsIgnoreCase("private")) {
						
						if(User.getPlayer(p).getTeamId() == null){
							p.sendMessage("§cYou aren't in team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(p).getTeamId()).getLeader() != p){
							p.sendMessage("§cYou aren't the leader of the team!");
							return true;
						}
						if(Team.teams.get(User.getPlayer(p).getTeamId()).isPrivateTeam()) {
							p.sendMessage("§cYour team is already private!");
							return true;
						}
						
						Team.teams.get(User.getPlayer(p).getTeamId()).setPrivateTeam(true);
						p.sendMessage("§eYour team is now private!");
						
					} else {
						p.sendMessage("§eUse /team <create, invite, kick, info, leave, disband, public, private>");
					}
					
				} else if(args.length == 2) {
					
					if(args[0].equalsIgnoreCase("join")) {
						
						Player target = Bukkit.getPlayer(args[1]);
						
						if(target == p) {
							p.sendMessage("§cYou can't join your team!");
							return true;
						}
						
						if(User.getPlayer(p).getFightId() != null) {
							p.sendMessage("§cYou can't join team when your are in fight!");
							return true;
						}
						
						if(target == null) {
							p.sendMessage("§cThis player isn't online!");
							return true;
						}
						
						if(User.getPlayer(p).getTeamId() != null){
							p.sendMessage("§cYou are already in team!");
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
						
						if(Team.teams.get(User.getPlayer(target).getTeamId()).isPrivateTeam() && !Team.teams.get(User.getPlayer(target).getTeamId()).getInvited().contains(p)) {
							p.sendMessage("§cYou hasn't been invited in this team!");
							return true;
						}
						
						Team.teams.get(User.getPlayer(target).getTeamId()).joinTeam(p);
						
					} else if(args[0].equalsIgnoreCase("invite")) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(User.getPlayer(p).getTeamId() == null) {
							p.sendMessage("§cYou aren't in team!");
							return true;
						}
						
						if(target == p) {
							p.sendMessage("§cYou can't invite yourself!");
							return true;
						}
						
						if(Team.getTeams().get(User.getPlayer(p).getTeamId()).getLeader() != p) {
							p.sendMessage("§cYou aren't the leader of your team!");
							return true;
						}
						
						if(target == null) {
							p.sendMessage("§cThis player isn't online!");
							return true;
						}
						
						if(User.getPlayer(target).getTeamId() != null){
							p.sendMessage("§cThis player is already in team!");
							return true;
						}
						
						if(Team.getTeams().get(User.getPlayer(p).getTeamId()).getInvited().contains(target)) {
							p.sendMessage("§cYou have already invited this player in your team!");
							return true;
						}
						
						Team.teams.get(User.getPlayer(p).getTeamId()).addInvite(target);
						p.sendMessage("§eThe Invitation has succefully sended!");
						
					} else if(args[0].equalsIgnoreCase("kick")) {
						Player target = Bukkit.getPlayer(args[1]);
						
						if(User.getPlayer(p).getTeamId() == null) {
							p.sendMessage("§cYou aren't in team!");
							return true;
						}
						
						if(Team.getTeams().get(User.getPlayer(p).getTeamId()).getLeader() != p) {
							p.sendMessage("§cYou aren't the leader of your team!");
							return true;
						}
						
						if(target == null) {
							p.sendMessage("§cThis player isn't online!");
							return true;
						}
						
						if(!Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers().contains(target)){
							p.sendMessage("§cThis player isn't in your team!");
							return true;
						}
						
						if(target == p) {
							p.sendMessage("§cYou can't kick yourself!");
							return true;
						}
						
						Team.teams.get(User.getPlayer(p).getTeamId()).kickedTeam(target);
						p.sendMessage("§eYou has succefully kicked §a" + target.getName() + "§e!");
					}
					
				} else {
					p.sendMessage("§eUse /team <create, invite, kick, info, leave, disband, public, private>");
				}
			} 
		}
		
		return false;
	}

}
