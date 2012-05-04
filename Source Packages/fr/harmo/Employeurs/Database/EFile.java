package fr.harmo.Employeurs.Database;

import fr.harmo.Employeurs.Employeurs;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author HarmO
 */
public class EFile {

	private Employeurs plugin;
	private FileWriter writer;
	private ArrayList jobList = new ArrayList();
	private ArrayList offerList = new ArrayList();
	private ArrayList postList = new ArrayList();

	public EFile(Employeurs plugin) {
		this.plugin = plugin;
		reload();
	}

	// JOBS
	public void addJob(String label, String type, ArrayList ids, String world) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Emplois");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Emplois" + System.getProperty("file.separator") + label + ".db");
		try {
			if (!file.exists()) {
				parent.mkdirs();
				file.createNewFile();
				writer = new FileWriter(file, true);
				writer.write(label + "::" + type + "::" + ids + "::" + world);
				writer.write(System.getProperty("line.separator"));
				writer.flush();
				writer.close();
			}
			else {
				// Le fichier existe, on remplace ou ajoute les valeurs
			}
			reload();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList getJob() {
		ArrayList aJobs = new ArrayList();

		return aJobs;
	}
	public String getJobType(String jobName) {
		for (int i = 0; i < this.jobList.size(); i++) {
			String job = (String) this.jobList.get(i);
			if(job.equals(jobName)) {
				return (String) this.jobList.get(i + 1);
			}
		}
		return null;
	}
	public boolean deleteJob(String label, String type) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	public ArrayList getJobList() throws IOException {
		return this.jobList;
	}
	public void setJobList() throws IOException {
		this.jobList.clear();
		List<World> world = plugin.getServer().getWorlds();
		for (World w : world) {
			String worldName = w.getName();
			File worldFile = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName);
			if (!worldFile.exists()) {
				worldFile.mkdirs();
			}
			File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Emplois");
			String[] fileList = parent.list();
			if (parent.exists()) {
				for (int i = 0; i < fileList.length; i++) {
					File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Emplois" + System.getProperty("file.separator") + fileList[i]);
					FileReader fr = new FileReader(file);
					BufferedReader reader = new BufferedReader(fr);
					String line;
					while ((line = reader.readLine()) != null) {
						String label = line.split("::")[0];
						String type = line.split("::")[1];
						String ids = line.split("::")[2];
						String worldname = line.split("::")[3];
						String[] job = {label, type, ids, worldname};
						this.jobList.add(job);
					}
					fr.close();
					reader.close();
				}
			}
		}
	}
	public boolean isInJobList(String job) {
		for (int i = 0; i < this.jobList.size(); i++) {
			String[] aJob = (String[]) this.jobList.get(i);
			for (int n = 0; n < aJob.length; n++) {
				if (aJob[n].equals(job))
					return true;
			}
		}
		return false;
	}
	public ArrayList getJobAuthorizedIds(String signJobName) {
		String[] aIds = null;
		ArrayList aReturn = new ArrayList();
		for (int i = 0; i < this.jobList.size(); i++) {
			String[] aJob = (String[]) this.jobList.get(i);
			for (int n = 0; n < aJob.length; n++) {
				if (aJob[0].equals(signJobName)) {
					aIds = aJob[2].toString().split(", ");
				}
			}
		}
		aReturn.addAll(Arrays.asList(aIds));
		return aReturn;
	}

	// OFFERS
	public boolean addOffer(String job, String pos , String boss,  Integer salary, String items, String world) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Offres");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Offres" + System.getProperty("file.separator") + job.toUpperCase() + ".db");
		try {
			if (!file.exists()) {
				plugin.getLogger().info("creation d'une offre !");
				parent.mkdirs();
				file.createNewFile();
				writer = new FileWriter(file, true);
				writer.write(job.toUpperCase() + "::" + boss + "::" + pos + "::" + salary + "::" + items + "::" + world);
				writer.write(System.getProperty("line.separator"));
				writer.flush();
				writer.close();
			}
			else {
				plugin.getLogger().info("ajout d'une offre !");
				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
				String line = "";
				ArrayList replace = new ArrayList();
				while ((line = reader.readLine()) != null) {
					replace.add(line + System.getProperty("line.separator"));
				}
				fr.close();
				reader.close();
				file.delete();
				file.createNewFile();
				writer = new FileWriter(file, true);
				for (int n = 0; n < replace.size(); n++) {
					writer.write(replace.get(n).toString());
				}
				writer.write(job.toUpperCase() + "::" + boss + "::" + pos + "::" + salary + "::" + items + "::" + world);
				writer.flush();
				writer.close();
			}
			reload();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public String[] getOffer(double x, double y, double z) {
		String[] aJob = null;
		for (int i = 0; i < this.offerList.size(); i++) {
			String[] job = (String[]) this.offerList.get(i);
			for (int n = 0; n < job.length; n++) {
				if (new Integer(job[2]) == x) {
					if (new Integer(job[3]) == y) {
						if (new Integer(job[4]) == z) {
							aJob = job;
						}
					}
				}
			}
		}
		return aJob;
	}
	public boolean deleteOffer(String job, int x, int y, int z, String world) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Offres");
		String[] fileList = parent.list();
		String jobCleaned = job.substring(2, job.length());
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].equals(jobCleaned.toUpperCase() + ".db")) {
				try {
					File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Offres" + System.getProperty("file.separator") + jobCleaned.toUpperCase() + ".db");
					FileReader fr = new FileReader(file);
					BufferedReader reader = new BufferedReader(fr);
					String line = "";
					ArrayList replace = new ArrayList();
					while ((line = reader.readLine()) != null) {
						String label = line.split("::")[0];
						String jobBoss = line.split("::")[1];
						Integer jobX = new Integer(line.split("::")[2]);
						Integer jobY = new Integer(line.split("::")[3]);
						Integer jobZ = new Integer(line.split("::")[4]);
						if (!jobCleaned.toUpperCase().equalsIgnoreCase(label) || x != jobX || y != jobY || z != jobZ) {
							replace.add(line + System.getProperty("line.separator"));
						}
					}
					fr.close();
					reader.close();
					if (replace.size() > 0) {
						file.delete();
						file.createNewFile();
						writer = new FileWriter(file, true);
						for (int n = 0; n < replace.size(); n++) {
							writer.write(replace.get(n).toString());
						}
						writer.flush();
						writer.close();
					}
					else {
						file.delete();
					}
					reload();
				} catch (IOException | NumberFormatException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
		}
		return false;
	}
	public ArrayList getOfferList() {
		return this.offerList;
	}
	public void setOfferList() throws FileNotFoundException, IOException {
		this.offerList.clear();
		List<World> world = plugin.getServer().getWorlds();
		for (World w : world) {
			String worldName = w.getName();
			File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Offres");
			String[] fileList = parent.list();
			if (parent.exists()) {
				for (int i = 0; i < fileList.length; i++) {
					File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Offres" + System.getProperty("file.separator") + fileList[i]);
					FileReader fr = new FileReader(file);
					BufferedReader reader = new BufferedReader(fr);
					String line;
					while ((line = reader.readLine()) != null) {
						String label = line.split("::")[0];
						String boss = line.split("::")[1];
						String x = line.split("::")[2];
						String y = line.split("::")[3];
						String z = line.split("::")[4];
						String salary = line.split("::")[5];
						String items = line.split("::")[6];
						String worldname = line.split("::")[7];
						String[] job = {label, boss, x, y, z, salary, items, worldname};
						this.offerList.add(job);
					}
					fr.close();
					reader.close();
				}
			}
		}
	}
	public boolean isInOfferList() {

		return false;
	}

	// POSTS
	public void addPost(String player, String[] aJob, String[] aChest, String world) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Postes");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Postes" + System.getProperty("file.separator") + player.toUpperCase() + ".db");
		try {
			if (!file.exists()) {
				plugin.getLogger().info("creation d'un poste !");
				parent.mkdirs();
				file.createNewFile();
				writer = new FileWriter(file, true);
				writer.write(player + "::");
				for (int i = 0; i < aJob.length; i++) {
					if (i == aJob.length-1) {
						writer.write(aJob[i]);
					}
					else {
						writer.write(aJob[i] + "::");
					}
				}
				writer.write(System.getProperty("line.separator"));
				writer.flush();
				writer.close();
				reload();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Coffres");
		file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Coffres" + System.getProperty("file.separator") + player.toUpperCase() + ".db");
		try {
			if (!file.exists()) {
				parent.mkdirs();
				file.createNewFile();
				/*writer = new FileWriter(file, true);
				writer.write(player + "::");
				for (int i = 0; i < aChest.length; i++) {
					if (i == aChest.length-1) {
						writer.write(aChest[i]);
					}
					else {
						writer.write(aChest[i] + "::");
					}
				}
				writer.write(System.getProperty("line.separator"));
				writer.flush();
				writer.close();*/
				reload();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String[] getPost(String player) {
		String[] aJob = null;
		for (int i = 0; i < this.postList.size(); i++) {
			String[] job = (String[]) this.postList.get(i);
			for (int n = 0; n < job.length; n++) {
				if (job[7].equals(player)) {
					aJob = job;
				}
			}
		}
		return aJob;
	}
	public boolean deletePost(String playername, String world) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Postes");
		String[] fileList = parent.list();
		for (int i = 0; i < fileList.length; i++) {
			String filename = fileList[i].substring(0, fileList[i].length()-3);
			if (filename.equals(playername.toUpperCase())) {
				File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + world + System.getProperty("file.separator") + "Postes" + System.getProperty("file.separator") + playername.toUpperCase() + ".db");
				if (file.delete())
					return true;
			}
		}
		reload();
		return false;
	}
	public ArrayList getPostList() {
		return this.postList;
	}
	public void setPostList() throws IOException {
		this.postList.clear();
		List<World> world = plugin.getServer().getWorlds();
		for (World w : world) {
			String worldName = w.getName();
			File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Postes");
			String[] fileList = parent.list();
			if (parent.exists()) {
				for (int i = 0; i < fileList.length; i++) {
					File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Postes" + System.getProperty("file.separator") + fileList[i]);
					FileReader fr = new FileReader(file);
					BufferedReader reader = new BufferedReader(fr);
					String line;
					while ((line = reader.readLine()) != null) {
						String player = line.split("::")[0];
						String label = line.split("::")[1];
						String boss = line.split("::")[2];
						String x = line.split("::")[3];
						String y = line.split("::")[4];
						String z = line.split("::")[5];
						String salary = line.split("::")[6];
						String items = line.split("::")[7];
						String worldname = line.split("::")[8];
						String[] aJob = {label, boss, x, y, z, salary, items, player, worldname};
						this.postList.add(aJob);
						plugin.jobManager.setPosts(player, aJob);
					}
					fr.close();
					reader.close();
				}
			}
		}
	}
	public boolean isInPostList() {

		return false;
	}
	public String[] getchestContent(String playername) throws FileNotFoundException, IOException {
		String[] chestContent = null;
		List<World> world = plugin.getServer().getWorlds();
		for (World w : world) {
			String worldName = w.getName();
			File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Coffres");
			String[] fileList = parent.list();
			if (parent.exists()) {
				String itemList = null;
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].substring(0, fileList[i].length()-3).toLowerCase().equals(playername.toLowerCase())) {
						File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + "Coffres" + System.getProperty("file.separator") + fileList[i]);
						FileReader fr = new FileReader(file);
						BufferedReader reader = new BufferedReader(fr);
						String line;
						while ((line = reader.readLine()) != null) {
							itemList = line.split("::")[4].substring(1, line.split("::")[4].length()-1);
						}
						fr.close();
						reader.close();
					}
				}
				if (itemList != null)
					chestContent = itemList.split(", ");
			}
		}
		return chestContent;
	}

	public void setChestContent(String playername, String worldname, Object[] newContent) throws FileNotFoundException, IOException {
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + worldname + System.getProperty("file.separator") + "Coffres" + System.getProperty("file.separator") + playername.toUpperCase() + ".db");
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			String line = "";
			ArrayList replace = new ArrayList();
			while ((line = reader.readLine()) != null) {
				replace.add(line + System.getProperty("line.separator"));
			}
			fr.close();
			reader.close();
			file.delete();
			file.createNewFile();
			writer = new FileWriter(file, true);
			String oldLine = replace.toString();
			oldLine = oldLine.substring(1, oldLine.length()-1);
			String[] aSplit = oldLine.split("::");
			String[] aPost = getPost(playername);
			String items = "[";
			for(int n = 0; n < newContent.length; n++) {
				String chestId = newContent[n].toString().split(":")[0];
				String chestNb = newContent[n].toString().split(":")[1];
				String[] aItems = aPost[6].substring(1, aSplit[4].length()-1).split(", ");
				for (int i = 0; i < aItems.length; i++) {
					String askId = aItems[i].split(":")[0];
					String askNb = aItems[i].split(":")[1];
					if (chestId.equals(askId)) {
						int askNbInt = new Integer(askNb);
						int chestNbInt = new Integer(chestNb);
						int newAskNb = askNbInt - chestNbInt;
						items += askId + ":" + newAskNb;
					}
				}
				if (n != 0 && n == newContent.length-1) {
					items += ", ";
				}
			}
			items += "]";
			String newLine = aPost[2] + "::" + aPost[3] + "::" + aPost[4] + "::" + aPost[5] + "::" + items + "::" + aPost[7];
			writer.write(newLine);
			writer.flush();
			writer.close();
		}
	}

	// OTHER
	public void reload() {
		try {
			setJobList();
			setOfferList();
			setPostList();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}










}
