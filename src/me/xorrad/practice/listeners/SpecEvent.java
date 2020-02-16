package me.xorrad.practice.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.xorrad.practice.Practice;
import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.kits.Kits;

public class SpecEvent implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		
		if(e.getItem() == null || e.getItem().getType().equals(Material.AIR) || e.getItem().getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRight click to stop spectate")){
				if(User.getPlayer(p).isSpectate()){
					Fight.getAllFights().get(Fight.getFightWatching(p)).removeSpec(p, false);
				} else if(User.getPlayer(p).getTeamId() != null) {
					p.sendMessage("§cYou stop spectate the match!");
					p.teleport(Practice.getInstance().getSpawn());
					
					if(Team.getTeams().get(User.getPlayer(p).getTeamId()).getLeader() != p) {
						Kits.giveTeamItem(p);
					} else {
						Kits.giveTeamLeaderItem(p);
					}
					
					p.setAllowFlight(false);
					p.setFlying(false);
					p.setFireTicks(0);
					p.setHealth(20.0D);
					
					for(Player pls : Bukkit.getOnlinePlayers()) {
						pls.showPlayer(p);
					}
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
		
		if(User.getPlayer(p).isSpectate()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		
		if(e.getPlayer() != null && User.getPlayer(e.getPlayer()).isSpectate()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		
		if(e.getPlayer() != null && User.getPlayer(e.getPlayer()).isSpectate()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		
		if(e.getDamager() != null){
			if(e.getDamager() instanceof Player){
				if(User.getPlayer((Player)e.getDamager()).isSpectate()){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageEvent e){
		if(e.getEntity() instanceof Player)
		{
			if(User.getPlayer((Player) e.getEntity()).isSpectate()){
				e.setCancelled(true);
			}
		}
	}
}
