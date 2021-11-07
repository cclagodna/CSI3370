package com.Sublight.Sounds;

import java.io.File;
import java.net.URL;
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
    public String uploadSongPath = "resources" + p + "Songs" + p;
    public MusicPlayer musicPlayer;
    public MediaPlayer mp;
    
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
        
        if (f.exists()) 
        {
            if (sName != null) 
            {
                if (artistName != null) 
                {
                    if (albumName != null) 
                    {
                        String filepath = uploadSongPath + sName + ".mp3";
                        File newFileLoc = new File(filepath);
                        Song s = new Song(newFileLoc, sName, artistName, albumName);
                        Helpers.upload(f, filepath);
                        s.createJSONFile(s);
                        uploadMP3Label.setText("MP3 Uploaded Successfully!");
                    } else {
                        uploadMP3Label.setText("No Album specified!");
                    }
                } else {
                    uploadMP3Label.setText("No Artist specified!");
                }
            } else {
                uploadMP3Label.setText("No Song Name specified!");
            }
        } else {
            uploadMP3Label.setText("No MP3 File to upload!");
        }
    }
    
}
