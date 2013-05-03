package org.biodynamicslab.accwalker.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AccWalkerServer implements EntryPoint {
	
	/**The mainPanel to hold the UI elements*/
	private VerticalPanel mainPanel = new VerticalPanel();
	
	/**The flexTable to display the list items*/
	private FlexTable listFlexTable = new FlexTable();

	/**The list of trials*/
	private ArrayList<String> mTrials;
	
	/**The list item service to talk to the server*/
	private GreetingServiceAsync greetSvc = GWT.create( GreetingService.class );
	
	/**The panel to plot the data*/
	private VerticalPanel vPanel= new VerticalPanel();
	
	/**The LineChart to plot data*/
	private LineChart dataChart;
	  
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		
	    // Add styles to elements to the trial list
	    listFlexTable.addStyleName( "itemList" );
	    listFlexTable.setCellPadding( 6 );
	    listFlexTable.getCellFormatter().addStyleName( 0, 3, "listRemoveColumn" );
	
	    // Assemble Main panel.
	    mainPanel.add( listFlexTable );
	    mainPanel.addStyleName( "mainPanel" );
	   
	    // Associate the Main panel and Plot panel with the HTML host page.
	    RootPanel.get( "accwalkerServer" ).add( mainPanel );
	    RootPanel.get( "accwalkerPlotter" ).add( vPanel );
	    
	    //Get the list from the server and set up the ArrayList to hold the data
	    mTrials= new ArrayList<String>();
	    getList();
	}
	
  /**
   * Add items to FlexTable. Executed when the user clicks the addItemButton or
   * presses enter in the itemText.
   * 
   * @param trial The trial info to add to the table
   * @param trialIndex The trial index value
   */
	private void addToTable( final String trial, final int trialIndex ) {
	
		  // Add the trial to the table.
		  int row = listFlexTable.getRowCount();
		  listFlexTable.setText( row, 0, trial );
		  listFlexTable.getCellFormatter().addStyleName( row, 3, "listRemoveColumn" );
		  
		  //Create buttons dynamically
		  //The plot button
		  Button btnPlot= new Button( "Plot Data" );
		  btnPlot.addClickHandler(new ClickHandler() {
			  
			  public void onClick( ClickEvent event ) {
				  
				 //The btnPlot was fired retrieve the data from the server
				 getData( trial );
			  }
		  });
		  
		  //The remove button
		  Button btnRemoveTrial = new Button( "x" );
		  btnRemoveTrial.addClickHandler(new ClickHandler( ) {
			 
			  public void onClick( ClickEvent event ) {
				  
				  //The btnRemoveTrial was fired, remove from database and flex table 
				  int removedIndex = mTrials.indexOf( trial );
				  removeFromList( trial );
				  mTrials.remove( removedIndex );
			      listFlexTable.removeRow( trialIndex + 1 );
			  }
		  });
		  
		  //Add buttons to the flex table
		  listFlexTable.setWidget(row, 3, btnPlot );
		  listFlexTable.setWidget(row, 4, btnRemoveTrial );
	  }
	
	 /**
	  * Populate the data table with data
	  * 
	  * @param data The data to be added to the dataTable
	  * @return A DataTable with the data to be plotted
	  */
	  private AbstractDataTable createTable( ArrayList<Float> data ) {
		    
		  // Underlying data
		  DataTable mData = DataTable.create();
		
		  mData.addColumn( ColumnType.NUMBER, "Index" );
		  mData.addColumn( ColumnType.NUMBER, "DataZ" );
		  mData.addRows( data.size() );
		  
		  //Now move through the data added each data point to the DataTable
		  for( int row= 0; row < data.size(); row++ ){
			  
			  mData.setValue( row, 0, row );
			  mData.setValue( row, 1, data.get( row ) );
		  }
		
		return mData;
	  }

	  /**
	   * The getData method handles retrieving the data associated with a trial
	   * 
	   * @param trial The trial to get data from
	   */
	  private void getData( String trial ) {
		  
		  // Initialize the service proxy.
		  if ( greetSvc == null )
			  greetSvc = GWT.create( GreetingService.class );

		  // Set up the callback objects
		  AsyncCallback<ArrayList<Float>> callback = new AsyncCallback<ArrayList<Float>>() {
	    
			  //Handle server communication error
			  public void onFailure(Throwable caught) {
				  
				  Window.alert( "Server Error: " + caught.toString() );
			  }

			  //Success! 
			  public void onSuccess( final ArrayList<Float> result ) {
				   				  
			      Runnable onLoadCallback = new Runnable() {
			    	
			          public void run() {
			        	  
			        	  //Create the DataTable
			              AbstractDataTable data = createTable( result );
			              
			              //Create and set options for the plot
			              Options options = Options.create();
			              options.setGridlineColor( "white" );
			              options.remove( "legend" );
			              options.setWidth(800); 
			              options.setHeight(480); 
			              
			              //Now, plot the data
						  if( dataChart == null )
							  dataChart = new LineChart( data, options );
						  else
							  dataChart.draw( data, options );
			             
						  //Add plot to the plotting panel
			              vPanel.add( dataChart );
			              vPanel.addStyleName( "mainPanel" );
			          }
			      };

			      VisualizationUtils.loadVisualizationApi( onLoadCallback, LineChart.PACKAGE );
			  }
		  };
		  
		  // Make the call to the server
		  greetSvc.getData( trial, callback );
	  }
	  
	/**
	 * The getList method retrieves the list off the server
	 */
	private void getList() {
		
		// Initialize the service proxy.
		if ( greetSvc == null )
			greetSvc = GWT.create( GreetingService.class );
		
		// Set up the callback objects
		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {

			//Handle server communication error
			public void onFailure(Throwable caught) {
	
				Window.alert( "Server Error: " + caught.toString() );
			}

			//Success!
			public void onSuccess( String[] result ) {
		
				//An array of trial names has been returned from the server
				for( int i= 0; i < result.length; i++ ){
		
					//Now move through this array adding the trial names to the flex table
					mTrials.add( result[i] ); 
					int itemIndex= mTrials.indexOf( result[i] );
				
					//Called to actually add the trials to the table
					addToTable( result[i], itemIndex );
				}
			}
		};
		 
		// Make the call to the server
		greetSvc.getList( callback );
	}
	
	/**
	 * The removeItem method handles removing trials from the server 
	 * 
	 * @param trial The trial to remove from the list
	 */
	@SuppressWarnings("rawtypes")
	private void removeFromList( String trial ){

		// Initialize the service proxy.
		if ( greetSvc == null )
			greetSvc = GWT.create( GreetingService.class );

		// Set up the callback objects
		AsyncCallback callback = new AsyncCallback() {
	 
			public void onFailure(Throwable caught) {
		
				Window.alert( "Server Error: " + caught.toString() );
			}

			public void onSuccess( Object result ) {
		
				//Alert user that the trail has been deleted
				Window.alert( "Trial Deleted" );
			}
		};

		// Make the call to the server.
		greetSvc.removeFromList( trial, callback );
	}
}