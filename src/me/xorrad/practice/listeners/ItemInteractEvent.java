package me.xorrad.practice.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.xorrad.practice.editkit.EditKitGui;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.User;

public class ItemInteractEvent implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		
		if(e.getItem() == null || e.getItem().getType().equals(Material.AIR) || e.getItem().getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Edit Kits")){
				EditKitGui.stuffGui(p);
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eCreate Team")){
				if(User.getPlayer(p).getFightId() != null){
					p.sendMessage("§cYou can't create team when your are in fight!");
					return;
				}
				if(User.getPlayer(p).getTeamId() != null){
					p.sendMessage("§cYou are already in team");
					return;
				}
				
				new Team(p, true);
				p.sendMessage("§eTeam has been created!");
			}
			
		}
		
	}

}
