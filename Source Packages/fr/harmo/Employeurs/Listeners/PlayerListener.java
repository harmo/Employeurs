package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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

	public PlayerListener(Employeurs plugin) {
		this.plugin = plugin;				
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		// ADMIN JOB ADD MODE
		if (plugin.jobCreation.isInAddMode(player)) {
			if (plugin.jobCreation.getEmpUsersValue(player) == 0) {
				// Asking job name
				String jobName = plugin.jobCreation.addJobName(player, message);
				if (jobName != null) {
					plugin.jobCreation.setEmpUsersValue(player, 1);
					String[] messageList = {
						Config.empAddCreationJobNamePrefixSuccess + " " + jobName,
						Config.empAddCreationJobType,
						"break, drop, place, kill, craft"
					};
					plugin.sendMessageList(player, Arrays.asList(messageList));
				}
				event.setCancelled(true);
			}
			else if (plugin.jobCreation.getEmpUsersValue(player) == 1) {
				// Asking job type
				String jobType = plugin.jobCreation.addJobType(player, message);
				if (jobType != null) {
					plugin.jobCreation.setEmpUsersValue(player, 2);
					String[] messageList = {
						Config.empAddCreationJobBlockIds1,
						Config.empAddCreationJobBlockIds2
					};
					plugin.sendMessageList(player, Arrays.asList(messageList));
				}
				event.setCancelled(true);
			}
			else if (plugin.jobCreation.getEmpUsersValue(player) == 2) {
				// Asking for which items
				ArrayList aRefusedIds = plugin.jobCreation.addBlockIds(player, message);
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
						ArrayList aAcceptedIds = plugin.blocksM.getAcceptedIds();
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
								Config.getDbFile().setJob(plugin.jobCreation.getJobName(), plugin.jobCreation.getJobType(), aAcceptedIds);
							}
							else {
								// Enregistrement en bdd
								
							}
							
							player.sendMessage(Config.empAddCreationSuccess);
							plugin.jobCreation.toggleAddMode(player);
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
		if (plugin.offerCreation.isInCreationMode(player)) {
			if (plugin.offerCreation.getEmpCreaUsersValue(player) == 0) {
				// Asking for which items
				ArrayList aRefusedIds = plugin.offerCreation.addBlocksInOffer(player, message);
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
						ArrayList aAcceptedIds = plugin.blocksM.getAcceptedIds();
						if (aAcceptedIds.size() > 0) {
							String success = "";
							for (int i = 0; i < aAcceptedIds.size(); i++) {
								if (i == aAcceptedIds.size() - 1)
									success += aAcceptedIds.get(i) ;
								else
									success += aAcceptedIds.get(i) + ", ";
							}
							String[] messageList = {
								"ids ajoutés :",
								success
							};
							plugin.sendMessageList(player, Arrays.asList(messageList));
							this.signCreationInfos= aAcceptedIds;
							this.nbItems = this.signCreationInfos.size();
							this.index = 0;
							this.totalItems.clear();
							plugin.offerCreation.setEmpCreaUsersValue(player, 1);
							
						}
						else {
							player.sendMessage(Config.empAddCreationJobBlockIdsSyntaxError);
						}
					}
				}
				event.setCancelled(true);
			}
			
			if (plugin.offerCreation.getEmpCreaUsersValue(player) == 1) {
				// Asking for how much for each item
				Integer idInt = plugin.tests.isInteger(message);
				if (idInt != 0) {
					if (plugin.offerCreation.getEmpCreaNumberIdValue(player) == 1) {
						this.totalItems.add(this.signCreationInfos.get(index) + ":" + idInt);
						if (this.index < this.nbItems - 1) {
							this.index++;
						}
						else {
							plugin.offerCreation.toggleNumberIdMode(player);
							plugin.offerCreation.setEmpCreaUsersValue(player, 2);
						}
						if (plugin.offerCreation.askHowMuchItems(player, this.index)) {
							plugin.offerCreation.setEmpCreaNumberIdValue(player, 0);
						}

					}
					else {
						this.totalItems.add(this.signCreationInfos.get(index) + ":" + idInt);
						if (this.index < this.nbItems - 1) {
							this.index++;
						}
						else {
							plugin.offerCreation.toggleNumberIdMode(player);
							plugin.offerCreation.setEmpCreaUsersValue(player, 2);
						}
						if (plugin.offerCreation.askHowMuchItems(player, this.index)) {
							plugin.offerCreation.setEmpCreaNumberIdValue(player, 1);
						}
					}
				}
				else {
					if (plugin.offerCreation.askHowMuchItems(player, this.index)) {
						plugin.offerCreation.toggleNumberIdMode(player);
					}
				}
				event.setCancelled(true);
			}
			if (plugin.offerCreation.getEmpCreaUsersValue(player) == 2) {
				// TODO meilleure gestion des erreurs
				String[] messageList = {
					"Quel est le salaire que vous proposez ?",
					"Indiquez le en tapant salaire=<montant>"
				};
				plugin.sendMessageList(player, Arrays.asList(messageList));
				String salary = plugin.tests.isSalarySyntaxIsOk(message);
				if (!salary.equals("noSalaryError") && !salary.equals("introError")) {
					Integer intMess = plugin.tests.isInteger(salary);
					if (intMess != 0) {
						this.salary = intMess;
						plugin.offerCreation.setEmpCreaUsersValue(player, 3);
					}
				}
				else {
					player.sendMessage(salary);
				}
				event.setCancelled(true);
			} 
			if (plugin.offerCreation.getEmpCreaUsersValue(player) == 3) {
				// Enregistrement de l' offre d' emploi
				if (Config.getDbFile().addJobOffer(plugin.blocksL.getSignJobName(), plugin.blocksL.getSignPos(), player.getName(), this.salary, this.totalItems.toString())) {
					player.sendMessage(Config.empAddOfferSuccess);
					plugin.offerCreation.toggleCreaMode(player);
				}
				else {
					player.sendMessage(Config.empAddOfferError);
					plugin.offerCreation.toggleCreaMode(player);
					plugin.blocksL.getBlock.breakNaturally();
				}
				event.setCancelled(true);
			}
		}
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
								// Display Job infos
							}
						}
						if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if (player.getGameMode().equals(GameMode.CREATIVE)){
								event.setCancelled(true);
							}
							else {
								if (!player.isSneaking()) {
									// Display how to get the job
								}
								else {
									// Player get the job !
								}
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
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		Item item = event.getItemDrop();
		
	}
	
}
