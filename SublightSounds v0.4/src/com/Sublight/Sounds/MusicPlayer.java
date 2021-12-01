/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/*
This is a helper class that allows us to make an object
that allows us to change songs and such in our MainScreenController class
*/
public class MusicPlayer {
    
    //OS's have different file separators, this gets the one for users device
    public final String p = System.getProperty("file.separator");
    //Path to the song folder, could be removed?
    private final String pathToSongJSONFolder = "Resources" + p + "SongJSONs";
    //File object of the song folder, could be removed?
    private final File songFolder = new File(pathToSongJSONFolder);
    //List of all song JSONs, will be iterated over to change currSong
    ArrayList<File> jsonFiles = new ArrayList(Arrays.asList(songFolder.listFiles()));
    //File of current Json holding information about current song
    //The Json will hold all metadata info and path to mp3
    private File currJson;
    //Song class loaded with information contained in JSON
    private Song currSong;
    //Get file holding all playlists
    private final File playlistFolder = new File("Resources" + p + "Playlists");
    //ArrayList of playlists in playlist folder, could be removed?
    ArrayList<File> playlists = new ArrayList(Arrays.asList(playlistFolder.listFiles()));
    //Current playlist the MediaPlayer is iterating over
    private Playlist currPlaylist;
    //Media object, must get passed a file location (as String)
    private Media media;
    //Create object that will actually play loaded song, must be passed Media object
    private MediaPlayer player;
    // current playlist the MusicPlayer is using
    private Playlist currentPlaylist;
    
    //CONSTRUCTORS #####################################################
    
    //If a file isn't specified, must retrieve a song to initialize with
    public MusicPlayer() throws FileNotFoundException {
        setDefaultSong();
        // So mediaPlayer doesn't immediately play when program opened
        player.pause();
    }
    
    public MusicPlayer(String pathname) throws FileNotFoundException {
        this.updateMusicPlayer(new File(pathname));
    }
    
    public MusicPlayer(File file) throws FileNotFoundException {
        updateMusicPlayer(file);
        // So mediaPlayer doesn't immediately play when program opened
        player.pause();
    }
    
    //CONSTRUCTORS #####################################################
    //FUNCTIONS ########################################################
    
    //Iterates music player to next song, or loops to first song in case of being at end of list
    //TODO: Should also be able to choose songs based on current playlist
    public void nextSong() throws FileNotFoundException {
        //If json folder has current song json(should always be true)
        //This if statement is mostly for playlists, whenever they'll be implemented
        //TODO: Needs to have option to check current playlist
        if (jsonFiles.contains(currJson)) {
            int i = jsonFiles.indexOf(currJson);

            //If there are more songs in folder (or playlist), go to next song
            if (i + 1 < jsonFiles.size()) {
                updateMusicPlayer(jsonFiles.get(i+1));
            }
            //If current song is last song in list, go to first song in folder
            else {
                updateMusicPlayer(jsonFiles.get(0));
            }
        }
    }
    
    //Iterates music player to previous song, or loops to last song in case of being at beginning of list
    //TODO: Should also be able to choose songs based on current playlist
    public void prevSong() throws FileNotFoundException {
        //If json folder has current song json(should always be true)
        //This if statement is mostly for playlists, whenever they'll be implemented
        //TODO: Needs to have option to check current playlist
        if (jsonFiles.contains(currJson)) {
            int i = jsonFiles.indexOf(currJson);
            //If there are more songs in folder (or playlist), go to prev song
            if (i - 1 >= 0) {
                updateMusicPlayer(jsonFiles.get(i-1));
            }
            //If current song is first song in list, go to last song in folder
            else {
                updateMusicPlayer(jsonFiles.get(jsonFiles.size() - 1));
            }
        }
    }
    
    //Loads a default song into MediaPlay
    //Used for when a file isn't specified at moment of construction
    private void setDefaultSong() throws FileNotFoundException {
        if(jsonFiles.isEmpty()) {
            System.out.println("No songs found!");
            return;
        }
        //Create ArrayList of all songs in SongFolder
        for (File j : jsonFiles) {
            System.out.println(j.getName());
        }
        //Update musicPlayer to have the new song
        updateMusicPlayer(jsonFiles.get(0));
    }
    
    //When setting a new song, updates all respective fields (path, media, mediaPlayer, etc)
    //Only the current song can be updated directly, this function handles setting other fields
    private void updateMusicPlayer(File f) throws FileNotFoundException {
        setSong(f);
        //New media object must be set before a new mediaPlayer
        setMedia();
        setPlayer();
        //Print confirmation message to console
        System.out.printf("Successfully loaded song: %s%n", currSong.getmp3Location());
        //Sets the musicPlayer to PLAY
        //This may cause unintentional behavior
        //However, since the player is likely playing when going to the next song, should be fine...
        getPlayer().play();
    }
    
    public void updateMusicPlayer(Song s) throws FileNotFoundException {
        setSong(s);
        //New media object must be set before a new mediaPlayer
        setMedia();
        setPlayer();
        //Print confirmation message to console
        System.out.printf("Successfully loaded song: %s%n", currSong.getmp3Location());
        //Sets the musicPlayer to PLAY
        //This may cause unintentional behavior
        //However, since the player is likely playing when going to the next song, should be fine...
        getPlayer().play();
    }
    
    //FUNCTIONS ########################################################
    //GET AND SET ######################################################
        //These get and set methods could be organized better (alphabetical?)
    
    //Sets a new song into the mediaPlayer
    //Funnels into updateMusicPlayer() function
    private void setSong(String s) throws FileNotFoundException {
        //Extract json into Song class object
        //Json holds metadata of song, and the mp3 path
        Gson gson = new Gson();
        this.currJson = new File(s);
        this.currSong = gson.fromJson(new FileReader(s), Song.class);
    }
    
    private void setSong(File f) throws FileNotFoundException {
        setSong(f.getPath());
    }
    
    private void setSong(Song s) throws FileNotFoundException {
        setSong(s.getmp3Location());
    }
    
    public Song getSong() {
        return currSong;
    }
    
    //Creates media object using new song file
    //Can call this method with a String, File, or Media object
    //Can only be called in updateMusicPlayer()
    private void setMedia() {
        this.media = new Media(currSong.getmp3Location().toURI().toString());
    }
    
    // Returns media object
    public Media getMedia() {
        return media;
    }
    
    // Returns mediaPlayer object
    public MediaPlayer getPlayer() {
        return player;
    }
    
    //Alternate name for getPlayer()
    public MediaPlayer getMediaPlayer() {
        return getPlayer();
    }
    
    //Creates new MediaPlayer to be used with new Media
    //If the mediaPlayer has any special properties, must be assigned here
    public void setPlayer() {
        
        MediaPlayer newPlayer = new MediaPlayer(getMedia());
        //Get volume property of previous media player
        try {
            newPlayer.setVolume(this.player.getVolume());
        } catch(Exception e){
            //If player doesn't have a volume (such as during program initialization), set value to 1 (max)
            newPlayer.setVolume(1.0);
        }
        
        //Get onEndOfMedia property of previous mediaPlayer
        //This property should run code allowing player to go to next song
        try {
            newPlayer.setOnEndOfMedia(this.player.getOnEndOfMedia());
        } catch (Exception e) {
            //This line allows the MediaPlayer to continue to next song in list
            //If this isn't specified, the 'play' button would need to be clicked after every song
            newPlayer.setOnEndOfMedia(() -> {
                try {
                    nextSong();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        
        
        //Get onReady property of previous mediaPlayer
        //This property will load the song's length to the main screen
        try {
            newPlayer.setOnReady(this.player.getOnReady());
        } catch (Exception e) {
            //...
        }
        
        
        //Set mediaPlayer to newly defined player
        setPlayer(newPlayer);
    }
    
    public void setPlayer(MediaPlayer mp) {
        this.player = mp;
    }
    
    // currentPlaylist getter
    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
    
    // currentPlaylist setter
    public void setCurrentPlaylist(Playlist p) {
        this.currentPlaylist = p;
    }
    
    
    //GET AND SET ######################################################
}
