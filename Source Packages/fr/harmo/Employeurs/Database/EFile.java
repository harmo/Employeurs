package fr.harmo.Employeurs.Database;

import fr.harmo.Employeurs.Employeurs;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author HarmO
 */
public class EFile {
	
	private Employeurs plugin;
	private FileWriter writer;

	public EFile(Employeurs plugin) {
		this.plugin = plugin;
	}
	
	public void setJob(String metier, String type, ArrayList ids) {
		File parent = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois");
		File file = new File("plugins" + System.getProperty("file.separator") + "Employeurs" + System.getProperty("file.separator") + "Emplois" + System.getProperty("file.separator") + metier + ".db");
		try {
			if (!file.exists()) {
				parent.mkdirs();
				file.createNewFile();
				writer = new FileWriter(file, true);
				writer.write(metier + "::" + type + " :: " + ids);
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

}
