package org.biodynamicslab.accwalker.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	
	void removeFromList( String input, @SuppressWarnings("rawtypes") AsyncCallback success );
	
	void getList( AsyncCallback<String[]> callback )
			throws IllegalArgumentException;
}
