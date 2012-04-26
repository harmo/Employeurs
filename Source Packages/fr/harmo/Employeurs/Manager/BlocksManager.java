package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;

/**
 *
 * @author HarmO
 */
public class BlocksManager {
	
	private Employeurs plugin;
	private ArrayList aBreakIdsList;
	private ArrayList aPlaceIdsList;
	private ArrayList aCraftIdsList;
	private ArrayList aDropIdsList;
	private ArrayList aAcceptedIds;
	private ArrayList aRefusedIds;
	
	public BlocksManager(Employeurs plugin) {
		this.plugin = plugin;
		this.aBreakIdsList = Config.getAuthorizedIds("break");
		this.aPlaceIdsList = Config.getAuthorizedIds("place");
		this.aCraftIdsList = Config.getAuthorizedIds("craft");
		this.aDropIdsList = Config.getAuthorizedIds("drop");
		this.aAcceptedIds = new ArrayList();
		this.aRefusedIds = new ArrayList();
	}

	public ArrayList getRefusedIds() {
		return this.aRefusedIds;
	}	
	public ArrayList getAcceptedIds() {
		return this.aAcceptedIds;
	}
	
	public void clearRefusedIds() {
		this.aRefusedIds.clear();
	}	
	public void clearAcceptedIds() {
		this.aAcceptedIds.clear();
	}
	
	public void addRefusedId (String id) {
		this.aRefusedIds.add(id);
	}	
	public void addAcceptedId (int id) {
		this.aAcceptedIds.add(id);
	}
	public void addAcceptedIdString (String id) {
		this.aAcceptedIds.add(id);
	}
	
	public ArrayList getBreakIdsList() {
		return this.aBreakIdsList;
	}
	public ArrayList getPlaceIdsList() {
		return this.aPlaceIdsList;
	}
	public ArrayList getCraftIdsList() {
		return this.aCraftIdsList;
	}
	public ArrayList getDropIdsList() {
		return this.aDropIdsList;
	}
	
}
