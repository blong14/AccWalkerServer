package org.biodynamicslab.accwalker.server;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * The Walker class stores and holds all data associated with a data collection
 * from the AccWalker Mobile app.
 * 
 * This class provides a framework for data to be saved on a server. (JDO API)
 * 
 * @author Ben Long
 *
 */
@PersistenceCapable( identityType = IdentityType.APPLICATION )
public class Walker {
	
	/**The Walker id*/
	@PrimaryKey
	@Persistent( valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Long id;
	
	/**The trial description*/
	@Persistent
	private String trial;
	
	/**The amount of time for the data collection*/
	@Persistent
	private int time;
	
	/**The data in the Z axis*/
	@Persistent
	private ArrayList<Float> DataZ;
	
	/**
	 * Walker constructor 
	 * 
	 * @param trial description
	 * @param time of data collection
	 * @param DataZ the data collected
	 */
	public Walker( String trial, int time, ArrayList<Float> DataZ ){
		
		this.trial= trial;
		this.time= time;
		this.DataZ= DataZ;
	}

	/**
	 * The getId method returns the unique ID associated with a given Walker object
	 * 
	 * @return The Walker object ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * The getTrial method returns the trial information associated with the Walker Object
	 * 
	 * @return The trial info
	 */
	public String getTrial() {
		return trial;
	}
	
	/**
	 * The getTime method returns the amount of time a trial lasted
	 * 
	 * @return The amount of time a trial lasted
	 */
	public int getTime(){
		return time;
	}
	
	/**
	 * the getDataZ method returns the data associated with this Walker object
	 * 
	 * @return The data associated with this collection
	 */
	public ArrayList<Float> getDataZ(){
		return DataZ;
	}

	/**
	 * The setTrial method handles setting trial information 
	 * 
	 * @param trial The info to be saved with this Walker object
	 */
	public void setTrial(String trial) {
		this.trial = trial;
	}
}