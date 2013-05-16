package org.biodynamicslab.accwalker.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
import com.google.gwt.user.client.ui.MenuItemSeparator;

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
	
	/**Menu separator*/
	private final MenuItemSeparator separator1 = new MenuItemSeparator();
	
	/**Menu separator*/
	private final MenuItemSeparator separator2 = new MenuItemSeparator();
	
	/**The list item service to talk to the server*/
	private GreetingServiceAsync greetSvc = GWT.create( GreetingService.class );
	
	/**The data associated with a trial*/
	private ArrayList<Float> trialData= new ArrayList<Float>();
	
	/**Variable used to determine if a trial was deleted from the server or not*/
	private boolean deleted;
	
	private VerticalPanel dockPanel;
	  
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		
	    //Set up view layout
	    dockPanel = new VerticalPanel();
	    RootPanel.get( "rootPanel" ).add( dockPanel );
	    
	    dockPanel.add( setUpMenuBar() );

		getTrialNames();
	}
	 
	private HorizontalPanel setUpMenuBar() {
	
		//Create about menu item
	    mntmAbout = new MenuItem( "About", false, new Command(){
	    	@Override
	    	public void execute(){
	    		Window.alert( "Coming Soon!" );
	    	}
	    });
	    menuBar.addItem( mntmAbout );
	    menuBar.addSeparator( separator1 );
	  
	    menuBar.addItem( "Plot", Plot );
	    menuBar.addSeparator( separator2 );
	    
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
	
	private Options getOptions(){
		
		Options options = Options.create();
		options.setGridlineColor( "white" );
		options.setColors( "red" );
		options.setLegend( "none" );
		options.setWidth(800);
		options.setHeight(480);
		
		return options;
	}
	
	/**
	 * The addPlotMenuItem method handles adding trials to the plotting menu options
	 * 
	 * @param trial The trial to plot and to be added to the plot menu
	 * @return The MenuItem to add to the plot menu
	 */
	private MenuItem addPlotMenuItem( final String trial ){
		
		MenuItem mItem= new MenuItem( trial, false, new Command(){
			
			@Override
			public void execute(){
				
				final ArrayList<Float> dataToPlot= getData( trial );
				
				Runnable onLoadCallback= new Runnable() {
					
					private LineChart dataChart;
					
					public void run() {
						
						//Create the DataTable
						AbstractDataTable data = createTable( dataToPlot );
			              
						//Now, plot the data
						dataChart = new LineChart( data, getOptions() );
						
						if( plotPanel.isVisible() )
							plotPanel.clear();
						
						RootPanel.get("plotPanel").add( plotPanel );
						plotPanel.add( dataChart );
					}
				};
				
				VisualizationUtils.loadVisualizationApi( onLoadCallback, LineChart.PACKAGE );
			}
		});
		
		return mItem;
	}
	
	/**
	 * The getTrialNames method retrieves the list of trial names off the server
	 */
	private void getTrialNames(){
		
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

					//Now move through this array adding the trial names to the list
					Plot.addItem( addPlotMenuItem( result[i] ) );
				}
			}
		};

		// Make the call to the server
		greetSvc.getList( callback );
	}
	
  /**
   * The getData method handles retrieving the data associated with a trial
   * 
   * @param trial The trial to get data from
   */
	private ArrayList<Float> getData( String trial ) {
	  
		// Initialize the service proxy.
		if ( greetSvc == null )
			greetSvc = GWT.create( GreetingService.class );

		final ArrayList<Float> data= new ArrayList<Float>();
		
		// Set up the callback objects
		AsyncCallback<ArrayList<Float>> callback = new AsyncCallback<ArrayList<Float>>() {
    
		  //Handle server communication error
		  public void onFailure(Throwable caught) {
			  //TODO: Handle Server Error in getData method
		  }

		  //Success! 
		  public void onSuccess( final ArrayList<Float> result ) {
			   				  
			  if( result == null ){
				  Window.alert( "No data to plot" );
				  return;
			  }
			  			  
		      for(int i= 0; i < result.size(); i++){
		    	  
		    	  data.add( result.get(i) );
		      }
		  }
	  };
	  
	  // Make the call to the server
	  greetSvc.getData( trial, callback );
	  
	  return data;
	
	}
	
	/**
	 * The removeItem method handles removing trials from the server 
	 * 
	 * @param trial The trial to remove from the list
	 */
	@SuppressWarnings("rawtypes")
	public boolean removeFromServer( String trial ){

		// Initialize the service proxy.
		if ( greetSvc == null )
			greetSvc = GWT.create( GreetingService.class );

		// Set up the callback objects
		AsyncCallback callback = new AsyncCallback() {
	 
			public void onFailure(Throwable caught) {
		
				//Server error, data was not deleted
				deleted= false;
			}

			public void onSuccess( Object result ) {
		
				//Alert user that the trail has been deleted
				deleted= true;
			}
		};

		// Make the call to the server.
		greetSvc.removeFromList( trial, callback );
		
		return deleted;
	}
}