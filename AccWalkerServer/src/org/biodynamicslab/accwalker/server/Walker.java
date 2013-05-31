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

	/**User info*/
	@Persistent
	private String user;

	/**The trial description*/
	@Persistent
	private String trial;

	/**The amount of time for the data collection*/
	@Persistent
	private int time;

	/**The data in the Z axis*/
	@Persistent
	private ArrayList<Float> DataZ;

	/**The differentiated data set*/
	@Persistent
	private ArrayList<Integer> diffData;

	/**The Sample Entropy value for the diffData*/
	@Persistent
	private double sampEntrpy;

	/**The DFA Alpha value for the diffData*/
	@Persistent
	private double dfaAlpha;

	/**The coefficient of variation for the diffData*/
	@Persistent
	private double cv;

	/**
	 * Walker constructor 
	 * 
	 * @param trial description
	 * @param time of data collection
	 * @param DataZ the data collected
	 */
	public Walker( String trial, String user, int time, ArrayList<Float> DataZ ){

		this.user= user;
		this.trial= trial;
		this.time= time;
		this.DataZ= DataZ;
	}

	/**
	 * Getter method for the unique ID associated with a given Walker object
	 * 
	 * @return The Walker object ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Getter method for the email address of the user who posted the data
	 * 
	 * @return The email of the user who posted the data
	 */
	public String getEmail() {
		return user;
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
	 * Getter method acceleration data associated with this Walker object
	 * 
	 * @return The data associated with this collection
	 */
	public ArrayList<Float> getDataZ() {
		return DataZ;
	}

	/**
	 * Getter method for the trial information associated with the Walker Object
	 * 
	 * @return The trial info
	 */
	public String getTrial() {
		return trial;
	}

	/**
	 * Setter method for the trial information 
	 * 
	 * @param trial The info to be saved with this Walker object
	 */
	public void setTrial( String trial ) {
		this.trial = trial;
	}

	/**
	 * Getter method for the differentiated time series data
	 * 
	 * @return The differentiated data set
	 */
	public ArrayList<Integer> getTimeSeries() {
		return diffData;
	}

	/**
	 * Setter method for the Time Series data
	 * 
	 * @param The differentiated data set
	 */
	public void setTimeSeries( ArrayList<Integer> data ) {
		this.diffData= data;
	}

	/**
	 * Getter method for the Sample Entropy value
	 * 
	 * @return The sample entropy value of the diff data
	 */
	public double getSampEntrpy() {
		return sampEntrpy;
	}

	/**
	 * Setter method for the Sample Entropy value
	 * 
	 * @param sampEntrpy The sample entropy value
	 */
	public void setSampEntrpy( double sampEntrpy ) {
		this.sampEntrpy = sampEntrpy;
	}

	/**
	 * Getter method for the DFA alpha value
	 * 
	 * @return The DFA alpha value
	 */
	public double getDfaAlpha() {
		return dfaAlpha;
	}

	/**
	 * Setter method for the DFA alpha value
	 * 
	 * @param dfaAlpha The DFA alpha value
	 */
	public void setDfaAlpha( double dfaAlpha ) {
		this.dfaAlpha = dfaAlpha;
	}

	/**
	 * Getter method for the coefficient of variation
	 * 
	 * @return The CV value
	 */
	public double getCv() {
		return cv;
	}

	/**
	 * Setter method for the coefficient of variation value
	 * 
	 * @param cv The CV value
	 */
	public void setCv( double cv ) {
		this.cv = cv;
	}
}