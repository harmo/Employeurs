package fr.harmo.Employeurs.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author
 * HarmO
 */
public class EConnection {

	private Connection conn;
	private EConnectionPool pool;

	public EConnection(Connection conn, EConnectionPool pool) {
		this.conn = conn;
		this.pool = pool;
	}

	public synchronized boolean isClosed() {
		try {
			return conn.isClosed();
		} catch (SQLException e) {
			return true;
		}
	}

	public synchronized boolean isValid(int timeout) throws SQLException {
		try {
			return conn.isValid(timeout);
		} catch (AbstractMethodError e) {
			return true;
		}
	}

	public synchronized void close() {
		pool.returnToPool(this);
	}

	public synchronized void closeConnection() throws SQLException {
		conn.close();
	}

	public synchronized Statement createStatement() throws SQLException {
		return conn.createStatement();
	}

	public synchronized PreparedStatement prepareStatement(String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}
}
