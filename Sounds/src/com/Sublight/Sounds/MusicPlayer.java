package com.Sublight.Sounds;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author rogervalade
 */

/*
This is a helper class that allows us to make an object
that allows us to change songs and such in our MainScreenController class
*/
public class MusicPlayer {
    
    private String path; // the string literal of the path of our MP3 file
    private Media media; // creating Media object for our path
    private MediaPlayer player; // creating mediaPlayer for interacting w/ songs
    private Song currentSong; // current song the MusicPlayer is playing
    private Playlist currentPlaylist; // current playlist the MusicPlayer is using
    
    // constructor to be called in MainScreenController on startup
    public MusicPlayer() 
    {
        Playlist p = Playlist.allSongs();
        ArrayList<Song> list = p.getPlaylist();
        if (!list.isEmpty()) // get the first song in the songs folder
        {
            this.path = list.get(0).getmp3Location().getAbsolutePath();
            this.currentSong = list.get(0);
            this.currentPlaylist = p;
        } else { // else use the default song
            this.path = new File("resources" + System.getProperty("file.separator") + "rickroll.mp3").getAbsolutePath();
        }
        this.media = new Media(new File(this.path).toURI().toString());
        this.player = new MediaPlayer(media);
    }
    
    // MusicPlayer constructor with Song, Playlist
    public MusicPlayer(Song s, Playlist p) 
    {
        this.path = s.getmp3Location().getPath();
        this.media = new Media(new File(this.path).toURI().toString());
        this.player = new MediaPlayer(media);
        this.currentSong = s;
        this.currentPlaylist = p;
    }
    
    // path getter
    public String getPath() {
        return path;
    }
    
    // path setter
    public void setPath(String newPath) {
        this.path = newPath;
    }
    
    // media getter
    public Media getMedia() {
        return media;
    }
    
    // media setter
    public void setMedia(Media newMedia) {
        this.media = newMedia;
    }
    
    // mediaPlayer getter
    public MediaPlayer getMediaPlayer() {
        return player;
    }
    
    // mediaPlayer setter
    public void setMediaPlayer(MediaPlayer newPlayer) {
        this.player = newPlayer;
    }

    // currentSong getter
    public Song getCurrentSong() {
        return currentSong;
    }
    
    // currentSong setter
    public void setCurrentSong(Song s) {
        this.currentSong = s;
    }
    
    // currentPlaylist getter
    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
    
    // currentPlaylist setter
    public void setCurrentPlaylist(Playlist p) {
        this.currentPlaylist = p;
    }
    
    // changing the song to a specified song & playlist
    public MusicPlayer changeSong(Song s, Playlist p) 
    {
        if (p.hasSong(s)) {
            return new MusicPlayer(s, p);
        }
        return null;
    }
    
    // skipping to next song in a given playlist and returning a new MusicPlayer that has updated information;
    public MusicPlayer skipSong() 
    {
        MusicPlayer send = null;
        Song newSong;
        ArrayList<Song> songs = currentPlaylist.getPlaylist();
        if (currentPlaylist.hasSong(currentSong)) // if the playlist has the specified song
        {
            int index = songs.indexOf(currentSong); // get the index of song passed in
            int size = songs.size(); // get the size of the playlist
            if ((index + 1) == size) // if it's the last song of the playlist, go to the first song
            {
                newSong = songs.get(0);
                send = new MusicPlayer(newSong, currentPlaylist);
            } else 
            { // else get the next song of the playlist
                newSong = songs.get(index + 1);
                send = new MusicPlayer(newSong, currentPlaylist);
            }
        }
        return send;
    }
    
}
