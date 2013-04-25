package org.biodynamicslab.accwalker.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@SuppressWarnings("serial")
@RemoteServiceRelativePath("data")
public class DataParser extends HttpServlet {
	
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory( "transactions-optional" );
	
	private static final Logger log= Logger.getLogger( DataParser.class.getName() );
	
	public void doPost( HttpServletRequest req, HttpServletResponse resp ){
		
		log.info( "Path Info: " + req.getPathInfo());
		
		StringBuilder sb= new StringBuilder();
		String line;
		
		try {
			
			BufferedReader reader= req.getReader();
			
			while( ( line = reader.readLine() ) != null ){
				
				sb.append( line );
			}
			
			parseData( sb.toString() );
	
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
	}
	
	private PersistenceManager getPersistenceManager() {
	    
		return PMF.getPersistenceManager();
	}
	
	private void parseData( String data ){
		
		@SuppressWarnings("rawtypes")
		Map jsonData = new Gson().fromJson( data, Map.class );
		Double time= (Double) jsonData.get( "time" );
		Float[] DataZ= (Float[]) jsonData.get( "DataZ" );

		PersistenceManager pm= getPersistenceManager();
		pm.makePersistent( new Walker( ( String )jsonData.get( "trial" ), time.intValue()/1000, DataZ ) );
		pm.close();
	}
}