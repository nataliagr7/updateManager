package com.m2csolutions.manager;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Vector;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.handler.*;

import com.m2csolutions.lib.sql.SqlConnection;
import com.m2csolutions.servlet.StatusServlet;
import com.m2csolutions.servlet.UpdateServlet;
import com.m2csolutions.sql.SqlGateway;
import com.m2csolutions.sql.SqlPacketSw;



import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Manager Class.
 * Support:
 * - send new version of software
 * - check status of the host
 * @author Natalia Gómez Rodríguez
 * @author Machine To Cloud Solutions SL
 *
 */


public class Manager {

	private final static Logger LOG = Logger.getLogger(Manager.class.getName());
	protected SqlConnection sqlManager = null;

	protected SqlPacketSw sqlPass = null;
	protected SqlGateway sqlHost = null;

	public static void main (String[] args) throws Exception, UnknownHostException{


		new Manager();

		Server server = new Server(8080);

		LOG.info("Server was created");

		ServletContextHandler context = new ServletContextHandler (ServletContextHandler.SESSIONS);
		context.setContextPath("/gw");
		server.setHandler(context);

		context.addServlet(new ServletHolder(new StatusServlet()),"/status");
		context.addServlet(new ServletHolder(new UpdateServlet()),"/sw_update");


		try {
			server.start();
			//server.join();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception (e.getMessage());
		}
	}

	/**
	 * Manager constructor
	 */
	public Manager ()  throws Exception{

		try {
			sqlManager = new SqlConnection ("com.mysql.jdbc.Driver", 
					"jdbc:mysql://localhost/m2c_update_manager?user=m2c_test&password=3j78.K.-2");
		} catch (SQLException e) {
			LOG.severe("Connecting with database failed");
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			LOG.severe("Connecting with database failed");
			e.printStackTrace();
			System.exit(1);
		}

		this.sqlPass = new SqlPacketSw (sqlManager);
		this.sqlHost = new SqlGateway (sqlManager);
	}

	/** get the password of the client that was saved in db
	 * @param client
	 * @return Password
	 */
	public String getPassword(String client){
		String password = "";
		try {
			password = this.sqlPass.getPasswordClient(client);
			if(password == ""){
				LOG.warning("Password not found");
			}
			//LOG.info("Password is: " + password);
		} catch (SQLException sqlEx) {
			LOG.warning("Client not found: " + client);
			LOG.info(sqlEx.getMessage());
		}
		return password;
	}

	/** get id of packetSw in db
	 * @param hostname
	 * @return id
	 */
	public int getIdPacketSoftware(String hostname){
		int id = 0;
		try {
			id = this.sqlPass.getIdPacketSw(hostname);
			if(id == 0){
				LOG.warning("id packet not found");
			}
			//LOG.info("id is: " + id);
		} catch (SQLException sqlEx) {
			LOG.warning("Hostname or id packetSw not found: " + hostname + " ," + id);
			LOG.info(sqlEx.getMessage());
		}
		return id;
	}

	/** get name of software
	 * @param id
	 * @return name
	 */
	public String getIdPacketSoftwareName(int id){
		String name = "";
		try {
			name = this.sqlPass.getSoftwareName(id);
			if(name.equals("")){
				LOG.warning("software name not found");
			}
			//LOG.info("id is: " + id);
		} catch (SQLException sqlEx) {
			LOG.warning("id packetSw or name of software not found: " + name + " ," + id);
			LOG.info(sqlEx.getMessage());
		}
		return name;
	}

	/** Compare dates of versions of software in db
	 * @param hostname
	 * @param id of the packetSw
	 * @return the last date of software
	 */
	public Date lastAvailableSoftware(String hostname, int id){

		Date lastDate = new Date(2000-01-01);
		Date compareDate;
		try {
			String packetName = this.sqlPass.getSoftwareName( id);
			if(packetName == ""){
				LOG.warning("packet name not found");
			}
			LOG.info("packet name is: " + packetName);
			int arraySize = this.sqlPass.getDateSoftware( packetName).length;
			if(arraySize == 0){
				LOG.warning("there isn't software to update");
			}else{
				lastDate= this.sqlPass.getDateSoftware( packetName)[0];
				for( int i=1; i<arraySize; i++){
					compareDate = this.sqlPass.getDateSoftware( packetName)[i];
					//LOG.info("compare date is: " + compareDate);
					if(compareDate.after(lastDate)){
						lastDate = compareDate;
					}
				}
				LOG.info("last date is: " + lastDate);
				//LOG.info("id is: " + id);
			}

		} catch (SQLException sqlEx) {
			LOG.warning("Date with hostname " + hostname + " and id " + id + " not found");
			LOG.info(sqlEx.getMessage());
		}

		return lastDate;
	}

	/** software should be update or not
	 * @param hostname
	 * @return boolean
	 */
	public boolean getNotUpdate(String hostname){
		boolean update = false;
		int notUpdate = 2;
		try {
			int id_gateway = this.sqlHost.idGateway(hostname);
			if(id_gateway !=0){
				notUpdate = this.sqlHost.getNotUpdateHost(id_gateway);
				if(notUpdate == 2){
					LOG.warning("notUpdate not found");
				}
				//LOG.info("notUpdate is: " + notUpdate);
				if(notUpdate == 0){
					update = true;
				}
			}
		} catch (SQLException sqlEx) {
			LOG.warning("Hostname " + hostname + " or notUpdate " + notUpdate + " not found");
			LOG.info(sqlEx.getMessage());
		}
		return update;
	}

	/** Software should be update
	 * @param hostname
	 * @return true if software must be update
	 */
	public boolean getForceUpdate(String hostname){
		boolean update = false;
		int forceUpdate = 2;
		try {
			int id_gateway = this.sqlHost.idGateway(hostname);
			if(id_gateway !=0){
				forceUpdate = this.sqlHost.getNotUpdateHost(id_gateway);
				if(forceUpdate == 2){
					LOG.warning("forceUpdate not found");
				}
				//LOG.info("forceUpdate is: " + forceUpdate);
				if(forceUpdate == 0){
					update = true;
				}
			}
		} catch (SQLException sqlEx) {
			LOG.warning("Hostname " + hostname + " or forceUpdate " + forceUpdate + " not found");
			LOG.info(sqlEx.getMessage());
		}
		return update;
	}

	/** get last version of software in db by last date
	 * @param hostname
	 * @param date
	 * @return last version
	 */
	public String getLastVersion(String hostname, Date date){
		String lastVersion = "";
		try {
			lastVersion = this.sqlPass.getLastVersionHost(date, hostname);
			if(lastVersion == ""){
				LOG.warning("Last version not found");
			}
			LOG.info("lastVersion is: " + lastVersion);
		} catch (SQLException sqlEx) {
			LOG.warning("Hostname  or date not found: " + hostname + " ," + date);
			LOG.info(sqlEx.getMessage());
		}
		return lastVersion;
	}

	/** get the actual version of the software
	 * @param id of packetSw
	 * @return version
	 */
	public String getActualVersion(int id){
		String actualVersion = "";
		try {
			actualVersion = this.sqlPass.getActualVersionHost( id);
			if(actualVersion == ""){
				LOG.warning("Actual version not found");
			}
			LOG.info("ActualVersion is: " + actualVersion);
		} catch (SQLException sqlEx) {
			LOG.warning("ActualVersion or id not found: " + actualVersion + " ," + id);
			LOG.info(sqlEx.getMessage());
		}
		return actualVersion;
	}

	/** get host's password that was saved in db
	 * @param hostname
	 * @return password
	 */
	public String getPasswordHost(String hostname){
		String password = "";
		try {
			password = this.sqlHost.getPasswordHost(hostname);
			if(password == ""){
				LOG.warning("Password not found");
			}
			//LOG.info("Password is: " + password);
		} catch (SQLException sqlEx) {
			LOG.warning("Hostname not found: " + hostname);
			LOG.info(sqlEx.getMessage());
		}
		return password;
	}

	/** get uptime 
	 * @param hostname
	 * @return uptime
	 */
	public int getUptime(String hostname){
		int uptime = 0;
		try {
			int id_gateway = this.sqlHost.idGateway(hostname);
			if(id_gateway != 0){
				uptime = this.sqlHost.getUptimeHost(id_gateway);
				//LOG.info("Uptime is: " + uptime);
			}else{
				LOG.warning("uptime not found for host: " + hostname);
			}

		} catch (SQLException sqlEx) {
			LOG.warning("Hostname or uptime not found: " + hostname);
			LOG.info(sqlEx.getMessage());
		}
		return uptime;
	}

	/** get gsmRange
	 * @param hostname
	 * @return gsmRange
	 */
	public int getGsmRange(String hostname){
		int gsmRange = 0;
		try {
			int id_gateway = this.sqlHost.idGateway(hostname);
			if(id_gateway != 0){
				gsmRange = this.sqlHost.getGsmRangeHost(id_gateway);
				//LOG.info("gsmRange is: " + gsmRange);
			}else{
				LOG.warning("gsmRange not found for host: " + hostname);
			}

		} catch (SQLException sqlEx) {
			LOG.warning("Hostname or gsmRange not found: " + hostname);
			LOG.info(sqlEx.getMessage());
		}
		return gsmRange;
	}


	/** find the file of the new version
	 * @param argFichero 
	 * @param argFile
	 * @return boolean
	 */
	public boolean searchNewVersion (String argFichero, File argFile) {
		File[] list = argFile.listFiles();
		boolean found= false;
		if (list != null) {
			for (File elemento : list) {
				if (elemento.isDirectory()) {
					searchNewVersion (argFichero, elemento);
				} else if (argFichero.equalsIgnoreCase(elemento.getName())) {
					System.out.println (elemento.getParentFile());
					found=true;
				}
			}
		}
		return found;
	}

}
