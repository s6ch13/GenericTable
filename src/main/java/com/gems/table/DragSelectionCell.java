/*
	DragSelectionCell.java
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

package com.gems.table;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class DragSelectionCell<S,T> extends TextFieldTableCell<S, T> {  
    
	static int startRow;
	static int startCol;
	
	static int lastIndex;
	
	public DragSelectionCell(final StringConverter<T> converter) {
		super(converter);
		
		dragEvent();
		
	}
	

	public DragSelectionCell() {
		
		dragEvent();
		
	}
	
	public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
		return forTableColumn(new DefaultStringConverter());
	}
	
	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
			final StringConverter<T> converter) {
		return list -> new DragSelectionCell<S, T>(converter);
	}
	
	// FIXME select rectangle doesnt extend above on scrollviews
	
	public void selectRectangle() {
		
        ObservableList<TableColumn <S,?>>  list = getTableColumn().getTableView().getColumns();        
        int endCol = list.indexOf(getTableColumn());
		int endRow = getIndex();

        System.out.println("On Drag Detected " +endRow + " " + endCol);
        
        // Clear old rectangle
        // FIXME - only the rectangle selection should be cleared and not the previous selection
		getTableColumn().getTableView().getSelectionModel().clearSelection();	
		
		// is the below two lines needed
		getTableColumn().getTableView().getSelectionModel().setCellSelectionEnabled(true);//
		getTableColumn().getTableView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Select Rectangle
        for (int i = (startRow <= endRow) ? startRow:endRow; i <= ((startRow < endRow)?endRow:startRow);i++) {        	
            for (int j = (startCol <= endCol) ? startCol:endCol; j <= ((startCol < endCol) ? endCol : startCol);j++) {
        		
            	getTableColumn().getTableView().getSelectionModel().select(i, list.get(j));  	
            	
            }       	
        }
           
	}
	
	
	public void dragEvent() {
	       setOnDragDetected(new EventHandler<MouseEvent>() {  
	            @Override  
	            public void handle(MouseEvent event) {  
	                startFullDrag();  
	                getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn());  
	                
	                ObservableList<TableColumn <S,?>>  columnList = getTableColumn().getTableView().getColumns();
	                startCol = columnList.indexOf(getTableColumn());
	                startRow = getIndex();
	                
	                System.out.println("On Drag Entered " +startRow + " " + startCol);

	            }  
	        });  
	       
	        setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {  

	            @Override  
	            public void handle(MouseDragEvent event) {  
	                System.out.println("On Drag Entered " +getIndex() + " " + getTableColumn());
	                
	                selectRectangle();

	            }  
	            
	        });  
	        
	        setOnMouseDragExited(new EventHandler<MouseDragEvent>() {  

	            @Override  
	            public void handle(MouseDragEvent event) {  
	                //getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn());  
	                System.out.println("On Drag exited " +getIndex() + " " + getTableColumn());

	            }  
	            
	        });  
	}
	    
}  
