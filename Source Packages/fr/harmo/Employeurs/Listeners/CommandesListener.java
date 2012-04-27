package fr.harmo.Employeurs.Listeners;

import com.sun.management.VMOption;
import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
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
							if (plugin.perms.has(player, "emp.list")) {
								ArrayList jobList = plugin.jobManager.getJobList();
								if (jobList.size() > 0) {
									for (int i = 0; i < jobList.size(); i++) {
										String[] aJob = (String[]) jobList.get(i);
										player.sendMessage("======- " + aJob[0] + " -======");
										player.sendMessage("Type : " + aJob[1]);
										player.sendMessage("Blocs autorisés : " + aJob[2]);
									}
								}
								else {
									player.sendMessage(Config.empEmptyJobList);
								}
							}
							else {
								player.sendMessage(Config.noListPermMessage);
							}
							return true;
						}
						else {
							if (args[1].equalsIgnoreCase("offers")) {
								if (plugin.perms.has(player, "emp.list.offers")) {
									ArrayList offerList = plugin.jobManager.getOfferList();
									if (offerList.size() > 0) {
										for (int i = 0; i < offerList.size(); i++) {
											String[] aOffer = (String[]) offerList.get(i);
											player.sendMessage("======- " + aOffer[0] + " -======");
											player.sendMessage("Employeur : " + aOffer[1]);
											player.sendMessage("Salaire : " + aOffer[5] + " " + plugin.economy.currencyNamePlural());
											player.sendMessage("Demande : ");
											String ids = aOffer[6].substring(1, aOffer[6].length()-1);
											String[] aIds = ids.split(", ");
											if (aIds.length > 0) {
												for (int n = 0; n < aIds.length; n++) {
													String[] aSplit = aIds[n].split(":");
													String item = Material.getMaterial(new Integer(aSplit[0])).toString();
													player.sendMessage("    ->  " + aSplit[1] + " de " + item);
												}
											}
											else {
												String[] aSplit = ids.split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												player.sendMessage("    ->  " + aSplit[1] + " de " + item);
											}
										}
									}
									else {
										player.sendMessage("Aucune offre !");
									}
								}
								else {
									player.sendMessage(Config.noListPermMessage);
								}
								return true;
							}
							if (args[1].equalsIgnoreCase("posts")) {
								if (plugin.perms.has(player, "emp.list.posts")) {
									ArrayList postList = plugin.jobManager.getPostList();
									if (postList.size() > 0) {
										for (int i = 0; i < postList.size(); i++) {
											String[] aPost = (String[]) postList.get(i);
											player.sendMessage("======- " + aPost[0] + " -======");
											player.sendMessage("Employeur : " + aPost[1]);
											player.sendMessage("Salaire : " + aPost[5] + " " + plugin.economy.currencyNamePlural());
											player.sendMessage("Demande : ");
											String ids = aPost[6].substring(1, aPost[6].length()-1);
											String[] aIds = ids.split(", ");
											if (aIds.length > 0) {
												for (int n = 0; n < aIds.length; n++) {
													String[] aSplit = aIds[n].split(":");
													String item = Material.getMaterial(new Integer(aSplit[0])).toString();
													player.sendMessage("    ->  " + aSplit[1] + " de " + item);
												}
											}
											else {
												String[] aSplit = ids.split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												player.sendMessage("    ->  " + aSplit[1] + " de " + item);
											}
										}
									}
									else {
										player.sendMessage("Aucune offre n'a ete acceptee !");
									}
								}
								else {
									player.sendMessage(Config.noListPermMessage);
								}
								return true;
							}
						}
					}
					if (args[0].equalsIgnoreCase("quit")) {
						if (plugin.perms.has(player, "emp.quit")) {
							if (plugin.jobManager.havePost(player.getName())) {
								String[] aPostInfos = plugin.jobManager.getPostInfos(player.getName());
								int x = new Integer(aPostInfos[2]);
								int y = new Integer(aPostInfos[3]);
								int z = new Integer(aPostInfos[4]);
								Location origin = new Location(player.getWorld(), x, y, z);
								Block block = (Block) origin.getBlock();
								Sign sign = (Sign)block.getState();
								if (plugin.jobManager.quitJob(player.getName())) {
									sign.setLine(3, "");
									sign.update();
									player.sendMessage("Vous avez quitte cet emploi !");
									// TODO reload de la sign
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
			if (label.equalsIgnoreCase("emp")) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("list")) {
						if (args.length < 2) {
							ArrayList jobList = plugin.jobManager.getJobList();
							if (jobList.size() > 0) {
								for (int i = 0; i < jobList.size(); i++) {
									String[] aJob = (String[]) jobList.get(i);
									plugin.getLogger().info("======- " + aJob[0] + " -======");
									plugin.getLogger().info("Type : " + aJob[1]);
									plugin.getLogger().info("Blocs autorises : " + aJob[2]);
								}
							}
							else {
								plugin.getLogger().info(Config.empEmptyJobList);
							}
						}
						else {
							if (args[1].equalsIgnoreCase("offers")) {
								ArrayList offerList = plugin.jobManager.getOfferList();
								if (offerList.size() > 0) {
									for (int i = 0; i < offerList.size(); i++) {
										String[] aOffer = (String[]) offerList.get(i);
										plugin.getLogger().info("======- " + aOffer[0] + " -======");
										plugin.getLogger().info("Employeur : " + aOffer[1]);
										plugin.getLogger().info("Salaire : " + aOffer[5] + " " + plugin.economy.currencyNamePlural().toString());
										plugin.getLogger().info("Demande : ");
										String ids = aOffer[6].substring(1, aOffer[6].length()-1);
										String[] aIds = ids.split(", ");
										if (aIds.length > 0) {
											for (int n = 0; n < aIds.length; n++) {
												String[] aSplit = aIds[n].split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												plugin.getLogger().info("    ->  " + aSplit[1] + " de " + item);
											}
										}
										else {
											String[] aSplit = ids.split(":");
											String item = Material.getMaterial(new Integer(aSplit[0])).toString();
											plugin.getLogger().info("    ->  " + aSplit[1] + " de " + item);
										}
									}
								}
								else {
									plugin.getLogger().info("Aucune offre !");
								}
							}
							if (args[1].equalsIgnoreCase("posts")) {
								ArrayList postList = plugin.jobManager.getPostList();
								if (postList.size() > 0) {
									for (int i = 0; i < postList.size(); i++) {
										String[] aPost = (String[]) postList.get(i);
										plugin.getLogger().info("======- " + aPost[0] + " -======");
										plugin.getLogger().info("Employeur : " + aPost[1]);
										plugin.getLogger().info("Salaire : " + aPost[5] + " " + plugin.economy.currencyNamePlural());
										plugin.getLogger().info("Demande : ");
										String ids = aPost[6].substring(1, aPost[6].length()-1);
										String[] aIds = ids.split(", ");
										if (aIds.length > 0) {
											for (int n = 0; n < aIds.length; n++) {
												String[] aSplit = aIds[n].split(":");
												String item = Material.getMaterial(new Integer(aSplit[0])).toString();
												plugin.getLogger().info("    ->  " + aSplit[1] + " de " + item);
											}
										}
										else {
											String[] aSplit = ids.split(":");
											String item = Material.getMaterial(new Integer(aSplit[0])).toString();
											plugin.getLogger().info("    ->  " + aSplit[1] + " de " + item);
										}
									}
								}
								else {
									plugin.getLogger().info("Aucune offre n'a ete acceptee !");
								}
							}
						}
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
