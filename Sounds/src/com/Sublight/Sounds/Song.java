package com.Sublight.Sounds;

import java.io.File;

/**
 *
 * @author rogervalade
 */
public class Song 
{
   
    private File location;
    private String songName;
    private String artistName;
    private String albumName;
    
    public Song(File location, String song, String artist, String album) 
    {
        this.location = location;
        this.songName = song;
        this.artistName = artist;
        this.albumName = album;
    }

    public File getLocation() {
        return location;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    
    public boolean checkFileLocation(File f) 
    {
        boolean exists = f.exists();
        return exists;
    }
}
