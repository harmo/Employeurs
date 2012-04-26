package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Employeurs;

/**
 *
 * @author HarmO
 */
public class Tests {
	
	private Employeurs plugin;
	
	public Tests(Employeurs plugin) {
		this.plugin = plugin;
	}

	public String isSyntaxIsOk(String message) {
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
	
	public String isSalarySyntaxIsOk(String message) {
		String[] aSplit = message.split("=");
		if (aSplit[0].equals("salaire")) {
			if (aSplit.length > 1) {
				return aSplit[1];
			}
			else {
				return "noSalaryError";
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

	public boolean isPlaceBlock(Integer blockId) {
		if (plugin.blocksM.getPlaceIdsList().contains(blockId)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isBreakBlock(Integer blockId) {
		if (plugin.blocksM.getBreakIdsList().contains(blockId)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isCraftBlock(Integer blockId) {
		if (plugin.blocksM.getCraftIdsList().contains(blockId)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isDropBlock(Integer blockId) {
		if (plugin.blocksM.getDropIdsList().contains("*")) {
			return true;
		}
		else {
			if (plugin.blocksM.getDropIdsList().contains(blockId)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
}
