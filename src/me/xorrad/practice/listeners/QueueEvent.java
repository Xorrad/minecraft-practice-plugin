package me.xorrad.practice.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.xorrad.practice.fight.Fight;
import me.xorrad.practice.fight.FightLadder;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.QueueManager;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.gui.Gui;

public class QueueEvent implements Listener{

	@EventHandler
	public void onClick(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		
		if(e.getItem() == null || e.getItem().getType().equals(Material.AIR) || e.getItem().getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9Un-Ranked Queue")){
				Gui.openUnrankedGui(p);
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aRanked Queue")){
				if(User.getPlayer(p).getUnrankedLeft() > 0) {
					p.sendMessage("§cYou must play " + User.getPlayer(p).getUnrankedLeft() + " Un-Ranked matches before you can play Ranked.");
					return;
				}
				Gui.openRankedGui(p);
			} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cRight click to leave the queue")){
				
				if(QueueManager.isInUnrankedQueue(p)) {
					QueueManager.removeFromUnrankedQueue(p);
				}
				if(QueueManager.isInUnranked2v2Queue(p)) {
					QueueManager.removeFromUnranked2v2Queue(p);
				}
				if(QueueManager.isInRankedQueue(p)) {
					QueueManager.removeFromRankedQueue(p);
				}
				
				/*if(QueueManager.bow_unranked.contains(p)){
					QueueManager.removeFromBowQueueUnranked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the un-ranked §aBow queue.");
				}
				if(QueueManager.debuff_unranked.contains(p)){
					QueueManager.removeFromDebuffQueueUnranked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the un-ranked §aDebuff queue.");
				}
				if(QueueManager.nodebuff_unranked.contains(p)){
					QueueManager.removeFromNoDebuffQueueUnranked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the un-ranked §aNoDebuff queue.");
				}
				if(QueueManager.gapple_unranked.contains(p)){
					QueueManager.removeFromGappleQueueUnranked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the un-ranked §aGapple queue.");
				}
				if(QueueManager.axe_unranked.contains(p)){
					QueueManager.removeFromAxeQueueUnranked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the un-ranked §aAxe queue.");
				}
				if(QueueManager.soup_unranked.contains(p)){
					QueueManager.removeFromSoupQueueUnranked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the un-ranked §aSoup queue.");
				}*/
				
				/*
				 * 
				 * 2V2 UNRANKED
				 * 
				 */
				
				
				/*if(QueueManager.bow_2v2.contains(p)){
					QueueManager.removeFromBowQueue2v2(p);
					Kits.giveTeamLeaderItem(p);
					if(User.getPlayer(p).getTeamId() != null) {
						for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
							pls.sendMessage("§eRemoved from the 2v2 §aBow queue.");
						}
					} else {
						p.sendMessage("§eRemoved from the 2v2 §aBow queue.");
					}
				}
				if(QueueManager.debuff_2v2.contains(p)){
					QueueManager.removeFromDebuffQueue2v2(p);
					Kits.giveTeamLeaderItem(p);
					if(User.getPlayer(p).getTeamId() != null) {
						for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
							pls.sendMessage("§eRemoved from the 2v2 §aDebuff queue.");
						}
					} else {
						p.sendMessage("§eRemoved from the 2v2 §aDebuff queue.");
					}
				}
				if(QueueManager.nodebuff_2v2.contains(p)){
					QueueManager.removeFromNoDebuffQueue2v2(p);
					Kits.giveTeamLeaderItem(p);
					if(User.getPlayer(p).getTeamId() != null) {
						for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
							pls.sendMessage("§eRemoved from the 2v2 §aNoDebuff queue.");
						}
					} else {
						p.sendMessage("§eRemoved from the 2v2 §aNoDebuff queue.");
					}
				}
				if(QueueManager.gapple_2v2.contains(p)){
					QueueManager.removeFromGappleQueue2v2(p);
					Kits.giveTeamLeaderItem(p);
					if(User.getPlayer(p).getTeamId() != null) {
						for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
							pls.sendMessage("§eRemoved from the 2v2 §aGapple queue.");
						}
					} else {
						p.sendMessage("§eRemoved from the 2v2 §aGapple queue.");
					}
				}
				if(QueueManager.axe_2v2.contains(p)){
					QueueManager.removeFromAxeQueue2v2(p);
					Kits.giveTeamLeaderItem(p);
					if(User.getPlayer(p).getTeamId() != null) {
						for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
							pls.sendMessage("§eRemoved from the 2v2 §aAxe queue.");
						}
					} else {
						p.sendMessage("§eRemoved from the 2v2 §aAxe queue.");
					}
				}
				if(QueueManager.soup_2v2.contains(p)){
					QueueManager.removeFromSoupQueue2v2(p);
					Kits.giveTeamLeaderItem(p);
					if(User.getPlayer(p).getTeamId() != null) {
						for(Player pls : Team.getTeams().get(User.getPlayer(p).getTeamId()).getPlayers()) {
							pls.sendMessage("§eRemoved from the 2v2 §aSoup queue.");
						}
					} else {
						p.sendMessage("§eRemoved from the 2v2 §aSoup queue.");
					}
				}
				/*
				 * 
				 * RANKED
				 * 
				 *//*
				if(QueueManager.bow_ranked.contains(p)){
					QueueManager.removeFromBowQueueRanked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the ranked §aBow queue.");
				}
				if(QueueManager.debuff_ranked.contains(p)){
					QueueManager.removeFromDebuffQueueRanked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the ranked §aDebuff queue.");
				}
				if(QueueManager.nodebuff_ranked.contains(p)){
					QueueManager.removeFromNoDebuffQueueRanked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the ranked §aNoDebuff queue.");
				}
				if(QueueManager.gapple_ranked.contains(p)){
					QueueManager.removeFromGappleQueueRanked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the ranked §aGapple queue.");
				}
				if(QueueManager.axe_ranked.contains(p)){
					QueueManager.removeFromAxeQueueRanked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the ranked §aAxe queue.");
				}
				if(QueueManager.soup_ranked.contains(p)){
					QueueManager.removeFromSoupQueueRanked(p);
					SpawnItem.giveSpawnItem(p);
					p.sendMessage("§eRemoved from the ranked §aSoup queue.");
				}*/
			}
			
		}
		
	}
	
	@EventHandler
	public void onGuiClick(InventoryClickEvent e){
		
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		
		if(i == null || i.getType().equals(Material.AIR) || i.getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(e.getInventory().getName().equalsIgnoreCase("§9Select a 2v2 Queue")){
			
			if(User.getPlayer(p).getTeamId() == null) {
				e.setCancelled(true);
				p.sendMessage("§cYou can't join queue if you aren't in team!");
				p.closeInventory();
				return;
			}
			if(Team.getTeams().get(Team.getTeamId(p)).getPlayers().size() > 2) {
				e.setCancelled(true);
				p.sendMessage("§cYou can't join queue if you aren't two in your team!");
				p.closeInventory();
				return;
			}
			
			if(Team.getTeams().get(Team.getTeamId(p)).getPlayers().size() <= 1) {
				e.setCancelled(true);
				p.sendMessage("§cYou can't join queue if you are alone in your team!");
				p.closeInventory();
				return;
			}
			
			for(FightLadder ladder : FightLadder.values()) {
				if(i.getItemMeta().getDisplayName().equalsIgnoreCase(ladder.getGuiString())){
					
					e.setCancelled(true);
					p.closeInventory();
					
					if(QueueManager.unranked2v2QueueSize(ladder) >= 1){
						Player p1 = p;
						Player p2 = QueueManager.unranked2v2FirstPlayer(ladder);
						QueueManager.removeFromUnranked2v2Queue(p2);
						
						ArrayList<Player> team1 = Team.getTeams().get(User.getPlayer(p1).getTeamId()).getPlayers();
						ArrayList<Player> team2 = Team.getTeams().get(User.getPlayer(p2).getTeamId()).getPlayers();
						
						for(Player t1 : team1) {
							t1.sendMessage("§eStarting duel against §a" + p2.getName() + "§e's team");
						}
						
						for(Player t2 : team2) {
							t2.sendMessage("§eStarting duel against §a" + p1.getName() + "§e's team");
						}
						
						new Fight(p1, p2, ladder, false, false, team1, team2);
					} else {
						QueueManager.addTo2v2Queue(p, ladder);
					}
					
				}
			}
			
			/*if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§6Bow")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.bow_2v2.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.bow_2v2.get(0);
					
					ArrayList<Player> team1 = Team.getTeams().get(PPlayer.getPlayer(p1).getTeamId()).getPlayers();
					ArrayList<Player> team2 = Team.getTeams().get(PPlayer.getPlayer(p2).getTeamId()).getPlayers();
					
					QueueManager.removeFromBowQueue2v2(p2);
					
					for(Player t1 : team1) {
						t1.sendMessage("§eStarting duel against §a" + p2.getName());
					}
					
					for(Player t2 : team2) {
						t2.sendMessage("§eStarting duel against §a" + p1.getName());
					}
					
					new Fight(p1, p2, FightLadder.Bow, false, false, team1, team2);
				} else {
					QueueManager.addToBowQueue2v2(p);
					Kits.giveQueueItem(p);
				}
				
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§2Debuff")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.debuff_2v2.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.debuff_2v2.get(0);
					
					ArrayList<Player> team1 = Team.getTeams().get(PPlayer.getPlayer(p1).getTeamId()).getPlayers();
					ArrayList<Player> team2 = Team.getTeams().get(PPlayer.getPlayer(p2).getTeamId()).getPlayers();
					
					QueueManager.removeFromDebuffQueue2v2(p2);
					
					for(Player t1 : team1) {
						t1.sendMessage("§eStarting duel against §a" + p2.getName());
					}
					
					for(Player t2 : team2) {
						t2.sendMessage("§eStarting duel against §a" + p1.getName());
					}
					
					new Fight(p1, p2, FightLadder.Debuff, false, false, team1, team2);
				} else {
					QueueManager.addToDebuffQueue2v2(p);
					Kits.giveQueueItem(p);
				}
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§cNoDebuff")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.nodebuff_2v2.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.nodebuff_2v2.get(0);
					
					ArrayList<Player> team1 = Team.getTeams().get(PPlayer.getPlayer(p1).getTeamId()).getPlayers();
					ArrayList<Player> team2 = Team.getTeams().get(PPlayer.getPlayer(p2).getTeamId()).getPlayers();
					
					QueueManager.removeFromNoDebuffQueue2v2(p2);
					
					for(Player t1 : team1) {
						t1.sendMessage("§eStarting duel against §a" + p2.getName());
					}
					
					for(Player t2 : team2) {
						t2.sendMessage("§eStarting duel against §a" + p1.getName());
					}
					
					new Fight(p1, p2, FightLadder.NoDebuff, false, false, team1, team2);
				} else {
					QueueManager.addToNoDebuffQueue2v2(p);
					Kits.giveQueueItem(p);
				}
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§6Gapple")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.gapple_2v2.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.gapple_2v2.get(0);
					
					ArrayList<Player> team1 = Team.getTeams().get(PPlayer.getPlayer(p1).getTeamId()).getPlayers();
					ArrayList<Player> team2 = Team.getTeams().get(PPlayer.getPlayer(p2).getTeamId()).getPlayers();
					
					QueueManager.removeFromGappleQueue2v2(p2);
					
					for(Player t1 : team1) {
						t1.sendMessage("§eStarting duel against §a" + p2.getName());
					}
					
					for(Player t2 : team2) {
						t2.sendMessage("§eStarting duel against §a" + p1.getName());
					}
					
					new Fight(p1, p2, FightLadder.Gapple, false, false, team1, team2);
				} else {
					QueueManager.addToGappleQueue2v2(p);
					Kits.giveQueueItem(p);
				}
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§7Axe")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.axe_2v2.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.axe_2v2.get(0);
					
					ArrayList<Player> team1 = Team.getTeams().get(PPlayer.getPlayer(p1).getTeamId()).getPlayers();
					ArrayList<Player> team2 = Team.getTeams().get(PPlayer.getPlayer(p2).getTeamId()).getPlayers();
					
					QueueManager.removeFromAxeQueue2v2(p2);
					
					for(Player t1 : team1) {
						t1.sendMessage("§eStarting duel against §a" + p2.getName());
					}
					
					for(Player t2 : team2) {
						t2.sendMessage("§eStarting duel against §a" + p1.getName());
					}
					
					new Fight(p1, p2, FightLadder.Axe, false, false, team1, team2);
				} else {
					QueueManager.addToAxeQueue2v2(p);
					Kits.giveQueueItem(p);
				}
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§dSoup")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.soup_2v2.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.soup_2v2.get(0);
					
					ArrayList<Player> team1 = Team.getTeams().get(PPlayer.getPlayer(p1).getTeamId()).getPlayers();
					ArrayList<Player> team2 = Team.getTeams().get(PPlayer.getPlayer(p2).getTeamId()).getPlayers();
					
					QueueManager.removeFromSoupQueue2v2(p2);
					
					for(Player t1 : team1) {
						t1.sendMessage("§eStarting duel against §a" + p2.getName());
					}
					
					for(Player t2 : team2) {
						t2.sendMessage("§eStarting duel against §a" + p1.getName());
					}
					
					new Fight(p1, p2, FightLadder.Soup, false, false, team1, team2);
				} else {
					QueueManager.addToSoupQueue2v2(p);
					Kits.giveQueueItem(p);
				}
			}*/
				
				
				
				
		} else if(e.getInventory().getName().equalsIgnoreCase("§9Select a Un-Ranked Queue")){
			
			for(FightLadder ladder : FightLadder.values()) {
				if(i.getItemMeta().getDisplayName().equalsIgnoreCase(ladder.getGuiString())){
					
					e.setCancelled(true);
					p.closeInventory();
					
					if(QueueManager.unrankedQueueSize(ladder) >= 1){
						Player p1 = p;
						Player p2 = QueueManager.unrankedFirstPlayer(ladder);
						QueueManager.removeFromUnrankedQueue(p2);
						
						p1.sendMessage("§eStarting duel against §a" + p2.getName());
						p2.sendMessage("§eStarting duel against §a" + p1.getName());
						
						new Fight(p1, p2, ladder, false, false);
					} else {
						QueueManager.addToQueue(p, ladder);
					}
					
				}
			}
			/*if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§6Bow")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.bow_unranked.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.bow_unranked.get(0);
					QueueManager.removeFromBowQueueUnranked(p2);
					
					p1.sendMessage("§eStarting duel against §a" + p2.getName());
					p2.sendMessage("§eStarting duel against §a" + p1.getName());
					
					new Fight(p1, p2, FightLadder.Bow, false, false);
				} else {
					QueueManager.addToBowQueueUnranked(p);
					Kits.giveQueueItem(p);
					p.sendMessage("§eAdded to the §aBow §equeue, please wait another player.");
				}
				
				Gui.updateUnrankedGui();
				
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§2Debuff")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.debuff_unranked.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.debuff_unranked.get(0);
					QueueManager.removeFromDebuffQueueUnranked(p2);
					
					p1.sendMessage("§eStarting duel against §a" + p2.getName());
					p2.sendMessage("§eStarting duel against §a" + p1.getName());
					
					new Fight(p1, p2, FightLadder.Debuff, false, false);
				} else {
					QueueManager.addToDebuffQueueUnranked(p);
					Kits.giveQueueItem(p);
					p.sendMessage("§eAdded to the §aDebuff §equeue, please wait another player.");
				}
				Gui.updateUnrankedGui();
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§cNoDebuff")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.nodebuff_unranked.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.nodebuff_unranked.get(0);
					QueueManager.removeFromNoDebuffQueueUnranked(p2);
					
					p1.sendMessage("§eStarting duel against §a" + p2.getName());
					p2.sendMessage("§eStarting duel against §a" + p1.getName());
					
					new Fight(p1, p2, FightLadder.NoDebuff, false, false);
				} else {
					QueueManager.addToNoDebuffQueueUnranked(p);
					Kits.giveQueueItem(p);
					p.sendMessage("§eAdded to the §aNoDebuff §equeue, please wait another player.");
				}
				Gui.updateUnrankedGui();
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§6Gapple")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.gapple_unranked.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.gapple_unranked.get(0);
					QueueManager.removeFromGappleQueueUnranked(p2);
					
					p1.sendMessage("§eStarting duel against §a" + p2.getName());
					p2.sendMessage("§eStarting duel against §a" + p1.getName());
					
					new Fight(p1, p2, FightLadder.Gapple, false, false);
				} else {
					QueueManager.addToGappleQueueUnranked(p);
					Kits.giveQueueItem(p);
					p.sendMessage("§eAdded to the §aGapple §equeue, please wait another player.");
				}
				Gui.updateUnrankedGui();
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§7Axe")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.axe_unranked.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.axe_unranked.get(0);
					QueueManager.removeFromAxeQueueUnranked(p2);
					
					p1.sendMessage("§eStarting duel against §a" + p2.getName());
					p2.sendMessage("§eStarting duel against §a" + p1.getName());
					
					new Fight(p1, p2, FightLadder.Axe, false, false);
				} else {
					QueueManager.addToAxeQueueUnranked(p);
					Kits.giveQueueItem(p);
					p.sendMessage("§eAdded to the §aAxe §equeue, please wait another player.");
				}
				Gui.updateUnrankedGui();
			} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§dSoup")){
				
				e.setCancelled(true);
				p.closeInventory();
				
				if(QueueManager.soup_unranked.size() >= 1){
					Player p1 = p;
					Player p2 = QueueManager.soup_unranked.get(0);
					QueueManager.removeFromSoupQueueUnranked(p2);
					
					p1.sendMessage("§eStarting duel against §a" + p2.getName());
					p2.sendMessage("§eStarting duel against §a" + p1.getName());
					
					new Fight(p1, p2, FightLadder.Soup, false, false);
				} else {
					QueueManager.addToSoupQueueUnranked(p);
					Kits.giveQueueItem(p);
					p.sendMessage("§eAdded to the §aSoup §equeue, please wait another player.");
				}
				Gui.updateUnrankedGui();
			}*/
				
				
				
				
			} else if(e.getInventory().getName().equalsIgnoreCase("§9Select a Ranked Queue")){ //--------------------------------------RANKED---------------------------------------------------------------
				
				for(FightLadder ladder : FightLadder.values()) {
					if(i.getItemMeta().getDisplayName().equalsIgnoreCase(ladder.getGuiString())){
						
						e.setCancelled(true);
						p.closeInventory();
						
						QueueManager.addToRankedQueue(p, ladder);
					}
				}
				
				/*if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§6Bow")){
					
					e.setCancelled(true);
					p.closeInventory();
					
					p.sendMessage("§eYou joined §aBow §eranked queue with §a" + EloConfig.getElo(p, "Bow") + " §eelo.");
					Kits.giveQueueItem(p);
					QueueManager.addToBowQueueRanked(p);
					
				} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§2Debuff")){
					
					e.setCancelled(true);
					p.closeInventory();
					
					p.sendMessage("§eYou joined §aDebuff §eranked queue with §a" + EloConfig.getElo(p, "Debuff") + " §eelo.");
					Kits.giveQueueItem(p);
					QueueManager.addToDebuffQueueRanked(p);
					
				} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§cNoDebuff")){
					
					e.setCancelled(true);
					p.closeInventory();
					
					p.sendMessage("§eYou joined §aNoDebuff §eranked queue with §a" + EloConfig.getElo(p, "NoDebuff") + " §eelo.");
					Kits.giveQueueItem(p);
					QueueManager.addToNoDebuffQueueRanked(p);
				} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§6Gapple")){
					
					e.setCancelled(true);
					p.closeInventory();
					
					p.sendMessage("§eYou joined §aGapple §eranked queue with §a" + EloConfig.getElo(p, "Gapple") + " §eelo.");
					Kits.giveQueueItem(p);
					QueueManager.addToGappleQueueRanked(p);
				} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§7Axe")){
					
					e.setCancelled(true);
					p.closeInventory();
					
					p.sendMessage("§eYou joined §aAxe §eranked queue with §a" + EloConfig.getElo(p, "Axe") + " §eelo.");
					Kits.giveQueueItem(p);
					QueueManager.addToAxeQueueRanked(p);
				} else if(i.getItemMeta().getDisplayName().equalsIgnoreCase("§dSoup")){
					
					e.setCancelled(true);
					p.closeInventory();
					
					p.sendMessage("§eYou joined §aSoup §eranked queue with §a" + EloConfig.getElo(p, "Soup") + " §eelo.");
					Kits.giveQueueItem(p);
					QueueManager.addToSoupQueueRanked(p);
				}*/
			}
		
		}
}
