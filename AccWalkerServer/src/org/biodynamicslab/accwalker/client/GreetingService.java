package org.biodynamicslab.accwalker.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	void removeFromList( String trial );

	String[] getList();

	ArrayList<Float> getData( String trial );

	ArrayList<String> getData() throws Exception;
	
	ArrayList<String> getRawData() throws Exception;
}
