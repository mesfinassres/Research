package mulseplayer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mulseplayer.MediaControl;

public class Main extends Application {

	public static void main(String[] args)
	{
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception
	{	
		stage.setTitle("NEWTON Mulseplayer");
		stage.setWidth(1000);
		stage.setHeight(700);
		
		Group root = new Group();			
		Scene scene = new Scene(root,stage.getWidth(), stage.getHeight(), Color.LIGHTGREY);
		
        MenuBar menuBar = new MenuBar();
        
        Menu menuFile = new Menu("File");
        menuFile.setAccelerator(KeyCombination.keyCombination("Alt+F"));
        
        MenuItem menuOpen = new MenuItem("Open");
        menuOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuOpen.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e){
        		//play
				try {
					new MediaControl();
				} catch (Exception e2) {
					e2.printStackTrace();
				}	
           }
        });
        
        menuFile.getItems().add(menuOpen);
        
        MenuItem menuExit = new MenuItem("Exit");
        menuExit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        menuExit.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e){
        		stage.close();
           }
        });
        menuFile.getItems().add(menuExit);

        Menu menuTools = new Menu("Tools");
        MenuItem menuSettings = new MenuItem("Settings");
        menuSettings.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        menuSettings.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e){
        		//Configure sensory effects metadata (SEM)
        		try {
					new SensoryConfig();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
           }
        });
        menuTools.getItems().add(menuSettings);
        
        Menu menuHelp = new Menu("Help");
        MenuItem menuAbout = new MenuItem("About");
        menuAbout.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e){
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Mulseplayer 1.0");
        		alert.setHeaderText("Copyright Brunel University 2017");
        		alert.setContentText("I will have a great value for you!");
        		alert.showAndWait();
           }
        }); 
        menuHelp.getItems().add(menuAbout);        
        menuBar.getMenus().addAll(menuFile, menuTools, menuHelp);
        root.getChildren().addAll(menuBar);
		stage.show();
		stage.setScene(scene); 
	}	
}