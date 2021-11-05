package com.Sublight.Sounds;

import java.util.ArrayList;

/**
 *
 * @author rogervalade
 */
public class Playlist 
{

    private ArrayList<Song> playlist; // arrayList for speicified playlist
    private String name; // name for specified Playlist
    
    // Playlist constructor
    public Playlist(String playlistName) 
    {
        this.playlist = new ArrayList<Song>();
        this.name = playlistName;
    }

    // Playlist getter
    public ArrayList<Song> getPlaylist() {
        return playlist;
    }
    
    // Playlist setter (useless)?
    public void setPlaylist(ArrayList<Song> playlist) {
        this.playlist = playlist;
    }
    
    // Playlist name getter
    public String getName() {
        return name;
    }

    // Playlist name setter
    public void setName(String name) {
        this.name = name;
    }
    
    // adding songs to Playlist
    public void addSong(Song s) {
        if (!playlist.contains(s)) {
            playlist.add(s);
        }
    }
    
    // removing songs from Playlist
    public void removeSong(Song s) {
        if (playlist.contains(s)) {
            playlist.remove(s);
        }
    }
    
    // checking if a song is within a Playlist
    public boolean hasSong(Song s) {
        return playlist.contains(s);
    }
    
    /*
    Add function to make a (Playlist Name).txt file that contains all the Song locations.
    */
    
    /*
    Add a function that loads all playlists at the beginning of the programs runtime.
    */
}
