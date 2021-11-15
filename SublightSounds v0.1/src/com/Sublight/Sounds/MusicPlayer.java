/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/*
This is a helper class that allows us to make an object
that allows us to change songs and such in our MainScreenController class
*/
public class MusicPlayer {
    
    //OS's have different file separators, this gets the one for users device
    public final String p = System.getProperty("file.separator");
    //Path to the song folder, could be removed?
    private final String pathToSongFolder = "resources" + p + "Songs";
    //File object of the song folder, could be reomved?
    private final File songFolder = new File(pathToSongFolder);
    //ArrayList of songs in song folder, could be removed?
    ArrayList<File> files = new ArrayList(Arrays.asList(songFolder.listFiles()));
    //The current song file the MediaPlayer is playing
    //private Song currSong;
    private File currSong;
    //Current playlist the MediaPlayer is itrating over
    private Playlist currPlaylist;
    //String designating file path of currSong. Will only update in setSong()
    private String path;
    //Media object, must get passed a file location (as String)
    private Media media;
    //Create object that will actually play loaded song, must be passed Media object
    private MediaPlayer player;
    
    //CONSTRUCTORS #####################################################
    
    //If a file isn't specified, must retrieve a song to initialize with
    public MusicPlayer() {
        setDefaultSong();
        setPath();
        setMedia();
        setPlayer();
        player.setOnEndOfMedia(() -> nextSong());
    }
    
    public MusicPlayer(String pathname) {
        setSong(pathname);
        setPath();
        setMedia();
        setPlayer();
        player.setOnEndOfMedia(() -> nextSong());
    }
    
    //CONSTRUCTORS #####################################################
    //FUNCTIONS ########################################################
    
    //Iterates music player to next song, or loops to first song in case of being at end of list
    //TODO: Should also be able to choose songs based on current playlist
    public void nextSong() {
        //If song folder has current song (should always be true)
        //TODO: Needs to have option to check current playlist
        if (files.contains(getSong())) {
            int i = files.indexOf(getSong());
            //If there are more songs in folder (or playlist), go to next song
            if (i + 1 < files.size()) {
                updateMusicPlayer(files.get(i+1));
            }
            //If current song is last song in list, go to first song in folder
            else {
                updateMusicPlayer(files.get(0));
            }
        }
        
    }
    
    //Loads a default song into MediaPlay
    //Used for when a file isn't specified at moment of construction
    public void setDefaultSong() {
        //Create ArrayList of all songs in SongFolder
        System.out.println(songFolder.listFiles());
        for (File f : files) {
            System.out.println(f.getName());
        }
        //If 'files' is not empty
        if (!files.isEmpty()) {
            //Set the song path to the first file in songFolder
            setSong(files.get(0));
        }
        //Print confirmation message to console
        System.out.printf("Successfully loaded song: %s%n", getSong().getName());
    }
    
    //When setting a new song, updates all respective fields (path, media, mediaPlayer, etc)
    //Only the current song can be updated directly, this function handles setting other fields
    public void updateMusicPlayer(File f) {
        this.currSong = f;
        setPath();
        setMedia();
        setPlayer();
        //Print confirmation message to console
        System.out.printf("Successfully loaded song: %s%n", getSong().getName());
        //Sets the song to PLAY
        //This may cause bugs; however, when going to the next song, the player is probably PLAYing
        //Should be fine... 
        getPlayer().play();
    }
    
    //FUNCTIONS ########################################################
    //GET AND SET ######################################################
        //These get and set methods could be organized better
    
    //Sets the player to a new song
    //Funnels into updateMusicPlayer() function
    private void setSong(String s) {
        //setSong(new File(s));
        this.currSong = new File(s);
    }
    
    private void setSong(File f) {
        //updateMusicPlayer(f);
        this.currSong = f;
    }
    
    public File getSong() {
        return currSong;
    }
    
    //Sets path to song
    //Can only be called in updateMusicPlayer()
    private void setPath() {
        this.path = getSong().toURI().toString();
    }
    
    // Returns path to media file
    public String getPath() {
        return path;
    }
    
    //Creates media object using new song file
    //Can call this method with a String, File, or Media object
    //Can only be called in updateMusicPlayer()
    private void setMedia(String s) {
        this.media = new Media(s);
    }
    
    private void setMedia(File f) {
        this.media = new Media(f.getName());
    }
    
    private void setMedia(Media newMedia) {
        this.media = newMedia;
    }
    
    private void setMedia() {
        this.media = new Media(path);
    }
    
    // Returns media object
    public Media getMedia() {
        return media;
    }
    
    // Returns mediaPlayer object
    public MediaPlayer getPlayer() {
        return player;
    }
    
    //Creates new MediaPlayer to be used with new Media
    //AUTOPLAYS SONGS WHEN PROGRAM LOADS, NEEDS TO BE FIXED
    private void setPlayer() {
        //System.out.println(newPlayer.getStatus());
        this.player = new MediaPlayer(getMedia());
        //This line allows the MediaPlayer to continue to next song in list
        //If this isn't specified, the 'play' button would need to be clicked after every song
        player.setOnEndOfMedia(() -> nextSong());
    }
    
    //GET AND SET ######################################################
    
}
