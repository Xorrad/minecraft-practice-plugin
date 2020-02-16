package me.xorrad.practice.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.gui.Gui;

public class TeamEvent implements Listener{

	@EventHandler
	public void onClick(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		
		if(e.getItem() == null || e.getItem().getType().equals(Material.AIR) || e.getItem().getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Unranked 2v2")){

				//OPEN FFA GUI
				if(Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers().size() <= 1) {
					p.sendMessage("§cYou are alone in the team, you can't join queue!");
					return;
				}
				if(Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers().size() > 2) {
					p.sendMessage("§cIts only 2v2 queue!");
					return;
				}
				Gui.openUnrankedTeamGui(p);
				
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Team Fight")){
				
				//OPEN TEAM FIGHT GUI
				if(Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers().size() <= 1) {
					p.sendMessage("§cYou are alone in the team, you can't start fight!");
					return;
				}
				Gui.openTeamFight(p);
				
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7Other Teams")){
				
				Gui.openOtherTeamDuelGui(p);
				
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§bYour team")){
				
				Team t = Team.teams.get(User.getPlayer(p).getTeamId());
				p.sendMessage("§eTeam of " + t.getLeader().getName());
				
				for(Player pls : t.getPlayers()) {
					p.sendMessage("- " + pls.getName());
				}
				
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cDisband Team")){
				Team.teams.get(User.getPlayer(p).getTeamId()).disbandTeam();
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cLeave Team")){
				Team.teams.get(User.getPlayer(p).getTeamId()).leaveTeam(p);
			}
			
			e.setCancelled(true);
			
		}
		
	}
	
	@EventHandler
	public void onGuiClick(InventoryClickEvent e){
		
		Player p = (Player) e.getWhoClicked();
		User pp = User.getPlayer(p);
		ItemStack i = e.getCurrentItem();
		
		if(i == null || i.getType().equals(Material.AIR) || i.getItemMeta() == null || i.getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getInventory().getName().equalsIgnoreCase("§9Menu of Team Fight")) {
		
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§6FFA")) {
				//OPEN FFA GUI
				if(Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers().size() <= 1) {
					p.sendMessage("§cYou are alone in the team, you can't start fight!");
					return;
				}
				Gui.openTeamFFADuelGui(p);
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§8Team-Fight")) {
				//OPEN TEAM FIGHT GUI
				if(Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers().size() <= 1) {
					p.sendMessage("§cYou are alone in the team, you can't start fight!");
					return;
				}
				Gui.openTeamFightDuelGui(p);
			}
			
		} else if(e.getInventory().getName().equalsIgnoreCase("§7Team List")) {
			
			e.setCancelled(true);
			
			if(pp.getTeamId() == null) {
				return;
			}
			if(Team.getTeams().get(pp.getTeamId()).getLeader() != p) {
				return;
			}
			
			String[] split = i.getItemMeta().getDisplayName().split("'");
			
			Player target = Bukkit.getPlayer(ChatColor.stripColor(split[0]));
			
			if(target == null) {
				p.sendMessage("§cThis player isn't online!");
				return;
			}
			
			if(target == p) {
				return;
			}
			
			if(User.getPlayer(target).getTeamId() == null) {
				p.sendMessage("§cThis player isn't in team!");
				return;
			}
			
			if(User.getPlayer(target).getFightId() != null) {
				p.sendMessage("§cThis player is in fight!");
				return;
			}
			
			if(User.getPlayer(p).getTeamId() == null) {
				p.sendMessage("§cYou aren't in team!");
				return;
			}
			if(User.getPlayer(p).getFightId() != null) {
				p.sendMessage("§cYou can't duel player when you are in fight!");
				return;
			}
			
			DuelEvent.targets.put(p, target);
			Gui.openDuelGui(p);
		}
	} 
}
