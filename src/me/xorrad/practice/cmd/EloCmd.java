package me.xorrad.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xorrad.practice.Practice;
import me.xorrad.practice.elo.EloConfig;
import me.xorrad.practice.fight.FightLadder;


public class EloCmd implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("elo")) {
            if (args.length == 0) {
            	 p.sendMessage("§e§m--------[- §f §6Elo Stats: §2" +p.getName() + " §e§m-]--------");
                 p.sendMessage("");
                 
                 for(FightLadder ladder : FightLadder.values()) {
                	 p.sendMessage("                 §e"+ladder.name()+": §a" + EloConfig.getElo(p, ladder.name()));
                 }
                 
                 /*p.sendMessage("                 §eNoDebuff: §a" + EloConfig.getElo(p, "NoDebuff"));
                 p.sendMessage("                 §eDebuff: §a" + EloConfig.getElo(p, "Debuff"));
                 p.sendMessage("                 §eBow: §a" + EloConfig.getElo(p, "Bow"));
                 p.sendMessage("                 §eGapple: §a" + EloConfig.getElo(p, "Gapple"));
                 p.sendMessage("                 §eAxe: §a" + EloConfig.getElo(p, "Axe"));
                 p.sendMessage("                 §eSoup: §a" + EloConfig.getElo(p, "Soup"));*/
                 p.sendMessage("");
                 p.sendMessage("                 §eGlobal: §a" + EloConfig.getGlobalElo(p));
                 p.sendMessage("§e§m------------------------------------");
                 p.sendMessage("");
            }
            else if (args.length == 1) {
                Player t = Bukkit.getPlayer(args[0]);
                if (t != null) {
                    if (t != p) {
                    	p.sendMessage("");
                        p.sendMessage("§e§m--------[- §f §6Elo Stats: §2" +t.getName() + " §e§m-]--------");
                        p.sendMessage("");
                        for(FightLadder ladder : FightLadder.values()) {
                       	 p.sendMessage("                 §e"+ladder.name()+": §a" + EloConfig.getElo(p, ladder.name()));
                        }
                        /*p.sendMessage("                 §eNoDebuff: §a" + EloConfig.getElo(t, "NoDebuff"));
                        p.sendMessage("                 §eDebuff: §a" + EloConfig.getElo(t, "Debuff"));
                        p.sendMessage("                 §eBow: §a" + EloConfig.getElo(t, "Bow"));
                        p.sendMessage("                 §eGapple: §a" + EloConfig.getElo(t, "Gapple"));
                        p.sendMessage("                 §eAxe: §a" + EloConfig.getElo(t, "Axe"));
                        p.sendMessage("                 §eSoup: §a" + EloConfig.getElo(t, "Soup"));*/
                        p.sendMessage("");
                        p.sendMessage("                 §eGlobal: §a" + EloConfig.getGlobalElo(t));
                        p.sendMessage("§e§m------------------------------------");
                        p.sendMessage("");
                    }
                    else {
                    	p.sendMessage("§e§m--------[- §f §6Elo Stats: §2" +p.getName() + " §e§m-]--------");
                        p.sendMessage("");
                        for(FightLadder ladder : FightLadder.values()) {
                       	 p.sendMessage("                 §e"+ladder.name()+": §a" + EloConfig.getElo(p, ladder.name()));
                        }
                        /*p.sendMessage("                 §eNoDebuff: §a" + EloConfig.getElo(p, "NoDebuff"));
                        p.sendMessage("                 §eDebuff: §a" + EloConfig.getElo(p, "Debuff"));
                        p.sendMessage("                 §eBow: §a" + EloConfig.getElo(p, "Bow"));
                        p.sendMessage("                 §eGapple: §a" + EloConfig.getElo(p, "Gapple"));
                        p.sendMessage("                 §eAxe: §a" + EloConfig.getElo(p, "Axe"));
                        p.sendMessage("                 §eSoup: §a" + EloConfig.getElo(p, "Soup"));*/
                        p.sendMessage("");
                        p.sendMessage("                 §eGlobal: §a" + EloConfig.getGlobalElo(p));
                        p.sendMessage("§e§m------------------------------------");
                        p.sendMessage("");
                    }
                }
                else {
                    Practice.getInstance().getMessageHelper().getMessage("offline_player");
                }
            }
            else {
                p.sendMessage("§cUse /elo <player>");
            }
        }
        return true;
    }
}
