package me.xorrad.practice.fight;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.xorrad.practice.Practice;
import me.xorrad.practice.arena.Arena;
import me.xorrad.practice.editkit.EditKitGui;
import me.xorrad.practice.team.Team;
import me.xorrad.practice.utils.InvUtils;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.kits.Kits;
import me.xorrad.practice.utils.kits.SpawnItem;

public class FightListener implements Listener{
	
	@EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
		User u = User.getPlayer(e.getPlayer());
        if (u.getFightId() != null) {
        	if(Fight.getAllFights().get(u.getFightId()).getMode().equals(FightLadder.Combo))
        	{
        		Vector vec = e.getPlayer().getVelocity();
                vec.setX(vec.getX() * 0.6);
                vec.setZ(vec.getZ() * 0.56);
                vec.setY(vec.getY() * 0.6);
                e.setVelocity(vec);
        	}
        	else if(Fight.getAllFights().get(u.getFightId()).getMode().equals(FightLadder.Sumo))
        	{
        		Vector vec = e.getPlayer().getVelocity();
                vec.setX(vec.getX() * 1.3);
                vec.setZ(vec.getZ() * 0.9);
                vec.setY(vec.getY() * 1.3);
                e.setVelocity(vec);
        	}
        	else if(Fight.getAllFights().get(u.getFightId()).getMode().equals(FightLadder.BuildUHC))
        	{
        		Vector vec = e.getPlayer().getVelocity();
                vec.setX(vec.getX() * 0.9);
                vec.setZ(vec.getZ() * 0.987);
                vec.setY(vec.getY() * 0.9);
                e.setVelocity(vec);
        	}
        }
    }
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if(User.getPlayer(e.getPlayer()).getFightId() != null) {
			
			if(Fight.getAllFights().get(User.getPlayer(e.getPlayer()).getFightId()).getMode().equals(FightLadder.Sumo)) {
				if(!Fight.getAllFights().get(User.getPlayer(e.getPlayer()).getFightId()).isStarted())
				{
					if(e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())
					{
						e.setCancelled(true);
					}
				}
				
				Integer looselevel = Arena.getArenaByID(Fight.getAllFights().get(User.getPlayer(e.getPlayer()).getFightId()).getArenaID()).getLooseLevel();
				if(e.getTo().getY() <= looselevel)
				{
					if(!Fight.getAllFights().get(User.getPlayer(e.getPlayer()).getFightId()).isEnd())
					{
						Player def = e.getPlayer();
						User pp = User.getPlayer(def);
						
						if(pp.getFightId() != null){
							Fight f = Fight.getAllFights().get(pp.getFightId());
							if(!f.isTeam()) {
								Player att = (f.getP1()==def) ? f.getP2() : f.getP1();
								def.teleport(Practice.getInstance().getSpawn());
								SpawnItem.giveSpawnItem(def);
								f.stopFight(att, def, false); //WINNER : LOSER : PlayerQuit
							} else if(f.isFFa()) {
								pp.setDeath(true);
								InvUtils.saveInv(def);
								for(Player t1 : f.getTeam1()) {
									if(!User.getPlayer(t1).isDeath()) {
										t1.hidePlayer(def);
									}
								}
								
								new BukkitRunnable() {
									
									@Override
									public void run() {
										def.setFireTicks(0);
										this.cancel();
									}
								}.runTaskTimer(Practice.getInstance(), 2, 2);
								
								if(f.isEndFFA()) {
									def.teleport(Practice.getInstance().getSpawn());
									SpawnItem.giveSpawnItem(def);
									f.stopFight(f.getWinner(), null);
								} else {
									def.setHealth(20.0D);
									def.setAllowFlight(true);
									def.setFlying(true);
									def.setFireTicks(0);
									Kits.giveSpecItem(def);
								}
								
							} else {
								InvUtils.saveInv(def);
								pp.setDeath(true);
								if(f.isTeam1Defeated()) {
									def.teleport(Practice.getInstance().getSpawn());
									SpawnItem.giveSpawnItem(def);
									f.stopFight(f.getTeam2().get(0), f.getTeam1().get(0));
								} else if(f.isTeam2Defeated()) {
									def.teleport(Practice.getInstance().getSpawn());
									SpawnItem.giveSpawnItem(def);
									f.stopFight(f.getTeam1().get(0), f.getTeam2().get(0));
								} else {
									
									for(Player t1 : f.getTeam1()) {
										if(!User.getPlayer(t1).isDeath()) {
											t1.hidePlayer(def);
										}
										if(f.isInTeamInTheFight(t1)) {
										}
									}
									for(Player t2 : f.getTeam2()) {
										if(!User.getPlayer(t2).isDeath()) {
											t2.hidePlayer(def);
										}
										if(f.isInTeamInTheFight(t2)) {
										}
									}
									
									def.setHealth(20.0D);
									def.setAllowFlight(true);
									def.setFlying(true);
									def.setFireTicks(0);
									Kits.giveSpecItem(def);
									def.setGameMode(GameMode.CREATIVE);
								}
							}
						} 
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onFoodLeft(FoodLevelChangeEvent e) {
		if(User.getPlayer((Player)e.getEntity()).getFightId() == null) {
			if(User.getPlayer((Player)e.getEntity()).getTournamentId() == null)
			{
				e.setCancelled(true);
				((Player)e.getEntity()).setFoodLevel(20);
				return;
			}
		} else {
			
			if(Fight.getAllFights().get(User.getPlayer((Player) e.getEntity()).getFightId()).getMode().equals(FightLadder.Soup)) {
				e.setCancelled(true);
			}
			
			if(Fight.getAllFights().get(User.getPlayer((Player) e.getEntity()).getFightId()).getMode().equals(FightLadder.Sumo)) {
				e.setCancelled(true);
			}
			
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			User pp = User.getPlayer((Player)e.getEntity());
			
			if(pp.getFightId() == null) {
				if(pp.getTournamentId() == null)
				{
					e.setCancelled(true);
					return;
				}
			}
			if(pp.getFightId() != null)
			{
				if(Fight.getAllFights().get(pp.getFightId()).isEnd() || !Fight.getAllFights().get(pp.getFightId()).isStarted()) {
					e.setCancelled(true);
					return;
				}
			
				if(Fight.getAllFights().get(pp.getFightId()).getMode().equals(FightLadder.Sumo)) {
					if(pp.getPlayer().getLocation().getY() > -100) {
						pp.getPlayer().setHealth(20.0D);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		Player p = e.getPlayer();
		
		if(User.getPlayer(p).canBuild() && User.getPlayer(p).canDestruct()) {
			return;
		}
		
		if(User.getPlayer(p).isDeath()) {
			e.setCancelled(true);
		}
		
		if(User.getPlayer(p).getFightId() != null){
		
			if(Fight.getAllFights().get(User.getPlayer(p).getFightId()).isEnd() || !Fight.getAllFights().get(User.getPlayer(p).getFightId()).isStarted()) {
				e.setCancelled(true);
			} else {
		        Item item = e.getItemDrop();
		        
		        Bukkit.getScheduler().scheduleSyncDelayedTask(Practice.getInstance(), new Runnable() {
		 
		            @Override
		            public void run() {
		                if(item != null && !item.isDead() && item.isValid()){
		                    item.remove();
		                }
		            }
		        }, 3 * 20);
			}
			
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e){
		
		Player p = e.getPlayer();
		
		if(User.getPlayer(p).isDeath()) {
			e.setCancelled(true);
		}
		
		/*if(!Practice.getInstance().entityHider.canSee(p, e.getItem())) {
			e.setCancelled(true);
			return;
		}*/
		
		if(User.getPlayer(p).getFightId() != null){
			if(Fight.getAllFights().get(User.getPlayer(p).getFightId()).isEnd()){
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void onGuiClick(InventoryClickEvent e){
		
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		
		if(i == null || i.getType().equals(Material.AIR)){
			return;
		}
		User pp = User.getPlayer(p);
		
		if(pp.canBuild() || pp.canDestruct()) {
			return;
		}
		
		if(pp.getFightId() == (Integer) null){
			if(EditKitGui.editdebuff.contains(p) || EditKitGui.editnodebuff.contains(p) || EditKitGui.editbow.contains(p) || EditKitGui.editgapple.contains(p) || EditKitGui.editaxe.contains(p) || EditKitGui.editsoup.contains(p) || EditKitGui.edituhc.contains(p)) {
				return;
			}
			e.setCancelled(true);
		} else {
			if(p.getGameMode().equals(GameMode.CREATIVE)) {
				e.setCancelled(true);
				return;
			}
			if(Fight.getAllFights().get(pp.getFightId()).isEnd()){
				e.setCancelled(true);
				return;
			}
			if(User.getPlayer(p).isDeath()) {
				e.setCancelled(true);
				return;
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			User pp = User.getPlayer((Player)e.getEntity());
			
			if(pp.isDeath()) {
				e.setCancelled(true);
			}
			
			if(pp.getFightId() == null){
				if(pp.getTournamentId() == null)
				{
					e.setCancelled(true);
				}
			} else {
				if(!Fight.getAllFights().get(pp.getFightId()).isStarted() || Fight.getAllFights().get(pp.getFightId()).isEnd()){
					e.setCancelled(true);
				}
				
				if(Fight.getAllFights().get(pp.getFightId()).isTeam() ) {
					if(e.getDamager() instanceof Player) {
						if(User.getPlayer((Player)e.getDamager()).isDeath()) {
							e.setCancelled(true);
						}
						
						if(Fight.getAllFights().get(pp.getFightId()).isFFa()) {
							return;
						}
						
						if(Fight.getAllFights().get(pp.getFightId()).isTeam1(pp.getPlayer()) && Fight.getAllFights().get(pp.getFightId()).isTeam1((Player) e.getDamager())) {
							e.setCancelled(true);
						} else if(Fight.getAllFights().get(pp.getFightId()).isTeam2(pp.getPlayer()) && Fight.getAllFights().get(pp.getFightId()).isTeam2((Player) e.getDamager())) {
							e.setCancelled(true);
						}
					} else if(e.getDamager() instanceof Arrow) {
						if(((Arrow)e.getDamager()).getShooter() instanceof Player) {
							
							if(User.getPlayer((Player)((Arrow)e.getDamager()).getShooter()).isDeath()) {
								e.setCancelled(true);
							}
							
							if(Fight.getAllFights().get(pp.getFightId()).isFFa()) {
								return;
							}
							
							if(Fight.getAllFights().get(pp.getFightId()).isTeam1(pp.getPlayer()) && Fight.getAllFights().get(pp.getFightId()).isTeam1((Player)((Arrow)e.getDamager()).getShooter())) {
								e.setCancelled(true);
							} else if(Fight.getAllFights().get(pp.getFightId()).isTeam2(pp.getPlayer()) && Fight.getAllFights().get(pp.getFightId()).isTeam2((Player)((Arrow)e.getDamager()).getShooter())) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	public static void addHealth(Player p) {
		
		Damageable dm = p;
		
		if(dm.getHealth() >= dm.getMaxHealth() - 6) {
			dm.setHealth(dm.getMaxHealth());
		} else {
			p.setHealth(dm.getHealth()+6);
		}
		
	}
	
	public static void addFood(Player p) {
		
		if(p.getFoodLevel() >= 20 - 4) {
			p.setFoodLevel(20);
		} else {
			p.setFoodLevel(p.getFoodLevel()+4);
		}
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		
		if(User.getPlayer(e.getPlayer()).canBuild()) {
			e.setCancelled(false);
			return;
		}
		
		if(User.getPlayer(e.getPlayer()).getFightId() == null) {
			e.setCancelled(true);
			return;
		}
		
		if(e.getItem() == null || !e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			return;
		}
		
		Player p = e.getPlayer();
		Damageable dm = p;
		
		if(e.getItem().getType().equals(Material.MUSHROOM_SOUP)) {
			if(dm.getHealth() < dm.getMaxHealth()) {
				addHealth(p);
				e.getItem().setType(Material.BOWL);
				addFood(p);
			} else {
				if(p.getFoodLevel() != 20) {
					addFood(p);
					e.getItem().setType(Material.BOWL);
				} else {
					e.setCancelled(true);
				}
			}
			return;
		}
		
		if(e.getItem().getType().equals(Material.POTION) && e.getItem().getDurability() == (short) 16421){
			if(User.getPlayer(p).getFightId() != null){
				if(!Fight.getAllFights().get(User.getPlayer(p).getFightId()).isStarted() || Fight.getAllFights().get(User.getPlayer(p).getFightId()).isEnd() ){
					e.setCancelled(true);
					p.updateInventory();
					return;
				}
			}
		}
		
		if(!e.getItem().getType().equals(Material.ENDER_PEARL)){
			return;
		}
		
		if(User.getPlayer(p).getFightId() != null){
			if(!Fight.getAllFights().get(User.getPlayer(p).getFightId()).isStarted() || Fight.getAllFights().get(User.getPlayer(p).getFightId()).isEnd() ){
				e.setCancelled(true);
				p.updateInventory();
				return;
			}
		} else {
			e.setCancelled(true);
			p.updateInventory();
			return;
		}
		
		if(User.getPlayer(p).haveLaunchEnderPearl){
			if(User.getPlayer(p).getEnderPearlTime() > 0) {
				p.sendMessage("§ePearl Cooldown: §c" + String.format("%1$,.1f", User.getPlayer(p).getEnderPearlTime()) + " seconds");
			}
			e.setCancelled(true);
		} else {
			User.getPlayer(p).haveLaunchEnderPearl = true;
			User.getPlayer(p).enderPearlTimer = new BukkitRunnable() {
				
				@Override
				public void run() {
					
					if(User.getPlayer(p).getEnderPearlTime() <= 0.0){
						User.getPlayer(p).setEnderPearlTime(Practice.getInstance().enderPearlDefaultTime);
						User.getPlayer(p).haveLaunchEnderPearl = false;
						this.cancel();
					} else {
						User.getPlayer(p).downEnderPearlTime();
					}
				}
			}.runTaskTimer(Practice.getInstance(), 2, 2);
		}
		
	}
	
	@EventHandler
	public void onRegen(EntityRegainHealthEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			User pp = User.getPlayer(p);
			if(pp.getFightId() != null){
				if(Fight.getAllFights().get(pp.getFightId()).getMode().equals(FightLadder.BuildUHC)){
					if(e.getRegainReason().equals(RegainReason.REGEN) || e.getRegainReason().equals(RegainReason.SATIATED)){
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	public void addGoldenHeadEffects(Player p) {
        PotionEffectType type = PotionEffectType.ABSORPTION;
        int duration = Integer.parseInt("120");
        int amplitude = Integer.parseInt("0");
        PotionEffect pEffect = new PotionEffect(type, duration * 20, amplitude);
        PotionEffectType type2 = PotionEffectType.REGENERATION;
        int duration2 = Integer.parseInt("10");
        int amplitude2 = Integer.parseInt("1");
        PotionEffect pEffect2 = new PotionEffect(type2, duration2 * 20, amplitude2);
        p.addPotionEffect(pEffect);
        p.addPotionEffect(pEffect2);
    }
    
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (!p.getInventory().getItemInHand().hasItemMeta() || p.getInventory().getItemInHand() == null || p.getInventory().getItemInHand().getItemMeta().getDisplayName() == null) {
            return;
        }
        if (e.getItem().getType() == Material.GOLDEN_APPLE) {
            ItemStack goldenHead = e.getItem();
            if (goldenHead.getItemMeta().getDisplayName().equals("§6Golden Head")) {
                for (PotionEffect effects : p.getActivePotionEffects()) {
                    p.removePotionEffect(effects.getType());
                }
                addGoldenHeadEffects(p);
            }
        }
    }
    
    @EventHandler
    public void onBlockChange(BlockPhysicsEvent e)
    {
    	if(e.getChangedType().equals(Material.GRASS))
    	{
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onBreaking(BlockBreakEvent e) {
        Player p = e.getPlayer();
        User pp = User.getPlayer(p);
        Block block = e.getBlock();
        if (pp.canDestruct()) {
            return;
        }
        if (pp.fightId == null) {
            e.setCancelled(true);
            return;
        }
        if (pp.isDeath()) {
            e.setCancelled(true);
        }
        else if(Fight.getAllFights().get(pp.getFightId()).getMode().equals(FightLadder.BuildUHC) && !Fight.getAllFights().get(pp.getFightId()).isEnd()) {
            if (block.getType() == Material.OBSIDIAN || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA || block.getType() == Material.WOOD || block.getType() == Material.COBBLESTONE || block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
                return;
            }
            e.setCancelled(true);
        }
        else {
            e.setCancelled(true);
        }
    }
	
    @EventHandler
    public void onPlace(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        User pp = User.getPlayer(p);
    	Block block = e.getBlockClicked().getWorld().getBlockAt(e.getBlockClicked().getLocation().add(e.getBlockFace().getModX(), e.getBlockFace().getModY(), e.getBlockFace().getModZ()));

    	if (pp.canBuild()) {
            return;
        }
        if (pp.fightId == (Integer)null) {
            e.setCancelled(true);
        }
        else
        {
	        if (pp.isDeath()) 
	        {
	            e.setCancelled(true);
	        }
	        else if (Fight.getAllFights().get(pp.getFightId()).getMode().equals(FightLadder.BuildUHC) && !Fight.getAllFights().get(pp.getFightId()).isEnd()) 
	        {
	        	 if (block != null && block.getLocation().getY() - Fight.getAllFights().get(pp.getFightId()).getArena().getSpawn1().getY() >= 8.0) 
	        	 {
	                 e.setCancelled(true);
	                 return;
	             }
	        	 Arena.getArenaByID(Fight.getAllFights().get(pp.getFightId()).getArenaID()).addBlocks(p, block);
	        }
	        else 
	        {
	            e.setCancelled(true);
	        }
        }
    }
    
    @EventHandler
    public void onPlacing(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        User pp = User.getPlayer(p);
        Block block = e.getBlock();
        if (pp.canBuild()) {
            return;
        }
        if (pp.fightId == (Integer)null) {
            e.setCancelled(true);
        }
        else
        {
	        if (pp.isDeath()) 
	        {
	            e.setCancelled(true);
	        }
	        else if (Fight.getAllFights().get(pp.getFightId()).getMode().equals(FightLadder.BuildUHC) && !Fight.getAllFights().get(pp.getFightId()).isEnd()) 
	        {
	        	 if (block != null && block.getLocation().getY() - Fight.getAllFights().get(pp.getFightId()).getArena().getSpawn1().getY() >= 8.0) 
	        	 {
	                 e.setCancelled(true);
	                 return;
	             }
	            if (block.getType() == Material.OBSIDIAN || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA || block.getType() == Material.WOOD || block.getType() == Material.COBBLESTONE || block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)
	            {
	            	Arena.getArenaByID(Fight.getAllFights().get(pp.getFightId()).getArenaID()).addBlocks(p, block);
	            	return;
	            }
	            e.setCancelled(true);
	        }
	        else 
	        {
	            e.setCancelled(true);
	        }
        }
    }
    
    @EventHandler
    public void onBurn(BlockBurnEvent e)
    {
    	e.setCancelled(true);
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
    	if(User.getPlayer(e.getPlayer()).getTeamId() == null) {
    		SpawnItem.giveSpawnItem(e.getPlayer());
    		User.getPlayer(e.getPlayer()).setDeath(false);
    	} else {
    		if(Team.getTeams().get(User.getPlayer(e.getPlayer()).getTeamId()).getLeader() == e.getPlayer()) {
    			Kits.giveTeamLeaderItem(e.getPlayer());
    		} else {
    			Kits.giveTeamItem(e.getPlayer());
    		}
    	}
    	e.setRespawnLocation(Practice.getInstance().getSpawn());
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void shoot(ProjectileLaunchEvent e) {
    	
    	if(e.getEntity().getShooter() instanceof Player) {
    		
    		Player p = (Player)e.getEntity().getShooter();
    		User pp = User.getPlayer(p);
    		
    		if(pp.getFightId() != null) {
    			if(!Fight.getAllFights().get(pp.getFightId()).isStarted() || Fight.getAllFights().get(pp.getFightId()).isEnd()) {
    				e.setCancelled(true);
    			}
    		}
    		
    	}
    	
    }
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player def = (Player) e.getEntity();
		User pp = User.getPlayer(def);
		
		String death_message = e.getDeathMessage();
		e.setDeathMessage(null);
		
		if(pp.getFightId() != null){
			Fight f = Fight.getAllFights().get(pp.getFightId());
			if(!f.isTeam()) {
				Player att = (f.getP1()==def) ? f.getP2() : f.getP1();
				
				pp.setDeath(true);
				
				att.sendMessage("§f" + death_message);
				def.sendMessage("§f" + death_message);
				f.stopFight(att, def, false); //WINNER : LOSER : PlayerQuit
			} else if(f.isFFa()) {
				
				pp.setDeath(true);
				InvUtils.saveInv(def);
				for(Player t1 : f.getTeam1()) {
					if(!User.getPlayer(t1).isDeath()) {
						t1.hidePlayer(def);
					}
					if(f.isInTeamInTheFight(t1)) {
						t1.sendMessage(death_message);
					}
				}
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						def.setFireTicks(0);
						this.cancel();
					}
				}.runTaskTimer(Practice.getInstance(), 2, 2);
				
				if(f.isEndFFA()) {
					f.stopFight(f.getWinner(), null);
				} else {
					e.setKeepInventory(true);
					def.setHealth(20.0D);
					def.setAllowFlight(true);
					def.setFlying(true);
					def.setFireTicks(0);
					Kits.giveSpecItem(def);
				}
				
			} else {
				InvUtils.saveInv(def);
				pp.setDeath(true);
				if(f.isTeam1Defeated()) {
					f.stopFight(f.getTeam2().get(0), f.getTeam1().get(0));
				} else if(f.isTeam2Defeated()) {
					f.stopFight(f.getTeam1().get(0), f.getTeam2().get(0));
				} else {
					
					for(Player t1 : f.getTeam1()) {
						if(!User.getPlayer(t1).isDeath()) {
							t1.hidePlayer(def);
						}
						if(f.isInTeamInTheFight(t1)) {
							t1.sendMessage(death_message);
						}
					}
					for(Player t2 : f.getTeam2()) {
						if(!User.getPlayer(t2).isDeath()) {
							t2.hidePlayer(def);
						}
						if(f.isInTeamInTheFight(t2)) {
							t2.sendMessage(death_message);
						}
					}
					
					e.setKeepInventory(true);
					def.setHealth(20.0D);
					def.setAllowFlight(true);
					def.setFlying(true);
					def.setFireTicks(0);
					Kits.giveSpecItem(def);
					def.setGameMode(GameMode.CREATIVE);
				}
			}
		} 
		
		e.getDrops().clear();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player def = e.getPlayer();
		User pp = User.getPlayer(def);
		
		if(pp.getFightId() != null){
			Fight f = Fight.getAllFights().get(pp.getFightId());
			if(!f.isTeam()) {
				Player att = (f.getP1()==def) ? f.getP2() : f.getP1();
				
				//String m = e.getQuitMessage().replaceAll("§e", "§f");
				String m = "§f" + def.getName() + " left the game.";
				att.sendMessage("§f" + m);
				f.stopFight(att, def, true); //WINNER : LOSER : PlayerQuit
			} else if(f.isFFa()) {
				
				pp.setDeath(true);
				e.setQuitMessage(null);
				for(Player t1 : f.getTeam1()) {
					if(!User.getPlayer(t1).isDeath()) {
						t1.hidePlayer(def);
					}
				}
				
				if(f.isEndFFA()) {
					f.stopFight(f.getWinner(), null);
				} /*else {
					def.setHealth(20.0D);
					def.setGameMode(GameMode.CREATIVE);
					Kits.giveTeamItem(def);*/
				
			} else {
				pp.setDeath(true);
				e.setQuitMessage(null);
				if(f.isTeam1Defeated()) {
					f.stopFight(f.getTeam2().get(0), f.getTeam1().get(0));
				} else if(f.isTeam2Defeated()) {
					f.stopFight(f.getTeam1().get(0), f.getTeam2().get(0));
				} /*else {
					def.setHealth(20.0D);
					def.setGameMode(GameMode.CREATIVE);
					Kits.giveTeamItem(def);
					
					for(Player t1 : f.getTeam1()) {
						if(!PPlayer.getPlayer(t1).isDeath()) {
							t1.hidePlayer(def);
						}
					}
					for(Player t2 : f.getTeam2()) {
						if(!PPlayer.getPlayer(t2).isDeath()) {
							t2.hidePlayer(def);
						}
					}
				}*/
			}
		} 
		
		e.setQuitMessage(null);
	}

}
