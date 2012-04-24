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
	public ArrayList jobList = new ArrayList();

	public EFile(Employeurs plugin) {
		this.plugin = plugin;
	}
	
	public void setJob(String job, String type, ArrayList ids) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois" + System.getProperty("file.separator") + job + ".db");
		try {
			if (!file.exists()) {
				parent.mkdirs();
				file.createNewFile();
				writer = new FileWriter(file, true);
				writer.write(job + "::" + type + "::" + ids);
				writer.write(System.getProperty("line.separator"));
				writer.flush();
				writer.close();
			}
			else {
				// Le fichier existe, on remplace ou ajoute les valeurs
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getJobList() throws Exception {
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
					this.jobList.addAll(Arrays.asList(job));
				}
				fr.close();
				reader.close();
			}
		}
	}

	public boolean isInJobList(String job) {
		if (this.jobList.contains(job)) {
			return true;
		}
		return false;
	}
	
	public boolean addJobOffer(String job, String pos , String boss,  Integer salary, String items) {
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
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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
				} catch (IOException | NumberFormatException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
		}
		return false;
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

	public ArrayList getJobAuthorizedIds(String signJobName) {
		String[] aIds = null;
		ArrayList aReturn = new ArrayList();
		for (int i = 0; i < this.jobList.size(); i++) {
			String job = (String) this.jobList.get(i);
			if(job.equals(signJobName)) {
				aIds = this.jobList.get(i + 2).toString().split(", ");
			}
		}
		aReturn.addAll(Arrays.asList(aIds));
		return aReturn;
	}
}
