/*
	GenericTableController.java
    Copyright (C) 2019  Sriram C.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/ 

/* Some portions of this code have been borrowed & modified from below sources
 * Copyright of these sections are as per the original author 	
 * // https://gist.github.com/Roland09/6fb31781a64d9cb62179
 * 
 * 
 * 
 */

package com.gems.table;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.StringTokenizer;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;



public class GenericTableController<S> implements TableEventListener{

	public TableView<S> table;
	protected ScrollPane tableScrollPane;
	protected boolean tableSelected = false; // variable to track if this table is under focus

	
	// create context Menu
	protected ContextMenu contextMenu;
	
	protected MenuItem copyMenuItem;
	protected MenuItem pasteMenuItem;
	protected MenuItem cutMenuItem;
	protected MenuItem deleteMenuItem;
	protected MenuItem selectAllMenuItem;
	
	protected MenuItem insertRowAboveMenuItem;
	protected MenuItem deleteRowMenuItem;
	
	public void init() {
		
		// enable multi-selection
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		installKeyboardHandler();
		initContextMenuItems();
        initContextMenu();		

		
		updateColumnHeaders();
		initFocusChange();
		
    	TableEventObject.getInstance().addTableEventListener(this);
	}
	
	// We receive this event when the table focus changes
	// so we can clear the selections in the table.  
	public void clearTableSelection(String id) {
		// return if we receive our own event or if we werent the last selected table
		if (table.getId() == id || tableSelected==false) return;
		System.out.println("old selection " + table.getId());
		tableSelected = false;
		table.getSelectionModel().clearSelection();
	}
	
	public void initFocusChange() {
		// Everytime a change occurs / new row is selected we receive this event		
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	if ( tableSelected == false) {
		    	// focus has entered this table
		    		System.out.println("current table focus  "+ table.getId());
		    		// Send an event to let other tables know that this table has gained focus
		    		// this way the other tables can clear their selections
		    		TableEventObject.getInstance().tableFocusChanged(table.getId());
		    		tableSelected = true;
		    	}
		    }
		});

	}
	
	public String getValue(int row,int col) {
		return new String("parent not valid");
	}
	
	public void setValue(int row,int col,String val) {

	}
	
	protected boolean isCellContentValid(int row,int col,String clipboardCellContent) {
		return true;
	}
	
	public void clearValue(int row,int col) {}
	
	protected void updateColumnHeaders() {}
	
	public ContextMenu getContextMenu() { 
		return contextMenu;
	}
			
	
	public void tableEventReceived(TableEvent event) {
		
		
		if (event.tableEventType() == TableEventType.COPY) {
			copySelectionToClipboard();
			System.out.println("generic table controller  - received copy");
		}
		
		if (event.tableEventType() == TableEventType.PASTE) {
			pasteFromClipboard();
			System.out.println("generic table controller  - received paste");
		}

		if (event.tableEventType() == TableEventType.CUT) {
    		copySelectionToClipboard();
    		deleteSelection();
    		System.out.println("generic table controller  - received cut");
		}
		
		if (event.tableEventType() == TableEventType.DELETE) {
    		deleteSelection();
			System.out.println("generic table controller  - received delete");
		}
		
		
		if (event.tableEventType() == TableEventType.TABLE_FOCUS_CHANGED) {
			clearTableSelection((String)event.tableEventObject());
		}
		
		if (event.tableEventType() == TableEventType.INSERT_ROW_ABOVE) {
			insertRowAbove();
		}
		
		if (event.tableEventType() == TableEventType.DELETE_ROW) {
			deleteRow();
		}
	
	}
	

	// Handle Right Click on the Table
	// Create a context menu with options
	
	protected void initContextMenuItems() {
        
		
		copyMenuItem = new MenuItem("Copy");
        copyMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
            	System.out.println("Copy context menu");
        		copySelectionToClipboard();
				event.consume();
            } 	
        });
        
        pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Paste context menu");
            	pasteFromClipboard();
				event.consume();
            }
        });
        
        cutMenuItem = new MenuItem("Cut");
        cutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
            	System.out.println("Cut context menu");	
        		copySelectionToClipboard();
        		deleteSelection();
				event.consume();
            } 	
        });
        
        deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Delete context menu");
            	deleteSelection();
				event.consume();
            }
        });

        selectAllMenuItem = new MenuItem("Select All");
        selectAllMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Select All context menu");
        		table.getSelectionModel().selectAll();
				event.consume();
            }
        });
        
        insertRowAboveMenuItem = new MenuItem("Insert row above");
        insertRowAboveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Select All context menu");
        		insertRowAbove();
				event.consume();
            }
        });
        
        deleteRowMenuItem = new MenuItem("Delete row");
        deleteRowMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Select All context menu");
        		deleteRow();
				event.consume();
            }
        });
        
        
        if (contextMenu == null) {
        	contextMenu = new ContextMenu();
        }
 
        initContextMenu();
 
        // When user right-click on Circle
        table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
 
            @Override
            public void handle(ContextMenuEvent event) {
 
                contextMenu.show(table, event.getScreenX(), event.getScreenY());
            }
        });
 
	}

	protected void addContextMenuItems(MenuItem [] mI) {
		int i = 0;
		while ( i < mI.length ) {
			contextMenu.getItems().add(mI[i]);
			i++;
		}
	}
	
	protected void initContextMenu() { 

		contextMenu.getItems().clear();
	    contextMenu.getItems().addAll(copyMenuItem,
	        		pasteMenuItem,
	        		cutMenuItem,
	        		deleteMenuItem,
	        		selectAllMenuItem,
	        		insertRowAboveMenuItem,
	        		deleteRowMenuItem);
	}

	protected void setTableEditable() {
		table.setEditable(true);
		// allows the individual cells to be selected
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);

	}
	

	
	@SuppressWarnings("unchecked")
	protected void editFocusedCell() {
		final TablePosition<S, ?> focusedCell = table
				.focusModelProperty().get().focusedCellProperty().get();
		table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}
	
	@SuppressWarnings("unchecked")
	protected void selectPrevious() {
		if (table.getSelectionModel().isCellSelectionEnabled()) {
			// in cell selection mode, we have to wrap around, going from
			// right-to-left, and then wrapping to the end of the previous line
			TablePosition<S, ?> pos = table.getFocusModel()
					.getFocusedCell();
			
/*			final TablePosition<S, ?> pos = table
					.focusModelProperty().get().focusedCellProperty().get();*/
			
			System.out.println("\nrow:" + pos.getRow() + " col:" + pos.getColumn());
			if (pos.getColumn() - 1 >= 0) {
				// go to previous row
				table.getSelectionModel().select(pos.getRow(),
						getTableColumn(pos.getTableColumn(), -1));
			} else if (pos.getRow() < table.getItems().size()) {
				// wrap to end of previous row
				table.getSelectionModel().select(pos.getRow() - 1,
						table.getVisibleLeafColumn(
								table.getVisibleLeafColumns().size() - 1));
			}
		} else {
			int focusIndex = table.getFocusModel().getFocusedIndex();
			if (focusIndex == -1) {
				table.getSelectionModel().select(table.getItems().size() - 1);
			} else if (focusIndex > 0) {
				table.getSelectionModel().select(focusIndex - 1);
			}
		}
	}
	
	private TableColumn<S, ?> getTableColumn(
			final TableColumn<S, ?> column, int offset) {
		int columnIndex = table.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return table.getVisibleLeafColumn(newColumnIndex);
	}
	
	  
	  
	public void refresh() {
		table.getColumns().get(0).setVisible(false);
		table.getColumns().get(0).setVisible(true);
	}
	
	// https://gist.github.com/Roland09/6fb31781a64d9cb62179
	
	public  void installKeyboardHandler() {
		// install copy/paste keyboard handler
		table.setOnKeyPressed(new TableKeyEventHandler());

	}
	
	
	
	/**
	 * Copy/Paste keyboard event handler. The handler uses the keyEvent's source for
	 * the clipboard data. The source must be of type TableView.
	 */
	public  class TableKeyEventHandler implements EventHandler<KeyEvent> {

		KeyCodeCombination cutKeyCodeCombination = new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN);
		KeyCodeCombination copyKeyCodeCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
		KeyCodeCombination pasteKeyCodeCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN);
		KeyCodeCombination selectAllKeyCodeCombination = new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN);

		public void handle(final KeyEvent keyEvent) {
			if (keyEvent.getSource() instanceof TableView) {
				
				if (cutKeyCodeCombination.match(keyEvent)) {

					// cut is simulated as to clipboard and clear cells
					copySelectionToClipboard();
					deleteSelection();

					// event is handled, consume it
					keyEvent.consume();

				} 
				else if (copyKeyCodeCombination.match(keyEvent)) {

					// copy to clipboard
					copySelectionToClipboard();
					keyEvent.consume();

				} else if (pasteKeyCodeCombination.match(keyEvent)) {

					pasteFromClipboard();
					keyEvent.consume();
					
				} else if (selectAllKeyCodeCombination.match(keyEvent)) {

	        		table.getSelectionModel().selectAll();
					keyEvent.consume();

				} else if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE ) {
					// Delete contents of cell
					deleteSelection();
					keyEvent.consume();
				}
				
				else if (keyEvent.getCode().isLetterKey() || keyEvent.getCode().isDigitKey()) {
					// when character or numbers pressed it will start edit in editable
					// fields
					editFocusedCell();
					
				} 
				
			  
			}

		}

	}
	
	public void deleteSelection() {
		List<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

		System.out.println("clear");
		if (positionList == null) 
			return;
		
		int len = positionList.size();
		int rowArray[] = new int[len];
		int colArray[] = new int[len];
		
		
		for (int i = 0;i<len;i++) {
			TablePosition p = positionList.get(i);
			rowArray[i] = p.getRow();
			colArray[i] = p.getColumn();
		}
		
		for (int i = 0;i<len;i++) {
			// we cannot clear the first row
			if (colArray[i] > 0)
				clearValue(rowArray[i],colArray[i]);
		}
	
	}

	/**
	 * Get table selection and copy it to the clipboard.
	 * @param table
	 */
	public void copySelectionToClipboard() {

		StringBuilder clipboardString = new StringBuilder();

		if( table.getSelectionModel().getSelectedCells().size() == 0) {
			return;
		}
		
		ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

		System.out.println("Copy");
		
		int prevRow = -1;

		for (TablePosition position : positionList) {

			int row = position.getRow();
			int col = position.getColumn();

			// determine whether we advance in a row (tab) or a column
			// (newline).
			if (prevRow == row) {				
				clipboardString.append('\t');				
			} else if (prevRow != -1) {				
				clipboardString.append('\n');				
			}
			
			// Call the child routine and get the contents of the row & column
			// values are read from the correponding inputCommon / outputCommon tables
			String text = getValue(row,col);
	
			// add new item to clipboard
			clipboardString.append(text);

			// remember previous
			prevRow = row;
		}

		// create clipboard content
		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(clipboardString.toString());

		// set clipboard content
		Clipboard.getSystemClipboard().setContent(clipboardContent);
	
		System.out.println(clipboardString.toString());
	
	}

	protected  void pasteFromClipboard( ) {
		
		System.out.println("Paste");

		
		// abort if there's not cell selected to start with
		if( table.getSelectionModel().getSelectedCells().size() == 0) {
			return;
		}
		
				
		// get the cell position to start with
		TablePosition pasteCellPosition = table.getSelectionModel().getSelectedCells().get(0);
		
		System.out.println("Pasting into cell " + pasteCellPosition);
		
		String pasteString = Clipboard.getSystemClipboard().getString();
		
		System.out.println(pasteString);

		int rowClipboard = -1;
		
		StringTokenizer rowTokenizer = new StringTokenizer( pasteString, "\n");
		while( rowTokenizer.hasMoreTokens()) {

			rowClipboard++;
			
			String rowString = rowTokenizer.nextToken();
			
		    StringTokenizer columnTokenizer = new StringTokenizer( rowString, "\t");

		    int colClipboard = -1;
		    
		    while( columnTokenizer.hasMoreTokens()) {

		    	colClipboard++;

		    	// get next cell data from clipboard
		    	String clipboardCellContent = columnTokenizer.nextToken();

		    	// calculate the position in the table cell
		    	int row = pasteCellPosition.getRow() + rowClipboard;
		    	int col = pasteCellPosition.getColumn() + colClipboard;

		    	// skip if we reached the end of the table
		    	if( row >= table.getItems().size()) {
		    		continue;
		    	}
		    	
		    	// we dont allow paste outside column
		    	if( col >= table.getColumns().size()) {
		    		continue;
		    	}

		    	if (isCellContentValid(row,col,clipboardCellContent)) {
		    		setValue(row,col,clipboardCellContent);
		    	} else {
		    		continue;
		    	}

		    	
		    	System.out.println(row + "/" + col);
		    }

		}
		
	}
	
	protected int insertRowAbove() { 
		// abort if there's not cell selected to start with
		if( table.getSelectionModel().getSelectedCells().size() == 0) {
			return -1;
		}
		

					
		// get the cell position to start with
		int row = table.getSelectionModel().getSelectedCells().get(0).getRow();

		System.out.println("inserting row above " + row);
		
    	// skip if we reached the end of the table
    	if( row >= table.getItems().size()) {
    		return -1;
    	}
    	
    	return row;
    	    	
	}
	
	
		
	protected int deleteRow() { 
		// abort if there's not cell selected to start with
		if( table.getSelectionModel().getSelectedCells().size() == 0) {
			return -1;
		}
					
		// get the cell position to start with
		int deleteRowPosition = table.getSelectionModel().getSelectedCells().get(0).getRow();

		System.out.println("delete Row " + deleteRowPosition);
		return deleteRowPosition;
	}
	

}
