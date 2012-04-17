package fr.harmo.Employeurs.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author
 * HarmO
 */
class EConnectionPool {
	private LinkedList<EConnection> pooledConnections;
	private String url;
	private String user;
	private String password;

	public EConnectionPool(String driver, String url, String user, String password) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.pooledConnections = new LinkedList<>();
		Class.forName(driver);
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public synchronized EConnection getConnection() throws SQLException {
		while(!pooledConnections.isEmpty()) {
			EConnection conn = pooledConnections.remove();
			if (conn.isClosed() || !conn.isValid(1)) {
				try {
					conn.closeConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				continue;
			}
			return conn;
		}
		Connection conn = DriverManager.getConnection(url, user, password);
		return new EConnection(conn, this);
	}
	
	public synchronized void returnToPool(EConnection conn){
		pooledConnections.add(conn);
	}

	void closeConnections() {
		while(!pooledConnections.isEmpty()) {
			EConnection conn = pooledConnections.remove();
			try {
				conn.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
