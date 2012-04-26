package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

/**
 *
 * @author HarmO
 */
public class JobCreation {
	
	private Employeurs plugin;
	private HashMap<Player, Integer> empAddPlayers = new HashMap();
	private String newJobName;
	private String newJobType;

	public JobCreation(Employeurs plugin) {
		this.plugin = plugin;
	}
	
	public boolean isInAddMode(Player player) {
		return this.empAddPlayers.containsKey(player);
	}
	public void toggleAddMode(Player player) {
		if (this.empAddPlayers.containsKey(player)) {
			this.empAddPlayers.remove(player);
			player.sendMessage(Config.empAddCreationOff);
		}
		else {
			this.empAddPlayers.put(player, Integer.valueOf(0));
			String[] defaultMessagesList = {
				Config.empAddCreationOn,
				Config.empAddCreationStop,
				Config.empAddCreationJobName
			};
			this.plugin.sendMessageList(player, Arrays.asList(defaultMessagesList));
		}
	}
	public void setEmpUsersValue(Player player, Integer value) {
		if (this.empAddPlayers.containsKey(player)) {
			this.empAddPlayers.remove(player);
			this.empAddPlayers.put(player, value);
		}
	}
	public int getEmpUsersValue(Player player) {
		if (this.empAddPlayers.containsKey(player)) {
			return ((Integer) this.empAddPlayers.get(player)).intValue();
		}
		return 0;
	}

	public String addJobName(Player player, String message) {
		String[] aSplit = message.split("=");
		if (aSplit.length > 1) {
			if (aSplit[0].equals(Config.empAddCreationJobNamePrefix)) {
				this.newJobName = aSplit[1];
				return this.newJobName;
			}
			else {
				player.sendMessage(Config.empAddCreationJobNamePrefixError);
				return null;
			}
		}
		else {
			player.sendMessage(Config.empAddCreationJobNamePrefixError);
			return null;
		}
	}
	public String addJobType(Player player, String message) {
		String[] aSplit = message.split("=");
		if (aSplit.length > 1) {
			if (aSplit[0].equals("type")) {
				if (aSplit[1].equals("break") || aSplit[1].equals("drop") || aSplit[1].equals("place") || aSplit[1].equals("kill") || aSplit[1].equals("craft")) {
					this.newJobType = aSplit[1];
					return this.newJobType;
				}
				else {
					player.sendMessage(Config.empAddCreationJobTypeResponseError);
					player.sendMessage("break, drop, place, kill, craft");
					return null;
				}
			}
			else {
				player.sendMessage(Config.empAddCreationJobTypeError);
				return null;
			}
		}
		else {
			player.sendMessage(Config.empAddCreationJobTypeError);
			return null;
		}
	}
	public ArrayList addBlockIds(Player player, String message) {
		plugin.blocksM.clearAcceptedIds();
		plugin.blocksM.clearRefusedIds();
		String syntaxTest = plugin.tests.isSyntaxIsOk(message);
		if ("ok".equals(syntaxTest)) {
			String[] idSplit = message.split("=")[1].split("_");
			for (int i = 0; i < idSplit.length; i++) {
				Integer idInt = plugin.tests.isInteger(idSplit[i]);
				if (idInt != 0) {
					switch (this.newJobType) {
						case "break": {
							if (!plugin.tests.isBreakBlock(idInt)) {
								plugin.blocksM.addRefusedId(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								plugin.blocksM.addAcceptedIdString(material.getItemType().toString().toLowerCase());
							}
							break;
						}
						case "place": {
							if (!plugin.tests.isPlaceBlock(idInt)) {
								plugin.blocksM.addRefusedId(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								plugin.blocksM.addAcceptedIdString(material.getItemType().toString().toLowerCase());
							}
							break;
						}
						case "drop": {
							if (!plugin.tests.isDropBlock(idInt)) {
								plugin.blocksM.addRefusedId(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								plugin.blocksM.addAcceptedIdString(material.getItemType().toString().toLowerCase());
							}
							break;
						}
						case "craft": {
							if (!plugin.tests.isCraftBlock(idInt)) {
								plugin.blocksM.addRefusedId(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								plugin.blocksM.addAcceptedIdString(material.getItemType().toString().toLowerCase());
							}
							break;
						}
					}
				}
				else {
					player.sendMessage(Config.empAddCreationJobBlockIdsIntegerError);
				}
			}
			return plugin.blocksM.getRefusedIds();
		}
		else {
			switch (syntaxTest) {
				case "introError":
					player.sendMessage(Config.empAddCreationJobBlockIds3);
					break;
				case "noIdsError":
					player.sendMessage(Config.empAddCreationJobBlockIdsFullError);
					break;
				default:
					player.sendMessage(Config.empAddCreationJobBlockIdsSyntaxError);
					break;
			}
			plugin.blocksM.addRefusedId("error");
			return plugin.blocksM.getRefusedIds();
		}
	}

	public String getJobName() {
		return newJobName;
	}
	public String getJobType() {
		return newJobType;
	}
	
}
