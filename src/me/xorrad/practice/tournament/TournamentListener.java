package me.xorrad.practice.tournament;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.xorrad.practice.arena.Arena;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.utils.User;

public class TournamentListener implements Listener {
	
	@EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
		User u = User.getPlayer(e.getPlayer());
        if (u.getTournamentId() != null) {
        	if(Tournament.getTournament(u.getTournamentId()).getLadder().equals(FightLadder.Sumo))
        	{
        		Vector vec = e.getPlayer().getVelocity();
                vec.setX(vec.getX() * 1.3);
                vec.setZ(vec.getZ() * 0.9);
                vec.setY(vec.getY() * 1.3);
                e.setVelocity(vec);
        	}
        }
    }
	
	@EventHandler
	public void onFoodLeft(FoodLevelChangeEvent e) {
		if(User.getPlayer((Player)e.getEntity()).getTournamentId() == null) {
			if(User.getPlayer((Player)e.getEntity()).getFightId() == null)
			{
				e.setCancelled(true);
				((Player)e.getEntity()).setFoodLevel(20);
				return;
			}
		} else {
			
			if(!Tournament.getTournament(User.getPlayer((Player) e.getEntity()).getTournamentId()).isInFight((Player) e.getEntity()))
			{
				e.setCancelled(true);
			}
			
			if(Tournament.getTournament(User.getPlayer((Player) e.getEntity()).getTournamentId()).getLadder().equals(FightLadder.Soup)) {
				e.setCancelled(true);
			}
			
			if(Tournament.getTournament(User.getPlayer((Player) e.getEntity()).getTournamentId()).getLadder().equals(FightLadder.Sumo)) {
				e.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			User pp = User.getPlayer((Player)e.getEntity());
			
			if(pp.getTournamentId() != null)
			{
				if(Tournament.getTournament(pp.getTournamentId()).isInFight())
				{
					if(!Tournament.getTournament(pp.getTournamentId()).isInFight((Player) e.getEntity()))
					{
						e.setCancelled(true);
						return;
					}
					else
					{
						if(Tournament.getTournament(pp.getTournamentId()).getLadder().equals(FightLadder.Sumo))
						{
							e.setDamage(0.0D);
							return;
						}
					}
				}
				else
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			User pp = User.getPlayer((Player)e.getEntity());
			
			if(pp.isDeath()) {
				e.setCancelled(true);
			}
			
			if(pp.getTournamentId() == null)
			{
				return;
			}
			
			Tournament t = Tournament.getTournament(pp.getTournamentId());
			if(!t.isStarted() || !t.isInFight() || t.isWaiting()){
				e.setCancelled(true);
				return;
			}
			
			if(e.getDamager() instanceof Player)
			{
				Player def = (Player) e.getEntity();
				Player att = (Player) e.getDamager();
				
				if(!t.isInFight(def) || !t.isInFight(att))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if(User.getPlayer(e.getPlayer()).getTournamentId() != null) {
			
			if(Tournament.getTournament(User.getPlayer(e.getPlayer()).getTournamentId()).getLadder().equals(FightLadder.Sumo)) {
				if(Tournament.getTournament(User.getPlayer(e.getPlayer()).getTournamentId()).isWaiting())
				{
					if(e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())
					{
						e.setCancelled(true);
					}
				}
				
				Integer looselevel = Arena.getArenaByID(Tournament.getTournaments().get(User.getPlayer(e.getPlayer()).getTournamentId()).getArenaId()).getLooseLevel();
				if(e.getTo().getY() <= looselevel)
				{
					Player def = e.getPlayer();
					User pp = User.getPlayer(def);
					
					if(Tournament.getTournament(User.getPlayer(e.getPlayer()).getTournamentId()).isInFight()){
						Tournament t = Tournament.getTournament(pp.getTournamentId());
						Player att = (t.getP1()==def) ? t.getP2() : t.getP1();
						Tournament.getTournament(pp.getTournamentId()).nextFight(att, def); //WINNER : LOSER : PlayerQuit
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player def = e.getPlayer();
		User pp = User.getPlayer(def);
		
		if(pp.getTournamentId() != null){
			Tournament t = Tournament.getTournament(pp.getTournamentId());
			if(t.isStarted())
			{
				if(t.isInFight(def)){
					Player att = (def == t.getP1()) ? t.getP2() : t.getP1();
					t.nextFight(att, def); //WINNER : LOSER
				}
				else
				{
					Tournament.getTournament(pp.getTournamentId()).quitTournament(def);
				}
			}
			else
			{
				Tournament.getTournament(pp.getTournamentId()).cancelTournament();
			}
		} 
		
		e.setQuitMessage(null);
	}
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		User pp = User.getPlayer(p);
		
		if(e.getItem() == null || e.getItem().getType().equals(Material.AIR) || e.getItem().getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRight click to leave the tournament")){
				if(pp.getTournamentId() == null)
				{
					p.sendMessage("§eYou aren't in a tournament!");
					return;
				}
				
				Tournament.getTournament(pp.getTournamentId()).quitTournament(p);
			}
			
			e.setCancelled(true);
			
		}
		
	}
	
	@EventHandler
	public void onGuiClick(InventoryClickEvent e){
		
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		
		if(i == null || i.getType().equals(Material.AIR) || i.getItemMeta() == null || i.getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getInventory().getName().equalsIgnoreCase("§7Create Tournament")) {
			for(FightLadder ladder : FightLadder.values()) {
				if(i.getItemMeta().getDisplayName().equalsIgnoreCase(ladder.getGuiString())){
					
					new Tournament(p, ladder, false);
					
					e.setCancelled(true);
					p.closeInventory();
				}
			}
		}
	}
}
