package me.xorrad.practice.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xorrad.practice.utils.InvUtils;

public class InvCmd implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player){
			Player p = (Player) sender;
			
			if(args.length >= 1){
				
				String target = args[0];
				
				if(!target.equalsIgnoreCase("")){
					if(InvUtils.invs.containsKey(target)){
						
						InvUtils.openInv(p, target);
						return false;
						
					} else {
						p.sendMessage("§cUnknown Inventory!!");
					}
				}
			} else {
				p.sendMessage("§cUse /inv <Player> !");
			}
			
		}
		
		return false;
	}

}
