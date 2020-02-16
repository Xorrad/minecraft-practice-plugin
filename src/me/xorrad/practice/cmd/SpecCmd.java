package me.xorrad.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.xorrad.practice.Practice;
import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.utils.User;

public class SpecCmd implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(args.length >= 1){
				
				Player target = Bukkit.getPlayer(args[0]);
				
				if(target == null){
					Practice.getInstance().getMessageHelper().getMessage("offline_player");
					return true;
				}
				if(User.getPlayer(target).getFightId() == null){
					p.sendMessage("§cThis player isn't in fight!");
					return true;
				}
				if(User.getPlayer(p).getFightId() != null){
					p.sendMessage("§cYou can't spectate when you are in fight!");
					return true;
				}
				if(User.getPlayer(p).getTeamId() != null){
					p.sendMessage("§cYou can't spectate when you are in team!");
					return true;
				}
				
				Fight.getAllFights().get(User.getPlayer(target).getFightId()).addSpec(p);
			} else {
				p.sendMessage("§cUse /spectate <Player> !");
			}
		}
		
		return false;
	}

}
