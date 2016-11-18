package com.m2csolutions.sql;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.m2csolutions.lib.sql.SqlConnection;
import com.m2csolutions.lib.sql.SqlTable;
import com.m2csolutions.lib.sql.SqlUnknownException;




public class SqlGateway extends SqlTable{

	public static final String TABLE_NAME = "gateway";

	protected SqlConnection conn = null;

	public SqlGateway (SqlConnection conn) {
		this.conn = conn;
	}

	public String getPasswordHost (String hostname) throws SQLException {
		//TODO
		String query1 = "SELECT key_host FROM " + TABLE_NAME + " WHERE hostname = '" + hostname + "'";
		ResultSet rs = conn.select(query1);
		String password = "";
		if (rs != null) {
			try {
				if (rs.next()) {
					password = rs.getString(1);

				} else {
					// empty
					throw new SqlUnknownException ("Empty result set");

				}
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs);
		}
		return password;
	}

	public int idGateway (String hostname) throws SQLException {
		//TODO
		String query1 = "SELECT id FROM " + TABLE_NAME + " WHERE hostname = '" + hostname + "'";
		ResultSet rs = conn.select(query1);
		int id = 0;
		if (rs != null) {
			try {
				if (rs.next()) {
					id = rs.getInt(1);

				} else {
					// empty
					throw new SqlUnknownException ("Empty result set");

				}
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs);
		}
		return id;
	}

	public int getUptimeHost (int id_gateway) throws SQLException {
		//TODO
		String query1 = "SELECT uptime FROM " + "status" + " WHERE id_gateway = '" + id_gateway + "'";
		ResultSet rs = conn.select(query1);
		int uptime = 0;
		if (rs != null) {
			try {
				if (rs.next()) {
					uptime = rs.getInt(1);

				} else {
					// empty
					throw new SqlUnknownException ("Empty result set");

				}
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs);
		}

		return uptime;
	}

	public int getGsmRangeHost (int id_gateway) throws SQLException {
		//TODO
		String query1 = "SELECT gsmRange FROM " + "status" + " WHERE id_gateway = '" + id_gateway + "'";
		ResultSet rs = conn.select(query1);
		int gsmRange = 0;
		if (rs != null) {
			try {
				if (rs.next()) {
					gsmRange = rs.getInt(1);

				} else {
					// empty
					throw new SqlUnknownException ("Empty result set");

				}
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs);
		}

		return gsmRange;
	}

	public int getNotUpdateHost (int id_gateway) throws SQLException {
		//TODO
		String query1 = "SELECT notUpdate FROM " + TABLE_NAME + " WHERE id = '" + id_gateway + "'";
		ResultSet rs = conn.select(query1);
		int update = 2;
		if (rs != null) {
			try {
				if (rs.next()) {
					update = rs.getInt(1);

				} else {
					// empty
					throw new SqlUnknownException ("Empty result set");

				}
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs);
		}

		return update;
	}

	public int getForceUpdateHost (int id_gateway) throws SQLException {
		//TODO
		String query1 = "SELECT notUpdate FROM " + TABLE_NAME + " WHERE id = '" + id_gateway + "'";
		ResultSet rs = conn.select(query1);
		int update = 2;
		if (rs != null) {
			try {
				if (rs.next()) {
					update = rs.getInt(1);

				} else {
					// empty
					throw new SqlUnknownException ("Empty result set");

				}
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs);
		}

		return update;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
