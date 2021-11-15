/*
 *@author Sublight Development
 */
package com.Sublight.Sounds;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * FXML Controller class
 */
public class MainScreenController implements Initializable {
    
    /*
    This "p" String is meant to get the separator of the OS the user is using,
    making it so that accessing files isn't broken inbetween OS' since MacOS and UNIX
    use /, while WindowsOS use \.
    */
    public String p = System.getProperty("file.separator"); 
    public MusicPlayer mp;
    
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
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button btnSkip;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize MediaPlayer object with a base song
        //musicPlayer = new MusicPlayer("resources" + p + "rickroll.mp3");
        mp = new MusicPlayer();
        //mp = musicPlayer.getMediaPlayer();
        
        uploadText.setEditable(false);
        
        //Create VolumeSlider object, allowing the user to control the output volume of the player
        volumeSlider.setValue(mp.getPlayer().getVolume() *100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                mp.getPlayer().setVolume(volumeSlider.getValue() /100);
                  } 
       });
        
    }    

    
    //TODO Add a button that runs this code
    @FXML
    private void btnSkipSong(ActionEvent event) {
        //Stops current song, then plays the next one
        btnStopClicked(event);
        mp.nextSong();
    }
    
    // this is the function for the play button
    @FXML
    private void btnPlayClicked(ActionEvent event) {
       mp.getPlayer().play();
    }
    
    // this is the function for the pause button
    @FXML
    private void btnPauseClicked(ActionEvent event) {
        mp.getPlayer().pause();
    }

    // this is the function for the stop button
    @FXML
    private void btnStopClicked(ActionEvent event) {
        mp.getPlayer().stop();
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
    
    // Result: Broken. loadPlaylists() needs to be fixed (Paths.get(filepath))
    public void loadPlaylistTest() 
    {
        allPlaylists = Playlist.loadPlaylists();
        for (Playlist p : allPlaylists) 
        {
            System.out.println("Playlist Name: " + p.getName());
            ArrayList<Song> songs = p.getPlaylist();
            for (Song s: songs) 
            {
                System.out.println("Song Name: " + s.getSongName());
                System.out.println("Artist Name: " + s.getArtistName());
                System.out.println("Album Name: " + s.getAlbumName());
                System.out.println("File Location: " + s.getmp3Location());
            }
        }
    }
    
}