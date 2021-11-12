package com.Sublight.Sounds;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.image.Image;

/**
 *
 * @author rogervalade
 */
public class Song 
{
    public static String p = System.getProperty("file.separator"); // File Separator
   
    private File mp3Location; // location of MP3 file for song
    private String songName; // song name
    private String artistName; // artist name
    private String albumName; // album name
    private Image albumArt; // album Image
    
    public Song(File location, String song, String artist, String album) 
    {
        this.mp3Location = location;
        this.songName = song;
        this.artistName = artist;
        this.albumName = album;
    }
    
    public Song(File location, String song, String artist, String album, Image image) 
    {
        this.mp3Location = location;
        this.songName = song;
        this.artistName = artist;
        this.albumName = album;
        this.albumArt = image;
    }

    public File getmp3Location() {
        return mp3Location;
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

    public void setmp3Location(File location) {
        this.mp3Location = location;
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
    
    public Image getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Image albumArt) {
        this.albumArt = albumArt;
    }
    
    // This is checking whether the song MP3 file still exists or not.
    public boolean checkmp3FileLocation(File f) {
        return f.exists();
    }
    
    // getting the location of the JSON file
    public static File getJSONLocation(Song s) {
        String n = s.songName;
        String a = s.artistName;
        File f = new File("resources" + p + "SongJSONs" + p + n + "+" + a + ".json"); // specifying where file is stored
        return f;
    }
    
    // This creates a JSON file with the name "(NameofSong)+(NameofArtist).json"
    public void createJSONFile(Song s)
    {
        File f = Song.getJSONLocation(s); // getting file location of JSON
        Gson gson = new Gson(); // creating a GSON object
        try (FileWriter writer = new FileWriter(f.getPath())) 
        {
            gson.toJson(s, writer);
            System.out.println("Made a JSON File at " + f.getPath());
        } catch (IOException e) {}
        
    }
    
    // This is a function that removes the MP3 from the program, and the JSON associated with it.
    public void removeSongFiles(Song s) 
    {
        File mp3File = s.getmp3Location().getAbsoluteFile();
        File jsonFile = Song.getJSONLocation(s).getAbsoluteFile();
        String n = s.getSongName();
        String a = s.getArtistName();
        if (mp3File.exists()) // if the mp3 file exists, delete it
        {
            mp3File.delete();
            System.out.println(n + " " + a + " MP3 file deleted.");
        } else { // else it wasn't found
            System.out.println(n + " " + a + " MP3 not found.");
        }
        if (jsonFile.exists()) { // if the JSON file exists, delete it
            jsonFile.delete();
            System.out.println(n + " " + a + " JSON file deleted.");
        } else { // else it wasn't found
            System.out.println(n + " " + a + " JSON not found.");
        }
    }
}
