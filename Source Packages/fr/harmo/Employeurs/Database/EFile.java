package fr.harmo.Employeurs.Database;

import fr.harmo.Employeurs.Employeurs;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

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
	public void addJob(String label, String type, ArrayList ids) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois" + System.getProperty("file.separator") + label + ".db");
		try {
			if (!file.exists()) {
				parent.mkdirs();
				file.createNewFile();
				writer = new FileWriter(file, true);
				writer.write(label + "::" + type + "::" + ids);
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
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois");
		String[] fileList = parent.list();
		if (parent.exists()) {
			for (int i = 0; i < fileList.length; i++) {
				File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois" + System.getProperty("file.separator") + fileList[i]);
				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
				String line;
				while ((line = reader.readLine()) != null) {
					String label = line.split("::")[0];
					String type = line.split("::")[1];
					String ids = line.split("::")[2];
					String[] job = {label, type, ids};
					this.jobList.add(job);
				}
				fr.close();
				reader.close();
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
	public boolean addOffer(String job, String pos , String boss,  Integer salary, String items) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Offres");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Offres" + System.getProperty("file.separator") + job.toUpperCase() + ".db");
		try {
			if (!file.exists()) {
				plugin.getLogger().info("creation d'une offre !");
				parent.mkdirs();
				file.createNewFile();
				writer = new FileWriter(file, true);
				writer.write(job.toUpperCase() + "::" + boss + "::" + pos + "::" + salary + "::" + items);
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
				writer.write(job.toUpperCase() + "::" + boss + "::" + pos + "::" + salary + "::" + items);
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
	public boolean deleteOffer(String job, int x, int y, int z) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Offres");
		String[] fileList = parent.list();
		String jobCleaned = job.substring(2, job.length());
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].equals(jobCleaned.toUpperCase() + ".db")) {
				try {
					File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Offres" + System.getProperty("file.separator") + jobCleaned.toUpperCase() + ".db");
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
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Offres");
		String[] fileList = parent.list();
		if (parent.exists()) {
			for (int i = 0; i < fileList.length; i++) {
				File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Offres" + System.getProperty("file.separator") + fileList[i]);
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
					String[] job = {label, boss, x, y, z, salary, items, };
					this.offerList.add(job);
				}
				fr.close();
				reader.close();
			}
		}
	}
	public boolean isInOfferList() {

		return false;
	}

	// POSTS
	public void addPost(String player, String[] aJob) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Postes");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Postes" + System.getProperty("file.separator") + player.toUpperCase() + ".db");
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
	public boolean deletePost(String playername) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Postes");
		String[] fileList = parent.list();
		for (int i = 0; i < fileList.length; i++) {
			String filename = fileList[i].substring(0, fileList[i].length()-3);
			if (filename.equals(playername.toUpperCase())) {
				File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Postes" + System.getProperty("file.separator") + playername.toUpperCase() + ".db");
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
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Postes");
		String[] fileList = parent.list();
		if (parent.exists()) {
			for (int i = 0; i < fileList.length; i++) {
				File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Postes" + System.getProperty("file.separator") + fileList[i]);
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
					String[] aJob = {label, boss, x, y, z, salary, items, player};
					this.postList.add(aJob);
					plugin.jobManager.setPosts(player, aJob);
				}
				fr.close();
				reader.close();
			}
		}
	}
	public boolean isInPostList() {

		return false;
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
