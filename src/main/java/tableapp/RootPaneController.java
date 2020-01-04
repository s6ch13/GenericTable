/*
	RootPaneController.java
	
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
package tableapp;

import java.net.URL;
import java.util.ResourceBundle;

import com.gems.table.TableEventObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;


public class RootPaneController implements Initializable {

	// Reference to the main application.
	private TableApp tableApp;
	private ResourceBundle bundle;

	@FXML
	private MenuItem MenuClose;

	@FXML
	private MenuItem MenuCut;
	@FXML
	private MenuItem MenuCopy;
	@FXML
	private MenuItem MenuPaste;
	@FXML
	private MenuItem MenuDelete;
	

	@FXML private AnchorPane PersonTableHolder;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
    }

	@FXML
	void MenuFileClose(ActionEvent event) {
		tableApp.exit();
	}

	@FXML
	void MenuCut(ActionEvent event) {
		TableEventObject.getInstance().cut();
	}

	@FXML
	void MenuCopy(ActionEvent event) {
		TableEventObject.getInstance().copy();
	}

	@FXML
	void MenuPaste(ActionEvent event) {
		TableEventObject.getInstance().paste();
	}

	@FXML
	void MenuDelete(ActionEvent event) {
		TableEventObject.getInstance().delete();
	}



    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param tableApp
     */
    public void setMainApp(TableApp tableApp) {
        this.tableApp = tableApp;
    }
    
}
