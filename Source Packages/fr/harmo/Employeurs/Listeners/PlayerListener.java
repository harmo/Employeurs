package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author HarmO
 */
public class PlayerListener implements Listener {
	
	private Employeurs plugin;

	public PlayerListener(Employeurs plugin) {
		this.plugin = plugin;				
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		if (plugin.manager.isInAddMode(player)) {
			//player.sendMessage("usrval= " + plugin.manager.getEmpUsersValue(player));
			if (plugin.manager.getEmpUsersValue(player) == 0) {
				// Nom du metier
				String jobName = plugin.manager.addJobName(player, message);
				if (jobName != null) {
					plugin.manager.setEmpUsersValue(player, 1);
					String[] messageList = {
						Config.empAddCreationJobNamePrefixSuccess + " " + jobName,
						Config.empAddCreationJobType,
						"break, drop, place, kill, craft"
					};
					plugin.sendMessageList(player, Arrays.asList(messageList));
				}
				event.setCancelled(true);
			}
			else if (plugin.manager.getEmpUsersValue(player) == 1) {
				// Type du metier
				String jobType = plugin.manager.addJobType(player, message);
				if (jobType != null) {
					plugin.manager.setEmpUsersValue(player, 2);
					String[] messageList = {
						Config.empAddCreationJobBlockIds1,
						Config.empAddCreationJobBlockIds2
					};
					plugin.sendMessageList(player, Arrays.asList(messageList));
				}
				event.setCancelled(true);
			}
			else if (plugin.manager.getEmpUsersValue(player) == 2) {
				// Blocks concernes
				ArrayList aRefusedIds = plugin.manager.addBlockIds(player, message);
				String errors = "";
				if (aRefusedIds.size() > 0 && aRefusedIds.get(0) != "error") {
					for (int i = 0; i < aRefusedIds.size(); i++) {
						if (i == aRefusedIds.size() - 1)
							errors += aRefusedIds.get(i) ;
						else
							errors += aRefusedIds.get(i) + ", ";
					}
					String[] messageList = {
						Config.empAddCreationJobBlockIdsError,
						errors
					};
					plugin.sendMessageList(player, Arrays.asList(messageList));
				}
				else {
					if (aRefusedIds.isEmpty()) {
						ArrayList aAcceptedIds = plugin.manager.getAcceptedIds();
						if (aAcceptedIds.size() > 0) {
							String success = "";
							for (int i = 0; i < aAcceptedIds.size(); i++) {
								if (i == aAcceptedIds.size() - 1)
									success += aAcceptedIds.get(i) ;
								else
									success += aAcceptedIds.get(i) + ", ";
							}
							String[] messageList = {
								Config.empAddCreationJobBlockIdsSuccess,
								success
							};
							plugin.sendMessageList(player, Arrays.asList(messageList));
							if (!Config.mysql) {
								// Enregistrement en fichier
								Config.getDbFile().setJob(plugin.manager.getJobName(), plugin.manager.getJobType(), aAcceptedIds);
							}
							else {
								// Enregistrement en bdd
							}
							
							player.sendMessage(Config.empAddCreationSuccess);
							plugin.manager.toggleAddMode(player);
						}
						else {
							player.sendMessage(Config.empAddCreationJobBlockIdsSyntaxError);
						}
					}
				}
				
				event.setCancelled(true);
			}
		}
	}
	
}
