package com.m2csolutions.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.m2csolutions.manager.Manager;
import com.m2csolutions.sql.SqlPacketSw;



public class UpdateServlet extends HttpServlet{
	private final static Logger LOG = Logger.getLogger(StatusServlet.class.getName());

	public static final String APP_JSON = "application/json";

	protected Manager manager = null;

	public UpdateServlet(){
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
	private static final long serialVersionUID = 73081878642120L;


	/**
	 * 
	 * HTTP GET msg is expected. Content body must be a JSON with the next format:
	 * { hostname: “hostname”, key: “publicKey”, mac: “mac eth0”}
	 * @throws ServletException 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{

		StringBuffer strReader = new StringBuffer();
		String line = null;
		BufferedReader reader;
		try {
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				strReader.append(line);
				LOG.info("recieve message is: " + strReader.append(line));
			}
		} catch (IOException e) {
			LOG.warning("IOException reading request content: " + e.getMessage());
			response.setStatus (HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Malformed json request");
			return;
		}

		try {
			if(strReader.toString().equals("")){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			JSONObject json = new JSONObject (strReader.toString());
			LOG.info("json recieved is: " + json);
			if(json.has("hostname")){
				String hostname = json.getString("hostname");
				//LOG.info("hostname is " + hostname);
				String key = json.getString("key");
				//LOG.info("key is " + key);
				if(this.manager.getPasswordHost(hostname).equals(key)){
					LOG.info("Welcome");
					String mac = json.getString("mac");
					//LOG.info("mac is " + mac);
					int id_packetSw = this.manager.getIdPacketSoftware(hostname);
					String softwareName = this.manager.getIdPacketSoftwareName(id_packetSw);
					String actualVersion = this.manager.getActualVersion(id_packetSw);
					Date lastDate = this.manager.lastAvailableSoftware(hostname, id_packetSw);
					String lastVersion = this.manager.getLastVersion(hostname, lastDate);
					if(actualVersion.equals(lastVersion)){
						LOG.info("Software is updated");
						response.setStatus (HttpServletResponse.SC_NOT_MODIFIED);
					}else{
						LOG.info("Software is not updated");
						if(this.manager.getNotUpdate(hostname) || this.manager.getForceUpdate(hostname)){
							LOG.info("Looking for new version");

							File directory = new File("/home/natalia/prueba/"); ///opt/m2csolutions/api/gw/swVersion/
							String nombreFichero = softwareName + ".zip";
							if(this.manager.searchNewVersion(nombreFichero,  directory)==true){
								LOG.info("File was found!");

								String fileName = directory.getAbsolutePath() + "/" + nombreFichero;
								File returnFile = new File(fileName);


								ServletOutputStream out = response.getOutputStream();
								ServletContext context = getServletConfig().getServletContext();
								String mimetype = context.getMimeType(fileName);

								response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
								response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);

								FileInputStream in = new FileInputStream(returnFile);
								byte[] buffer = new byte[1000000000]; 

								int length;
								while((length = in.read(buffer)) > 0) {
									out.write(buffer, 0, length);
								}
								in.close();
								out.flush();


								response.setStatus (HttpServletResponse.SC_OK);
								LOG.info("Status is: " + response.getStatus());


							}else{
								LOG.warning("There isn't available a new version in that directory");
								response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							}
						}
					}
				}else{
					LOG.info("wrong hostname or key");
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}

			}
		}catch (JSONException jsonEx) {
			LOG.warning("Malformed JSON: " + jsonEx.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
}
