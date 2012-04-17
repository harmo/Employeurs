package fr.harmo.Employeurs.Database;

import fr.harmo.Employeurs.Employeurs;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author
 * HarmO
 */
public class EMysql extends Database{
	
	private Employeurs plugin;

	public EMysql(Employeurs plugin, String url, String user, String password, String prefix) {
		super(plugin, "com.mysql.jdbc.Driver", url, user, password, prefix);
		this.plugin = plugin;
		this.setUp();
	}

	private void setUp() {
		try {
			EConnection conn = getConnection();
			if(conn != null) {
				Statement st = conn.createStatement();
				String table = "CREATE TABLE IF NOT EXISTS `" + getPrefix() + "emplois` (id int(10) AUTO_INCREMENT, x int(10) NOT NULL, y int(10) NOT NULL, z int(10) NOT NULL, world varchar(100) NOT NULL, metier int(10) NOT NULL, type int(10) NOT NULL, nombre INT(10) NOT NULL, salaire INT(10) NOT NUL, PRIMARY KEY(id));";
				st.executeUpdate(table);
				conn.close();
				plugin.getLogger().info("MySQL connection ok !!!");
			}
			else {
				System.err.println("MySQL erreur connexion !!!");
				plugin.disablePlugin();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			plugin.disablePlugin();
		}
	}
	
}
