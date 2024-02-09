package mulseplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SensoryConfig {
	ObservableList<ScentConfig> data = null;
	/*
	 * This constructor initialises a dialog form to get 
	 * configuration information (scent and seek time).
	 */
	public SensoryConfig() throws Exception{
	    TableView<ScentConfig> table = new TableView<ScentConfig>();
	    final HBox hb = new HBox();
		String[] configData = this.getConfig();
	    if (configData.length > 0) {
	    		data= FXCollections.observableArrayList(new ScentConfig(configData[0].split(" ")[0],configData[0].split(" ")[1]));
	    }
		int i = 1;
		while(i < configData.length){
            data.add(new ScentConfig(configData[i].split(" ")[0], configData[i].split(" ")[1]));
            i++;
		}
		
		// Create custom dialog scent configuration.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Setting");
		dialog.setHeaderText("Enter the seek time and scent configuration.");
		
		//Table view
        final Label label = new Label();
        label.setFont(new Font("Arial", 20));
        table.setEditable(true);
        
        TableColumn seekTimeCol = new TableColumn("Seek time");
        seekTimeCol.setMinWidth(100);
        seekTimeCol.setCellValueFactory(
            new PropertyValueFactory<ScentConfig, String>("seekTime"));
        seekTimeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        seekTimeCol.setOnEditCommit(
            new EventHandler<CellEditEvent<ScentConfig, String>>() {
                @Override
                public void handle(CellEditEvent<ScentConfig, String> t) {
                    ((ScentConfig) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setSeekTime(t.getNewValue());
                }
            }
        );
 
        TableColumn scentConfigCol = new TableColumn("Scent configuration");
        scentConfigCol.setMinWidth(200);
        scentConfigCol.setCellValueFactory(
            new PropertyValueFactory<ScentConfig, String>("scentConfig"));
        scentConfigCol.setCellFactory(TextFieldTableCell.forTableColumn());
        scentConfigCol.setOnEditCommit(
            new EventHandler<CellEditEvent<ScentConfig, String>>() {
                @Override
                public void handle(CellEditEvent<ScentConfig, String> t) {
                    ((ScentConfig) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setScentConfig(t.getNewValue());
                }
            }
        );
 
        table.setItems(data);
        table.getColumns().addAll(seekTimeCol, scentConfigCol);
 
        final TextField addSeekTime = new TextField();
        addSeekTime.setPromptText("Seek time");
        addSeekTime.setMaxWidth(seekTimeCol.getPrefWidth());
        final TextField addScentConfig = new TextField();
        addScentConfig.setMaxWidth(scentConfigCol.getPrefWidth());
        addScentConfig.setPromptText("Scent configuration");
 
        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                data.add(new ScentConfig(
                        addSeekTime.getText(),
                        addScentConfig.getText()));
                addSeekTime.clear();
                addScentConfig.clear();
            }
        });
 
        hb.getChildren().addAll(addSeekTime, addScentConfig, addButton);
        hb.setSpacing(3);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
        
		ButtonType applyButtonType = new ButtonType("Apply", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);
        //end Tableview
		dialog.getDialogPane().setContent(vbox);
		
		// Enable/Disable apply button depending on whether a scent was entered.
		Node applyButton = dialog.getDialogPane().lookupButton(applyButtonType);
		//applyButton.setDisable(true);
		// Do validation (using the Java 8 lambda syntax).
		table.editableProperty().addListener((observable, oldValue, newValue) -> {
		    //applyButton.setDisable(newValue.trim().isEmpty());
		});
		// Request focus on the scent field by default.
		Platform.runLater(() -> table.requestFocus());
		// Convert the result to a scent-seek time-pair when the apply button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == applyButtonType) {
	    		BufferedWriter bw;
	    		StringBuilder sb = new StringBuilder();
	    		ScentConfig retrieveData;
				try {
					bw = new BufferedWriter(new FileWriter("scents.txt"));
					//bw.write(data.remove(1).getSeekTime() +" "+ data.remove(1).getScentConfig());
					while(data.size() > 0){
						retrieveData = data.remove(0);
						sb.append(retrieveData.getSeekTime() +" "+ retrieveData.getScentConfig()+System.lineSeparator());
					}
					bw.write(sb.toString());
		    		bw.close();	
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		    }
		    return null;
		});
		
		Optional<Pair<String, String>> result = dialog.showAndWait();
		result.ifPresent(scentTime -> {
		    System.out.println("Scent=" + scentTime.getKey() + ", Seek time=" + scentTime.getValue());
		});
	}
	static String[] getConfig() throws Exception{
		BufferedReader br = new 
				BufferedReader(new FileReader("scents.txt"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    return sb.toString().split("\\r?\\n"); 
		} finally {
		    br.close();
		}
	}
	
	public class ScentConfig {
		 
	    private final SimpleStringProperty seekTime;
	    private final SimpleStringProperty scentConfig;
	
	    private ScentConfig(String seekTime, String scentConfig) {
	        this.seekTime = new SimpleStringProperty(seekTime);
	        this.scentConfig = new SimpleStringProperty(scentConfig);
	    }
	
	    public String getSeekTime() {
	        return seekTime.get();
	    }
	
	    public void setSeekTime(String fName) {
	    	seekTime.set(fName);
	    }
	
	    public String getScentConfig() {
	        return scentConfig.get();
	    }
	
	    public void setScentConfig(String seekTime) {
	        scentConfig.set(seekTime);
	    }
	}
}