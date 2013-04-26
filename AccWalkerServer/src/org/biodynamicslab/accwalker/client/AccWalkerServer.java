package org.biodynamicslab.accwalker.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		
		//First, Create table for the list data.
		listFlexTable.setText( 0, 0, "Trial Info:" );
	    
	    // Add styles to elements in the stock list table.
	    listFlexTable.addStyleName( "itemList" );
	    listFlexTable.setCellPadding( 6 );
	    listFlexTable.getCellFormatter().addStyleName( 0, 3, "listRemoveColumn" );
	
	    // Assemble Main panel.
	    mainPanel.add( listFlexTable );
	    mainPanel.addStyleName( "mainPanel" );
	    
	    // Associate the Main panel with the HTML host page.
	    RootPanel.get( "accwalkerServer" ).add( mainPanel );
	    
	    //Get the list from the server and set up the ArrayList to hold the data
	    mTrials= new ArrayList<String>();
	    getList();
	
	}
	
  /**
   * Add items to FlexTable. Executed when the user clicks the addItemButton or
   * presses enter in the itemText.
   */
	private void addToTable( final String trial, final int trialIndex ) {
	
		  // Add the item to the table.
		  int row = listFlexTable.getRowCount();
		  listFlexTable.setText( row, 0, trial );
		  listFlexTable.getCellFormatter().addStyleName( row, 3, "listRemoveColumn" );
		  
		  //Create buttons dynamically
		  Button btnPlot= new Button( "Plot Data" );
		  btnPlot.addClickHandler(new ClickHandler() {
			  
			  public void onClick( ClickEvent event ) {
				  new PlotDialog().show();
			  }
		  });
		  
		  Button btnRemoveTrial = new Button( "x" );
		  btnRemoveTrial.addClickHandler(new ClickHandler( ) {
			 
			  public void onClick( ClickEvent event ) {
				  
				  int removedIndex = mTrials.indexOf(trial);
				  removeFromList( trial );
				  mTrials.remove( removedIndex );
			      listFlexTable.removeRow( trialIndex + 1 );
			  }
		  });
		  
		  listFlexTable.setWidget(row, 3, btnPlot );
		  listFlexTable.setWidget(row, 4, btnRemoveTrial );
	  }

	/**
	 * The getList method retrieves the list off the server
	 */
	private void getList() {
		
		  // Initialize the service proxy.
		  if ( greetSvc == null ) {
			  greetSvc = GWT.create( GreetingService.class );
		  }

		  // Set up the callback objects
		  AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
	    
			  public void onFailure(Throwable caught) {
				  System.err.println( caught );
			  }

			  public void onSuccess( String[] result ) {
				  
				  for( int i= 0; i < result.length; i++ ){
					  
					  mTrials.add( result[i] );
					  int itemIndex= mTrials.indexOf( result[i] );
					  
					  addToTable( result[i], itemIndex );
				  }
			  }
		  };
		  
		  // Make the call to the stock price service.
		  greetSvc.getList( callback );
	}
	
	/**
	 * The removeItem method handles removing list items from the server 
	 */
	@SuppressWarnings("rawtypes")
	private void removeFromList( String item ){
		
		  // Initialize the service proxy.
		  if ( greetSvc == null ) {
			  greetSvc = GWT.create( GreetingService.class );
		  }

		  // Set up the callback objects
		  AsyncCallback callback = new AsyncCallback() {
	    
			  public void onFailure(Throwable caught) {
	        
				  // TODO: Do something with errors
			  }

			  public void onSuccess( Object result ) {
				  
				  //TODO: Do something with success
			  }
		  };
		  
		  // Make the call to the listItem service.
		  greetSvc.removeFromList( item, callback );
	}
	
	 
	private static class PlotDialog extends DialogBox {

		public PlotDialog() {
		      
			// Set the dialog box's caption.
			setText("Plot Data");

			// Enable animation.
			setAnimationEnabled(true);
			
			// Enable glass background.
			setGlassEnabled(true);
			// DialogBox is a SimplePanel, so you have to set its widget property to
			// whatever you want its contents to be.
			Button ok = new Button("OK");
		      
			ok.addClickHandler(new ClickHandler() {
		        
				public void onClick(ClickEvent event) {

					PlotDialog.this.hide();
				}
			});
		      
			setWidget(ok);
		}	  
	}
}
