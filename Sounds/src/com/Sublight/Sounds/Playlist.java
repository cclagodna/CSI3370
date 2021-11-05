package com.Sublight.Sounds;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rogervalade
 */
public class Playlist 
{
    public String p = System.getProperty("file.separator"); 
    public String resourcePath = "resources" + p + "Playlist" + p;
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
    TODO: Add function to make a (Playlist Name).txt file that contains all the Song locations (their text/JSON files that contain their metaData)
    */
    public void updateTextFile() 
    {
        File f = new File(resourcePath + this.name + ".txt");
        try {
            if (f.createNewFile()) 
            {
                FileWriter fr = new FileWriter(f, true);
                if (!playlist.isEmpty()) 
                {
                    for (Song s : playlist) 
                    {
                        String songPath = s.getLocation().getAbsolutePath();
                        fr.append(songPath + "/n");
                    }
                }
                fr.close();
            } else // if it's not a new file we want to see if the old Text file has changed at all
            {
                // TODO
            }
        } catch (IOException ex) {
            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    TODO: Add a function that loads all playlists at the beginning of the programs runtime.
    */
}
