package mulseplayer;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MediaControl extends HBox{
	MediaPlayer player;
	String[] scentConfig;
	final VBox vbox = new VBox();
	
	public MediaControl() throws Exception{
		Stage stage = new Stage();
		stage.setTitle("NEWTON Mulseplayer");
		stage.setWidth(1000);
		stage.setHeight(700);
		stage.show();
		Group root = new Group();
		Scene scene = new Scene(root,stage.getWidth(), stage.getHeight(), Color.LIGHTGREY);
		stage.setScene(scene); 

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open a video file");
		File file = fileChooser.showOpenDialog(stage);	
      	if (file != null) {
			Media media = new Media(file.toURI().toString());
			player = new MediaPlayer(media);

			MediaView view = new MediaView(player);
			view.setFitWidth(stage.getWidth());
			view.setFitHeight(stage.getHeight());			
			root.getChildren().add(view);
            //switch off volume
            player.setVolume(0.0);
			player.play();
			this.slider(root, view, stage);
			this.control(root);
			scentConfig = SensoryEffect.getConfig();
			SensoryEffect.diffuseScent(player, scentConfig);
			player.currentTimeProperty().addListener(new ChangeListener<Duration>(){
				@Override
				public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current){
					int currentTime = (int)player.getCurrentTime().toMillis();
					if (currentTime>119000) {
						player.dispose();
						view.isDisable();
						Feedback fb = new Feedback(stage, root);
					}
				};
			});
       	}
	}
	//Media slider
	void slider(Group root, MediaView view, Stage stage){
		//Slider
		final Timeline slideIn = new Timeline();
		final Timeline slideOut = new Timeline();
		
		root.setOnMouseExited(new EventHandler<MouseEvent>(){
			@Override
			public void handle (MouseEvent mouseEvent){
				slideOut.play();
			}
		});
		root.setOnMouseEntered(new EventHandler<MouseEvent>(){
			@Override
			public void handle (MouseEvent mouseEvent){
				slideIn.play();
			}
		});

		//VBox taken from here
		Slider slider = new Slider();
		vbox.getChildren().add(slider);
		root.getChildren().add(vbox);

		player.setOnReady(new Runnable(){
			@Override
			public void run(){
				
				double w = stage.getWidth();
				double h = stage.getHeight();				
				stage.setMinWidth(w);
				stage.setMinHeight(h);
				int marigin = 100;
				vbox.setMinSize(w, marigin);
				vbox.setTranslateY(h-marigin);
				
				slider.setMin(0.0);
				slider.setValue(0.0);
				slider.setMax(player.getTotalDuration().toSeconds());
				/*
				slideOut.getKeyFrames().addAll(
					new KeyFrame(new Duration(0),
							new KeyValue(vbox.translateYProperty(), h-marigin),
							new KeyValue(vbox.opacityProperty(),0.9)
					),
					new KeyFrame(new Duration(300),
							new KeyValue(vbox.translateYProperty(), h),
							new KeyValue(vbox.opacityProperty(),0.0)
					)						
				);
				
				slideIn.getKeyFrames().addAll(
						new KeyFrame(new Duration(0),
								new KeyValue(vbox.translateYProperty(), h),
								new KeyValue(vbox.opacityProperty(),0.0)
						),
						new KeyFrame(new Duration(300),
								new KeyValue(vbox.translateYProperty(), h - marigin),
								new KeyValue(vbox.opacityProperty(),0.9)
						)						
				);
				*/
			}
		});	
		player.currentTimeProperty().addListener(new ChangeListener<Duration>(){
			@Override
			public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration current){
				slider.setValue(current.toSeconds());
			};
		});
		slider.setOnMouseDragged(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent){
				player.seek(Duration.seconds(slider.getValue()));
			}
		});
		slider.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent){
				player.seek(Duration.seconds(slider.getValue()));
			}
		});
	}
	//Play control
	public void control (Group root) {

	    Button playButton=new Button("||");
	    Button volumeButton=new Button("Volume On");
	    playButton.setPrefWidth(30);
        playButton.setBackground(Background.EMPTY);
		vbox.getChildren().addAll(playButton, volumeButton);
		
        volumeButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
            public void handle(MouseEvent e){
        		if(player.getVolume()==0.0){
        			player.setVolume(1.0);
        			volumeButton.setText("Volume Off");
        		}
        		else {
        			player.setVolume(0.0);
        			volumeButton.setText("Volume On");
        		}
            }
        });

        playButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
            public void handle(MouseEvent e){
                Status status=player.getStatus();
                if(status==Status.PLAYING){
                    if(player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())){
                         player.seek(player.getStartTime());
                         player.play();
                    }
                    else{
                        player.pause();
                        playButton.setText(">");
                    }
                }
	            if(status==Status.PAUSED ||status==Status.HALTED||status==Status.STOPPED){
	                player.play();
	                playButton.setText("||");
	            }

            }
        });
    }	
}//Class
