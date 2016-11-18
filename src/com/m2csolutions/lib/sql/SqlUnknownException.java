package com.m2csolutions.lib.sql;

/* Copyright (C) Machine To Cloud Solutions, S.L. - 2015 - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
*/

import java.sql.SQLException;

/**
* Generic Exception for managed SQL errors
* @author Félix Mencías Morante
* @organization Machine To Cloud Solutions, S.L.
* @version 0.1
* @date 2015-8-10
*/
public class SqlUnknownException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6663311001119636349L;

	public SqlUnknownException() {
		// TODO Auto-generated constructor stub
	}

	public SqlUnknownException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SqlUnknownException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public SqlUnknownException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}



}

