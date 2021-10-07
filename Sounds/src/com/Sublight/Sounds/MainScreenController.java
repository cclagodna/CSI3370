package com.Sublight.Sounds;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.MediaPlayer;

/**
 * FXML Controller class
 *
 * @author rogervalade
 */
public class MainScreenController implements Initializable {
    
    /*
    This "p" String is meant to get the separator of the OS the user is using,
    making it so that accessing files isn't broken inbetween OS' since MacOS and UNIX
    use /, while WindowsOS use \.
    */
    public String p = System.getProperty("file.separator"); 
    public MusicPlayer musicPlayer;
    public MediaPlayer mp;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        musicPlayer = new MusicPlayer("resources" + p + "rickroll.mp3");
        mp = musicPlayer.getMediaPlayer();
    }    

    // this is the function for the play button
    @FXML
    private void btnPlayClicked(ActionEvent event) {
       mp.play();
    }
    
    // this is the function for the pause button
    @FXML
    private void btnPauseClicked(ActionEvent event) {
        mp.pause();
    }

    // this is the function for the stop button
    @FXML
    private void btnStopClicked(ActionEvent event) {
        mp.stop();
    }
    
}
