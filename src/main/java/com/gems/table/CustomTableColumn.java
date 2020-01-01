/*
	CustomTableColumn.java
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

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

public class CustomTableColumn<S,T> extends TableColumn<S,T> {
    String headerString;
	Label label;
    StackPane stack;

	
	public CustomTableColumn() {
		super();
		label = new Label();
		stack = new StackPane();
		
		headerString = new String(getText());
		customHeader();
	}
	
	public CustomTableColumn(String s) {
		super(s);
		headerString = new String(s);

		label = new Label();
		stack = new StackPane();
		customHeader();
	}
	
	
	
	public void customHeader() {
	    //label.setStyle("-fx-padding: 8px;");
		label.setText(headerString);
	    label.setWrapText(true);
	    label.setAlignment(Pos.CENTER);
	    label.setTextAlignment(TextAlignment.CENTER);
	    label.setContentDisplay(ContentDisplay.RIGHT);
	    label.setGraphicTextGap(4);
	    
	    if (stack.getChildren() !=null) {
	    	stack.getChildren().remove(label);
	    }
	    stack.getChildren().add(label);
	    stack.prefWidthProperty().bind(this.widthProperty());
	    label.prefWidthProperty().bind(stack.prefWidthProperty());
	    this.setText(null);
	    this.setGraphic(stack);		
	}
	
	public void setHeaderText(String s) {
		headerString = s;
		customHeader();
	}

	public String getHeaderString() {
		return headerString;
	}
	
	public void setHeaderGraphic(Node n) {
		label.setGraphic(n);		
	}
	
	public Node getHeaderGraphic() {
		return(label.getGraphic());
	}
	
}
