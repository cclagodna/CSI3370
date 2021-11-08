package com.Sublight.Sounds;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

/**
 *
 * @author rogervalade
 */
public class Playlist 
{

    private ArrayList<Song> playlist;
    private String name;
    
    //CONSTRUCTORS
    
    public Playlist() {
        this("NewPlaylist");
    }
    
    public Playlist(String playlistName) 
    {
        this.playlist = new ArrayList<Song>();
        this.name = playlistName;
    }
    
    //CONSTRUCTORS
    
    
    
    public ArrayList shuffle() {
        /*
        Inputs: ArrayList stored in class instance
        Output: New ArrayList with suffled values
        */
        
        ArrayList clone = (ArrayList) playlist.clone();
        ArrayList shuffled = new ArrayList(clone.size());
        int r;
        
        //Loops when "clone" is NOT empty
        while (!clone.isEmpty()) {
            //Pops random value from "clone" and adds it to "shuffled"
            r  = (int) (Math.random() * clone.size());
            shuffled.add(clone.get(r));
            clone.remove(r);
        }
        
        return shuffled;
    }
    
    public ArrayList getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList playlist) {
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
    
    public void removeSong(int index) {
        Song s = playlist.get(index);
        playlist.remove(s);
    }
    
    public boolean hasSong(Song s) {
        return playlist.contains(s);
    }
}
