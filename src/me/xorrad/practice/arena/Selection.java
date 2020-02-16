package me.xorrad.practice.arena;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.xorrad.practice.utils.User;

public class Selection implements Listener
{
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();
        if (!p.getInventory().getItemInHand().hasItemMeta() || p.getInventory().getItemInHand() == null || p.getInventory().getItemInHand().getItemMeta().getDisplayName() == null) {
            return;
        }
        if (e.getAction().equals((Object)Action.LEFT_CLICK_BLOCK)) {
            if (item.getType().equals(Material.STICK) && item.getItemMeta().getDisplayName().equalsIgnoreCase("§cSelection") && p.hasPermission("region.selection")) {
                User.getPlayer(p).setL1(e.getClickedBlock().getLocation());
                p.sendMessage("§d Point 1 selected!");
                e.setCancelled(true);
            }
        }
        else if (e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) ){
        		if(item.getType().equals(Material.STICK) && item.getItemMeta().getDisplayName().equalsIgnoreCase("§cSelection") && p.hasPermission("region.selection")) {
	            User.getPlayer(p).setL2(e.getClickedBlock().getLocation());
	            p.sendMessage("§d Point 2 selected!");
	            e.setCancelled(true);
        	}
        }
    }
}


/* give outil stick name '§c Selection' */