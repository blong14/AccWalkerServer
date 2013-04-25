package org.biodynamicslab.accwalker.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
	private Float[] DataZ;
	
	public Walker( String trial, int time, Float[] DataZ ){
		
		this.trial= trial;
		this.time= time;
		this.DataZ= DataZ;
	}

	public Long getId() {
		return id;
	}

	public String getTrial() {
		return trial;
	}

	public void setTrial(String trial) {
		this.trial = trial;
	}
	
	public int getTime(){
		return time;
	}
	
	public Float[] getDataZ(){
		return DataZ;
	}
}