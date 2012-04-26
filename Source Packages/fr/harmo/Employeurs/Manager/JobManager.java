package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import org.bukkit.entity.Player;

/**
 *
 * @author HarmO
 */
public class JobManager {
	
	private Employeurs plugin;

	public JobManager(Employeurs plugin) {
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
	
	
}
