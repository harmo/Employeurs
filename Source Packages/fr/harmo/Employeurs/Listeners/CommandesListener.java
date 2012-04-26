package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author HarmO
 */
public class CommandesListener implements CommandExecutor {
	
	private Employeurs plugin;
	
	public CommandesListener (Employeurs plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (label.equalsIgnoreCase("emp")) {
				// Player commands
				if (args.length == 0) {
					sendHelp(player);
					return true;
				}
				else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("list")) {
						if (plugin.perms.has(player, "emp.list")) {
							plugin.jobManager.getJobList(player);
						}
						else {
							player.sendMessage(Config.noListPermMessage);
						}
						return true;
					}
				}
			}
			if(label.equalsIgnoreCase("empadm")) {
				// Admin commands
				if (args.length == 0) {
					sendHelpAdmin(player);
					return true;
				}
				else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("add")) {
						if (plugin.perms.has(player, "emp.admin.add")) {
							plugin.jobCreation.toggleAddMode(player);
						}
						else {
							player.sendMessage(Config.noAddPermMessage);
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("stop") && plugin.jobCreation.isInAddMode(player)) {
						plugin.jobCreation.toggleAddMode(player);
						return true;
					}
				}
			}
		}
		else {
			if (label.equalsIgnoreCase("emp")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("list")) {
						plugin.jobManager.getJobListFromConsole();
					}
				}
				return true;
			}
		}
		return false;
	}
	
	private void sendHelp(Player player) {
		player.sendMessage("====== Employeurs - Help =======");
		player.sendMessage(Config.helpList);
	}
	
	private void sendHelpAdmin(Player player) {
		player.sendMessage("====== Employeurs - AdminHelp =======");
		player.sendMessage(Config.helpAdd);
	}
	
}
