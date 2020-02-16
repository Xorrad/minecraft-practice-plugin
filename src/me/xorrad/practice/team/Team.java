package me.xorrad.practice.team;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.xorrad.practice.Practice;
import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.utils.InvUtils;
import me.xorrad.practice.utils.JsonBuilder;
import me.xorrad.practice.utils.JsonBuilder.ClickAction;
import me.xorrad.practice.utils.JsonBuilder.HoverAction;
import me.xorrad.practice.utils.QueueManager;
import me.xorrad.practice.utils.Request;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.kits.Kits;
import me.xorrad.practice.utils.kits.SpawnItem;
public class Team {
	
	public static HashMap<Integer, Team> teams;
	
	static {
		teams = new HashMap<>();
	}
	
	public HashMap<Player, Request> requests;
	private int id;
	private ArrayList<Player> players;
	private Integer requestTarget;
	private BukkitTask requestTask;
	private boolean privateTeam;
	private ArrayList<Player> invited;
	
	public Team(Player leader, boolean privateTeam) {
		
		Kits.giveTeamLeaderItem(leader);
		this.players = new ArrayList<>();
		this.players.add(leader);
		this.id = leader.getEntityId();
		User.getPlayer(leader).setTeamId(this.id);
		this.requests = new HashMap<>();
		this.invited = new ArrayList<>();
		this.setPrivateTeam(privateTeam);
		
		teams.put(id, this);
	}
	
	public Player getLeader() {
		return players.get(0);
	}

	public HashMap<Player, Request> getRequests() {
		return requests;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addInvite(Player p) {
		this.invited.add(p);
		
		JsonBuilder message = new JsonBuilder(new String[0]);
		message.withText(getLeader().getName() + " has invited your in is team | ").withColor(ChatColor.YELLOW).withText("JOIN").withColor(ChatColor.GREEN).withClickEvent(ClickAction.RUN_COMMAND, "/team join " + getLeader().getName()).withHoverEvent(HoverAction.SHOW_TEXT, "§eClick to join");
		message.sendJson(p);
	}
	
	public ArrayList<Player> getInvited() {
		return invited;
	}

	public void addPlayer(Player p) {
		players.add(p);
		User.getPlayer(p).setTeamId(this.id);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
		User.getPlayer(p).setTeamId(null);
	}

	public Integer getRequestTarget() {
		return requestTarget;
	}

	public void setRequestTarget(Integer requestTarget) {
		this.requestTarget = requestTarget;
	}

	public BukkitTask getRequestTask() {
		return requestTask;
	}

	public void setRequestTask(BukkitTask requestTask) {
		this.requestTask = requestTask;
	}
	
	public static Integer getTeamId(Player leader) {
		for(Team t : teams.values()) {
			if(t.getLeader() == leader) {
				return t.getId();
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public boolean isInFight() {
		return User.getPlayer(getLeader()).getFightId() == null ? false : true;
	}
	
	public static HashMap<Integer, Team> getTeams() {
		return teams;
	}
	
	public void acceptRequest(Player sender){
		
		Player player = getLeader();
		
		if(User.getPlayer(sender).getTeamId() != null && teams.get(User.getPlayer(player).getTeamId()).requests.containsKey(sender)){
			if(User.getPlayer(player).getFightId() == null){
				if(!teams.get(User.getPlayer(sender).getTeamId()).isInFight()) {
					
					for(Player t1 : players) {
						t1.sendMessage("§eStarting duel against §a" + sender.getName());
					}
					
					for(Player t2 : teams.get(getTeamId(sender)).players) {
						t2.sendMessage("§eStarting duel against §a" + player.getName());
					}
					
					new Fight(player, sender, requests.get(sender).getLadder(), false, false, players, teams.get(getTeamId(sender)).players);
					teams.get(User.getPlayer(sender).getTeamId()).setRequestTarget(null);
					if(teams.get(User.getPlayer(player).getTeamId()).requests.get(sender).requestRunnable != null) {
						teams.get(User.getPlayer(player).getTeamId()).requests.get(sender).requestRunnable.cancel();
					}
					teams.get(getTeamId(player)).requests.remove(sender);
					QueueManager.removeFromQueue(sender);
				} else {
					player.sendMessage("§cThis player is in fight!");
				}
			} else {
				player.sendMessage("§cYou cannot accept request in fight!");
			}
		} else {
			player.sendMessage("§cThis player hasn't send a request to you!");
		}
	}
	
	public void declineRequest(Player p){
		Player player = getLeader();
		if(teams.get(User.getPlayer(player).getTeamId()).requests.containsKey(p)){
			
			p.sendMessage("§a" + player.getName() + " §ehas denied you duel request!");
			teams.get(User.getPlayer(p).getTeamId()).setRequestTarget(null);
			
			if(teams.get(User.getPlayer(player).getTeamId()).requests.get(p).requestRunnable != null) {
				teams.get(User.getPlayer(player).getTeamId()).requests.get(p).requestRunnable.cancel();
			}
			teams.get(User.getPlayer(player).getTeamId()).requests.remove(p);
		}
	}
	
	public void duelPlayer(Player target, FightLadder ladder){
		Player player = getLeader();
		if(requestTarget == null || requestTarget != null && teams.get(requestTarget).getLeader() != target){
			if(User.getPlayer(target).getFightId() == null){
				
				setRequestTarget(getTeamId(target));
				teams.get(getTeamId(target)).requests.put(player, new Request(ladder));
				
				player.sendMessage("§eYou have sucefully sending a request to §a" + target.getName() +"'s §eteam");
				
				JsonBuilder message = new JsonBuilder(new String[0]).withText(getLeader().getName()+"'s").withColor(ChatColor.GREEN).withText(" team has dueled your team in ").withColor(ChatColor.YELLOW).withText(ladder.name()).withColor(ChatColor.GREEN).withText(" | ").withColor(ChatColor.YELLOW).withText("ACCEPT").withColor(ChatColor.GREEN).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/accept " + player.getName()).withText(" DENY").withColor(ChatColor.RED).withClickEvent(JsonBuilder.ClickAction.RUN_COMMAND, "/decline " + player.getName());
		        message.sendJson(target);
				
		        teams.get(getTeamId(target)).requests.get(player).requestRunnable = new BukkitRunnable() {
					
					@Override
					public void run() {
						setRequestTarget(null);
						teams.get(getTeamId(target)).requests.get(player).requestRunnable.cancel();
						teams.get(getTeamId(target)).requests.remove(player);
						player.sendMessage("§cThe request to " + target.getName() + "'s team have expired!");
					}
				}.runTaskTimer(Practice.getInstance(), 600, 600);
				
			} else {
				player.sendMessage("§c" + target.getName() + "'s team aren't in the lobby!");
			}
		} else {
			player.sendMessage("§cYou have already sending a request to this team!");
		}
	}
	
	public void joinTeam(Player p) {
		addPlayer(p);
		p.sendMessage("§eYou have join §a" + getLeader().getName() + "'s §eteam.");
		Kits.giveTeamItem(p);
		if(invited.contains(p)) {
			invited.remove(p);
		}
		
		for(Player t : players) {
			if(t != p) {
				t.sendMessage("§a"+p.getName()+" §ehas join the team!");
			}
		}
		
		QueueManager.removeFromQueue(getLeader());
	}
	
	public void leaveTeam(Player p) {
		if(getLeader() == p) {
			p.sendMessage("§cYou can't leave your team, Use /team disband");
			return;
		}
		
		if(!InvUtils.invs.containsKey(p.getName())){
			InvUtils.saveInv(p);
		}
		p.setFlying(false);
		p.setGameMode(GameMode.SURVIVAL);
		User.getPlayer(p).setDeath(false);
		User.getPlayer(p).setFightId(null);
		
		Practice.getInstance().clearEffect(p);
		SpawnItem.giveSpawnItem(p);
		if(Practice.getInstance().getSpawn() != null) {
			p.teleport(Practice.getInstance().getSpawn());
		}
		User.getPlayer(p).setTeamId(null);
		p.sendMessage("§eYou have left the §a" + getLeader().getName() + "'s §eteam.");
		players.remove(p);
		QueueManager.removeFromQueue(getLeader());
	}
	
	public void kickedTeam(Player p) {
		if(getLeader() == p) {
			p.sendMessage("§cYou can't kick yourself!");
			return;
		}
		
		if(!InvUtils.invs.containsKey(p.getName())){
			InvUtils.saveInv(p);
		}
		p.setGameMode(GameMode.SURVIVAL);
		User.getPlayer(p).setDeath(false);
		User.getPlayer(p).setFightId(null);
		
		Practice.getInstance().clearEffect(p);
		SpawnItem.giveSpawnItem(p);
		if(Practice.getInstance().getSpawn() != null) {
			p.teleport(Practice.getInstance().getSpawn());
		}
		User.getPlayer(p).setTeamId(null);
		p.sendMessage("§eYou have been kicked of the §a" + getLeader().getName() + "'s §eteam.");
		players.remove(p);
		QueueManager.removeFromQueue(getLeader());
	}
	
	public void disbandTeam() {
		if(isInFight()) {
			Fight.getAllFights().get(User.getPlayer(getLeader()).getFightId()).cancelFight();
		}
		
		for(Player pls : players) {
			SpawnItem.giveSpawnItem(pls);
			if(Practice.getInstance().getSpawn() != null) {
				pls.teleport(Practice.getInstance().getSpawn());
			}
			User.getPlayer(pls).setTeamId(null);
			if(pls != getLeader()) {
				pls.sendMessage("§a" + getLeader().getName() + " §ehas disband the team.");
			}
		}
		
		for(Team t : teams.values()) {
			for(Player p : t.getRequests().keySet()) {
				if(p == getLeader()) {
					t.getRequests().get(p).requestRunnable.cancel();
					t.getRequests().remove(p);
				}
			}
		}
		
		QueueManager.removeFromQueue(getLeader());
		players.clear();
		getTeams().remove(this.getId());
	}

	public boolean isPrivateTeam() {
		return privateTeam;
	}

	public void setPrivateTeam(boolean privateTeam) {
		this.privateTeam = privateTeam;
	}

}
