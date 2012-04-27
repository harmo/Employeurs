package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author HarmO
 */
public class OfferCreation {

	private Employeurs plugin;
	private HashMap<Player, Integer> empCreaPlayers = new HashMap();
	private HashMap<Player, Integer> empCreaNumberId = new HashMap();

	public OfferCreation(Employeurs plugin) {
		this.plugin = plugin;
	}

	public boolean isInCreationMode(Player player) {
		return this.empCreaPlayers.containsKey(player);
	}
	public void toggleCreaMode(Player player) {
		if (this.empCreaPlayers.containsKey(player)) {
			this.empCreaPlayers.remove(player);
			player.sendMessage(Config.empSignCreationOff);
			Config.reload();
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

	public boolean isInNumberIdMode(Player player) {
		return this.empCreaNumberId.containsKey(player);
	}
	public void toggleNumberIdMode(Player player) {
		if (this.empCreaNumberId.containsKey(player)) {
			this.empCreaNumberId.remove(player);
		}
		else {
			this.empCreaNumberId.put(player, Integer.valueOf(0));
		}
	}
	public void setEmpCreaNumberIdValue(Player player, Integer value) {
		if (this.empCreaNumberId.containsKey(player)) {
			this.empCreaNumberId.remove(player);
			this.empCreaNumberId.put(player, value);
		}
	}
	public int getEmpCreaNumberIdValue(Player player) {
		if (this.empCreaNumberId.containsKey(player)) {
			return this.empCreaNumberId.get(player);
		}
		return 0;
	}

	public ArrayList addBlocksInOffer(Player player, String message) {
		plugin.blocksM.clearAcceptedIds();
		plugin.blocksM.clearRefusedIds();
		String syntaxTest = plugin.tests.isSyntaxIsOk(message);
		if ("ok".equals(syntaxTest)) {
			String[] idSplit = message.split("=")[1].split("_");
			ArrayList signJobAuthorizedIds = plugin.blocksL.getSignJobAuthorizedIds();
			for (int i = 0; i < idSplit.length; i++) {
				Integer idInt = plugin.tests.isInteger(idSplit[i]);
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
						plugin.blocksM.addAcceptedId(idInt);
					}
					else {
						plugin.blocksM.addRefusedId(idInt.toString());
					}
				}
				else {
					player.sendMessage("pas entier !!!");
				}
			}
		}
		else {
			player.sendMessage(syntaxTest);
			plugin.blocksM.addRefusedId("error");
		}
		return plugin.blocksM.getRefusedIds();
	}

	public boolean askHowMuchItems(Player player, Integer index) {
		try {
			Integer id = (Integer) plugin.playerL.signCreationInfos.get(index);
			String item = Material.getMaterial(id).toString().toLowerCase();
			player.sendMessage("Combien voulez-vous de " + item + " ?");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
