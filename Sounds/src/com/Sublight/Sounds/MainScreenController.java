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
    
    public ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();
    public Playlist selectedPlaylist;
    public Song selectedSong;
    
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        musicPlayer = new MusicPlayer();
        uploadText.setEditable(false);
        albumArtText.setEditable(false);
        allPlaylists = Helpers.onStartLoadPlaylists();
        playlistViewInitializer();
    }    

    // this is the function for the play button
    @FXML
    private void btnPlayClicked(ActionEvent event) {
       musicPlayer.getMediaPlayer().play();
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
    
    @FXML
    void btnSkipClicked(ActionEvent event) 
    {
        if (musicPlayer != null) 
        {
            musicPlayer.getMediaPlayer().stop();
            musicPlayer = musicPlayer.skipSong();
            TestCases.checkMusicPlayer(musicPlayer);
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
        File albumArtFile = Helpers.checkForAlbumCover(sName, artistName);
        if (albumArtFile != null) {
            s = new Song(mp3File, sName, artistName, albumName, albumArtFile);
        } 
        else if (albumArtText.getText() != null) 
        {
            File art = new File(albumArtText.getText());
            s = new Song(mp3File, sName, artistName, albumName, art);
        } 
        else {
            s = new Song(mp3File, sName, artistName, albumName);
        }        
        uploadMP3Label.setText(Helpers.uploadCheck(s));
    }
    
    // not quite done yet, but is a start.
    public void playlistViewInitializer() 
    {
        ArrayList<String> playlistNames = new ArrayList<String>();
        for (Playlist p : allPlaylists) {
            playlistNames.add(p.getName());
        }
        playlistView.getItems().addAll(playlistNames);
        playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) 
            {
                selectedPlaylist = allPlaylists.get(playlistView.getSelectionModel().getSelectedIndex());
                musicPlayer.setCurrentPlaylist(selectedPlaylist);
                playlistViewLabel.setText("Selected Playlist: " + selectedPlaylist.getName()); 
                songViewInitializer();
            }
        });
    }
    
    // this is not quite done yet, but is a start.
    public void songViewInitializer() 
    {
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
                selectedSong = selectedPlaylist.getPlaylist().get(songView.getSelectionModel().getSelectedIndex());
                musicPlayer.setCurrentSong(selectedSong);
                songViewLabel.setText("Selected Song: " + selectedSong.getSongName());
                musicPlayer = musicPlayer.changeSong(selectedSong, selectedPlaylist);
            }
        });
    }
    
}
