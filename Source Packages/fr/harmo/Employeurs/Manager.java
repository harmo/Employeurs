package fr.harmo.Employeurs;

import fr.harmo.Employeurs.Config.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

/**
 *
 * @author
 * HarmO
 */
public class Manager {

	private Employeurs plugin;
	public final HashMap<Player, Integer> empAddPlayers = new HashMap();
	private String newJobName;
	private String newJobType;
	private ArrayList aAcceptedIds = new ArrayList();
	private ArrayList aRefusedIds = new ArrayList();
	private ArrayList aBreakIdsList = new ArrayList();
	private ArrayList aPlaceIdsList = new ArrayList();
	private ArrayList aCraftIdsList = new ArrayList();
	private ArrayList aDropIdsList = new ArrayList();
	public final HashMap<Player, Integer> empCreaPlayers = new HashMap();

	public Manager(Employeurs plugin) {
		this.plugin = plugin;
		this.aBreakIdsList = Config.getAuthorizedIds("break");
		this.aPlaceIdsList = Config.getAuthorizedIds("place");
		this.aCraftIdsList = Config.getAuthorizedIds("craft");
		this.aDropIdsList = Config.getAuthorizedIds("drop");
	}

	public void getJobList(Player player) {
		if (!Config.mysql) {
			// File
			try {
				Config.getDbFile().getJobList();
				ArrayList jobList = Config.getDbFile().jobList;
				if (jobList.size() > 0) {
					for (int i = 0; i < jobList.size(); i++) {
						player.sendMessage(jobList.get(i).toString());
					}
				}
				else {
					player.sendMessage(Config.empEmptyJobList);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
	}

	public void getJobListFromConsole() {
		if (!Config.mysql) {
			// File
			try {
				Config.getDbFile().getJobList();
				ArrayList jobList = Config.getDbFile().jobList;
				if (jobList.size() > 0) {
					for (int i = 0; i < jobList.size(); i++) {
						plugin.getLogger().info(jobList.get(i).toString());
					}
				}
				else {
					plugin.getLogger().info(Config.empEmptyJobList);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
	}

	public ArrayList getAcceptedIds() {
		return aAcceptedIds;
	}

	/********************************************/
	/*                       ADMIN ADD MODE                          */
	/********************************************/
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
		aRefusedIds.clear();
		aAcceptedIds.clear();
		String syntaxTest = isSyntaxIsOk(message);
		if ("ok".equals(syntaxTest)) {
			String[] idSplit = message.split("=")[1].split("_");
			for (int i = 0; i < idSplit.length; i++) {
				Integer idInt = isInteger(idSplit[i]);
				if (idInt != 0) {
					switch (this.newJobType) {
						case "break": {
							if (!isBreakBlock(idInt)) {
								aRefusedIds.add(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								aAcceptedIds.add(material.getItemType().toString().toLowerCase());
							}
							break;
						}
						case "place": {
							if (!isPlaceBlock(idInt)) {
								aRefusedIds.add(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								aAcceptedIds.add(material.getItemType().toString().toLowerCase());
							}
							break;
						}
						case "drop": {
							if (!isDropBlock(idInt)) {
								aRefusedIds.add(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								aAcceptedIds.add(material.getItemType().toString().toLowerCase());
							}
							break;
						}
						case "craft": {
							if (!isCraftBlock(idInt)) {
								aRefusedIds.add(idSplit[i]);
							}
							else {
								MaterialData material = new MaterialData(idInt);
								aAcceptedIds.add(material.getItemType().toString().toLowerCase());
							}
							break;
						}
					}
				}
				else {
					player.sendMessage(Config.empAddCreationJobBlockIdsIntegerError);
				}
			}
			return aRefusedIds;
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
			aRefusedIds.add("error");
			return aRefusedIds;
		}
	}

	public String getJobName() {
		return newJobName;
	}

	public String getJobType() {
		return newJobType;
	}

	/********************************************/
	/*                    SIGN CREATION MODE                       */
	/********************************************/
	public boolean isInCreationMode(Player player) {
		return this.empCreaPlayers.containsKey(player);
	}

	public void toggleCreaMode(Player player) {
		if (this.empCreaPlayers.containsKey(player)) {
			this.empCreaPlayers.remove(player);
			player.sendMessage(Config.empSignCreationOff);
		}
		else {
			this.empCreaPlayers.put(player, Integer.valueOf(0));
			String[] defaultMessagesList = {
				Config.empSignCreationOn,
				Config.empSignCreationBlocks1,
				Config.empSignCreationBlocks2
			};
			this.plugin.sendMessageList(player, Arrays.asList(defaultMessagesList));
		}
	}

	public void setEmpCreaUsersValue(Player player, Integer value) {
		if (this.empCreaPlayers.containsKey(player)) {
			this.empCreaPlayers.remove(player);
			this.empCreaPlayers.put(player, value);
		}
	}

	public int getEmpCreaUsersValue(Player player) {
		if (this.empCreaPlayers.containsKey(player)) {
			return ((Integer) this.empCreaPlayers.get(player)).intValue();
		}
		return 0;
	}

	public ArrayList addBlocksInOffer(Player player, String message) {
		this.aRefusedIds.clear();
		this.aAcceptedIds.clear();
		String syntaxTest = isSyntaxIsOk(message);
		if ("ok".equals(syntaxTest)) {
			String[] idSplit = message.split("=")[1].split("_");
			ArrayList signJobAuthorizedIds = plugin.blocksL.getSignJobAuthorizedIds();
			for (int i = 0; i < idSplit.length; i++) {
				Integer idInt = isInteger(idSplit[i]);
				if (idInt != 0) {
					ArrayList aIdsOk = new ArrayList();
					for (Object oItem : signJobAuthorizedIds) {
						String item = oItem.toString();
						item = item.replace("[", "").replace("]", "");
						Material material = Material.matchMaterial(item);
						Integer id = material.getId();
						if (idInt.compareTo(idInt) == 0) {
							aIdsOk.add(id);
						}
					}
					if (aIdsOk.contains(idInt)) {
						aAcceptedIds.add(idInt);
					}
					else {
						aRefusedIds.add(idInt);
					}
				}
				else {
					player.sendMessage("pas entier !!!");
				}
			}
		}
		else {
			player.sendMessage(syntaxTest);
			aRefusedIds.add("error");
		}
		return this.aRefusedIds;
	}
	
	public boolean askHowMuchItems(Player player, Integer index) {
		try {
			Integer id = (Integer) plugin.playerL.signCreationInfos.get(index);
			String item = Material.getMaterial(id).toString().toLowerCase();
			player.sendMessage("Combien voulez-vous de " + item + " ?");
			return true;
		} catch (Exception e) {
			return false;
		}
			
	}

	/********************************************/
	/*                              TESTS                                    */
	/********************************************/
	private String isSyntaxIsOk(String message) {
		String[] aSplit = message.split("=");
		if (aSplit[0].equals("ids")) {
			if (aSplit.length > 1) {
				String[] idSplit = aSplit[1].split("_");
				if (idSplit.length >= 1) {
					return "ok";
				}
				else {
					return "splitError";
				}
			}
			else {
				return "noIdsError";
			}
		}
		else {
			return "introError";
		}
	}

	public Integer isInteger(String id) {
		try {
			Integer Int = new Integer(id);
			return Int;
		} catch (Exception e) {
			return 0;
		}
	}

	private boolean isPlaceBlock(Integer blockId) {
		if (aPlaceIdsList.contains(blockId)) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean isBreakBlock(Integer blockId) {
		if (aBreakIdsList.contains(blockId)) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean isCraftBlock(Integer blockId) {
		if (aCraftIdsList.contains(blockId)) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean isDropBlock(Integer blockId) {
		if (aDropIdsList.contains("*")) {
			return true;
		}
		else {
			if (aDropIdsList.contains(blockId)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
}
