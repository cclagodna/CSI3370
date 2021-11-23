package com.Sublight.Sounds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private File albumArt; // album Image
    
    // constructor for song w/o albumArt Image
    public Song(File location, String song, String artist, String album) 
    {
        this.mp3Location = location;
        this.songName = song;
        this.artistName = artist;
        this.albumName = album;
    }
    
    // constructor for song w/ albumArt Image
    public Song(File location, String song, String artist, String album, File image) 
    {
        this.mp3Location = location;
        this.songName = song;
        this.artistName = artist;
        this.albumName = album;
        this.albumArt = image;
    }
    
    // mp3Location getter
    public File getmp3Location() {
        return mp3Location;
    }
    
    // songName getter
    public String getSongName() {
        return songName;
    }
    
    // artistName getter
    public String getArtistName() {
        return artistName;
    }

    // albumName getter
    public String getAlbumName() {
        return albumName;
    }
    
    // albumArt getter
    public File getAlbumArt() {
        return albumArt;
    }
    
    // mp3Location setter
    public void setmp3Location(File location) {
        this.mp3Location = location;
    }

    // songName setter
    public void setSongName(String songName) {
        this.songName = songName;
    }

    // artistName setter
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    // albumName setter
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    
    // albumArt setter
    public void setAlbumArt(File albumArt) {
        this.albumArt = albumArt;
    }
    
    // this converts our albumArt file into a JavaFX Image
    public Image albumArtToJFX()
    {
        FileInputStream input = null;
        try {
            input = new FileInputStream(this.albumArt.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Image(input);
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // creating a GSON object
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
