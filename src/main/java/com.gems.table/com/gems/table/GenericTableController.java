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
import java.util.Set;
import java.util.StringTokenizer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
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
import javafx.scene.input.MouseEvent;


public abstract class GenericTableController<S> implements TableEventListener {

	protected TableView<S> table;
	protected boolean tableSelected = false; // variable to track if this table is under focus

	// create context Menu
	protected ContextMenu contextMenu;
	protected MenuItem copyMenuItem, pasteMenuItem, cutMenuItem, deleteMenuItem, selectAllMenuItem;
	protected MenuItem insertRowAboveMenuItem, deleteRowMenuItem;

	protected ScrollPane tableScrollPane;
	Bounds tableBounds;
	double topYProximity, bottomYProxmity,leftXProxmity, rightXProxmity;
	final double tableEdgeRegion = 50;	// Proximity from the edge where scroll is started

	private enum ScrollMode {
		UP, DOWN, LEFT, RIGHT, NONE
	}

	private AutoScrollableTableThread autoScrollThread = null;
	
	public void init() {
		
		// enable multi-selection
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		installKeyboardHandler();
		initContextMenuItems();
        initContextMenu();		
		
		updateColumnHeaders();
		initFocusChange();
	    	
		table.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				if (newValue == null)
					return;

				tableBounds = newValue;

				// Area At Top Of Table View. i.e Initiate Upwards Auto Scroll If
				// We Detect Mouse Being Dragged Above This Line.
				topYProximity = tableBounds.getMinY() + tableEdgeRegion;

				// Area At Bottom Of Table View. i.e Initiate Downwards Auto Scroll If
				// We Detect Mouse Being Dragged Below This Line.
				bottomYProxmity = tableBounds.getMaxY() - tableEdgeRegion;

				// Area At Left Of Table View. i.e Initiate leftward Auto Scroll If
				// We Detect Mouse Being Dragged left of this Line.
				leftXProxmity = tableBounds.getMinX() + tableEdgeRegion;

				// Area At Right Of Table View. i.e Initiate rightward Auto Scroll If
				// We Detect Mouse Being Dragged right of this Line.
				rightXProxmity = tableBounds.getMaxX() - tableEdgeRegion;
			}
		});

		tableScrollOnDrag();

    	TableEventObject.getInstance().addTableEventListener(this);
	}
    	
	
	// We receive this event when the table focus changes
	// so we can clear the selections in the table.  
	private void clearTableSelection(String id) {
		// return if we receive our own event or if we weren't the last selected table
		if (table.getId() == id || tableSelected==false) 
			return;

		tableSelected = false;
		table.getSelectionModel().clearSelection();
	}
	
	/**
	 * When a change occurs / new row is selected we receive this event. Let other
	 * tables know about the selection in this table so they can clear their
	 * selections (which would now be stale)
	 */
	protected void initFocusChange() {
		
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
	
	
	/**
	 * Returns the String value of the cell selected. This method should be
	 * implemented in the child classes
	 * 
	 * @param row - row number
	 * @param col - column number
	 * @return String value of the cell selected
	 */
	public abstract String getValue(int row,int col);

	
	/**
	 * Sets the String value of the cell selected. This method should be implemented
	 * in the child classes
	 * 
	 * @param row    - row number
	 * @param col    - column number
	 * @param String value of the cell selected
	 */
	public abstract void setValue(int row,int col,String val);
	
	
	/**
	 * Checkf if the cell content for a given row / column is valid. This prevents
	 * pasting of incorrect type into a table column. This can also be used to
	 * prevent pasting contents into certain cells.
	 * 
	 * This method should be overridden where the check is actually implemented.
	 * 
	 * @param row                  - table row for cell
	 * @param col                  - table col for cell
	 * @param clipboardCellContent - content for cell
	 * @return true if content is valid.
	 */
	protected boolean isCellContentValid(int row, int col, String clipboardCellContent) {
		return true;
	}
	
	/**
	 * clears value of the cell selected. This method should be implemented in the
	 * child class where the value of the cell after clearing is set.
	 * 
	 * @param row - row number
	 * @param col - column number
	 */
	public abstract void clearValue(int row,int col);
	
	/**
	 * Handle event for updating the column headers of the table
	 */
	protected void updateColumnHeaders() {}
	
	/**
	 * 
	 * @return contextMenu
	 */
	public ContextMenu getContextMenu() { 
		return contextMenu;
	}
			
	/**
	 * Handle table event received by this class for cut/copy/paste/delete
	 * table-focus change, insert row above, delete row
	 */
	public void tableEventReceived(TableEvent event) {
		
		if (event.tableEventType() == TableEventType.TABLE_FOCUS_CHANGED) {
			clearTableSelection((String)event.tableEventObject());
		}
		
		if (!isTableCellSelected())	
			return;
		
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
		
		if (event.tableEventType() == TableEventType.INSERT_ROW_ABOVE) {
			insertRowAbove();
		}
		
		if (event.tableEventType() == TableEventType.DELETE_ROW) {
			deleteRow();
		}
	
	}
	
	/**
	 * Initialize context menu items with handling functions for commonly used
	 * actions like cut copy, paste, delete insert row above, delete row.
	 */
	protected void initContextMenuItems() {
        		
		copyMenuItem = new MenuItem("Copy");
        copyMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {

        		if (isTableCellSelected())	
            		copySelectionToClipboard();

				event.consume();
            } 	
        });
        
        pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

        		if (isTableCellSelected())	
                	pasteFromClipboard();

				event.consume();
            }
        });
        
        cutMenuItem = new MenuItem("Cut");
        cutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {        		

        		if (isTableCellSelected()) {
        			copySelectionToClipboard();
        			deleteSelection();
        		}
        		
				event.consume();
            } 	
        });
        
        deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

        		if (isTableCellSelected())	
                	deleteSelection();

				event.consume();
            }
        });

        selectAllMenuItem = new MenuItem("Select All");
        selectAllMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

        		if (isTableCellSelected())	
            		table.getSelectionModel().selectAll();

				event.consume();
            }
        });
        
        insertRowAboveMenuItem = new MenuItem("Insert row above");
        insertRowAboveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

        		if (isTableCellSelected())	
            		insertRowAbove();

				event.consume();
            }
        });
        
        deleteRowMenuItem = new MenuItem("Delete row");
        deleteRowMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

        		if (isTableCellSelected())	
            		deleteRow();

				event.consume();
            }
        });
               
        if (contextMenu == null) {
        	contextMenu = new ContextMenu();
        }
 
        initContextMenu();
 
        // When user right-clicks on table cell
        table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
 
            @Override
            public void handle(ContextMenuEvent event) {
 
                contextMenu.show(table, event.getScreenX(), event.getScreenY());
            }
        });
 
	}

	/**
	 * Add additional items to the context menu
	 *
	 * @param mI - Array of the object of type MenuItem.
	 */
	protected void addContextMenuItems(MenuItem [] mI) {
		int i = 0;
		while ( i < mI.length ) {
			contextMenu.getItems().add(mI[i]);
			i++;
		}
	}

	/**
	 * Handle Right Click on the Table Create a context menu with options. typically
	 * used for cut, copy, paste, delete insert rows above, delete rows. To create a
	 * context menu specific to a table, override this method with required items in
	 * the context Menu
	 */
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

	/**
	 * 	Sets the table as editable and let individual selection of cells
	 */
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
		  
 
	/**
	 * Refresh the table contents when the underlying observable list tracked by
	 * table gets updated
	 */
	public void refresh() {
		table.getColumns().get(0).setVisible(false);
		table.getColumns().get(0).setVisible(true);
	}
	
	/**
	 * Install the keyboard handler for commonly used actions on table like cut,
	 * copy, paste, select all. reference -
	 * https://gist.github.com/Roland09/6fb31781a64d9cb62179
	 */
	private void installKeyboardHandler() {
		// install copy/paste keyboard handler
		table.setOnKeyPressed(new TableKeyEventHandler());
	}
	
	/**
	 * Copy/Paste/Cut/Select all/delete/backspace keyboard event handler. The
	 * handler uses the keyEvent's source for the clipboard data. The source must be
	 * of type TableView.
	 */
	private class TableKeyEventHandler implements EventHandler<KeyEvent> {

		KeyCodeCombination cutKeyCodeCombination = new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN);
		KeyCodeCombination copyKeyCodeCombination = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
		KeyCodeCombination pasteKeyCodeCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN);
		KeyCodeCombination selectAllKeyCodeCombination = new KeyCodeCombination(KeyCode.A,
				KeyCombination.SHORTCUT_DOWN);

		public void handle(final KeyEvent keyEvent) {
			if (keyEvent.getSource() instanceof TableView && isTableCellSelected()) {

				if (cutKeyCodeCombination.match(keyEvent)) {

					// cut is simulated as copy to clipboard and clear cells
					copySelectionToClipboard();
					deleteSelection();
					keyEvent.consume();
				} else if (copyKeyCodeCombination.match(keyEvent)) {

					// copy to clipboard
					copySelectionToClipboard();
					keyEvent.consume();	
					
				} else if (pasteKeyCodeCombination.match(keyEvent)) {

					pasteFromClipboard();
					keyEvent.consume();
					
				} else if (selectAllKeyCodeCombination.match(keyEvent)) {

					table.getSelectionModel().selectAll();
					keyEvent.consume();
					
				} else if (keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE) {
					// Delete contents of cell
					deleteSelection();
					keyEvent.consume();	
					
				} else if (keyEvent.getCode().isLetterKey() || keyEvent.getCode().isDigitKey()) {
					// when character or numbers pressed it will start edit in editable
					// fields
					editFocusedCell();
				}

			}
		}
	}
	
	/**
	 * Check if any cells in this table are selected
	 * 
	 * @return true / false
	 */
	public boolean isTableCellSelected() {
		return (table.getSelectionModel().getSelectedCells().size() > 0);
	}
	
	/**
	 * Delete the contents of the selected cell.
	 */
	public void deleteSelection() {
		if (!isTableCellSelected()) 
			return;
		
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
			// we cannot clear the first column
			if (colArray[i] > 0)
				clearValue(rowArray[i],colArray[i]);
		}
	
		refresh();
	}

	/**
	 * Get table selection and copy it to the clipboard.
	 * 
	 * @param table
	 */
	public void copySelectionToClipboard() {

		if (!isTableCellSelected()) 
			return;

		StringBuilder clipboardString = new StringBuilder();
		
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

	/**
	 * Paste the contents of the clipboard at the cell selected in the table. Only
	 * the cells that fit into the table are pasted and remaining clip board
	 * contents are ignored Rows are expected to be delineated by /n while columns
	 * are expected to be delineated by /t
	 */
	protected void pasteFromClipboard() {
		
		if (!isTableCellSelected()) 
			return;
						
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
		refresh();
	}
	
	/**
	 * Insert row to the table above the selected cell.
	 * 
	 * @return position of the row being inserted. On error, returns -1
	 */
	protected int insertRowAbove() {
		if (!isTableCellSelected()) 
			return -1;
					
		// get the cell position to start with
		int row = table.getSelectionModel().getSelectedCells().get(0).getRow();
		
    	// skip if we reached the end of the table
    	if( row >= table.getItems().size()) {
    		return -1;
    	}
    	
    	return row;   	    	
	}
	
	/**
	 * Delete row at the cursor. Only one row is deleted at a time. If multiple rows
	 * are selected, only the top most row is deleted
	 * 
	 * @return index of the row being deleted. On error, returns -1
	 */
	protected int deleteRow() {
		if (!isTableCellSelected()) 
			return -1;
					
		// get the cell position to start with
		int deleteRowPosition = table.getSelectionModel().getSelectedCells().get(0).getRow();

		return deleteRowPosition;
	}	
	
	
	/**
	 * Setup the table such that when the mouse is dragged to select cells and goes
	 * beyond the table visible view, the table needs to scroll to enable further
	 * selection.
	 */
	private void tableScrollOnDrag() {
		table.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				double dragY = event.getY();
				double dragX = event.getX();

				// Make Use Of A Thread To scroll the table Up/Down/Left/Right if
				// The cells selected are within the edges
				if (dragY < topYProximity) {
					// Scroll Up
					if (autoScrollThread == null) {
						autoScrollThread = new AutoScrollableTableThread(table);
						autoScrollThread.scrollUp();
						autoScrollThread.start();
					}

				} else if (dragY > bottomYProxmity) {
					// Scroll Down
					if (autoScrollThread == null) {
						autoScrollThread = new AutoScrollableTableThread(table);
						autoScrollThread.scrollDown();
						autoScrollThread.start();
					}

				} else if (dragX < leftXProxmity) {
					// Scroll Left
					if (autoScrollThread == null) {
						autoScrollThread = new AutoScrollableTableThread(table);
						autoScrollThread.scrollLeft();
						autoScrollThread.start();
					}

				} else if (dragX > rightXProxmity) {
					// Scroll Right
					if (autoScrollThread == null) {
						autoScrollThread = new AutoScrollableTableThread(table);
						autoScrollThread.scrollRight();
						autoScrollThread.start();
					}

				} else {
					// No Auto Scroll Required We Are Within Bounds
					if (autoScrollThread != null) {
						autoScrollThread.stopScrolling();
						autoScrollThread = null;
					}
				}

			}
		});

		table.setOnMouseReleased(event -> {
			// Mouse has been released and auto-scroll should stop if active.
			if (autoScrollThread != null) {
				autoScrollThread.stopScrolling();
				autoScrollThread = null;
			}
		});
	}

	class AutoScrollableTableThread extends Thread {

		private boolean running = true;
		private ScrollMode scrollMode = ScrollMode.NONE;
		private ScrollBar verticalScrollBar = null;
		private ScrollBar horizontalScrollBar = null;

		// scrollIncrement can support accelerated scrolling of rows/columns
		private double scrollIncrement = 0.01;

		public AutoScrollableTableThread(TableView<S> tableView) {
			super();
			setDaemon(true);

			// setup the scrollbar nodes if available.
			Set<Node> scrollBars = tableView.lookupAll(".scroll-bar");

			for (Node n : scrollBars) {
				switch (((ScrollBar) n).getOrientation()) {
				case HORIZONTAL:
					horizontalScrollBar = (ScrollBar) n;
					break;
				case VERTICAL:
					verticalScrollBar = (ScrollBar) n;
					break;
				}
			}
		}

		@Override
		public void run() {

			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			while (running) {

				Platform.runLater(() -> {

					if (scrollMode == ScrollMode.UP && verticalScrollBar != null) {
						if (verticalScrollBar.getValue() > verticalScrollBar.getMin()) {
							verticalScrollBar.setValue(verticalScrollBar.getValue() - scrollIncrement);
							scrollIncrement = scrollIncrement < 0.1 ? scrollIncrement * 1.1 : 0.25;
						}
					} else if (scrollMode == ScrollMode.DOWN && verticalScrollBar != null) {
						if (verticalScrollBar.getValue() < verticalScrollBar.getMax()) {
							verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollIncrement);
							scrollIncrement = scrollIncrement < 0.1 ? scrollIncrement * 1.1 : 0.25;
						}
					} else if (scrollMode == ScrollMode.LEFT && horizontalScrollBar != null) {
						if (horizontalScrollBar.getValue() > horizontalScrollBar.getMin()) {
							horizontalScrollBar.setValue(horizontalScrollBar.getValue() - scrollIncrement);
							scrollIncrement = scrollIncrement < 5 ? scrollIncrement * 2 : 5;
						}
					} else if (scrollMode == ScrollMode.RIGHT && horizontalScrollBar != null) {
						if (horizontalScrollBar.getValue() < horizontalScrollBar.getMax()) {
							horizontalScrollBar.setValue(horizontalScrollBar.getValue() + scrollIncrement);
							scrollIncrement = scrollIncrement < 5 ? scrollIncrement * 2 : 5;
						}
					} else {
						stopScrolling();
					}
				});

				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void scrollUp() {
			scrollMode = ScrollMode.UP;
			running = true;
		}

		public void scrollDown() {
			scrollMode = ScrollMode.DOWN;
			running = true;
		}

		public void scrollLeft() {
			scrollMode = ScrollMode.LEFT;
			running = true;
		}

		public void scrollRight() {
			scrollMode = ScrollMode.RIGHT;
			running = true;
		}

		public void stopScrolling() {
			running = false;
			scrollMode = ScrollMode.NONE;
		}
	}

}
