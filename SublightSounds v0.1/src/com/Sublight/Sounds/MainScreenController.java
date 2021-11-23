
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
import com.sun.javafx.property.adapter.PropertyDescriptor;
import com.sun.javafx.property.adapter.PropertyDescriptor.Listener;
import java.io.File;
import java.io.IOException;
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
    private Label totalTimeDisplay;
    @FXML
    private Label currentTimeDisplay;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider timeSlider;
    @FXML
    private Button btnSkip;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize MediaPlayer object
        mp = new MusicPlayer();
        
        uploadText.setEditable(false);
        
        initializeVolumeSlider();
        
        initializeTimeSlider();
        
        updateTimeValues();
    }    

    //Create VolumeSlider object, allowing the user to control the output volume of the player
    private void initializeVolumeSlider() {
        volumeSlider.setValue(mp.getPlayer().getVolume() *100);
        volumeSlider.valueProperty().addListener((Observable o) -> {
            mp.getPlayer().setVolume(volumeSlider.getValue() /100);
        });
    }
    
    private void initializeTimeSlider() {
        
        //set up the timeSlider so it has relevant and current values
        
        mp.getPlayer().currentTimeProperty().addListener((Observable o) -> {
            updateTimeValues();
        });
        // get duration of current song so it can be displayed
        mp.getPlayer().setOnReady(() -> {
            updateTimeValues();
        });
        
        //make scrubbing possible for timeSlider
        timeSlider.valueProperty().addListener((Observable o) -> {
            if(timeSlider.isValueChanging()){
                mp.getPlayer().seek(getDuration().multiply(timeSlider.getValue()/100));
                updateTimeValues();
            }
        });
        
        mp.getPlayer().currentTimeProperty().addListener((Observable o) -> {
            updateTimeValues();
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
    void btnUploadClicked(ActionEvent event) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
        //Get file uploaded thru btnSelectMP3Clicked
        File f = new File(uploadText.getText());
        //Get information in text boxes inputted by user
        String sName = songNameText.getText();
        String artistName = artistNameText.getText();
        String albumName = albumNameText.getText();
        
        //Create new mp3 and tag
        Mp3File mp3 = new Mp3File(f);
        ID3v2 tag;
        
        //Remove id3v1 and Custom tags, as we want to tag with just id3v2 (3v2 supports images)
        //Unsure why this is, creator of mp3agic says this may prevent errors
        //May be removed if no errors arise, or undesired behavior arises
        if ( mp3.hasId3v1Tag() ) mp3.removeId3v1Tag();
        if ( mp3.hasCustomTag() ) mp3.removeCustomTag();
        //If mp3 already has an id3v2 tag, retrieve it. If not, make a new id3v2 instance
        //Creating a new id3v2 tag must call class ID3v24Tag, unsure why
        tag = ( mp3.hasId3v2Tag() ) ? mp3.getId3v2Tag() : new ID3v24Tag();
        
        //Put user inputted parameters into mp3 id3v2 tag (ie metadata)
        tag.setTitle(sName);
        tag.setArtist(artistName);
        tag.setAlbum(albumName);
        //Set id3v2 tag to mp3
        mp3.setId3v2Tag(tag);
        
        //Get path of original file
        String path = f.getAbsolutePath();
        //Create placeholder pathname
        String tempPath = path + ".placeholder";
        
        //Save mp3 using placeholder path
        //Mp3agic can't overwrite into a path/ file with same name
        //Must use File class operations
        mp3.save(tempPath);
        
        //Create new File using tempory path name
        File newf = new File(tempPath);
        //Delete original file
        f.delete();
        //Rename new file with updated id3v2 tags to old file name (essentially replacing it)
        newf.renameTo(f);
        //Actually upload the file to default directory (Resources/Songs)
        Helpers.uploadFile(f);
        //Set label to status of upload
        uploadMP3Label.setText(Helpers.getUploadStatus());
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
    
    
    public void getListener() {
        mp.getPlayer().currentTimeProperty().addListener((Observable o) -> {
            this.updateTimeValues();
        });
    }
}