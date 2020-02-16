package me.xorrad.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.EntityPlayer;

public class PingCmd implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("ping")) {
            EntityPlayer ep = ((CraftPlayer)p).getHandle();
            if (args.length == 1) {
                String player = args[0];
                Player victime = Bukkit.getPlayer(player);
                if (victime != null) {
                    ep = ((CraftPlayer)victime).getHandle();
                }
            }
            int ms = ep.ping;
            if (args.length == 1) {
                p.sendMessage("§a" + ep.getName() + "§e's ping §a" + ms + " §ems");
            }
            else if (args.length == 0) {
                p.sendMessage("§a" + p.getDisplayName() + "§e's ping: §a" + ms + " §ems");
            }
            else if (args.length > 1) {
                p.sendMessage("§eUse /ping <player>");
            }
        }
        return true;
    }
}
