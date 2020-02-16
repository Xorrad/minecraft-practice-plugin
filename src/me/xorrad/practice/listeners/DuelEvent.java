package me.xorrad.practice.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.User;

public class DuelEvent implements Listener{
	
	public static HashMap<Player, Player> targets;

	static {
		targets = new HashMap<>();
	}
	
	@EventHandler
	public void onGuiTeamClick(InventoryClickEvent e){
		
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		
		if(i == null || i.getType().equals(Material.AIR) || i.getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getInventory().getName().equalsIgnoreCase("§8Team-Fight")){
			
			if(Team.getTeams().get(Team.getTeamId(p)).getPlayers().size() <= 1) {
				e.setCancelled(true);
				p.sendMessage("§cYou can't join queue if you are alone in your team!");
				p.closeInventory();
				return;
			}
			
			for(FightLadder ladder : FightLadder.values()) {
				if(i.getItemMeta().getDisplayName().equalsIgnoreCase(ladder.getGuiString())){
					
					ArrayList<Player> team1 = new ArrayList<>();
					ArrayList<Player> team2 = new ArrayList<>();
					boolean b = true;
					for(Player pls : Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers()) {
						if(b) {
							team1.add(pls);
							b = !b;
						} else {
							team2.add(pls);
							b = !b;
						}
					}
					new Fight(team1.get(0), team2.get(0), ladder, false, team1, team2);
					
					e.setCancelled(true);
					p.closeInventory();
				}
			}
		} else if(e.getInventory().getName().equalsIgnoreCase("§6FFA")) {
			
			if(Team.getTeams().get(Team.getTeamId(p)).getPlayers().size() <= 1) {
				e.setCancelled(true);
				p.sendMessage("§cYou can't join queue if you are alone in your team!");
				p.closeInventory();
				return;
			}
			
			for(FightLadder ladder : FightLadder.values()) {
				if(i.getItemMeta().getDisplayName().equalsIgnoreCase(ladder.getGuiString())){
					
					ArrayList<Player> players = new ArrayList<>();
					for(Player pls : Team.teams.get(User.getPlayer(p).getTeamId()).getPlayers()) {
						players.add(pls);
					}
					new Fight(players.get(0), null, ladder, true, players);
					
					e.setCancelled(true);
					p.closeInventory();
				}
			}
		}
	}
	
	@EventHandler
	public void onGuiClick(InventoryClickEvent e){
		
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		
		if(i == null || i.getType().equals(Material.AIR) || i.getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getInventory().getName().equalsIgnoreCase("§8Select PvP Style")){
			
			for(FightLadder ladder : FightLadder.values()) {
				if(i.getItemMeta().getDisplayName().equalsIgnoreCase(ladder.getGuiString())){
					
					if(User.getPlayer(p).getTeamId() == null) {
						User.getPlayer(p).duelPlayer(targets.get(p), ladder);
					} else {
						Team.teams.get(User.getPlayer(p).getTeamId()).duelPlayer(targets.get(p), ladder);
					}
					
					e.setCancelled(true);
					p.closeInventory();
				}
			}
		}
	}
	
	@EventHandler
	public void onLeave(InventoryCloseEvent e){
		if(e.getInventory().getName() != null && e.getInventory().getName().equalsIgnoreCase("§8Select PvP Style")){
			targets.remove(e.getPlayer());
		}
	}
}
