package com.m2csolutions.lib.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author Félix Mencías Morante
 *
 */
public class SqlConnection {
	
	protected Connection conn = null;
	
	/**
	 * Logger
	 */
	protected Logger log = Logger.getLogger(SqlConnection.class.getName());
	

	/**
	 * Public constructor
	 * @param conn opened SQL connection
	 */
	public SqlConnection (Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * Public constructor to build a new connection
	 * @param driver
	 * @param strConn
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public SqlConnection (String driver, String strConn) throws SQLException, ClassNotFoundException {
		
        Class.forName(driver);
        this.conn = DriverManager.getConnection(strConn);
	}
	
	/**
	 * Invoke a SELECT command.
	 * It's recomended to close the returned stmt
	 * @param sql String 
	 * @return resultSet or null
	 */
	public ResultSet select (String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException sqlEx) {
			log.warning(sqlEx.getMessage());
			sqlEx.printStackTrace();
			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// ignored
				}
			}
			return null;
		}
		
		return rs;
	}
	
	/**
	 * Insert and retrieve id fields
	 * @param sql
	 * @return resultSet with id fields
	 * @throws SQLException
	 */
	public ResultSet insertAndGetId (String sql) throws SQLException {
		Statement stmt = null;

		stmt = this.conn.createStatement();
		stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs =  stmt.getGeneratedKeys();
		stmt.close();
		return rs;
	}
	
	/**
	 * Insert a new field
	 * @param sql
	 * @throws SQLException
	 */
	public void insert (String sql) throws SQLException {
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

}