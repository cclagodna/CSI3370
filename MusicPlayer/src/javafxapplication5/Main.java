package javafxapplication5;

import java.io.File;
import java.io.IOException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Carl
 * Last edited: 2021/09/27
 */
public class Main extends Application {
    
    //Must be static to allow manipultaion in fxml controller class
    static MediaPlayer mp;
    static String currSong;
    
    @Override
    public void start(Stage primaryStage) throws IOException{
        
        /*
        Can play a song, buttons start/stop
        Very limited in scope so far
        
        Problem: some mp3s won't play or are bugged. Those I [Carl] downloaded from youtube don't work
        */
        
        
        //Path of mp3 file. For now, please put all resources in 'resources' folder
        String path = "resources\\bensound-betterdays.mp3";
        //Find the file and convert it to a media object
        Media media = new Media(new File(path).toURI().toString());
        //Create a MediaPLayer object
        mp = new MediaPlayer(media);
        
        currSong = path;
        
        //Initialize stage with title
        primaryStage.setTitle("Playing: " + path);
        
        //Initialize root and scene
        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
        Scene scene = new Scene(root, 300, 150);
        
        //Set scene to stage then render it
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //Start
        launch(args);
    }
    
}
