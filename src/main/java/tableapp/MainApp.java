/*
	MainApp.java
	
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

package tableapp;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private ResourceBundle bundle;
    
    private RootPaneController rootPaneController;


	@Override
	public void start(Stage primaryStage) throws IOException 
	{
		this.primaryStage = primaryStage;
        
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
           // exception.setText(e.toString());
            e.printStackTrace();
        });

        initRootLayout();
	}
	
	
	
	 /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
        	
        	
    		String fxmlDocPath = "/View/RootPane.fxml";
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            Locale locale = new Locale("en", "IN");

            loader.setLocation(MainApp.class.getResource(fxmlDocPath));
            
            this.primaryStage.setTitle("Table App demo");


            this.rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            this.primaryStage.setScene(scene);
            
            // Give the controller access to the main app.
            this.rootPaneController = loader.getController();
            this.rootPaneController.setMainApp(this);
            
            this.primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    
	// Initialize all the systems before launching the GUI
	public static  void initialize() {
				
		InputCommon iC = InputCommon.getInstance();
		iC.init();		
		
		// Lets setup 3 names in this example to start.
		// Row number starts at 0 while index starts with 1
		iC.addPerson(0,new Persons(1,"Mary",23));
		iC.addPerson(1,new Persons(2,"John",31));
		iC.addPerson(2,new Persons(3,"Alex",53));
	}
	
	public void exit() {
		System.exit(0);
	}
	


	public static  void main(java.lang.String[] args) {
		initialize();
		
		// Launch the UI
		launch(args);
		
		return;
	}
}
