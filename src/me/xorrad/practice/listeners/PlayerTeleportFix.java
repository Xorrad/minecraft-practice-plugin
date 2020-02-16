package me.xorrad.practice.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportFix implements Listener {
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onTeleport(PlayerTeleportEvent e) {
		
		/*Player p = e.getPlayer();
		PPlayer pp = PPlayer.getPlayer(p);*/
		
		Location l = e.getTo();
		l.setY(e.getTo().getY()+0.1f);
		
		e.setTo(l);
		
		e.getTo().getWorld().loadChunk(e.getTo().getChunk().getX(), e.getTo().getChunk().getZ());
		
		/*if(pp == null) {
			return;
		}
		
		if(pp.getFightId() != null) {
			
			if(Fight.getAllFights().get(pp.getFightId()).is1v1Fight()) {
				
				Player p2 = Fight.getAllFights().get(pp.getFightId()).getP1() == p ? Fight.getAllFights().get(pp.getFightId()).getP2() : Fight.getAllFights().get(pp.getFightId()).getP1();
				
				p2.showPlayer(p);
				p.showPlayer(p2);
				
			} else {
				
				for(Player t1 : Fight.getAllFights().get(pp.getFightId()).getTeam1()) {
					t1.showPlayer(p);
					p.showPlayer(t1);
				}
				for(Player t2 : Fight.getAllFights().get(pp.getFightId()).getTeam2()) {
					t2.showPlayer(p);
					p.showPlayer(t2);
				}
			}
			
		}*/ /*else {
			for(Player pls : Bukkit.getOnlinePlayers()) {
				pls.showPlayer(p);
			}
		}*/
	}

}
