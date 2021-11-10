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
    @FXML
    private Slider volumeSlider;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        musicPlayer = new MusicPlayer("resources" + p + "rickroll.mp3");
        mp = musicPlayer.getMediaPlayer();
        uploadText.setEditable(false);
        //playlistOneSong();
        //playlistTwoSongs();
        
        volumeSlider.setValue(mp.getVolume() *100);
        //mp.volumeProperty().bindBidirectional(volumeController.valueProperty());
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                mp.setVolume(volumeSlider.getValue() /100);
                  }
           
       });
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
    
    
    public void playlistOneSong() {
        Playlist p = new Playlist("TestPlaylist");
        Song s1 = new Song(new File("resources/Songs/Test+123.mp3"), "Test", "123", "abc");
        p.addSong(s1);
        p.updateTextFile();
    }
   
    
    public void playlistTwoSongs() 
    {
        Song s1 = new Song(new File("resources/Songs/Test+123.mp3"), "Test", "123", "abc");
        Song s2 = new Song(new File("resources/Songs/Hello+World.mp3"), "Hello", "World", "Java");
        Playlist p = new Playlist("TestPlaylist");
        p.addSong(s1);
        p.addSong(s2);
        p.updateTextFile();
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