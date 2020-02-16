package me.xorrad.practice.listeners;

import org.bukkit.event.Listener;

public class EntityHiderEvent implements Listener{
	
	/*@SuppressWarnings("deprecation")
	@EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
		EntityHider hider = Practice.getInstance().entityHider;
        
        Player p = e.getPlayer();
        User pp = User.getPlayer(p);
        
        if (pp.getFightId() != null) {
        	
            for (Player pls : Bukkit.getOnlinePlayers()) {
                
            	Fight f = Fight.getAllFights().get(pp.getFightId());
            			
            	if(f.is1v1Fight()) {
            		Player p2 = Fight.getAllFights().get(pp.getFightId()).getP1() == p ? Fight.getAllFights().get(pp.getFightId()).getP2() : Fight.getAllFights().get(pp.getFightId()).getP1();
            		
            		if(pls != p && pls != p2 && !f.getSpecs().contains(pls)) {
            			hider.hideEntity(pls, (Entity)e.getItemDrop());
            		}
            	} else {
            		if(!f.getTeam1().contains(pls) && !f.getTeam2().contains(pls) && !f.getSpecs().contains(pls)) {
            			hider.hideEntity(pls, (Entity)e.getItemDrop());
            		}
            	}
            	
            }
            
        } else {
        	for (Player pls : Bukkit.getOnlinePlayers()) {
            	if(pls != p) {
            		hider.hideEntity(pls, (Entity)e.getItemDrop());
            	}
            }
        }
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onThrowItem(ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.SPLASH_POTION || event.getEntityType() == EntityType.ARROW || event.getEntityType() == EntityType.ENDER_PEARL || event.getEntityType() == EntityType.ENDER_PEARL) {
            EntityHider hider = Practice.getInstance().entityHider;
            
            Player shooter = (Player)event.getEntity().getShooter();
            User pp = User.getPlayer(shooter);
            
            if (pp.getFightId() != null) {
            	
	            for (Player pls : Bukkit.getOnlinePlayers()) {
	                
	            	Fight f = Fight.getAllFights().get(pp.getFightId());
	            			
	            	if(f.is1v1Fight()) {
	            		Player p2 = Fight.getAllFights().get(pp.getFightId()).getP1() == shooter ? Fight.getAllFights().get(pp.getFightId()).getP2() : Fight.getAllFights().get(pp.getFightId()).getP1();
	            		
	            		if(pls != shooter && pls != p2 && !f.getSpecs().contains(pls)) {
	            			hider.hideEntity(pls, (Entity)event.getEntity());
	            		}
	            	} else {
	            		if(!f.getTeam1().contains(pls) && !f.getTeam2().contains(pls) && !f.getSpecs().contains(pls)) {
	            			hider.hideEntity(pls, (Entity)event.getEntity());
	            		}
	            	}
	            	
	            }
	            
            } else {
            	for (Player pls : Bukkit.getOnlinePlayers()) {
                	if(pls != shooter) {
                		hider.hideEntity(pls, (Entity)event.getEntity());
                	}
	            }
            }
        }
    }
	
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPotionSplashEvent(PotionSplashEvent e) {
        
        Player shooter = (Player)e.getEntity().getShooter();
        User pp = User.getPlayer(shooter);
        
        e.setCancelled(true);
        
        if (pp.getFightId() != null) {
        	
            for (Player pls : Bukkit.getOnlinePlayers()) {
                
            	Fight f = Fight.getAllFights().get(pp.getFightId());
            	
            	if(f.is1v1Fight()) {
            		Player p2 = Fight.getAllFights().get(pp.getFightId()).getP1() == shooter ? Fight.getAllFights().get(pp.getFightId()).getP2() : Fight.getAllFights().get(pp.getFightId()).getP1();
            		
            		if(pls != shooter && pls != p2 && !f.getSpecs().contains(pls)) {
            			e.getAffectedEntities().stream().filter(entity -> entity != shooter && entity != p2).forEach(entity -> e.getAffectedEntities().remove(entity));              
            		} else {
            			 e.getAffectedEntities().stream().filter(entity -> entity == shooter || entity == p2).forEach(entity -> entity.addPotionEffects(e.getEntity().getEffects()));
            		}
            			
            	} else {
            		if(!f.getTeam1().contains(pls) && !f.getTeam2().contains(pls) && !f.getSpecs().contains(pls)) {
            			e.getAffectedEntities().stream().filter(entity -> !f.getTeam1().contains(entity) && !f.getTeam2().contains(entity) && !f.getSpecs().contains(entity)).forEach(entity -> e.getAffectedEntities().remove(entity));
            		} else {
            			e.getAffectedEntities().stream().filter(entity -> f.getTeam1().contains(entity) || f.getTeam2().contains(entity)).forEach(entity -> entity.addPotionEffects(e.getEntity().getEffects()));
            		}
            	}
            	
            }
            
        } else {
    		e.getAffectedEntities().stream().filter(entity -> entity != shooter).forEach(entity -> e.getAffectedEntities().remove(entity));
    		e.setCancelled(true);
            e.getAffectedEntities().stream().filter(entity -> entity == shooter).forEach(entity -> entity.addPotionEffects(e.getEntity().getEffects()));
        }
    }


    public static void registerPacketListeners() {
		Practice.getInstance().protocolManager.addPacketListener(
				new PacketAdapter(Practice.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
					
					@Override
					public void onPacketSending(PacketEvent event) {
						PacketContainer packet = event.getPacket();
						String sound = packet.getStrings().read(0);
						World world = event.getPlayer().getWorld();
						
						Bukkit.broadcastMessage(sound);

						if (sound.contains("random.successful_hit") || sound.contains("weather")) {
							event.setCancelled(false);
							return;
						}

						Player player = event.getPlayer();

						if (User.getPlayer(player).getFightId() == null) {
							event.setCancelled(true);
							return;
						}

						double x = (packet.getIntegers().read(0) / 8.0);
						double y = (packet.getIntegers().read(1) / 8.0);
						double z = (packet.getIntegers().read(2) / 8.0);
						Location loc = new Location(world, x, y, z);

						Player closest = null;
						double bestDistance = Double.MAX_VALUE;

						// Find the player closest to the sound
						for (Player p : world.getPlayers()) {
							double distance = p.getLocation().distance(loc);

							if (distance < bestDistance && User.getPlayer(player).getFightId() != null) {
								bestDistance = distance;
								closest = p;
							}
						}

						if (closest != null) {
							
							Fight f = Fight.getAllFights().get(User.getPlayer(player).getFightId());
							if(f.isFFa()){
								if(!f.getTeam1().contains(closest) && !f.getSpecs().contains(closest)) {
									event.setCancelled(true);
								}
							} else if(f.isTeam()) {
								if(!f.getTeam1().contains(closest) && !f.getTeam2().contains(closest) && !f.getSpecs().contains(closest)) {
									event.setCancelled(true);
								}
							} else {
								if(f.getP1() != closest && f.getP2() != closest && !f.getSpecs().contains(closest)) {
									event.setCancelled(true);
								}
							}
						}
					}
				}
		);

		Practice.getInstance().protocolManager.addPacketListener(
				new PacketAdapter(Practice.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_EVENT) {
					@SuppressWarnings("deprecation")
					@Override
					public void onPacketSending(PacketEvent event) {
						Player p = event.getPlayer();
						
						Bukkit.broadcastMessage(event.getPacket().getIntegers().read(0).toString());
						
						if (event.getPacket().getIntegers().read(0) == 1002) {
							event.setCancelled(true);
						}
						
						if(event.getPacket().getIntegers().read(0) == 2002){
							
							WrapperPlayServerEntityMetadata wrapped = new WrapperPlayServerEntityMetadata(event.getPacket());
							Bukkit.broadcastMessage(String.valueOf((wrapped.getEntity(event).getEntityId())));
							Projectile potion = (Projectile)wrapped.getEntity(event);
							Bukkit.broadcastMessage(((Player)potion.getShooter()).getName());
							if(potion.getShooter() instanceof Player) {
								Player shooter = (Player) potion.getShooter();
								
								Fight f = Fight.getAllFights().get(User.getPlayer(shooter).getFightId());
								if(f.isFFa()){
									if(!f.getTeam1().contains(p)) {
										event.setCancelled(true);
									}
								} else if(f.isTeam()) {
									if(!f.getTeam1().contains(p) && !f.getTeam2().contains(p)) {
										event.setCancelled(true);
									}
								} else {
									if(f.getP1() != p && f.getP2() != p) {
										event.setCancelled(true);
									}
								}
							}
						}
					}
				}
		);

	}*/

}
