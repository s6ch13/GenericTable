/*
	GenericTableController.java
	Copyright (C) 2019  Sriram C.

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met:
	
	1. Redistributions of source code must retain the above copyright notice, this
	   list of conditions and the following disclaimer.
	2. Redistributions in binary form must reproduce the above copyright notice,
	   this list of conditions and the following disclaimer in the documentation
	   and/or other materials provided with the distribution.
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
	ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/ 

/*
 * Reference:
 * Some portions of the code for edit cells was initially developed by DanNewton and enhanced
 * https://dzone.com/articles/editable-tables-in-javafx
 * 
 * Copy and paste portions of this code have been borrowed & modified from below sources
 * Copyright of these sections are as per the original author Roland09	
 * // https://gist.github.com/Roland09/6fb31781a64d9cb62179
 * 
 * Roland09 commented on 15 Jul 2019
 * Thanks for asking, but just use it as you need it, there's no license, 
 * it's part of a a tutorial, i. e. an answer on StackOverflow:
 * https://stackoverflow.com/questions/31708840/save-table-in-clipboad-javafx
 */


package com.gems.table;

import java.util.List;
import java.util.StringTokenizer;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;


public abstract class GenericTableController<S> implements TableEventListener{

	public TableView<S> table;
	protected ScrollPane tableScrollPane;
	protected boolean tableSelected = false; // variable to track if this table is under focus

	// create context Menu
	protected ContextMenu contextMenu;
	protected MenuItem copyMenuItem, pasteMenuItem, cutMenuItem, deleteMenuItem, selectAllMenuItem;
	protected MenuItem insertRowAboveMenuItem, deleteRowMenuItem;
	
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

		tableSelected = false;
		table.getSelectionModel().clearSelection();
	}
	
	public void initFocusChange() {
		// Everytime a change occurs / new row is selected we receive this event		
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	if ( tableSelected == false) {
		    		// focus has entered this table
		    		// Send an event to let other tables know that this table has gained focus
		    		// this way the other tables can clear their selections
		    		TableEventObject.getInstance().tableFocusChanged(table.getId());
		    		tableSelected = true;
		    	}
		    }
		});

	}
	
	public abstract String getValue(int row,int col);
	
	public abstract void setValue(int row,int col,String val);
	
	protected boolean isCellContentValid(int row,int col,String clipboardCellContent) {
		return true;
	}
	
	public abstract void clearValue(int row,int col);
	
	protected void updateColumnHeaders() {}
	
	public ContextMenu getContextMenu() { 
		return contextMenu;
	}
			
	
	public void tableEventReceived(TableEvent event) {
		
		
		if (event.tableEventType() == TableEventType.COPY) {
			copySelectionToClipboard();
		}
		
		if (event.tableEventType() == TableEventType.PASTE) {
			pasteFromClipboard();
		}

		if (event.tableEventType() == TableEventType.CUT) {
    		copySelectionToClipboard();
    		deleteSelection();
		}
		
		if (event.tableEventType() == TableEventType.DELETE) {
    		deleteSelection();
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
        		copySelectionToClipboard();
				event.consume();
            } 	
        });
        
        pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	pasteFromClipboard();
				event.consume();
            }
        });
        
        cutMenuItem = new MenuItem("Cut");
        cutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
        		copySelectionToClipboard();
        		deleteSelection();
				event.consume();
            } 	
        });
        
        deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	deleteSelection();
				event.consume();
            }
        });

        selectAllMenuItem = new MenuItem("Select All");
        selectAllMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		table.getSelectionModel().selectAll();
				event.consume();
            }
        });
        
        insertRowAboveMenuItem = new MenuItem("Insert row above");
        insertRowAboveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		insertRowAbove();
				event.consume();
            }
        });
        
        deleteRowMenuItem = new MenuItem("Delete row");
        deleteRowMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
	
	// To create a context menu specific to a table, override this method with 
	// required items in the context Menu
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
		  
	// refresh the table contents when the underlying observable list tracked by table gets updated  
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

		if (positionList == null) 
			return;
		
		int len = positionList.size();
		int rowArray[] = new int[len];
		int colArray[] = new int[len];
		
		
		for (int i = 0;i<len;i++) {
			TablePosition<S,?> p = positionList.get(i);
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
	}

	protected  void pasteFromClipboard( ) {
		
		// abort if there's not cell selected to start with
		if( table.getSelectionModel().getSelectedCells().size() == 0) {
			return;
		}
						
		// get the cell position to start with
		TablePosition<S, ?> pasteCellPosition = table.getSelectionModel().getSelectedCells().get(0);
				
		String pasteString = Clipboard.getSystemClipboard().getString();
		
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

		return deleteRowPosition;
	}	
}
