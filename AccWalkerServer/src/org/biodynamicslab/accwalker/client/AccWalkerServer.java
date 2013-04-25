package org.biodynamicslab.accwalker.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AccWalkerServer implements EntryPoint, ClickHandler, KeyPressHandler {
	
	/**The mainPanel to hold the UI elements*/
	private VerticalPanel mainPanel = new VerticalPanel();
	
	/**The flexTable to display the list items*/
	private FlexTable listFlexTable = new FlexTable();
	
	/**The processPanel to add items to the list*/
	private HorizontalPanel analyzePanel = new HorizontalPanel();
	
	/**The trial text to display to the user*/
	private TextBox trialText = new TextBox();
	
	/**The add item button to add items to the list*/
	private Button btnAnalyzeWalker = new Button( "Analyze" );
	
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
	
	    // Assemble Add Item panel.
	    analyzePanel.add( trialText );
	    analyzePanel.add( btnAnalyzeWalker );
	    analyzePanel.addStyleName( "addPanel" );

	    // Assemble Main panel.
	    mainPanel.add( listFlexTable );
	    mainPanel.add( analyzePanel );
	    mainPanel.addStyleName( "mainPanel" );
	    
	    // Associate the Main panel with the HTML host page.
	    RootPanel.get( "accwalkerServer" ).add( mainPanel );
	    
	    // Move cursor focus to the input box.
	    trialText.setFocus( true );
	    
	    //Get the list from the server and set up the ArrayList to hold the data
	    mTrials= new ArrayList<String>();
	    getList();
	    
	    btnAnalyzeWalker.addClickHandler( this );
	    
	    trialText.addKeyPressHandler( this );
	}
	
	/**
	 * The onClick method handles user interaction from the mouse
	 * 
	 * @param the event that was fired
	 */
	@Override
	public void onClick( ClickEvent event ) {

	}
	
	/**
	 * The onKeyPress method handles events from the key board
	 * 
	 * @param the key that was pressed by the user
	 */
	@Override
	public void onKeyPress( KeyPressEvent event ){
		
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
		  
		  trialText.setFocus( true );
		  trialText.setText( "" );

		  //Create remove buttons dynamically
		  Button btnRemoveTrial = new Button( "x" );
		  btnRemoveTrial.addClickHandler(new ClickHandler( ) {
			 
			  public void onClick( ClickEvent event ) {
				  
				  int removedIndex = mTrials.indexOf(trial);
				  removeFromList( trial );
				  mTrials.remove( removedIndex );
			      listFlexTable.removeRow( trialIndex + 1 );
			  }
		  });
		  
		  listFlexTable.setWidget(row, 3, btnRemoveTrial );
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
}
