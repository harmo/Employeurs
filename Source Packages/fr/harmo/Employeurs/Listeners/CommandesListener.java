package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
				else if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("list")) {
						if (args.length < 2) {
							ArrayList aMessages = new ArrayList();
							if (plugin.perms.has(player, "emp.list")) {
								ArrayList jobList = plugin.jobManager.getJobList();
								if (jobList.size() > 0) {
									for (int i = 0; i < jobList.size(); i++) {
										String[] aJob = (String[]) jobList.get(i);
										aMessages.add("======- " + aJob[0] + " -======");
										aMessages.add("||  Type : " + aJob[1]);
										aMessages.add("||  Blocs autorisés : " + aJob[2]);
									}
								}
								else {
									aMessages.add("||  " + Config.empEmptyJobList);
								}
							}
							else {
								aMessages.add("||  " + Config.noListPermMessage);
							}
							aMessages.add("----------------------------------");
							plugin.sendMessageList(player, aMessages);
							return true;
						}
						else {
							if (args[1].equalsIgnoreCase("offers")) {
								ArrayList aMessages = new ArrayList();
								if (plugin.perms.has(player, "emp.list.offers")) {
									ArrayList offerList = plugin.jobManager.getOfferList();
									if (offerList.size() > 0) {
										for (int i = 0; i < offerList.size(); i++) {
											String[] aOffer = (String[]) offerList.get(i);
											aMessages.add("======- " + aOffer[0] + " -======");
											aMessages.add("||  Employeur : " + aOffer[1]);
											aMessages.add("||  Salaire : " + aOffer[5] + " " + plugin.economy.currencyNamePlural());
											aMessages.add("||  Demande : ");
											String ids = aOffer[6].substring(1, aOffer[6].length()-1);
											String[] aIds = ids.split(", ");
											if (aIds.length > 0) {
												for (int n = 0; n < aIds.length; n++) {
													String[] aSplit = aIds[n].split(":");
													String item = Material.getMaterial(new Integer(aSplit[0])).toString();
													aMessages.add("||      ->  " + aSplit[1] + " de " + item);
												}
											}
											else {
												String[] aSplit = ids.split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												aMessages.add("||      ->  " + aSplit[1] + " de " + item);
											}
										}
									}
									else {
										aMessages.add("||  Aucune offre !");
									}
								}
								else {
									aMessages.add(Config.noListPermMessage);
								}
								aMessages.add("----------------------------------");
								plugin.sendMessageList(player, aMessages);
								return true;
							}
							if (args[1].equalsIgnoreCase("posts")) {
								// TODO separate list and infos
								ArrayList aMessages = new ArrayList();
								if (plugin.perms.has(player, "emp.list.posts")) {
									ArrayList postList = plugin.jobManager.getPostList();
									if (postList.size() > 0) {
										for (int i = 0; i < postList.size(); i++) {
											String[] aPost = (String[]) postList.get(i);
											aMessages.add("======- " + aPost[0] + " -======");
											aMessages.add("||  Employeur : " + aPost[1]);
											aMessages.add("||  Salaire : " + aPost[5] + " " + plugin.economy.currencyNamePlural());
											aMessages.add("||  Demande : ");
											String ids = aPost[6].substring(1, aPost[6].length()-1);
											String[] aIds = ids.split(", ");
											if (aIds.length > 0) {
												for (int n = 0; n < aIds.length; n++) {
													String[] aSplit = aIds[n].split(":");
													String item = Material.getMaterial(new Integer(aSplit[0])).toString();
													aMessages.add("||      ->  " + aSplit[1] + " de " + item);
												}
											}
											else {
												String[] aSplit = ids.split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												aMessages.add("||      ->  " + aSplit[1] + " de " + item);
											}
										}
									}
									else {
										aMessages.add("||  Aucune offre n'a ete acceptee !");
									}
								}
								else {
									aMessages.add(Config.noListPermMessage);
								}
								aMessages.add("----------------------------------");
								plugin.sendMessageList(player, aMessages);
								return true;
							}
						}
					}
					if (args[0].equalsIgnoreCase("quit")) {
						if (plugin.perms.has(player, "emp.quit")) {
							if (plugin.jobManager.havePost(player.getName())) {
								if (plugin.jobManager.quitJob(player)) {
									String[] aPostInfos = plugin.jobManager.getPostInfos(player.getName());
									int x = new Integer(aPostInfos[2]);
									int y = new Integer(aPostInfos[3]);
									int z = new Integer(aPostInfos[4]);
									Location origin = new Location(player.getWorld(), x, y, z);
									Block block = (Block) origin.getBlock();
									Sign sign = (Sign)block.getState();
									Location loc = sign.getLocation();
									plugin.blocksM.destroyChest(loc);
									sign.setLine(3, "");
									sign.update();
									player.sendMessage("Vous avez quitte cet emploi !");
								}
							}
							else {
								player.sendMessage("Vous n'avez aucun emploi !");
							}
							return true;
						}
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
			//////////////////////////////
			/* Console commands */
		       //////////////////////////////
			if (label.equalsIgnoreCase("emp")) {
				if (args.length == 0) {
					sendConsoleHelp();
					return true;
				}
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("list")) {
						if (args.length < 2) {
							ArrayList jobList = plugin.jobManager.getJobList();
							ArrayList aMessages = new ArrayList();
							if (jobList.size() > 0) {
								for (int i = 0; i < jobList.size(); i++) {
									String[] aJob = (String[]) jobList.get(i);
									aMessages.add("======- " + aJob[0] + " -======");
									aMessages.add("||  Type : " + aJob[1]);
									aMessages.add("||  Blocs autorises : " + aJob[2]);
								}
							}
							else {
								aMessages.add(Config.empEmptyJobList);
							}
							aMessages.add("----------------------------------");
							plugin.sendConsoleMessageList(aMessages);
						}
						else {
							if (args[1].equalsIgnoreCase("offers")) {
								ArrayList offerList = plugin.jobManager.getOfferList();
								ArrayList aMessages = new ArrayList();
								if (offerList.size() > 0) {
									for (int i = 0; i < offerList.size(); i++) {
										String[] aOffer = (String[]) offerList.get(i);
										aMessages.add("======- " + aOffer[0] + " -======");
										aMessages.add("||  Employeur : " + aOffer[1]);
										aMessages.add("||  Salaire : " + aOffer[5] + " " + plugin.economy.currencyNamePlural().toString());
										aMessages.add("||  Demande : ");
										String ids = aOffer[6].substring(1, aOffer[6].length()-1);
										String[] aIds = ids.split(", ");
										if (aIds.length > 0) {
											for (int n = 0; n < aIds.length; n++) {
												String[] aSplit = aIds[n].split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												aMessages.add("||      ->  " + aSplit[1] + " de " + item);
											}
										}
										else {
											String[] aSplit = ids.split(":");
											String item = Material.getMaterial(new Integer(aSplit[0])).toString();
											aMessages.add("||      ->  " + aSplit[1] + " de " + item);
										}
									}
								}
								else {
									aMessages.add("||  Aucune offre !");
								}
								aMessages.add("----------------------------------");
								plugin.sendConsoleMessageList(aMessages);
							}
							if (args[1].equalsIgnoreCase("posts")) {
								ArrayList postList = plugin.jobManager.getPostList();
								ArrayList aMessages = new ArrayList();
								if (postList.size() > 0) {
									for (int i = 0; i < postList.size(); i++) {
										String[] aPost = (String[]) postList.get(i);
										aMessages.add("======- " + aPost[0] + " -======");
										aMessages.add("||  Employeur : " + aPost[1]);
										aMessages.add("||  Salaire : " + aPost[5] + " " + plugin.economy.currencyNamePlural());
										aMessages.add("||  Demande restante : ");
										// TODO changer message si fichier coffre vide
										String ids = aPost[6].substring(1, aPost[6].length()-1);
										String[] aIds = ids.split(", ");
										if (aIds.length > 0) {
											for (int n = 0; n < aIds.length; n++) {
												String[] aSplit = aIds[n].split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												int x = new Integer(aPost[2]);
												int y = new Integer(aPost[3]);
												int z = new Integer(aPost[4]);
												Integer rest = plugin.jobManager.getRecolted(new Integer(aSplit[0]), aPost[7], x, y, z);
												aMessages.add("||      ->  " + rest + " de " + item);
											}
										}
										else {
											String[] aSplit = ids.split(":");
											String item = Material.getMaterial(new Integer(aSplit[0])).toString();
											int x = new Integer(aPost[2]);
											int y = new Integer(aPost[3]);
											int z = new Integer(aPost[4]);
											Integer rest = plugin.jobManager.getRecolted(new Integer(aSplit[0]), aPost[7], x, y, z);
											aMessages.add("||      ->  " + rest + " de " + item);
										}
									}
								}
								else {
									aMessages.add("||  Aucune offre n'a ete acceptee !");
								}
								aMessages.add("----------------------------------");
								plugin.sendConsoleMessageList(aMessages);
							}
						}
					}
				}
				return true;
			}
			else {
				plugin.getLogger().info("Commande utilisable seulement en jeu !");
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

	private void sendConsoleHelp() {
		String[] aMessages = {
			"====== Employeurs - Help =======",
			Config.helpList,
			Config.helpAdd
		};
		plugin.sendConsoleMessageList(Arrays.asList(aMessages));
	}

}
