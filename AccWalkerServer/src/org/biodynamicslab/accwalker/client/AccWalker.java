package org.biodynamicslab.accwalker.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AccWalkerServer implements EntryPoint {
	
	/**The mainPanel to hold the UI elements*/
	private HorizontalPanel menuPanel = new HorizontalPanel();
	
	/**The panel to plot the data*/
	private VerticalPanel plotPanel= new VerticalPanel();
	
	/**The main menu bar item*/
	private final MenuBar menuBar = new MenuBar( false );
	
	/**The plot menu bar to plot the data for a trial*/
	private final MenuBar Plot = new MenuBar( true );
	
	/**About menu item*/
	private MenuItem mntmAbout;
	
	/**Analyze menu item*/
	private MenuItem mntmAnalyze;
	
	/**The list item service to talk to the server*/
	private GreetingServiceAsync greetSvc = GWT.create( GreetingService.class );
	
	/**The main panel to had UI components*/
	private VerticalPanel mainPanel;
	
	/**Popup panel to tell user that data is being loaded*/
	private PopupPanel loadPanel;
	  
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
	
	    //Set up view layout
	    mainPanel = new VerticalPanel();

	    //Make a call to the server to retrieve the data
	    getData();
	    
	    //Display the loadPanel while data is being retreived from the server
	    loadPanel= new PopupPanel();
	    loadPanel.setWidget(new Label( "Please be patient while trial data is loaded from server" ) );
	    loadPanel.setGlassEnabled(true);
	    mainPanel.add( loadPanel );
	    
		RootPanel.get( "rootPanel" ).add( mainPanel );
	}
	 
	/**
	 * The setUpMenuBar method handles create menu items
	 * 
	 * @return a HorizontalPanel with menu items associated with it
	 */
	private HorizontalPanel setUpMenuBar() {
	
		//Create about menu item
	    mntmAbout = new MenuItem( "About", false, new Command(){
	    	@Override
	    	public void execute(){
	    		Window.alert( "Coming Soon!" );
	    	}
	    });
	    menuBar.setStyleName( "gwt-MenuBar" );
	    menuBar.addItem( mntmAbout );
	 
	    //Add the plot item
	    menuBar.addItem( "Plot", Plot );
	    
	    //Add analyze menu item
	    mntmAnalyze = new MenuItem( "Analyze Data", false, new Command(){
	    	@Override
	    	public void execute(){
	    		
	    		Window.alert( "Coming Soon!" );
	    	}
	    });
	    menuBar.addItem( mntmAnalyze );
	    
	    //Finally, add the menuPar to the menu panel
	    menuPanel.add( menuBar );
	    
	    return menuPanel;
	}
	/**
	 * Populate the data table with data
	 * 
	 * @param data The data to be added to the dataTable
	 * @return A DataTable with the data to be plotted
	 * 
	 */
	private AbstractDataTable createTable( ArrayList<Float> data ) {
		
		// Underlying dataDataTable 
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
	 * The addPlotMenuItem method handles adding trials to the plotting menu options
	 * 
	 * @param trial The trial to plot and to be added to the plot menu
	 * @return The MenuItem to add to the plot menu
	 */
	private MenuItem addPlotMenuItem( final String trial, final ArrayList<Float> dataToPlot ){

		MenuItem mItem= new MenuItem( trial, false, new Command(){

			@Override
			public void execute(){

				Runnable onLoadCallback= new Runnable() {

					//The data chart to plot data
					private LineChart dataChart;

					public void run() {

						//Create the DataTable
						AbstractDataTable data = createTable( dataToPlot );

						Options options = Options.create();
						options.setGridlineColor( "white" );
						options.setColors( "red" );
						options.setLegend( "none" );
						options.setWidth(800);
						options.setHeight(480);

						//Now, plot the data
						dataChart = new LineChart( data, options );

						//Check to see if a plot is currently visible
						//If it is, remove it
						if( plotPanel.isVisible() )
							plotPanel.clear(); RootPanel.get( "plotPanel" ).clear();

						//Now add the new chart to the plot panel
						plotPanel.add( dataChart );
						RootPanel.get( "plotPanel" ).add( plotPanel );
					}
				};

				VisualizationUtils.loadVisualizationApi( onLoadCallback, LineChart.PACKAGE );
			}
		});

		return mItem;
	}
	
 /**
   * The getData method handles retrieving the data from the server
   * 
   */
	private void getData() {

		// Initialize the service proxy.
		if ( greetSvc == null )
			greetSvc = GWT.create( GreetingService.class );

		// Set up the callback object
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
    
		  //Handle server communication error
		  public void onFailure(Throwable caught) {
			  
			  Window.alert( "Server communication error: " + caught.getMessage() );
		  }

		  //Success! 
		  public void onSuccess( final ArrayList<String> result ) {

			  //Move through the result array and parse JSON data
			  for( int i= 0; i < result.size(); i++ ) {
				  
				  //Parse JSON data now
				  JSONValue value= JSONParser.parseStrict( result.get(i) );
				  JSONObject jObject = value.isObject();
				  String trial= jObject.get("trial").isString().stringValue();
				  JSONArray data= jObject.get("DataZ").isArray();
				  
				  double number;
				  ArrayList<Float> mData= new ArrayList<Float>();
				  for( int j= 0; j < data.size(); j++ ) {
					  
					  number= data.get(j).isNumber().doubleValue();
					  mData.add( (float)number );
				  }

				  //Add the trial and data to plot to the plot menu item
				  Plot.addItem( addPlotMenuItem( trial, mData ) );
			  }
			  
			 //Data parsing is complete so remove the loadPanel and show the menu for the user
			 mainPanel.remove( loadPanel );
			 mainPanel.add( setUpMenuBar() );
		  }
	  };

	  // Make the call to the server
	  greetSvc.getData( callback );
	
	}
}