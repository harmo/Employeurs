package fr.harmo.Employeurs;

import fr.harmo.Employeurs.Config.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

	public Manager(Employeurs plugin) {
		this.plugin = plugin;
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
				} else {
					player.sendMessage(Config.empEmptyJobList);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
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
				} else {
					plugin.getLogger().info(Config.empEmptyJobList);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			// Database
		}
	}

	public boolean isInAddMode(Player player) {
		return this.empAddPlayers.containsKey(player);
	}

	public void toggleAddMode(Player player) {
		if (this.empAddPlayers.containsKey(player)) {
			this.empAddPlayers.remove(player);
			player.sendMessage(Config.empAddCreationOff);
		} else {
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
		if (aSplit[0].equals(Config.empAddCreationJobNamePrefix)) {
			this.newJobName = aSplit[1];
			return this.newJobName;
		} else {
			player.sendMessage(Config.empAddCreationJobNamePrefixError);
			return null;
		}
	}

	public String addJobType(Player player, String message) {
		String[] aSplit = message.split("=");
		if (aSplit[0].equals("type")) {
			if (aSplit[1].equals("break") || aSplit[1].equals("drop") || aSplit[1].equals("place") || aSplit[1].equals("kill") || aSplit[1].equals("craft")) {
				this.newJobType = aSplit[1];
				return this.newJobType;
			} else {
				player.sendMessage(Config.empAddCreationJobTypeResponseError);
				player.sendMessage("break, drop, place, kill, craft");
				return null;
			}
		} else {
			player.sendMessage(Config.empAddCreationJobTypeError);
			return null;
		}
	}

	public ArrayList addBlockIds(Player player, String message) {
		String[] aSplit = message.split("=");
		if (aSplit[0].equals("ids")) {
			if (aSplit.length > 1) {
				String[] idSplit = aSplit[1].split("_");
				aRefusedIds.clear();
				if (idSplit.length >= 1) {
					aAcceptedIds.clear();
					for (int i = 0; i < idSplit.length; i++) {
						String id = idSplit[i];
						switch (this.newJobType) {
							case "break": {
								try {
									Integer idInt = new Integer(idSplit[i]);
									MaterialData material = new MaterialData(idInt);
									if (!isBreakBlock(idInt)) {
										aRefusedIds.add(id);
									} else {
										aAcceptedIds.add(material.getItemType().toString().toLowerCase());
									}
								} catch (Exception e) {
									player.sendMessage(Config.empAddCreationJobBlockIdsIntegerError);
								}

								break;
							}
							case "place": {
								try {
									Integer idInt = new Integer(idSplit[i]);
									MaterialData material = new MaterialData(idInt);
									if (!isPlaceBlock(idInt)) {
										aRefusedIds.add(id);
									} else {
										aAcceptedIds.add(material.getItemType().toString().toLowerCase());
									}
								} catch (Exception e) {
									player.sendMessage(Config.empAddCreationJobBlockIdsIntegerError);
								}
								break;
							}
							case "craft": {
								try {
									Integer idInt = new Integer(idSplit[i]);
									MaterialData material = new MaterialData(idInt);
									if (!isCraftBlock(idInt)) {
										aRefusedIds.add(id);
									} else {
										aAcceptedIds.add(material.getItemType().toString().toLowerCase());
									}
								} catch (Exception e) {
									player.sendMessage(Config.empAddCreationJobBlockIdsIntegerError);
								}
								break;
							}
							case "kill":
								break;
							case "drop":
							{
								try {
									Integer idInt = new Integer(idSplit[i]);
									MaterialData material = new MaterialData(idInt);
									aAcceptedIds.add(material.getItemType().toString().toLowerCase());
								} catch (Exception e) {
									player.sendMessage(Config.empAddCreationJobBlockIdsIntegerError);
								}
								break;
							}
						}
					}
				} else {
					player.sendMessage(Config.empAddCreationJobBlockIdsSyntaxError);
					aRefusedIds.add("error");
				}
			} else {
				player.sendMessage(Config.empAddCreationJobBlockIdsFullError);
				aRefusedIds.add("error");
			}

			return aRefusedIds;
		} else {
			player.sendMessage(Config.empAddCreationJobBlockIds3);
			return null;
		}
	}

	private boolean isBreakBlock(Integer blockId) {
		Integer[] aBreakIds = {1, 2, 3, 4, 5, 12, 13, 14, 15, 16, 17, 21, 24, 35, 43, 47, 48, 49, 52, 56, 64, 65, 66, 67, 68, 69, 70, 71, 73, 74, 77, 80, 81, 82, 83, 85, 86, 87, 88, 98, 99, 100, 101, 103, 106, 112, 113, 114, 355};
		ArrayList aBreakIdsList = new ArrayList();
		aBreakIdsList.addAll(Arrays.asList(aBreakIds));
		if (aBreakIdsList.contains(blockId)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isPlaceBlock(Integer blockId) {
		Integer[] aPlaceIds = {1, 3, 4, 5, 6, 8, 10, 12, 13, 17, 20, 22, 23, 24, 25, 27, 28, 29, 33, 35, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 53, 54, 55, 57, 58, 61, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 76, 77, 81, 84, 85, 86, 87, 88, 89, 91, 92, 93, 94, 95, 96, 98, 101, 102, 103, 106, 107, 108, 109, 112, 113, 114, 321, 323, 324, 328, 330, 342, 343, 354, 355};
		ArrayList aPlaceIdsList = new ArrayList();
		aPlaceIdsList.addAll(Arrays.asList(aPlaceIds));
		if (aPlaceIdsList.contains(blockId)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isCraftBlock(Integer blockId) {
		Integer[] aCraftIds = {5, 20, 23, 25, 27, 28, 29, 33, 35, 41, 42, 43, 44, 45, 46, 47, 50, 53, 54, 57, 58, 61, 64, 65, 66, 67, 69, 70, 71, 72, 75, 77, 84, 85, 86, 89, 93, 95, 98, 101, 102, 107, 108, 113, 114, 256, 257, 258, 259, 2561, 262, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 290, 291, 292, 293, 294, 297, 298, 299, 300, 301, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 321, 323, 324, 325, 328, 330, 333, 336, 339, 340, 342, 343, 345, 346, 351, 353, 354, 355, 356, 357, 358};
		ArrayList aCraftIdsList = new ArrayList();
		aCraftIdsList.addAll(Arrays.asList(aCraftIds));
		if (aCraftIdsList.contains(blockId)) {
			return true;
		} else {
			return false;
		}
	}

	public String getJobName() {
		return newJobName;
	}

	public String getJobType() {
		return newJobType;
	}

	public ArrayList getAcceptedIds() {
		return aAcceptedIds;
	}
}
