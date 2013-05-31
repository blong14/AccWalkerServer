package org.biodynamicslab.accwalker.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	void removeFromList( String input, @SuppressWarnings("rawtypes") AsyncCallback success );

	void getList( AsyncCallback<String[]> callback )
			throws IllegalArgumentException;

	void getData( String trial, AsyncCallback <ArrayList<Float>> callback )
			throws IllegalArgumentException;

	void getData( AsyncCallback <ArrayList<String>> callback )
			throws IllegalArgumentException;
	
	void getRawData( AsyncCallback <ArrayList<String>> callback )
			throws IllegalArgumentException;
}
