package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author HarmO
 */
public class PlayerListener implements Listener {
	
	private Employeurs plugin;
	public ArrayList signCreationInfos = new ArrayList();
	private int nbItems = 0;
	private int index;
	public ArrayList totalItems = new ArrayList();
	public Integer salary;
	private int currentId;

	public PlayerListener(Employeurs plugin) {
		this.plugin = plugin;				
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		// ADMIN JOB ADD MODE
		if (plugin.manager.isInAddMode(player)) {
			if (plugin.manager.getEmpUsersValue(player) == 0) {
				// Asking job name
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
				// Asking job type
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
				// Asking for which items
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
		// PLAYER SIGN JOB CREATE
		if (plugin.manager.isInCreationMode(player)) {
			if (plugin.manager.getEmpCreaUsersValue(player) == 0) {
				// Asking for which items
				ArrayList aRefusedIds = plugin.manager.addBlocksInOffer(player, message);
				String errors = "";
				if (aRefusedIds.size() > 0 && aRefusedIds.get(0) != "error") {
					for (int i = 0; i < aRefusedIds.size(); i++) {
						if (i == aRefusedIds.size() - 1)
							errors += aRefusedIds.get(i) ;
						else
							errors += aRefusedIds.get(i) + ", ";
					}
					String[] messageList = {
						"ids refuses :",
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
								"ids ajouté a la sign :",
								success
							};
							plugin.sendMessageList(player, Arrays.asList(messageList));
							this.signCreationInfos= aAcceptedIds;
							this.nbItems = 0;
							plugin.manager.setEmpCreaUsersValue(player, 1);
						}
						else {
							player.sendMessage(Config.empAddCreationJobBlockIdsSyntaxError);
						}
					}
				}
				event.setCancelled(true);
			}
			
			if (plugin.manager.getEmpCreaUsersValue(player) == 1) {
				// Asking for how much for each item
				// TODO Debug that shit !!!
				if (this.nbItems == 0) {
					this.nbItems = this.signCreationInfos.size();
					this.index = 0;
					this.currentId = 0;
					this.totalItems.clear();
				}
				if(nextId() != null) {
					if (plugin.manager.askHowMuchItems(player, this.index)) {
						Integer idInt = plugin.manager.isInteger(message);
						if (idInt != 0) {
							this.totalItems.add(this.signCreationInfos.get(index) + ":" + idInt);
							this.index++;
						}
					}
				}
				else {
					player.sendMessage("Quel est le salaire que vous proposez ?");
					plugin.manager.setEmpCreaUsersValue(player, 2);
				}
				event.setCancelled(true);
			}
			if (plugin.manager.getEmpCreaUsersValue(player) == 2) {
				// Asking for global price
				plugin.manager.setEmpCreaUsersValue(player, 3);
				event.setCancelled(true);
			}
			if (plugin.manager.getEmpCreaUsersValue(player) == 3) {
				
				Integer intMess = plugin.manager.isInteger(message);
				if (intMess != 0) {
					this.salary = intMess;
				}
				// Enregistrement de l' offre d' emploi
				if (Config.getDbFile().addJobOffer(plugin.blocksL.getSignJobName(), plugin.blocksL.getSignPos(), player.getName(), this.salary, this.totalItems.toString())) {
					player.sendMessage(Config.empAddOfferSuccess);
					plugin.manager.toggleCreaMode(player);
				}
				else {
					player.sendMessage(Config.empAddOfferError);
					plugin.manager.toggleCreaMode(player);
					plugin.blocksL.getBlock.breakNaturally();
				}
				event.setCancelled(true);
			} 
		}
	}
	
	private Integer nextId() {
		plugin.getLogger().info("passage!!! " + this.currentId);
		Integer nextId = null;
		if(this.currentId < this.nbItems) {
			nextId = this.currentId;
			this.currentId++;
		}
		return nextId;
	}
	
	@EventHandler
	public void onclick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getClickedBlock() != null) {
			String blockType = event.getClickedBlock().getType().toString();
			if ("WALL_SIGN".equals(blockType)) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains(Config.prefix)) {
					if (plugin.perms.has(player, "emp.use")) {
						if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
							if (player.getGameMode().equals(GameMode.CREATIVE)){
								event.setCancelled(true);
							}
							else {
								// Okayy let's left-interact
								
							}
						}
						if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if (player.getGameMode().equals(GameMode.CREATIVE)){
								event.setCancelled(true);
							}
							else {
								// Okayy let's right-interact
								
							}
						}
					}
					else {
						player.sendMessage(Config.noUsePermMessage);
					}
				}
			}
		}
	}
	
}
