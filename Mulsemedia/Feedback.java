package mulseplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Feedback {
	int i = 0;
	int max = 20;
    String[] questions = new String[max];
    String[] responses = new String[max];
    String endTime = (String) Calendar.getInstance().getTime().toString();
	public Feedback(Stage stage, Group root){

	    HBox hbox = new HBox();
	    VBox vbox = new VBox();
	    root.getChildren().add(hbox);
		
		Label inst = new Label();		
		Label lbInst = new Label();
		Label Item = new Label();
		Item.setText("");	
		Item.setFont(new javafx.scene.text.Font("Bold", 20));
		
		
		inst.setText("INSTRUCTION");
	    inst.setFont(new javafx.scene.text.Font("Bold", 20));

		lbInst.setText("This is a feedback page for the previous video scene.\n"+
					   "Please select your appropriate response.");
		lbInst.setFont(new javafx.scene.text.Font("Bold", 20));

	    RadioButton rb1 = new RadioButton("Strongly Disagree");
		rb1.setFont(new javafx.scene.text.Font("Bold", 20));
	    RadioButton rb2 = new RadioButton("Disagree");
		rb2.setFont(new javafx.scene.text.Font("Bold", 20));
	    RadioButton rb3 = new RadioButton("Neutral");
		rb3.setFont(new javafx.scene.text.Font("Bold", 20));
	    RadioButton rb4 = new RadioButton("Agree");
		rb4.setFont(new javafx.scene.text.Font("Bold", 20));
	    RadioButton rb5 = new RadioButton("Strongly Agree");
		rb5.setFont(new javafx.scene.text.Font("Bold", 20));
	    
	    Button next = new Button();
		next.setAlignment(Pos.BOTTOM_LEFT);
		next.setText("Next>>");
	    Button back = new Button();
	    back.setText("<<Back");
	    Button submit = new Button();
	    submit.setText("Submit");
	    	    
	    vbox.getChildren().addAll(inst, lbInst, Item);		
	    vbox.getChildren().addAll(rb1, rb2, rb3, rb4, rb5);
	    vbox.setSpacing(10);
	
	    hbox.setSpacing(50);
	    hbox.setPadding(new Insets(20, 10, 10, 20));	
		vbox.getChildren().addAll(next, back, submit);		
	    hbox.getChildren().add(vbox);

		final ToggleGroup group = new ToggleGroup();
	    rb1.setToggleGroup(group);
	    rb1.setUserData("1");
	    rb2.setToggleGroup(group);
	    rb2.setUserData("2");
	    rb3.setToggleGroup(group);
	    rb3.setUserData("3");
	    rb4.setToggleGroup(group);
	    rb4.setUserData("4");
	    rb5.setToggleGroup(group);
	    rb5.setUserData("5");
	    
	    try {
			questions = getQuestions();
			if (i < questions.length) Item.setText(i+1 +"."+ questions[i]);
		} catch (Exception e) {	}
	    
	    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
	    	  //update the array of responses
	    	  if (group.getSelectedToggle() != null) responses[i] = i+1 + "." + group.getSelectedToggle().getUserData().toString();	
	      }
	    });

	    next.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
		    	rb1.setSelected(false);
		    	rb2.setSelected(false);
		    	rb3.setSelected(false);
		    	rb4.setSelected(false);
		    	rb5.setSelected(false);
				int length = questions.length;
				if(i < length -1) i++;
				//Display next question
				if (i < length) Item.setText(i+1 + "." + questions[i]);
			}
		});
		back.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				if(i>0) i--;
				//Display previous question
				if (i < questions.length) Item.setText(i+1 + "." + questions[i]);
			}
		});
		submit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				Alert alert = new Alert(AlertType.CONFIRMATION, "Please confirm",ButtonType.OK,ButtonType.CANCEL);
				alert.setTitle("");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					//submission goes here
					saveResponses(responses);
				    //close the window and go back to the main menu
				    stage.close();
				}
			}
		});
	} 
	static String[] getQuestions() throws Exception{
		BufferedReader br = new 
				BufferedReader(new FileReader("questions.txt"));
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
    void saveResponses(String[] responses) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("responses.txt", true));
            writer.write(endTime);
	        for (String response: responses) {
	        	if (response!=null){
		            writer.write(response);
		            writer.newLine();
	        	}
	        }
	        writer.close();
        }catch (IOException e1) {}
    }
}