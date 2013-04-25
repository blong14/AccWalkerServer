package org.biodynamicslab.accwalker.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.biodynamicslab.accwalker.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory( "transactions-optional" );
		
	@Override
	public void removeFromList( String trial ){
		
	    PersistenceManager pm = getPersistenceManager();
	    
	    try {
	    	
	    	Query q = pm.newQuery( Walker.class );
	        @SuppressWarnings("unchecked")
			List<Walker> mWalkers = (List<Walker>) q.execute();
	        
	        for ( Walker nWalker : mWalkers ) {
	        	
	            if ( trial.equalsIgnoreCase( nWalker.getTrial() ) ) {
	            
	              pm.deletePersistent( nWalker );
	            }
	          }
	    } finally {
	    	
	      pm.close();
	    }
	}
	
	@Override
	public String[] getList() {
		
		PersistenceManager pm = getPersistenceManager();
		List<String> mWalkers= new ArrayList<String>();
		
		try{
		
			Query q= pm.newQuery( Walker.class );
			
			@SuppressWarnings("unchecked")
			List<Walker> walkCollections = (List<Walker>) q.execute();
		      
			for ( Walker nWalker : walkCollections ) {
		        
				mWalkers.add( nWalker.getTrial() );
			}
		
		}finally{
			
			pm.close();
		}
		
		return (String[]) mWalkers.toArray( new String[0] );
	}
	
	private PersistenceManager getPersistenceManager() {
		    
		return PMF.getPersistenceManager();
	}
}