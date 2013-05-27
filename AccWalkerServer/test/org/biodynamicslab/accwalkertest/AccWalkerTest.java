package org.biodynamicslab.accwalkertest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccWalkerTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper( new LocalDatastoreServiceTestConfig() );

    @Before
    public void setUp() {
    	
        helper.setUp();
    }

    @After
    public void tearDown() {
    	
        helper.tearDown();
    }

    // run this test twice to prove we're not leaking any state across tests
    private void doTest() {
    	
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        assertEquals( 0, ds.prepare( new Query( "yam" ) ).countEntities( withLimit( 10 ) ) );
        ds.put( new Entity( "yam" ) );
        ds.put( new Entity( "yam" ) );
        assertEquals( 2, ds.prepare( new Query( "yam" ) ).countEntities( withLimit( 10 ) ) );
    }
    
    private void insertWalker() throws JSONException {
    	
    	String trial= "Sub001";
    	int time= 5;
    	ArrayList<Float> data= new ArrayList<Float>();
    	data.add( new Float(0.0) );
    	data.add( new Float(0.1) );
    	data.add( new Float(2.2) );
    	data.add( new Float(0.3) );
    	data.add( new Float(1.3) );
    	data.add( new Float(2.0) );
    	data.add( new Float(0.3) );
    	
    	HttpPost post = new HttpPost( "http://localhost:8888/accwalkerserver/uploadData" );
    	
    	JSONObject json = new JSONObject();
    	json.put( "user", "14benj@gmail.com" );
		json.put( "trial", trial );
		json.put( "time", time );
		JSONArray dz= new JSONArray( data );
		json.put( "DataZ", dz );
		
		HttpClient client = new DefaultHttpClient();
		
		try {
			
			StringEntity se = new StringEntity( json.toString() );
			se.setContentType(new BasicHeader( HTTP.CONTENT_TYPE, "application/json" ) );
			post.setEntity( se );
			HttpResponse response = client.execute( post );
			
			System.out.println(response.getStatusLine());
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }

    @Test
    public void testInsert1() {
    	
        doTest();
    }

    @Test
    public void testInsert2() {
    	
    	try {
    		
			insertWalker();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
        doTest();
    }
}
