package com.Sublight.Sounds;

import java.io.File;
import java.io.FileNotFoundException;
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
    public MusicPlayer musicPlayer;
    public MusicPlayer oldPlayer;
    
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
        uploadText.setEditable(false);
        albumArtText.setEditable(false);
        allPlaylists = Helpers.onStartLoadPlaylists();
        playlistViewInitializer();
    }    

    // this is the function for the play button
    
    // TODO BUG FIX: Switch to a Song, Play, Select Other Song, then go Back to Playing Song, Press Play = : (
    @FXML
    private void btnPlayClicked(ActionEvent event) throws FileNotFoundException 
    {
       if (( selectedSong != null) && (selectedPlaylist != null)) 
       {
            if (!musicPlayer.getCurrentSong().equals(selectedSong) &&
            !musicPlayer.getCurrentPlaylist().equals(selectedPlaylist)) 
            {
                System.out.println("Here 1");
                musicPlayer.getMediaPlayer().stop();
                musicPlayer = musicPlayer.changeSong(selectedSong, selectedPlaylist);
            } 
            else if (!musicPlayer.getCurrentSong().equals(selectedSong) &&
                   musicPlayer.getCurrentPlaylist().equals(selectedPlaylist)) 
            {
                System.out.println("Here 2");
                System.out.println("Current Song: " + musicPlayer.getCurrentSong().getSongName());
                System.out.println("Selected Song: " + selectedSong.getSongName());
                System.out.println("Current Playlist: " + musicPlayer.getCurrentPlaylist().getName());
                System.out.println("Selected Playlist: " + selectedPlaylist.getName());
                musicPlayer.getMediaPlayer().stop();
                musicPlayer = musicPlayer.changeSong(selectedSong, selectedPlaylist);
            }
            else if (musicPlayer.getCurrentSong().equals(selectedSong) &&
                     !musicPlayer.getCurrentPlaylist().equals(selectedPlaylist)) 
            {
                System.out.println("Here 3");
                //selectedPlaylist = musicPlayer.getCurrentPlaylist();
                return; // we want to make sure it doesn't play the song over again
            }
       }
       musicPlayer.getMediaPlayer().play();
       if (selectedSong != null) 
       {
           System.out.println("Here 1 Song Info");
           songInfoInitializer(selectedSong);
           albumArtInitializer(selectedSong);
       } 
       else 
       {
           System.out.println("Here 2 Song Info");
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
    
    @FXML
    void btnSkipClicked(ActionEvent event) 
    {
        if (musicPlayer != null) 
        {
            musicPlayer.getMediaPlayer().stop();
            musicPlayer = musicPlayer.skipSong();
            selectedSong = musicPlayer.getCurrentSong();
            selectedPlaylist = musicPlayer.getCurrentPlaylist();
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
        //playlistView.getSelectionModel().selectedIndexProperty().removeListener(cl);
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
    
    public void songInfoInitializer(Song s) 
    {
        Text songName = new Text("Song Name: " + s.getSongName());
        Text artistName = new Text("Artist Name: " + s.getArtistName());
        Text albumName = new Text("Album Name: " + s.getAlbumName());
        songInfo = new TextFlow(songName, artistName, albumName);
        songInfo.setLineSpacing(2.0);
    }
    
    public void albumArtInitializer(Song s) throws FileNotFoundException 
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
