package com.m2csolutions.sql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import org.eclipse.jetty.util.log.Log;

import com.m2csolutions.lib.sql.SqlConnection;
import com.m2csolutions.lib.sql.SqlUnknownException;
import com.m2csolutions.manager.Manager;



public class SqlPacketSw {
	public static final String TABLE_NAME = "packetSw";
	private final static Logger LOG = Logger.getLogger(Manager.class.getName());

	protected SqlConnection conn = null;

	public SqlPacketSw (SqlConnection conn) {
		this.conn = conn;
	}

	public String getPasswordClient (String name) throws SQLException {
		//TODO
		String query1 = "SELECT password FROM " + "customer" + " WHERE name = '" + name + "'";
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

	public String getPasswordHost (String hostname) throws SQLException {
		//TODO
		String query1 = "SELECT key FROM " + TABLE_NAME + " WHERE hostname = '" + hostname + "'";
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

	public int getIdPacketSw (String hostname ) throws SQLException {
		String query1 = "SELECT installedPacket FROM " + "gateway" + " WHERE hostname = '" + hostname + "'";
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

	public String getSoftwareName ( int id) throws SQLException {
		String query1 = "SELECT packet FROM " + TABLE_NAME + " WHERE id = " + id ;
		ResultSet rs = conn.select(query1);
		String packet = "";
		if (rs != null) {
			try {
				if (rs.next()) {
					packet = rs.getString(1);

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
		return packet;
	}

	public Date[] getDateSoftware ( String packet) throws SQLException {
		String query1 = "SELECT count(date) FROM " + TABLE_NAME + " WHERE packet = '" + packet + "'" ;
		ResultSet rs1 = conn.select(query1);
		int i = 0;
		if(rs1 != null){
			try{
				if (rs1.next()) {
					i =rs1.getInt(1);
				}

			}catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs1);

		}

		String query2 = "SELECT date FROM " + TABLE_NAME + " WHERE packet = '" + packet + "'" ;
		ResultSet rs2 = conn.select(query2);
		Date[] compareDate = new Date[i] ;
		int k = 1;
		int j=0;
		if (rs2 != null) {
			try {
				while (rs2.next()) {
					compareDate[j] = rs2.getDate("date");
					k++;
					j++;
				} 
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet, is " + rs2);
		}
		return compareDate;
	}

	public String getLastVersionHost ( Date date, String hostname) throws SQLException {
		String query1 = "SELECT version FROM " + TABLE_NAME + " WHERE date = '" + date + "'";
		ResultSet rs = conn.select(query1);
		String lastVersion = "";
		if (rs != null) {
			try {
				if (rs.next()) {
					lastVersion = rs.getString(1);

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
		return lastVersion;
	}

	public String getActualVersionHost ( int id) throws SQLException {
		String query1 = "SELECT version FROM " + TABLE_NAME + " WHERE id = " + id ;
		ResultSet rs = conn.select(query1);
		String version = "";
		if (rs != null) {
			try {
				if (rs.next()) {
					version = rs.getString(1);

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
		return version;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
