package fr.harmo.Employeurs.Database;

import fr.harmo.Employeurs.Employeurs;
import java.sql.SQLException;

/**
 *
 * @author HarmO
 */
public class Database {
	
	private Employeurs plugin;
	private String prefix;
	private EConnectionPool pool;

	public Database(Employeurs plugin, String driver, String url, String user, String password, String prefix) {
		this.plugin = plugin;
		this.prefix = prefix;
		try {
			this.pool = new EConnectionPool(driver, url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.print("Connection database impossible");
			plugin.disablePlugin();
		}
	}
	
	/********************************************/
	/*                   SQL REQUESTS HERE                          */
	/********************************************/
	
	
	/********************************************/
	
	protected String getPrefix() {
		return this.prefix;
	}
	
	protected EConnection getConnection() throws SQLException {
		return this.pool.getConnection();
	}
	
	public void closeConnections() {
		this.pool.closeConnections();
	}
	
}
