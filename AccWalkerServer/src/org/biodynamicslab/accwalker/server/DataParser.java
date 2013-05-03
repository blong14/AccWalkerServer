package org.biodynamicslab.accwalker.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The DataParser class handles parsing Accelerometer data collected on
 * and Android device and saving that data to the server
 * 
 * @author Ben Long
 *
 */
@SuppressWarnings("serial")
@RemoteServiceRelativePath("data")
public class DataParser extends HttpServlet {
	
	/**The Factory to persist objects on the server*/
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory( "transactions-optional" );
	
	/**Logger to log important events during data parsing*/
	private static final Logger log= Logger.getLogger( DataParser.class.getName() );
	
	/**
	 * The doPost method handles requests from the client. This method directs
	 * all incoming traffic.
	 * 
	 * @param req The request
	 * @param resp The response
	 */
	public void doPost( HttpServletRequest req, HttpServletResponse resp ){
		
		//Log the request info
		log.info( "Path Info: " + req.getPathInfo());
		
		//Variables used to read in info from the request
		StringBuilder sb= new StringBuilder();
		String line;
		
		//Read data in from the request
		try {
			
			BufferedReader reader= req.getReader();
			
			while( ( line = reader.readLine() ) != null ){
				
				sb.append( line );
			}
			
			//Send data to be parsed and saved to the server
			parseData( sb.toString() );
	
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
	}
	
	/**
	 * The getPersistenceManager method returns the persistence manager for this 
	 * servlet
	 * 
	 * @return The persistence manager
	 */
	private PersistenceManager getPersistenceManager() {
	    
		return PMF.getPersistenceManager();
	}
	
	/**
	 * the parseData method handles parsing JSON data to be stored on the Server
	 * 
	 * @param data The data to parse
	 */
	private void parseData( String data ){
		
		//Here we use GSON to decode JSON encoded data from the client
		Gson gson= new Gson();
		Walker mWalker= gson.fromJson( data, Walker.class );
		
		//Now that data is decoded, we can push the persistent manager to handle storing the data
		//as a Walker object on the server
		PersistenceManager pm= getPersistenceManager();
		pm.makePersistent( mWalker );
		pm.close();
	}
}