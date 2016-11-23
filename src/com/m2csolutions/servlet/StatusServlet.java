package com.m2csolutions.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
//import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServlet;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import org.eclipse.jetty.http.*;

import com.m2csolutions.manager.Manager;
import com.m2csolutions.sql.SqlPacketSw;



import javax.servlet.http.*;


public class StatusServlet extends HttpServlet{

	private final static Logger LOG = Logger.getLogger(StatusServlet.class.getName());

	public static final String APP_JSON = "application/json";

	protected Manager manager = null;

	public StatusServlet(){
		try {
			this.manager = new Manager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1963982992111411319L;



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}


	/**
	 * 
	 * HTTP POST msg is expected. Content body must be a JSON with the next format:
	 * { hostname: “hostname”, key: “publicKey”, uptime: uptime, gsmRange: gsmRange, date: timestamp, flashStatus: “flashStatus”, serviceStatus: “serviceStatus”}
	 * @throws ServletException 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		StringBuffer strReader = new StringBuffer();
		String line = null;
		BufferedReader reader;
		try {
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strReader.append(line);
			}
		} catch (IOException e) {
			LOG.warning("IOException reading request content: " + e.getMessage());
			response.setStatus (HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Malformed json request");
			return;
		}

		try {
			JSONObject json = new JSONObject (strReader.toString());
			LOG.info("json recieved is: " + json);
			if(json.has("hostname")){
				String hostname = json.getString("hostname");
				//LOG.info("hostname is " + hostname);
				String key = json.getString("key");
				//LOG.info("public key is " + key);

				String passwordDB = this.manager.getPasswordHost(hostname);
				if(passwordDB == ""){
					LOG.warning("Password not found");
				}

				if(key.equals(passwordDB)){
					LOG.info("Welcome");

					Integer uptime = json.getInt("uptime");
					LOG.info("uptime json is " + uptime);

					if(this.manager.getUptime(hostname) == uptime){
						Integer gsmRange = json.getInt("gsmRange");
						LOG.info("gsmRange is " + gsmRange);
						if(this.manager.getGsmRange(hostname) == gsmRange){
							long date = json.getLong("date");
							LOG.info("jsonDate is " + date);

							//Actual date in UTC format
							DateFormat converter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date localtime = new Date();
							converter.setTimeZone(TimeZone.getTimeZone("UTC"));
							String actualDateString = converter.format(localtime);
							LOG.info("Fecha formato UTC: " + actualDateString);

							//Convert to date to compare
							Date actualDate = new Date();
							DateFormat converterToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							actualDate = converterToDate.parse(actualDateString);
							//long time = actualDate.getTime();
							//System.out.println("El actualDate es: " + actualDate + " y en milisegundos: " + time);
							Date hostDate = new Date(date);
							LOG.info("hostDate is: " + hostDate);

							//Add 10 seconds to actual date
							Calendar calendarActualDate = Calendar.getInstance();
							calendarActualDate.setTime(actualDate);
							calendarActualDate.add(Calendar.SECOND, 10);
							Date actualDate10 = calendarActualDate.getTime();

							LOG.info("actualDate +10 is: " + actualDate10);

							//Subtract 10 seconds to actual date
							Calendar calendarActualDate_10 = Calendar.getInstance();
							calendarActualDate_10.setTime(actualDate);
							calendarActualDate_10.add(Calendar.SECOND, -10);
							Date actualDate_10 = calendarActualDate_10.getTime();

							LOG.info("actualDate -10 is: " + actualDate_10);

							//System.out.println("local time : " + localtime);
							//System.out.println("time in UTC : " + converter.format(localtime));


							if(hostDate.equals(actualDate) == false && (hostDate.after(actualDate10) || hostDate.before(actualDate_10))){

								LOG.warning("Date is wrong, actual date is: " + actualDateString + " and hostDate is: "+ hostDate);
								JSONObject obj = new JSONObject();
								obj.put("date", actualDateString);

								FileWriter writeJson = new FileWriter("/home/natalia/prueba/" + actualDateString + ".json"); ///opt/m2csolutions/api/gw/correctDate/
								writeJson.write(obj.toString());
								writeJson.flush();
								writeJson.close();

								File file = new File("/home/natalia/prueba/" + actualDateString + ".json"); ///opt/m2csolutions/api/gw/correctDate/

								String fileName = file.getAbsolutePath();
								LOG.info("Path is: " + fileName);
								ServletOutputStream out = response.getOutputStream();
								ServletContext context = getServletConfig().getServletContext();
								String mimetype = context.getMimeType(fileName);

								response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
								response.setHeader("Content-Disposition", "attachment; filename=" + actualDateString + ".json");
								response.setStatus(HttpServletResponse.SC_FORBIDDEN);

								FileInputStream in = new FileInputStream(file);
								byte[] buffer = new byte[1000000000]; //4096

								int length;
								while((length = in.read(buffer)) > 0) {
									out.write(buffer, 0, length);
								}
								in.close();
								out.flush();


							}else{

								String flashStatus = json.getString("flashStatus");
								LOG.info("flashStatus is " + flashStatus);
								String serviceStatus = json.getString("serviceStatus");
								LOG.info("serviceStatus is " + serviceStatus);

								response.setStatus (HttpServletResponse.SC_OK);
							}
						}else{
							LOG.warning("wrong gsmRange");
							response.setStatus (HttpServletResponse.SC_FORBIDDEN);
							int status = response.getStatus();
							LOG.info("status is: " + status);
						}

					}else{
						LOG.warning("wrong uptime");
						response.setStatus (HttpServletResponse.SC_FORBIDDEN);
						int status = response.getStatus();
						LOG.info("status is: " + status);
					}

				}else{
					LOG.info("wrong hostname or key");
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}



			}
		} catch (JSONException jsonEx) {
			LOG.warning("Malformed JSON: " + jsonEx.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//response.getStatus();
		int status = response.getStatus();
		LOG.info("status is: " + status);

	}

}
