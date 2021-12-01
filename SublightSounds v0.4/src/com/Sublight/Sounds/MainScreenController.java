
/*
 *@author Sublight Development
 */
package com.Sublight.Sounds;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

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
    public static MusicPlayer mp;
    private final File defaultAlbumArtPath = new File("Resources" + p + "AlbumArt" + p + "default.png");
    public final Image defaultAlbumArt = new Image(defaultAlbumArtPath.getAbsolutePath());
    
    public ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>(); // an arraylist of all playlists in our program
    public Playlist selectedPlaylist; // selected playlist based off of what's selected in playlist ListView
    public Song selectedSong; // selected song based off of what's selected in song ListView
    
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
    private Label totalTimeDisplay;
    @FXML
    private Label currentTimeDisplay;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider timeSlider;
    @FXML
    private Button btnPlayPause;
    @FXML
    private Button btnSkipForward;
    @FXML
    private Button btnSkipBackward;
    @FXML
    private ImageView volumeIcon;
    @FXML
    private TextField albumArtText;
    @FXML
    private ListView<String> playlistView;
    @FXML
    private Label playlistViewLabel;
    @FXML
    private ListView<String> songView;
    @FXML
    private Label songViewLabel;
    @FXML
    private TextFlow songInfo;
    @FXML
    private ImageView albumArtView;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize MediaPlayer object
        try {
            mp = new MusicPlayer();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //Add these functions to the music palyer
        mp.getPlayer().setOnReady(() -> {
            //This will allow the time slider to go along with each song, even after making a new mediaPlayer
            //Without this, the time bar would only move along with the first song
            //This is a very hack-y way of going abou this, but a better solution counld not be found
            initializeTimeSlider();
            updateTimeValues();
            updateAlbumArt();
        });
        
        uploadText.setEditable(false);
        
        initializeVolumeSlider();
        
        initializeTimeSlider();
        
        updateAlbumArt();
        
        updateTimeValues();
    }    

    //Create VolumeSlider object, allowing the user to control the output volume of the player
    private void initializeVolumeSlider() {
        volumeSlider.setValue(mp.getPlayer().getVolume() * 100);
        volumeSlider.valueProperty().addListener((Observable o) -> {
            mp.getPlayer().setVolume(volumeSlider.getValue() /100);
            
            //Change volume icon based on value of volumeSlider
            //Three levels: loud, quiet, mute
            //Remove all icons, then add the specefic one based on volume
            volumeIcon.getStyleClass().removeAll("volume-bar-mute", "volume-bar-quiet", "volume-bar-loud");
            if (volumeSlider.getValue() <= 0) volumeIcon.getStyleClass().add("volume-bar-mute");
            else if (volumeSlider.getValue() <= 50) volumeIcon.getStyleClass().add("volume-bar-quiet");
            else volumeIcon.getStyleClass().add("volume-bar-loud");
        });
    }
    
    private void initializeTimeSlider() {
        
        //set up the timeSlider so it has relevant and current values
        mp.getPlayer().currentTimeProperty().addListener((Observable o) -> {
            updateTimeValues();
        });
        
        
        
        //make scrubbing possible for timeSlider
        timeSlider.valueProperty().addListener((Observable o) -> {
            if(timeSlider.isValueChanging()){
                mp.getPlayer().seek(getDuration().multiply(timeSlider.getValue()/100));
            }
        });
        
    }
    
    @FXML
    private void updateAlbumArt() {
        try {
            albumArtView.setImage(new Image(mp.getSong().getAlbumArt().getAbsolutePath()));
        }
        catch (Exception e) {
            System.out.println("Album art not found! Displaying default.");
            albumArtView.setImage(defaultAlbumArt);
        }
        
        
    }
    
    //TODO Add a button that runs this code
    @FXML
    private void btnSkipForwardClicked(ActionEvent event) throws FileNotFoundException {
        //Stops current song, then plays the next one
        btnStopClicked(event);
        mp.nextSong();
    }
    
    @FXML
    private void btnSkipBackwardClicked(ActionEvent event) throws FileNotFoundException {
        //Stops current song, then plays the next one
        btnStopClicked(event);
        mp.prevSong();
    }
    
    //Play / pause button functionality
    @FXML
    private void btnPlayPauseClicked(ActionEvent event) {
        //If song is already playing, pause it
        if (mp.getPlayer().getStatus() == Status.PLAYING) {
            mp.getPlayer().pause();
            //Change style to show a play button while song is paused
            btnPlayPause.getStyleClass().remove("pause-button");
            btnPlayPause.getStyleClass().add("play-button");
        }
        //If song is paused, play it
        else {
            mp.getPlayer().play();
            //Change style to show a pause button while song is playing
            btnPlayPause.getStyleClass().remove("play-button");
            btnPlayPause.getStyleClass().add("pause-button");
        }
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
    
    // function for select album art button
    @FXML
    void btnSelectAlbumArtClicked(ActionEvent event) 
    {
        File f = Helpers.imageFileChooser();
        if (f != null) {
            albumArtText.setText(f.getAbsolutePath());
        }
    }
    
    // this is the function for the upload button
    @FXML
    void btnUploadClicked(ActionEvent event) 
    {
        File mp3File = new File(uploadText.getText());
        String sName = songNameText.getText().replace(" ", "_");
        String artistName = artistNameText.getText().replace(" ", "_");
        String albumName = albumNameText.getText().replace(" ", "_");
        Song s;
        // this checks if there's already album art found for the specified artist & album
        File albumArtFile = Helpers.checkForAlbumCover(sName, artistName);
        if (albumArtFile != null) {
            s = new Song(mp3File, sName, artistName, albumName, albumArtFile);
        } 
        else if (albumArtText.getText() != null) // else if there's no album art found in program
        {
            File art = new File(albumArtText.getText());
            s = new Song(mp3File, sName, artistName, albumName, art);
        } 
        else { // else there's no album art provided
            s = new Song(mp3File, sName, artistName, albumName);
        }        
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
    
    //update the timeLabels and timeSlider to current song time location values
    public void updateTimeValues(){
        updateTimeValues(mp.getPlayer().getCurrentTime(), mp.getMedia().getDuration());
    }
    
    public void updateTimeValues(Duration curr, Duration total) {
        if (curr == null || curr.isUnknown() || total == null || total.isUnknown()) return;
        totalTimeDisplay.setText(formatDuration(total));
        currentTimeDisplay.setText(formatDuration(curr));
        timeSlider.setValue(curr.divide(total).toMillis() * 100);
    }
    
    //format duration into readable time for songs
    public String formatDuration(Duration d){
        double seconds = d.toSeconds();
        int absSeconds = (int)(seconds);
        String returner = String.format("%d:%02d",
                (absSeconds / 60), (absSeconds % 60));
        return returner;
    }
    
    public Duration getDuration() {
        return mp.getMedia().getDuration();
    }
    
    public Duration getCurrentTime() {
        return mp.getPlayer().getCurrentTime();
    }
    
    // initializes the playlistView ListView with all playlists stored in the program.
    public void playlistViewInitializer() 
    {
        ArrayList<String> playlistNames = new ArrayList<String>();
        for (Playlist p : allPlaylists) {
            playlistNames.add(p.getName());
        }
        playlistView.getItems().addAll(playlistNames); // add playlist names to playlist ListView
        // adding a listener that updates selectedPlaylist when selection is changed
        playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) 
            {
                selectedPlaylist = allPlaylists.get(playlistView.getSelectionModel().getSelectedIndex());
                mp.setCurrentPlaylist(selectedPlaylist);
                playlistViewLabel.setText("Selected Playlist: " + selectedPlaylist.getName());
                // update the songs ListView based on the given playlist
                songViewInitializer();
            }
        });
    }
    
    // this shows all the songs in the selected Playlist.
    public void songViewInitializer() 
    {
        // clear the songView ListView if there's already stuff in there
        if (songView.getItems() != null) {
            songView.getItems().clear();
        }
        ArrayList<String> songNames = new ArrayList<String>();
        for (Song s: selectedPlaylist.getPlaylist()) {
            songNames.add(s.getSongName());
        }
        songView.getItems().addAll(songNames);
        songView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) 
            {
                // this if statement is here in-case we select something from the playlistView
                if (songView.getSelectionModel().getSelectedIndex() >= 0) 
                {
                    selectedSong = selectedPlaylist.getPlaylist().get(songView.getSelectionModel().getSelectedIndex());
                    songViewLabel.setText("Selected Song: " + selectedSong.getSongName());
                } else { // this is to accomodate when switching back to playlist ListView
                    selectedSong = null;
                    songViewLabel.setText("Selected Song: N/A");
                }
            }
        });
    }
    
    // this should put the songs Metadata into a TextFlow object
    public void songInfoInitializer(Song s) 
    {
        Text songName = new Text("Song Name: " + s.getSongName());
        Text artistName = new Text("Artist Name: " + s.getArtistName());
        Text albumName = new Text("Album Name: " + s.getAlbumName());
        songInfo = new TextFlow(songName, artistName, albumName);
        songInfo.setLineSpacing(2.0);
    }
    
    // this makes it so album art appears in an ImageView if there's album art associated with provided song
    public void albumArtInitializer(Song s)
    {
        if (s.getAlbumArt() != null) 
        {
            Image albumImage = s.albumArtToJFX();
            albumArtView.setImage(albumImage);
            albumArtView.setPreserveRatio(true);
        }
        else if ((albumArtView.getImage() != null) &&
                  !(s.getAlbumArt() != null)) 
        {
            albumArtView.setImage(null);
        }
    }
}