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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.controlsfx.control.Notifications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private ResourceBundle bundle;
    
    private RootPaneController rootPaneController;

    //private MenuBarController menuBarController;


	@Override
	public void start(Stage primaryStage) throws IOException 
	{
		this.primaryStage = primaryStage;
        
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
           // exception.setText(e.toString());
            e.printStackTrace();
        });

        initRootLayout();
        //showMenuBar();        
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
    
/*    public void showMenuBar() {
        try {
            // Load menuBar
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/View/MenuBar.fxml"));
            
            MenuBar menuBar = (MenuBar) loader.load();
            this.rootLayout.setTop(menuBar);
            
            // Give the controller access to the main app.
            this.menuBarController = loader.getController();
            this.menuBarController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }    	
    }

    public MenuBarController getMenuBarController() {
    	return this.menuBarController;
    }
 */   
    
            
    
	// Initialize all the systems before launching the GUI
	public static  void initialize() {
				
		InputCommon iC = InputCommon.getInstance();
		iC.init();		
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
