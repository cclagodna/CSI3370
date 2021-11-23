package com.Sublight.Sounds;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    public MusicPlayer musicPlayer; // our musicplayer that's used throughout the program
    
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
    private TextField albumArtText;
    @FXML
    private Label uploadMP3Label;
    
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        musicPlayer = new MusicPlayer();
        uploadText.setEditable(false); // making it so we can't edit mp3file textfield
        albumArtText.setEditable(false); // making it so we can't edit albumartFile textfield
        allPlaylists = Helpers.onStartLoadPlaylists(); // load the playlists at the beginning of the program
        playlistViewInitializer(); // load the playlist ListView at beginning of the program
    }    

    // this is the function for the play button
    @FXML
    private void btnPlayClicked(ActionEvent event) 
    {
       // if the user has selected a song and playlist
       if ((selectedSong != null) && (selectedPlaylist != null)) 
       {
            //if the selected song and playlist is not equal to the music players current song and playlist
            if (!musicPlayer.getCurrentSong().equals(selectedSong) &&
            !musicPlayer.getCurrentPlaylist().equals(selectedPlaylist)) 
            {
                musicPlayer.getMediaPlayer().stop();
                musicPlayer = musicPlayer.changeSong(selectedSong, selectedPlaylist);
            }
            // if the selected song is not the music players current song, but in the same playlist
            else if (!musicPlayer.getCurrentSong().equals(selectedSong) &&
                   musicPlayer.getCurrentPlaylist().equals(selectedPlaylist)) 
            {
                musicPlayer.getMediaPlayer().stop();
                musicPlayer = musicPlayer.changeSong(selectedSong, selectedPlaylist);
            }
            // if the song is the same but in a different playlist
            else if (musicPlayer.getCurrentSong().equals(selectedSong) &&
                     !musicPlayer.getCurrentPlaylist().equals(selectedPlaylist)) 
            {
                //selectedPlaylist = musicPlayer.getCurrentPlaylist();
                return; // we want to make sure it doesn't play the song over again
            }
       }
       musicPlayer.getMediaPlayer().play();
       // show any album art and metadata associated with the song chosen
       if (selectedSong != null) 
       {
           System.out.println("Here 1 Song Info");
           songInfoInitializer(selectedSong);
           albumArtInitializer(selectedSong);
       } 
       else 
       {
           songInfoInitializer(musicPlayer.getCurrentSong());
           albumArtInitializer(musicPlayer.getCurrentSong());
       }
    }
    
    // this is the function for the pause button
    @FXML
    private void btnPauseClicked(ActionEvent event) {
        musicPlayer.getMediaPlayer().pause();
    }

    // this is the function for the stop button
    @FXML
    private void btnStopClicked(ActionEvent event) {
        musicPlayer.getMediaPlayer().stop();
    }
    
    // this is the function for the skip button, skips to next song in current playlist
    @FXML
    void btnSkipClicked(ActionEvent event) 
    {
        if (musicPlayer != null) 
        {
            musicPlayer.getMediaPlayer().stop();
            musicPlayer = musicPlayer.skipSong();
            selectedSong = musicPlayer.getCurrentSong();
            selectedPlaylist = musicPlayer.getCurrentPlaylist();
            // outputs any errors with the mp3 files
            musicPlayer.getMediaPlayer().setOnError(() -> System.out.println("Error : " + musicPlayer.getMediaPlayer().getError().toString()));
        }
    }
    
    // this is the function for the select mp3 button
    @FXML
    private void btnSelectMP3Clicked(ActionEvent event) {
        File f = Helpers.mp3FileChooser();
        if (f != null) {
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
        String sName = songNameText.getText();
        String artistName = artistNameText.getText();
        String albumName = albumNameText.getText();
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
                musicPlayer.setCurrentPlaylist(selectedPlaylist);
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
