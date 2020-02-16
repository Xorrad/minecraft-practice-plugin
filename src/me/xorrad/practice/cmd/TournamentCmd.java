package me.xorrad.practice.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.xorrad.practice.tournament.Tournament;
import me.xorrad.practice.utils.User;
import me.xorrad.practice.utils.gui.Gui;


public class TournamentCmd implements CommandExecutor, Listener {
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if(sender instanceof Player)
        {
        	Player p = (Player) sender;
        	
        	if(args.length >= 1)
        	{
        		if(args[0].equalsIgnoreCase("leave"))
        		{
        			User u = User.getPlayer(p);
    				if(u.getTournamentId() == null)
    				{
    					p.sendMessage("§eYou aren't in tournament");
    					return true;
    				}
    				
    				Tournament t = Tournament.getTournament(u.getTournamentId());
    				if(t.isStarted())
    				{
    					p.sendMessage("§eYou can't quit the tournament if he's already started !");
    					return true;
    				}
    				
    				t.quitTournament(p);
        		}
        		else if(args[0].equalsIgnoreCase("join"))
        		{
        			if(args.length >= 2)
        			{
        				Player target = Bukkit.getPlayer(args[1]);
        				
        				if(target == null)
        				{
        					p.sendMessage("§eThis player isn't online!");
        					return true;
        				}
        				
        				if(User.getPlayer(target).getTournamentId() == null)
        				{
        					p.sendMessage("§eThis player isn't in tournament!");
        					return true;
        				}
        				
        				if(User.getPlayer(p).getTournamentId() != null)
        				{
        					p.sendMessage("§eYou are already in a tournament!");
        					return true;
        				}
        				
        				Tournament t = Tournament.getTournament(User.getPlayer(target).getTournamentId());
        				if(target != t.getLeader())
        				{
        					p.sendMessage("§eThis player isn't leader of his tournament!");
        					return true;
        				}
        				
        				if(!t.isOpen())
        				{
        					if(!t.invited.contains(p))
        					{
	        					p.sendMessage("§eThis tournament isn't public!");
	        					return true;
        					}
        				}
        				
        				if(t.isStarted())
        				{
        					Tournament.getTournament(User.getPlayer(target).getTournamentId()).addSpec(p);
        					//p.sendMessage("§eThis tournament is already started!");
        					return true;
        				}
        				
        				t.joinTournament(p);
        			}
        		}
        		else if(args[0].equalsIgnoreCase("create"))
        		{
        			if(p.hasPermission("practice.tournament"))
        			{
        				if(User.getPlayer(p).getTournamentId() != null)
        				{
        					p.sendMessage("§eYou are already in a tournament!");
        					return true;
        				}
        				
        				Gui.openCreateTournamentGui(p);
        			}
        		}
        		else if(args[0].equalsIgnoreCase("private"))
        		{
        			if(p.hasPermission("practice.tournament"))
        			{
        				if(User.getPlayer(p).getTournamentId() == null)
        				{
        					p.sendMessage("§eYou aren't in a tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).getLeader() != p)
        				{
        					p.sendMessage("§eYou aren't the leader of your tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).isOpen() == false)
        				{
        					p.sendMessage("§eYour tournament is already private!");
        					return true;
        				}
        			
        				Tournament.getTournament(User.getPlayer(p).getTournamentId()).setOpen(false);
        				p.sendMessage("§eYour tournament is now private!");
        			}
        		}
        		else if(args[0].equalsIgnoreCase("public"))
        		{
        			if(p.hasPermission("practice.tournament"))
        			{
        				if(User.getPlayer(p).getTournamentId() == null)
        				{
        					p.sendMessage("§eYou aren't in a tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).getLeader() != p)
        				{
        					p.sendMessage("§eYou aren't the leader of your tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).isOpen() == true)
        				{
        					p.sendMessage("§eYour tournament is already public!");
        					return true;
        				}
        			
        				Tournament.getTournament(User.getPlayer(p).getTournamentId()).setOpen(true);
        				p.sendMessage("§eYour tournament is now public!");
        			}
        		}
        		else if(args[0].equalsIgnoreCase("start"))
        		{
        			if(p.hasPermission("practice.tournament"))
        			{
        				if(User.getPlayer(p).getTournamentId() == null)
        				{
        					p.sendMessage("§eYou aren't in a tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).getLeader() != p)
        				{
        					p.sendMessage("§eYou aren't the leader of your tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).isStarted())
        				{
        					p.sendMessage("§eYour tournament is already started!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).getPlayers().size() <= 1)
        				{
        					p.sendMessage("§eThere are not enough players in your tournament!");
        					return true;
        				}
        			
        				Tournament.getTournament(User.getPlayer(p).getTournamentId()).startTournament();
        			}
        		}
        		else if(args[0].equalsIgnoreCase("broadcast"))
        		{
        			if(p.hasPermission("practice.tournament"))
        			{
        				if(User.getPlayer(p).getTournamentId() == null)
        				{
        					p.sendMessage("§eYou aren't in a tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).getLeader() != p)
        				{
        					p.sendMessage("§eYou aren't the leader of your tournament!");
        					return true;
        				}
        				
        				if(Tournament.getTournament(User.getPlayer(p).getTournamentId()).isStarted())
        				{
        					p.sendMessage("§eYour tournament is already started!");
        					return true;
        				}
        			
        				Tournament.getTournament(User.getPlayer(p).getTournamentId()).setOpen(true);
        				Tournament.getTournament(User.getPlayer(p).getTournamentId()).annonceTournament();
        				p.sendMessage("§eBroadcast has succefully sended!");
        			}
        		}
        		else if(args[0].equalsIgnoreCase("invite"))
        		{
        			if(args.length >= 2)
        			{
        				Player target = Bukkit.getPlayer(args[1]);
        				
        				if(target == null)
        				{
        					p.sendMessage("§eThis player isn't online!");
        					return true;
        				}
        				
        				if(User.getPlayer(p).getTournamentId() == null)
        				{
        					p.sendMessage("§eYou aren't in a tournament!");
        					return true;
        				}
        				
        				Tournament t = Tournament.getTournament(User.getPlayer(p).getTournamentId());
        				if(p != t.getLeader())
        				{
        					p.sendMessage("§eYou aren't isn't leader of his tournament!");
        					return true;
        				}
        				
        				if(User.getPlayer(target).getTournamentId() != null)
        				{
        					p.sendMessage("§eThis player is already in tournament!");
        					return true;
        				}
        				
        				if(t.isStarted())
        				{
        					p.sendMessage("§eYour tournament is already started!");
        					return true;
        				}
        				
        				t.invitePlayer(target);
        				p.sendMessage("§eYou have succefully invited §a" + target.getName() + " §ein your tournament!");
        			}
        		}
        		else if(args[0].equalsIgnoreCase("help"))
        		{
        			p.sendMessage("§e§m--------------------------");
        			if(p.hasPermission("practice.tournament"))
        			{
        				p.sendMessage("§e/tournament create");
        				p.sendMessage("§e/tournament start");
        				p.sendMessage("§e/tournament broadcast");
        				p.sendMessage("§e/tournament public");
        				p.sendMessage("§e/tournament private");
        				p.sendMessage("§e/tournament invite");
        				p.sendMessage("");
        			}
                    p.sendMessage("§e/tournament join <Player>");
                    p.sendMessage("§e/tournament leave");
                    p.sendMessage("§e/tournament help");
                    p.sendMessage("§e§m--------------------------");
        		}
        	}
        	else
        	{
        		p.sendMessage("§e§m--------------------------");
        		if(p.hasPermission("practice.tournament"))
    			{
    				p.sendMessage("§e/tournament create");
    				p.sendMessage("§e/tournament start");
    				p.sendMessage("§e/tournament broadcast");
    				p.sendMessage("§e/tournament public");
    				p.sendMessage("§e/tournament private");
    				p.sendMessage("§e/tournament invite");
    				p.sendMessage("");
    			}
                p.sendMessage("§e/tournament join <Player>");
                p.sendMessage("§e/tournament leave");
                p.sendMessage("§e/tournament help");
                p.sendMessage("§e§m--------------------------");
        	}
        }
        
        return false;
    }
}
