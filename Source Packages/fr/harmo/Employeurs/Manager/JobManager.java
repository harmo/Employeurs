package fr.harmo.Employeurs.Manager;

import fr.harmo.Employeurs.Config.Config;
import fr.harmo.Employeurs.Employeurs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author HarmO
 */
public class JobManager {

	private Employeurs plugin;
	private HashMap<String, String[]> aJobs = new HashMap();

	public JobManager(Employeurs plugin) {
		this.plugin = plugin;
	}

	public ArrayList getJobList() {
		ArrayList jobList = new ArrayList();
		if (!Config.mysql) {
			// File
			try {
				jobList = Config.getDbFile().getJobList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
		return jobList;
	}
	public ArrayList getOfferList() {
		ArrayList offerList = new ArrayList();
		if (!Config.mysql) {
			// File
			try {
				offerList = Config.getDbFile().getOfferList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
		return offerList;
	}
	public ArrayList getPostList() {
		ArrayList postList = new ArrayList();
		if (!Config.mysql) {
			// File
			try {
				postList = Config.getDbFile().getPostList();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			// Database
		}
		return postList;
	}

	public boolean takeJob(Player player, Location loc) {
		if (!Config.mysql) {
			// File
			try {
				String[] aJob = Config.getDbFile().getOffer(loc.getX(), loc.getY(), loc.getZ());
				if (aJobs.containsValue(aJob)) {
					player.sendMessage("Cette offre est déjà prise !!!");
					return false;
				}
				else if (aJobs.containsKey(player.getName())) {
					if (haveThisPost(player.getName(), loc)) {
						player.sendMessage("Vous avez déjà cet emploi !!!");
					}
					else {
						player.sendMessage("Vous avez déjà un emploi !!!");
					}
					return false;
				}
				else {
					aJobs.put(player.getName(), aJob);
					Config.getDbFile().addPost(player.getName(), aJob);
					return true;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			// Database
			return true;
		}
	}

	public void setPosts(String playername, String[] aJob) {
		if (!aJobs.containsKey(playername)) {
			aJobs.put(playername, aJob);
		}
	}

	public boolean havePost(String playername) {
		if (aJobs.containsKey(playername)) {
			return true;
		}
		return false;
	}
	public boolean haveThisPost(String playername, Location loc) {
		if (!Config.mysql) {
			String[] aJob = Config.getDbFile().getOffer(loc.getX(), loc.getY(), loc.getZ());
			if (aJobs.containsKey(playername)) {
				String[] playerJob = aJobs.get(playername);
				if (Arrays.equals(playerJob, aJob))
					return true;
			}
		}

		return false;
	}

	public String[] getJobInfos(Location loc) {
		String[] aInfos = null;
		if (!Config.mysql) {
			aInfos = Config.getDbFile().getOffer(loc.getX(), loc.getY(), loc.getZ());
			return aInfos;
		}
		else {

			return aInfos;
		}
	}

	public String getJobSalary(Location loc) {
		String[] aJob = getJobInfos(loc);
		return aJob[5];
	}

	public boolean quitJob(String playername) {
		if (!Config.mysql) {
			if (Config.getDbFile().deletePost(playername)) {
				aJobs.remove(playername);
				return true;
			}
		}
		return false;
	}

	public String[] getPostInfos(String playername) {
		String[] aInfos = null;
		if (!Config.mysql) {
			aInfos = Config.getDbFile().getPost(playername);
		}
		return aInfos;
	}

}
