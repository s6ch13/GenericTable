/*
	RootPaneController.java
	
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
    along with this program.  If not, see www.gnu.org/licenses.
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
	private MainApp mainApp;
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
		mainApp.exit();
	}

	@FXML
	void MenuCut(ActionEvent event) {
		System.out.println("Cut Menu");
		TableEventObject.getInstance().cut();
	}

	@FXML
	void MenuCopy(ActionEvent event) {
		System.out.println("Copy Menu");
		TableEventObject.getInstance().copy();
	}

	@FXML
	void MenuPaste(ActionEvent event) {
		System.out.println("Paste Menu");
		TableEventObject.getInstance().paste();
	}

	@FXML
	void MenuDelete(ActionEvent event) {
		System.out.println("Delete Menu");
		TableEventObject.getInstance().delete();
	}



    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
}
