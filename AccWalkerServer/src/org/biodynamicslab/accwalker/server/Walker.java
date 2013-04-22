package org.biodynamicslab.accwalker.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable( identityType = IdentityType.APPLICATION )
public class Walker {
	
	/**The item id*/
	@PrimaryKey
	@Persistent( valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Long id;
	
	/**The trial description*/
	@Persistent
	private String trial;
	
	public Walker( String trial ){
		
		this.setTrial(trial);
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

}
