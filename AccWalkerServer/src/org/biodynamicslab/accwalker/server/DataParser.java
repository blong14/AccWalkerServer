package org.biodynamicslab.accwalker.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class DataParser extends HttpServlet {
	
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory( "transactions-optional" );
	
	public void doPost( HttpServletRequest req, HttpServletResponse resp ){
		
		PersistenceManager pm = getPersistenceManager();
	    
		try {
			
			pm.makePersistent( new Walker( "Ben Long" ) );
			
			} finally {
		    	
				pm.close();
			}
	}
	
	private PersistenceManager getPersistenceManager() {
	    
		return PMF.getPersistenceManager();
	}

}
