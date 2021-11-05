package com.Sublight.Sounds;

import java.util.ArrayList;

/**
 *
 * @author rogervalade
 */
public class Playlist 
{

    private ArrayList<Song> playlist;
    private String name;
    
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
    
    
}
