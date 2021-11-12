package com.Sublight.Sounds;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
    
    public ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();
    
    @FXML
    private TextField uploadText;
    @FXML
    private TextField songNameText;
    @FXML
    private TextField artistNameText;
    @FXML
    private TextField albumNameText;
    @FXML
    private Label uploadMP3Label;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        musicPlayer = new MusicPlayer("resources" + p + "rickroll.mp3");
        mp = musicPlayer.getMediaPlayer();
        uploadText.setEditable(false);
        //TestCases.playlistOneSong();
        //TestCases.playlistTwoSongs();
        //TestCases.loadPlaylistTest(allPlaylists);
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
    
    // this is the function for the select button
    @FXML
    private void btnSelectMP3Clicked(ActionEvent event) {
        FileChooser choose = new FileChooser();
        choose.setTitle("Uploading MP3");
        choose.getExtensionFilters().add(new ExtensionFilter("Audio Files", "*.mp3"));
        File f = choose.showOpenDialog(null);
        if (f != null) 
        {
            uploadText.setText(f.getAbsolutePath());
        }
    }
    
    // this is the function for the upload button
    @FXML
    void btnUploadClicked(ActionEvent event) 
    {
        File f = new File(uploadText.getText());
        String sName = songNameText.getText();
        String artistName = artistNameText.getText();
        String albumName = albumNameText.getText();
        
        Song s = new Song(f, sName, artistName, albumName);
        uploadMP3Label.setText(Helpers.uploadCheck(s));
    }
    
}
