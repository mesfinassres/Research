package mulseplayer;

import exhalia.CScentDiffusionContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.util.Pair;

public class SensoryEffect {
	static int scentIndex=0;
	/*
	 * This constructor initialises a dialog form to get 
	 * configuration information (scent and seek time).
	 */
	public SensoryEffect(){
		// Create custom dialog scent configuration.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Setting");
		dialog.setHeaderText("Enter scent and seek time.");

		ButtonType applyButtonType = new ButtonType("Apply", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

		// Create the scent and seek time labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField scent = new TextField();
		scent.setPromptText("Scent");
		TextField seekTime = new TextField();
		seekTime.setPromptText("Seek time");

		grid.add(new Label("Scent:"), 0, 0);
		grid.add(scent, 1, 0);
		grid.add(new Label("Seek time:"), 0, 1);
		grid.add(seekTime, 1, 1);

		// Enable/Disable apply button depending on whether a scent was entered.
		Node applyButton = dialog.getDialogPane().lookupButton(applyButtonType);
		applyButton.setDisable(true);

		// Do validation (using the Java 8 lambda syntax).
		scent.textProperty().addListener((observable, oldValue, newValue) -> {
		    applyButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the scent field by default.
		Platform.runLater(() -> scent.requestFocus());

		// Convert the result to a scent-seek time-pair when the apply button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == applyButtonType) {
		        return new Pair<>(scent.getText(), seekTime.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(scentTime -> {
		    System.out.println("Scent=" + scentTime.getKey() + ", Seek time=" + scentTime.getValue());
		});
	}
	/*
	 * This method use Exhalia's scent diffusion configuration
	 * to diffuse scent at the appropriate time by making use 
	 * of the currenttimeProperty of the media being played.
	 * 
	 * It is called by the MediaConrol class
	 */
	static void diffuseScent(MediaPlayer player, String[] scentConfig){

		player.currentTimeProperty().addListener(
				new ChangeListener<Duration>(){		
			@Override
			public void changed(ObservableValue<? extends Duration> 
						observableValue, Duration duration, Duration current){
				CScentDiffusionContext diffusionContext = 
						new CScentDiffusionContext("","","","","","","");

				int currentTime = (int)player.getCurrentTime().toSeconds();
				if(scentIndex < scentConfig.length)
					if(currentTime == Integer.parseInt(scentConfig[scentIndex].split(" ")[0])) {
						try {
							diffusionContext.DiffuseCommand(scentConfig[scentIndex].split(" ")[1]);
							scentIndex++;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				//System.out.println(currentTime+" " +scentIndex);
			};
		});
	}
	/*
	 * Get scent configuration and seek time from a text
	 * file.
	 */
	static String[] getConfig() throws Exception{
		scentIndex = 0;
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
}
