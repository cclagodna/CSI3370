package javafxapplication5;

import javafx.scene.media.MediaView;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Carl
 * Last edited: 2021/09/27
 */
public class JavaFXApplication5 extends Application {
    
    @Override
    public void start(Stage primaryStage){
        
        //Path of mp3 file. For now, please put all resources in 'resources' folder
        String path = "resources\\bensound-betterdays.mp3";
        //Find the file and convert it to a media object
        //Media media = new Media(new File(path).toURI().toString());
        Media media = new Media(new File(path).toURI().toString());
        //Create a MediaPLayer object
        MediaPlayer mp = new MediaPlayer(media);
        //Create a MediaView object, probably not needed at the moment
        MediaView mediaView = new MediaView(mp);
        
        //Initialize stage with title
        primaryStage.setTitle("Playing: " + path);
        //Create new group and insert mediaView into it
        Group root = new Group(mediaView);
        //Create new scene with group and sizes
        Scene scene = new Scene(root, 500, 200);
        //Render stage
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Play mediaPlayer
        mp.setAutoPlay(true);
        System.out.print(media.durationProperty());
    }

    public static void main(String[] args) {
        //Start
        launch(args);
    }
    
}
