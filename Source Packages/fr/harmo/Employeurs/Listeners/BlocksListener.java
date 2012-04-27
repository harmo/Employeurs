package fr.harmo.Employeurs.Listeners;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author HarmO
 */
public class BlocksListener implements Listener {

	private Employeurs plugin;
	private static final BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	private String signJobName;
	private String signJobType;
	private ArrayList aSignJobAuthorizedIds;
	private String signPos;
	public Block getBlock;

	public BlocksListener(Employeurs plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event) throws Exception {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material material = block.getType();
		if ("WALL_SIGN".equals(material.toString())) {
			Sign sign = (Sign) event.getBlock().getState();
			if (sign.getLine(0).contains(Config.prefix)) {
				if (plugin.perms.has(player, "emp.admin.destroy")) {
					if (!Config.mysql) {
						// File
						if (Config.getDbFile().deleteOffer(sign.getLine(1), block.getX(), block.getY(), block.getZ())) {
							player.sendMessage(Config.empDeleteOfferSuccess);
							if (plugin.offerCreation.isInCreationMode(player)) {
								plugin.offerCreation.toggleCreaMode(player);
							}
							if (plugin.offerCreation.isInNumberIdMode(player)) {
								plugin.offerCreation.toggleNumberIdMode(player);
							}
						}
						else{
							player.sendMessage(Config.empDeleteOfferError);
						}
					}
				}
				else {
					player.sendMessage(Config.empDeleteOfferNoPerm);
					event.setCancelled(true);
				}
			}
		}
		else {
			for (BlockFace face : faces) {
				material = block.getRelative(face).getType();
				Block nearBlock = block.getRelative(face);
				if ("WALL_SIGN".equals(material.toString())) {
					if ((nearBlock.getData() == 3 & face.equals(BlockFace.WEST)) || (nearBlock.getData() == 2 & face.equals(BlockFace.EAST)) || (nearBlock.getData() == 4 & face.equals(BlockFace.NORTH)) || (nearBlock.getData() == 5 & face.equals(BlockFace.SOUTH))) {
						Sign sign = (Sign) block.getRelative(face).getState();
						if (sign.getLine(0).contains(Config.prefix)) {
							if (plugin.perms.has(player, "emp.admin.destroy")) {
								if (!Config.mysql) {
									// File
									if (Config.getDbFile().deleteOffer(sign.getLine(1), sign.getX(), sign.getY(), sign.getZ())) {
										player.sendMessage(Config.empDeleteOfferSuccess);
										if (plugin.offerCreation.isInCreationMode(player)) {
											plugin.offerCreation.toggleCreaMode(player);
										}
										if (plugin.offerCreation.isInNumberIdMode(player)) {
											plugin.offerCreation.toggleNumberIdMode(player);
										}
									}
									else{
										player.sendMessage(Config.empDeleteOfferError);
									}
								}
							}
							else {
								player.sendMessage(Config.empDeleteOfferNoPerm);
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onSignPlace(SignChangeEvent event) {
		Player player = event.getPlayer();
		String ligne1 = event.getLine(0);
		String ligne2 = event.getLine(1);
		String ligne3 = event.getLine(2);
		String ligne4 = event.getLine(3);
		Integer posX = event.getBlock().getX();
		Integer posY = event.getBlock().getY();
		Integer posZ = event.getBlock().getZ();
		Material material = event.getBlock().getType();
		if ("WALL_SIGN".equals(material.toString())) {
			if (ligne1.equalsIgnoreCase(Config.prefix)) {
				if (plugin.perms.has(player, "emp.create")) {
					if (!Config.mysql) {
						// File
						if (Config.getDbFile().isInJobList(ligne2)) {
							event.setLine(0, Config.signColor + Config.prefix);
							event.setLine(1, Config.signColor + ligne2.toUpperCase());
							this.signJobName = ligne2;
							this.signPos = posX.toString() + "::" + posY.toString() + "::" + posZ.toString();
							plugin.offerCreation.toggleCreaMode(player);
							plugin.offerCreation.setEmpCreaUsersValue(player, 0);
							this.aSignJobAuthorizedIds = Config.getDbFile().getJobAuthorizedIds(this.signJobName);
							this.getBlock = event.getBlock();
						}
						else {
							player.sendMessage(Config.empCreateSignNoJob);
							event.getBlock().breakNaturally();
						}
					}
				}
				else {
					player.sendMessage(Config.noCreatePermMessage);
					event.getBlock().breakNaturally();
				}
			}
		}
	}

	public String getSignJobName() {
		return this.signJobName;
	}

	public String getSignJobType() {
		return this.signJobType;
	}

	public String getSignPos() {
		return this.signPos;
	}

	public ArrayList getSignJobAuthorizedIds() {
		return this.aSignJobAuthorizedIds;
	}

}
