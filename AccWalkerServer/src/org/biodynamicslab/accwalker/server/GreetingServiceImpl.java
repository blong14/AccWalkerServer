package org.biodynamicslab.accwalker.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.biodynamicslab.accwalker.client.GreetingService;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 * 
 * @author Ben Long
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	/**The Factory to persist and retrieve objects on the server*/
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory( "transactions-optional" );
		
	/**
	 * The removeFromList method handles deleted Walker objects from the server
	 * 
	 * @param The trial to delete from the server
	 */
	@Override
	public void removeFromList( String trial ){
	
	    PersistenceManager pm = getPersistenceManager();
	    
	    try {
	    	
	    	//Query all of the Walker objects on the server
	    	Query q = pm.newQuery( Walker.class );
	    	
	        @SuppressWarnings("unchecked")
			List<Walker> mWalkers = (List<Walker>) q.execute();
	        
	        //Look for the trial to delete
	        for ( Walker nWalker : mWalkers ) {
	        	
	            if ( trial.equalsIgnoreCase( nWalker.getTrial() ) ) {
	            	
	            	//Now delete the Walker object from the server
	            	pm.deletePersistent( nWalker );
	            }
	          }
	    } finally {
	    	
	      pm.close();
	    }
	}
	
	/**
	 * The getList method handles retrieving the trial information saved on the server
	 * 
	 * @return The list of trials saved on the server
	 */
	@Override
	public String[] getList() {
		
		PersistenceManager pm = getPersistenceManager();
		
		List<String> mWalkers= new ArrayList<String>();
		
		try{
		
			//Query all of the Walker objects on the server
			Query q= pm.newQuery( Walker.class );
			q.setOrdering( "trial ascending" );//Order by alphabetical 
			
			@SuppressWarnings("unchecked")
			List<Walker> walkCollections = (List<Walker>) q.execute();
		      
			//Move through the list of Walker objects on the server
			//and save their trial information to the output array list
			for ( Walker nWalker : walkCollections ) {
		        
				mWalkers.add( nWalker.getTrial() );
			}
		
		}finally{
			
			pm.close();
		}
		
		//Return the trial information
		return (String[]) mWalkers.toArray( new String[0] );
	}
	
	/**
	 * The getData method handles retrieving the data collected and saved on the server
	 * 
	 * Note: Currently there is bug in the App Engine API. Developers have to explicitly
	 * create a java.util.ArrayList to be passed from the server to the client. If this isn't done,
	 * a gwt ArrayList is created and sent to the client. This, more than likely, will wreak havoc on the
	 * client side. Just importing the Java.util.ArrayList package doesn't work for this bug.
	 * 
	 * @param The trial to retrieve the data from
	 * @return An arraylist of the data
	 */
	@Override
	public ArrayList<Float> getData( String trial ) {
		
		java.util.ArrayList<Float> data = new ArrayList<Float>();
	
	    PersistenceManager pm = getPersistenceManager();
	    
	    try {
	    	
	    	//Query all of the Walker objects on the server
	    	Query q = pm.newQuery( Walker.class );
	    	
	        @SuppressWarnings("unchecked")
			List<Walker> mWalkers = ( List<Walker> ) q.execute();
	        
			//Move through the list of Walker objects on the server
			//and save their data information to the output array list
	        for ( Walker nWalker : mWalkers ) {
	            	
	            if ( trial.equalsIgnoreCase( nWalker.getTrial() ) ) {

	            	//Save the data from the Walker object to the data array list
	            	data = new java.util.ArrayList<Float>( nWalker.getDataZ() );
	            }
	        }
	        
	    } finally {
	    	
	      pm.close();
	    }
	    
		return data;
	}
	
	public ArrayList<String> getData() throws Exception {
		
		java.util.ArrayList<String> data= new ArrayList<String>();
		
		PersistenceManager pm= getPersistenceManager();
		
		try {
			
			//Query all of the Walker objects on the server
			Query q = pm.newQuery( Walker.class );
			
			@SuppressWarnings("unchecked")
			List<Walker> mWalkers = ( List<Walker> ) q.execute();
			
			for ( Walker nWalker : mWalkers ) {
				
				JSONObject json = new JSONObject();
				json.put( "trial", nWalker.getTrial() );
				json.put( "email", nWalker.getEmail() );
				JSONArray dz= new JSONArray( nWalker.getTimeSeries() );
				JSONArray rawD= new JSONArray( nWalker.getDataZ() );
				json.put( "Raw", rawD );
				json.put( "DataZ", dz );
				json.put( "sampleEntropy", nWalker.getSampEntrpy() );
				json.put( "dfaAlpha", nWalker.getDfaAlpha() );
				json.put( "CV", nWalker.getCv() );
				
				data.add( json.toString() );
			}
			
		}finally {
			
			pm.close();
		}
		
		return data;
	}
	
	public ArrayList<String> getRawData() throws Exception {
		
		java.util.ArrayList<String> data= new ArrayList<String>();
		
		PersistenceManager pm= getPersistenceManager();
		
		try {
			
			//Query all of the Walker objects on the server
			Query q = pm.newQuery( Walker.class );
			
			@SuppressWarnings("unchecked")
			List<Walker> mWalkers = ( List<Walker> ) q.execute();
			
			for ( Walker nWalker : mWalkers ) {
				
				JSONObject json = new JSONObject();
				json.put( "trial", nWalker.getTrial() );
				json.put( "email", nWalker.getEmail() );
				JSONArray dz= new JSONArray( nWalker.getDataZ() );
				json.put( "DataZ", dz );
				json.put( "sampleEntropy", nWalker.getSampEntrpy() );
				json.put( "dfaAlpha", nWalker.getDfaAlpha() );
				json.put( "CV", nWalker.getCv() );
				
				data.add( json.toString() );
			}
			
		}finally {
			
			pm.close();
		}
		
		return data;
	}
	
	public int getNumberOfTrials() {
		
		int count;
		
		PersistenceManager pm = getPersistenceManager();
		
		try{
		
			//Query all of the Walker objects on the server
			Query q= pm.newQuery( Walker.class );
			
			@SuppressWarnings("unchecked")
			List<Walker> walkCollections = (List<Walker>) q.execute();
			
			//Find the number of walks on the server
			count= walkCollections.size();
		
		}finally{
			
			pm.close();
		}
		
		return count;
	}
	
	/**
	 * The getPersistenceManager handles setting up a manager for the server side
	 * data store.
	 * 
	 * @return The manager to retrieve data from the server
	 */
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}