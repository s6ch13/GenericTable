/*
	TableApp.java
	
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

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class TableApp extends Application {

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

            loader.setLocation(TableApp.class.getResource(fxmlDocPath));
            
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
		iC.addPerson(0,new Person(1,"Mary",23));
		iC.addPerson(1,new Person(2,"John",31));
		iC.addPerson(2,new Person(3,"Alex",53));
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
