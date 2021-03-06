package fr.harmo.Employeurs.Config;

import fr.harmo.Employeurs.Database.EFile;
import fr.harmo.Employeurs.Database.EMysql;
import fr.harmo.Employeurs.Employeurs;
import java.io.File;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author
 * HarmO
 */
public class Config {

	public static boolean mysql;
	private static EMysql database;
	private static EFile datafile;
	private static Employeurs plugin;
	public static String prefix;
	private static final ChatColor[] colors = new ChatColor[]{ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
	public static ChatColor signColor;
	public static ChatColor textColor;
	private static String breakableIds;
	private static String placeableIds;
	private static String craftableIds;
	private static String dropableIds;
	public static String noCreatePermMessage;
	public static String noListPermMessage;
	public static String noAddPermMessage;
	public static String noUsePermMessage;
	public static String helpList;
	public static String helpAdd;
	public static String empAddCreationOff;
	public static String empAddCreationOn;
	public static String empAddCreationJobName;
	public static String empAddCreationJobNamePrefix;
	public static String empAddCreationJobNamePrefixError;
	public static String empAddCreationJobNamePrefixSuccess;
	public static String empAddCreationJobType;
	public static String empAddCreationJobTypeError;
	public static String empAddCreationJobTypeResponseError;
	public static String empAddCreationJobBlockIds1;
	public static String empAddCreationJobBlockIds2;
	public static String empAddCreationJobBlockIds3;
	public static String empAddCreationJobBlockIdsError;
	public static String empAddCreationJobBlockIdsSyntaxError;
	public static String empAddCreationJobBlockIdsIntegerError;
	public static String empAddCreationJobBlockIdsFullError;
	public static String empAddCreationJobBlockIdsSuccess;
	public static String empAddCreationSuccess;
	public static String empAddCreationStop;
	public static String empEmptyJobList;
	public static String empCreateSignNoJob;
	public static String empAddOfferSuccess;
	public static String empAddOfferError;
	public static String empDeleteOfferSuccess;
	public static String empDeleteOfferError;
	public static String empDeleteOfferNoPerm;
	public static String empSignCreationOff;
	public static String empSignCreationOn;
	public static String empSignCreationBlocks1;
	public static String empSignCreationBlocks2;

	public Config(Employeurs plugin) {
		Config.plugin = plugin;
	}

	public boolean setConfig() {

		breakableIds = plugin.getConfig().getString("config.breakableIds");
		placeableIds = plugin.getConfig().getString("config.placeableIds");
		craftableIds = plugin.getConfig().getString("config.craftableIds");
		dropableIds = plugin.getConfig().getString("config.dropableIds");
		signColor = colors[plugin.getConfig().getInt("messages.sign.color")];
		textColor = colors[plugin.getConfig().getInt("messages.chat.color")];
		prefix = plugin.getConfig().getString("messages.sign.prefix");
		noCreatePermMessage = textColor + plugin.getConfig().getString("messages.chat.noCreatePerm");
		noListPermMessage = textColor + plugin.getConfig().getString("messages.chat.noListPerm");
		noAddPermMessage = textColor + plugin.getConfig().getString("messages.chat.noAddPerm");
		noUsePermMessage = textColor + plugin.getConfig().getString("messages.chat.noUsePerm");
		empAddCreationOff = textColor + plugin.getConfig().getString("messages.chat.creationOff");
		empAddCreationOn = textColor + plugin.getConfig().getString("messages.chat.creationOn");
		empAddCreationJobName = textColor + plugin.getConfig().getString("messages.chat.creationJobName");
		empAddCreationJobNamePrefix = plugin.getConfig().getString("messages.chat.creationJobNamePrefix");
		empAddCreationJobNamePrefixError = textColor + plugin.getConfig().getString("messages.chat.creationJobNamePrefixError");
		empAddCreationJobNamePrefixSuccess = textColor + plugin.getConfig().getString("messages.chat.creationJobNamePrefixSuccess");
		empAddCreationJobType = textColor + plugin.getConfig().getString("messages.chat.creationJobType");
		empAddCreationJobTypeError = textColor + plugin.getConfig().getString("messages.chat.creationJobTypeError");
		empAddCreationJobTypeResponseError = textColor + plugin.getConfig().getString("messages.chat.creationJobTypeResponseError");
		empAddCreationJobBlockIds1 = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIds1");
		empAddCreationJobBlockIds2 = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIds2");
		empAddCreationJobBlockIds3 = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIds3");
		empAddCreationJobBlockIdsError = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIdsError");
		empAddCreationJobBlockIdsSyntaxError = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIdsSyntaxError");
		empAddCreationJobBlockIdsIntegerError = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIdsIntegerError");
		empAddCreationJobBlockIdsFullError = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIdsFullError");
		empAddCreationJobBlockIdsSuccess = textColor + plugin.getConfig().getString("messages.chat.creationjobBlockIdsSuccess");
		empAddCreationSuccess = textColor + plugin.getConfig().getString("messages.chat.creationSuccess");
		empAddCreationStop = textColor + plugin.getConfig().getString("messages.chat.creationStop");
		empEmptyJobList = textColor + plugin.getConfig().getString("messages.chat.emptyJobList");
		helpList = textColor + plugin.getConfig().getString("messages.help.list");
		helpAdd = textColor + plugin.getConfig().getString("messages.help.admin.add");
		empCreateSignNoJob = textColor + plugin.getConfig().getString("messages.sign.error.noJob");
		empAddOfferSuccess = textColor + plugin.getConfig().getString("messages.sign.success.addOffer");
		empAddOfferError = textColor + plugin.getConfig().getString("messages.sign.error.addOffer");
		empDeleteOfferSuccess = textColor + plugin.getConfig().getString("messages.sign.success.deleteOffer");
		empDeleteOfferError = textColor + plugin.getConfig().getString("messages.sign.error.deleteOffer");
		empDeleteOfferNoPerm = textColor + plugin.getConfig().getString("messages.sign.error.deleteNoPerm");
		empSignCreationOff = textColor + plugin.getConfig().getString("messages.chat.signCreationOff");
		empSignCreationOn = textColor + plugin.getConfig().getString("messages.chat.signCreationOn");
		empSignCreationBlocks1 = textColor + plugin.getConfig().getString("messages.chat.signCreationBlocks1");
		empSignCreationBlocks2 = textColor + plugin.getConfig().getString("messages.chat.signCreationBlocks2");

		// MySQL settings
		String dbType = plugin.getConfig().getString("database.type");
		if (dbType.equalsIgnoreCase("mysql")) {
			Config.mysql = true;
			String url = plugin.getConfig().getString("database.url");
			String user = plugin.getConfig().getString("database.user");
			String password = plugin.getConfig().getString("database.password");
			String dbPrefix = plugin.getConfig().getString("database.prefix");
			if (plugin.isEnabled()) {
				Config.database = new EMysql(plugin, url, user, password, dbPrefix);
			}
		} else {
			try {
				Config.datafile = new EFile(plugin);
				plugin.getLogger().info("Stockage en fichier ok.");
			} catch (Exception e) {
				plugin.getLogger().info("erreur lors de l'initialisation de stockage en fichier.");
				e.printStackTrace();
			}
		}

		return true;
	}

	public boolean setDefaultConfig() {

		FileConfiguration config;
		try {
			plugin.reloadConfig();
			config = plugin.getConfig();
			File configFile = new File("plugins" + System.getProperty("file.separator") + "Employeurs");
			configFile.mkdirs();

			// Database choice and config for mySql
			if (!config.contains("database.type")) {
				config.addDefault("database.type", "file");
			}
			if (!config.contains("database.user")) {
				config.addDefault("database.user", "root");
			}
			if (!config.contains("database.password")) {
				config.addDefault("database.password", "******");
			}
			if (!config.contains("database.url")) {
				config.addDefault("database.url", "jdbc:mysql://localhost:3306/minecraft");
			}
			if (!config.contains("database.prefix")) {
				config.addDefault("database.prefix", "emp_");
			}
			config.options().copyDefaults(true);
			config.options().header("Fichier de configuration du plugin Employeurs \r\n @author : HarmO \r\n");
			config.options().copyHeader(true);

			// Plugin config
			if (!config.contains("config.breakableIds")) {
				config.set("config.breakableIds", "1, 2, 3, 4, 5, 12, 13, 14, 15, 16, 17, 21, 24, 35, 43, 47, 48, 49, 52, 56, 64, 65, 66, 67, 68, 69, 70, 71, 73, 74, 77, 80, 81, 82, 83, 85, 86, 87, 88, 98, 99, 100, 101, 103, 106, 112, 113, 114, 355");
			}
			if (!config.contains("config.placeableIds")) {
				config.set("config.placeableIds", "1, 3, 4, 5, 6, 8, 10, 12, 13, 17, 20, 22, 23, 24, 25, 27, 28, 29, 33, 35, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 53, 54, 55, 57, 58, 61, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 76, 77, 81, 84, 85, 86, 87, 88, 89, 91, 92, 93, 94, 95, 96, 98, 101, 102, 103, 106, 107, 108, 109, 112, 113, 114, 321, 323, 324, 328, 330, 342, 343, 354, 355");
			}
			if (!config.contains("config.craftableIds")) {
				config.set("config.craftableIds", "5, 20, 23, 25, 27, 28, 29, 33, 35, 41, 42, 43, 44, 45, 46, 47, 50, 53, 54, 57, 58, 61, 64, 65, 66, 67, 69, 70, 71, 72, 75, 77, 84, 85, 86, 89, 93, 95, 98, 101, 102, 107, 108, 113, 114, 256, 257, 258, 259, 2561, 262, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 290, 291, 292, 293, 294, 297, 298, 299, 300, 301, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 321, 323, 324, 325, 328, 330, 333, 336, 339, 340, 342, 343, 345, 346, 351, 353, 354, 355, 356, 357, 358");
			}
			if (!config.contains("config.dropableIds")) {
				config.set("config.dropableIds", "*");
			}
			if (!config.contains("messages.sign.color")) {
				config.set("messages.sign.color", 9);
			}
			if (!config.contains("messages.sign.prefix")) {
				config.set("messages.sign.prefix", "[Emploi]");
			}
			if (!config.contains("messages.chat.color")) {
				config.set("messages.chat.color", 9);
			}
			if (!config.contains("messages.chat.noCreatePerm")) {
				config.set("messages.chat.noCreatePerm", "Vous n' avez pas la permission de creer un emploi.");
			}
			if (!config.contains("messages.chat.noListPerm")) {
				config.set("messages.chat.noListPerm", "Vous n' avez pas la permission d'afficher les emplois.");
			}
			if (!config.contains("messages.chat.noAddPerm")) {
				config.set("messages.chat.noAddPerm", "Vous n' avez pas la permission d'ajouter des emplois.");
			}
			if (!config.contains("messages.chat.noUsePerm")) {
				config.set("messages.chat.noUsePerm", "Vous n' avez pas la permission d'utiliser cela.");
			}
			if (!config.contains("messages.chat.creationOff")) {
				config.set("messages.chat.creationOff", "Mode creation d'emploi [OFF]");
			}
			if (!config.contains("messages.chat.creationOn")) {
				config.set("messages.chat.creationOn", "Mode creation d'emploi [ON]");
			}
			if (!config.contains("messages.chat.creationJobName")) {
				config.set("messages.chat.creationJobName", "Veuillez taper metier=<nom> :");
			}
			if (!config.contains("messages.chat.creationJobNamePrefix")) {
				config.set("messages.chat.creationJobNamePrefix", "metier");
			}
			if (!config.contains("messages.chat.creationJobNamePrefixError")) {
				config.set("messages.chat.creationJobNamePrefixError", "Veuillez utiliser metier=<nom>");
			}
			if (!config.contains("messages.chat.creationJobNamePrefixSuccess")) {
				config.set("messages.chat.creationJobNamePrefixSuccess", "Emploi cree avec succes :");
			}
			if (!config.contains("messages.chat.creationJobType")) {
				config.set("messages.chat.creationJobType", "Veuillez entrer l'un des types suivants en tapant type=<type> :");
			}
			if (!config.contains("messages.chat.creationJobTypeError")) {
				config.set("messages.chat.creationJobTypeError", "Veuillez utiliser type=<type>");
			}
			if (!config.contains("messages.chat.creationJobTypeResponseError")) {
				config.set("messages.chat.creationJobTypeResponseError", "Veuillez utiliser l'un des types suivants :");
			}
			if (!config.contains("messages.chat.creationjobBlockIds1")) {
				config.set("messages.chat.creationjobBlockIds1", "Veuillez entrer les ID des blocs concernes en tapant ids=<ID>");
			}
			if (!config.contains("messages.chat.creationjobBlockIds2")) {
				config.set("messages.chat.creationjobBlockIds2", "En cas de plusieurs blocks, veillez a les separer par un _");
			}
			if (!config.contains("messages.chat.creationjobBlockIds3")) {
				config.set("messages.chat.creationjobBlockIds3", "Veuillez utiliser ids=<ID>");
			}
			if (!config.contains("messages.chat.creationjobBlockIdsError")) {
				config.set("messages.chat.creationjobBlockIdsError", "Les blocks suivant ne sont pas disponible pour ce type :");
			}
			if (!config.contains("messages.chat.creationjobBlockIdsSyntaxError")) {
				config.set("messages.chat.creationjobBlockIdsSyntaxError", "Les identifiants doivent etre separes d'un _");
			}
			if (!config.contains("messages.chat.creationjobBlockIdsIntegerError")) {
				config.set("messages.chat.creationjobBlockIdsIntegerError", "Veuillez indiquer un ID entier !!!");
			}
			if (!config.contains("messages.chat.creationjobBlockIdsFullError")) {
				config.set("messages.chat.creationjobBlockIdsFullError", "Veuillez indiquer un ou plusieurs ID !!!");
			}
			if (!config.contains("messages.chat.creationjobBlockIdsSuccess")) {
				config.set("messages.chat.creationjobBlockIdsSuccess", "Identifiants enregistres pour ce metier :");
			}
			if (!config.contains("messages.chat.creationSuccess")) {
				config.set("messages.chat.creationSuccess", "Metier enregistre avec success !");
			}
			if (!config.contains("messages.chat.creationStop")) {
				config.set("messages.chat.creationStop", "tapez /empadm stop pour sortir du mode creation d'emploi.");
			}
			if (!config.contains("messages.chat.emptyJobList")) {
				config.set("messages.chat.emptyJobList", "Aucun emploi de disponible.");
			}
			if (!config.contains("messages.help.list")) {
				config.set("messages.help.list", "/emp list  => Affiche la liste des emplois disponibles.");
			}
			if (!config.contains("messages.help.admin.add")) {
				config.set("messages.help.admin.add", "/empadm add  => Ajoute un metier en database.");
			}
			if (!config.contains("messages.sign.error.noJob")) {
				config.set("messages.sign.error.noJob", "Ce metier n existe pas, demandez a un administrateur de le creer");
			}
			if (!config.contains("messages.sign.success.addOffer")) {
				config.set("messages.sign.success.addOffer", "offre d'emploi enregistree avec succes !");
			}
			if (!config.contains("messages.sign.error.addOffer")) {
				config.set("messages.sign.error.addOffer", "Probleme lors de l'enregistrement de l'offre !");
			}
			if (!config.contains("messages.sign.success.deleteOffer")) {
				config.set("messages.sign.success.deleteOffer", "Offre supprimee !");
			}
			if (!config.contains("messages.sign.error.deleteOffer")) {
				config.set("messages.sign.error.deleteOffer", "Probleme lors de la suppression de l'offre !");
			}
			if (!config.contains("messages.sign.error.deleteNoPerm")) {
				config.set("messages.sign.error.deleteNoPerm", "Vous n'avez pas le droit de supprimer cette offre");
			}
			if (!config.contains("messages.chat.signCreationOff")) {
				config.set("messages.chat.signCreationOff", "Creation d'offre d'emploi [OFF]");
			}
			if (!config.contains("messages.chat.signCreationOn")) {
				config.set("messages.chat.signCreationOn", "Creation d'offre d'emploi [ON]");
			}
			if (!config.contains("messages.chat.signCreationBlocks1")) {
				config.set("messages.chat.signCreationBlocks1", "Veuillez entrer les ID des items desires");
			}
			if (!config.contains("messages.chat.signCreationBlocks2")) {
				config.set("messages.chat.signCreationBlocks2", "Separez les avec un _ :");
			}

			plugin.saveConfig();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static EMysql getDb() {
		return database;
	}

	public static EFile getDbFile() {
		return datafile;
	}

	public static void reload() {
		if (!mysql) {
			getDbFile().reload();
		}
		else {
			//getDb().reload();
		}
	}

	public static ArrayList getAuthorizedIds(String type) {
		ArrayList aReturn = new ArrayList();
		switch (type) {
			case "break": {
				String[] aSplit = breakableIds.split(", ");
				for (int i = 0; i < aSplit.length; i++) {
					aReturn.add(new Integer(aSplit[i]));
				}
				break;
			}
			case "place": {
				String[] aSplit = placeableIds.split(", ");
				for (int i = 0; i < aSplit.length; i++) {
					aReturn.add(new Integer(aSplit[i]));
				}
				break;
			}
			case "craft": {
				String[] aSplit = craftableIds.split(", ");
				for (int i = 0; i < aSplit.length; i++) {
					aReturn.add(new Integer(aSplit[i]));
				}
				break;
			}
			case "drop":
				if (!dropableIds.equals("*")) {
					String[] aSplit = dropableIds.split(", ");
					for (int i = 0; i < aSplit.length; i++) {
						aReturn.add(new Integer(aSplit[i]));
					}
				} else {
					aReturn.add(-1);
				}
				break;
			default:
				aReturn.add("error");
				break;
		}
		return aReturn;
	}
}
