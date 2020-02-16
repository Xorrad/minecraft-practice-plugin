package me.xorrad.practice.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.xorrad.practice.Practice;
import me.xorrad.practice.utils.User;


public class BuildCmd implements CommandExecutor, Listener {
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        
        if (p.hasPermission("practice.build")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("on")) {
                    if (!User.getPlayer(p).canBuild()) {
                        User.getPlayer(p).setBuild(true);
                        User.getPlayer(p).setDestruct(true);
                        p.sendMessage("§eBuild mode: §aon");
                    } else {
                        p.sendMessage("§cUse /build off");
                    }
                }
                if (args[0].equalsIgnoreCase("off")) {
                    if (User.getPlayer(p).canBuild()) {
                    	User.getPlayer(p).setBuild(false);
                    	User.getPlayer(p).setDestruct(false);
                        p.sendMessage("§eBuild mode: §coff");
                    } else {
                        p.sendMessage("§cUse /build on");
                    }
                }
            } else {
                p.sendMessage("§cUse /build <on/off>");
            }
        } else {
        	p.sendMessage(Practice.getInstance().getMessageHelper().getMessage("no_permission"));
        }
        return false;
    }
}
