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

    public ArrayList<Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<Song> playlist) {
        this.playlist = playlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addSong(Song s) {
        if (!playlist.contains(s)) {
            playlist.add(s);
        }
    }
    
    public void removeSong(Song s) {
        if (playlist.contains(s)) {
            playlist.remove(s);
        }
    }
    
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
