package com.m2csolutions.lib.sql;

/*
 * Copyright (C) Machine To Cloud Solutions, S.L. - 2015 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * SQL Utils
 * @author Félix Mencías Morante
 * @organization Machine To Cloud Solutions, S.L.
 * @version 0.1
 * @date 2015-8-10
 */
public class SqlUtils {

	/**
	 * Get first short and close the result set
	 * @param rs
	 * @return
	 * @throws SqlUnknownException 
	 */
	public static short getFirstShortAndClose (ResultSet rs) throws SqlUnknownException {
		return (short) SqlUtils.getFirstIntAndClose(rs);
		
	}
	
	/**
	 * Get first short and close the result set
	 * @param rs
	 * @return
	 * @throws SqlUnknownException 
	 */
	public static int getFirstIntAndClose (ResultSet rs) throws SqlUnknownException {
		if (rs != null) {
			try {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					// empty
					throw new SqlUnknownException ("Empty result set");

				}
			} catch (SQLException sqlEx) {
				throw new SqlUnknownException ("new SQLException: " + sqlEx);
			}
		} else {
			throw new SqlUnknownException ("Error in resultSet");
		}
	}
	
}

