/*
	CustomTableColumn.java
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

package com.gems.table;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

public class CustomTableColumn<S,T> extends TableColumn<S,T> {
    private String headerText;
	Label label;
    StackPane stack;

	
	public CustomTableColumn() {
		super();
		label = new Label();
		stack = new StackPane();
		
		headerText = new String(getText());
		customHeader();
	}
	
	public CustomTableColumn(String s) {
		super(s);
		headerText = new String(s.trim());

		label = new Label();
		stack = new StackPane();
		customHeader();
	}
	
	
	
	public void customHeader() {
	    //label.setStyle("-fx-padding: 8px;");
		label.setText(headerText);
	    label.setWrapText(true);
	    label.setAlignment(Pos.CENTER_RIGHT);
	    label.setTextAlignment(TextAlignment.RIGHT);
	    label.setContentDisplay(ContentDisplay.RIGHT);
	    label.setGraphicTextGap(2);
	    
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
		this.headerText = s.trim();
		customHeader();
	}

	public String getHeaderText() {
		return this.headerText;
	}
	
	public void setHeaderGraphic(Node n) {
		label.setGraphic(n);		
	}
	
	public Node getHeaderGraphic() {
		return(label.getGraphic());
	}
	
	public void setHeaderAlignment(Pos p) { 
		label.setAlignment(p);
	}
	
	public void setHeaderAlignment(String p) { 
		setHeaderAlignment(Pos.valueOf(p));
	}	

	public void setTextAlignment(TextAlignment tA) { 
		label.setTextAlignment(tA);
	}
	
	public void setTextAlignment(String tA) { 
		label.setTextAlignment(TextAlignment.valueOf(tA));
	}	
	
}
