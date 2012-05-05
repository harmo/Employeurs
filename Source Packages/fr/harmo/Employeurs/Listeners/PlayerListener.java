package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
	private boolean askSalary = false;
	private HashMap<Integer, Integer> aItems = new HashMap<>();

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
								Config.getDbFile().addJob(plugin.jobCreation.getJobName(), plugin.jobCreation.getJobType(), aAcceptedIds, player.getWorld().getName());
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
							if (plugin.offerCreation.askHowMuchItems(player, this.index)) {
								plugin.offerCreation.setEmpCreaNumberIdValue(player, 0);
							}
						}
						else {
							plugin.offerCreation.toggleNumberIdMode(player);
							plugin.offerCreation.setEmpCreaUsersValue(player, 2);
							String[] messageList = {
								"Quel est le salaire que vous proposez ?",
								"Indiquez le en tapant salaire=<montant>"
							};
							plugin.sendMessageList(player, Arrays.asList(messageList));
						}


					}
					else {
						this.totalItems.add(this.signCreationInfos.get(index) + ":" + idInt);
						if (this.index < this.nbItems - 1) {
							this.index++;
							if (plugin.offerCreation.askHowMuchItems(player, this.index)) {
								plugin.offerCreation.setEmpCreaNumberIdValue(player, 1);
							}
						}
						else {
							plugin.offerCreation.toggleNumberIdMode(player);
							plugin.offerCreation.setEmpCreaUsersValue(player, 2);
							String[] messageList = {
								"Quel est le salaire que vous proposez ?",
								"Indiquez le en tapant salaire=<montant>"
							};
							plugin.sendMessageList(player, Arrays.asList(messageList));
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
				if (this.askSalary == true) {
					String error = plugin.tests.isSalarySyntaxIsOk(message);
					if (!error.equals("noSalaryError") && !error.equals("introError")) {
						Integer intMess = plugin.tests.isInteger(error);
						if (intMess != 0) {
							this.salary = intMess;
							plugin.offerCreation.setEmpCreaUsersValue(player, 3);
						}
					}
					else {
						//player.sendMessage(salary);
					}
				}
				else {
					this.askSalary = true;
				}
				event.setCancelled(true);
			}
			if (plugin.offerCreation.getEmpCreaUsersValue(player) == 3) {
				// Enregistrement de l' offre d' emploi
				if (Config.getDbFile().addOffer(plugin.blocksL.getSignJobName(), plugin.blocksL.getSignPos(), player.getName(), this.salary, this.totalItems.toString(), player.getWorld().getName())) {
					player.sendMessage(Config.empAddOfferSuccess);
					plugin.offerCreation.toggleCreaMode(player);
					String[] aPos = plugin.blocksL.getSignPos().split("::");
					Location loc = new Location(player.getWorld(), new Integer(aPos[0]), new Integer(aPos[1]), new Integer(aPos[2]));
					Sign sign = (Sign) loc.getBlock().getState();
					sign.setLine(2, this.salary.toString() + " " + plugin.economy.currencyNameSingular());
					sign.update();
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
								String[] aOffer = plugin.jobManager.getJobInfos(sign.getLocation());
								ArrayList aMessages = new ArrayList();
								aMessages.add("======- " + aOffer[0] + " -======");
								aMessages.add("Employeur : " + aOffer[1]);
								aMessages.add("Salaire : " + aOffer[5] + " " + plugin.economy.currencyNamePlural());
								aMessages.add("Demande : ");
								String ids = aOffer[6].substring(1, aOffer[6].length()-1);
								String[] aIds = ids.split(", ");
								if (aIds.length > 0) {
									for (int n = 0; n < aIds.length; n++) {
										String[] aSplit = aIds[n].split(":");
										String item = Material.getMaterial(new Integer(aSplit[0])).toString();
										aMessages.add("    ->  " + aSplit[1] + " de " + item);
									}
								}
								else {
									String[] aSplit = ids.split(":");
									String item = Material.getMaterial(new Integer(aSplit[0])).toString();
									aMessages.add("    ->  " + aSplit[1] + " de " + item);
								}
								plugin.sendMessageList(player, aMessages);
							}
						}
						if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if (player.getGameMode().equals(GameMode.CREATIVE)){
								event.setCancelled(true);
							}
							else {
								if (!player.isSneaking()) {
									if (!plugin.jobManager.havePost(player.getName())) {
										String[] defaultMessagesList = {
											"Click gauche pour afficher les infos de l'offre.",
											"Sneak + click droit pour accepter l'offre."
										};
										plugin.sendMessageList(player, Arrays.asList(defaultMessagesList));
									}
									else {
										String already;
										if (plugin.jobManager.haveThisPost(player.getName(), sign.getLocation())) {
											already = "Vous avez deja cet emploi";
										}
										else {
											already = "Vous avez deja un emploi";
										}
										String[] defaultMessagesList = {
											already,
											"tapez /emp quit pour quitter cet emploi."
										};
										plugin.sendMessageList(player, Arrays.asList(defaultMessagesList));
									}
								}
								else {
									Location loc = sign.getLocation();
									if (!plugin.jobManager.isSignHasChest(loc)) {
										String chestCreation = plugin.blocksM.createChest(loc);
										if (chestCreation.equals("bottomBlockError")) {
											player.sendMessage("Veuillez detruire ce qui se trouve sous le panneau.");
											event.setCancelled(true);
										}
										else if (chestCreation.equals("rightBlockError")) {
											player.sendMessage("Veuillez detruire ce qui se trouve en bas a droite du panneau.");
											event.setCancelled(true);
										}
										else {
											if (plugin.jobManager.takeJob(player, sign.getLocation())) {
												sign.setLine(3, player.getName().toString());
												sign.update();
												player.sendMessage("Vous avez le poste !!");
											}
											else {
												plugin.blocksM.destroyChest(loc);
											}
										}
									}
									else {
										plugin.jobManager.registerPlayerChest(player, player.getWorld().getName());
										player.sendMessage("Coffre mis a jour !");
										HashMap<Integer, Integer> restItems = plugin.jobManager.getRestItems(loc);
										int restSize = restItems.size();
										if (restSize > 0 ) {
											for (Map.Entry<Integer, Integer> item : restItems.entrySet()) {
												player.sendMessage("Il manque " + item.getValue() + " de " + Material.getMaterial(item.getKey()).toString());
											}
										}
										else {
											// change sign status (wait boss aprovation)
											plugin.blocksM.changeSignStatus(sign);
											player.sendMessage("Mission accomplie, veuillez attendre la confirmation de l employeur");
										}
									}
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
	public void onChestOpen(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (block.getType().toString().equals("CHEST")) {
			if (plugin.jobManager.isJobChest(block)) {
				if (!plugin.perms.has(player, "empadm") || !plugin.jobManager.isPlayerAuthorized(player, block)) {
					player.sendMessage("Vous ne pouvez pas ouvrir ce coffre");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onChestItemAdd(InventoryClickEvent event) {
		String type = event.getInventory().getType().toString();
		Player player = (Player) event.getWhoClicked();
		if (type.equals("CHEST")) {
			DoubleChest chest = (DoubleChest) event.getInventory().getHolder();
			Chest lChest = (Chest) chest.getLeftSide();
			Block lBlock = lChest.getBlock();
			if (plugin.jobManager.isJobChest(lBlock)) {
				if (event.getRawSlot() < 54) {
					 // TODO debug items exchange with not authorized one
					if (!plugin.jobManager.isItemInPost(event.getCurrentItem().getTypeId(), player) && !plugin.jobManager.isItemInPost(event.getCursor().getTypeId(), player)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String worldname = player.getWorld().getName();
		// TODO register chest if player got one
		plugin.jobManager.registerPlayerChest(player, worldname);
	}
}
