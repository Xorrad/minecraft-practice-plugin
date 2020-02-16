package me.xorrad.practice.listeners;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.xorrad.practice.Practice;
import me.xorrad.practice.elo.EloConfig;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.kits.SpawnItem;

public class PlayerJoin implements Listener{

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		new User(p);
		EloConfig.updateElo(p);
		e.setJoinMessage(null);
        p.setMaximumNoDamageTicks(20);
        p.setExp(0.0f);
        p.setLevel(0);
        
        p.teleport(Practice.getInstance().getSpawn());
        	
        if(User.getPlayer(p).getEnderPearlTimer() != null && Bukkit.getScheduler().isCurrentlyRunning(User.getPlayer(p).getEnderPearlTimer().getTaskId()))
        	User.getPlayer(p).getEnderPearlTimer().cancel();
        User.getPlayer(p).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
        Practice.getInstance().clearEffect(p);
        
        if(Practice.getInstance().player_infos.contains(p.getUniqueId().toString())) {
        	User.getPlayer(p).setUnrankedLeft(Practice.getInstance().player_infos.getInt(p.getUniqueId().toString() + "." + "Unranked-Left"));
        } else {
        	User.getPlayer(p).setUnrankedLeft(15);
        	Practice.getInstance().player_infos.set(p.getUniqueId().toString() + "." + "Unranked-Left", 15);
            try {
            	Practice.getInstance().player_infos.save(Practice.getInstance().player_infos_file);
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setSaturation(20.0f);
        p.setFireTicks(0);
        SpawnItem.giveSpawnItem(p);
        p.setGameMode(GameMode.SURVIVAL);
        /*p.sendMessage("§7§m-*-------------------------------------*-");
        p.sendMessage("§eBienvenue sur le practice dev par Xorrad");
        p.sendMessage("§7§m-*-------------------------------------*-");*/
	}
	
}
