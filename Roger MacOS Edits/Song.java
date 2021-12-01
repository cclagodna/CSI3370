/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/*
This class will hold all information regaring a song file and metadata, such as:
    file location,
    song name,
    artist name,
    album name,
    album art
*/


public class Song
{
    //File separator character, dependent on end-user's system
    public static String p = System.getProperty("file.separator");
   
    public Gson a;
    
    //File containing mp3
    private File mp3Location;
    //Song name
    private String songName;
    //Song artist's name
    private String artistName;
    //Song album's name
    private String albumName;
    //Image of album
    private File albumArt;
    
    
    //CONSTRUCTORS #####################################################
    
    //Just file location passed as a String
    public Song(String location) {
        this(new File(location));
    }
    
    //Just file location passed as a File object
    public Song(File location) {
        this.mp3Location = location;
        this.songName = location.toString();
        this.artistName = "NotFound";
        this.albumName = "NotFound";

    }
    
    //Song File, name, artist, album
    public Song(File location, String name, String artist, String album) 
    {
        this.mp3Location = location;
        this.songName = name;
        this.artistName = artist;
        this.albumName = album;
        this.albumArt = null;
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
    
    //CONSTRUCTORS #####################################################

    // Returns file object of mp3
    public File getmp3Location() {
        return mp3Location;
    }
    
    // Returns file location as a string
    public String getName() {
        return getmp3Location().getName();
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
    
    public File getAlbumArt() {
        return albumArt;
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
    
    public void setAlbumArt(File location) {
        this.albumArt = location;
    }
    
    // This is checking whether the song MP3 file still exists or not.
    public boolean checkmp3FileLocation(File f) {
        return f.exists();
    }
    
    
    // getting the location of the JSON file
    public File getJSONLocation() {
        String n = getSongName();
        String a = getArtistName();
        File f = new File("resources" + p + "SongJSONs" + p + n + "+" + a + ".json"); // specifying where file is stored
        return f;
    }
    
    // This creates a JSON file with the name "(NameofSong)+(NameofArtist).json"
    public void createJSONFile(Song s)
    {
        File f = getJSONLocation(); // getting file location of JSON
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
        File jsonFile = getJSONLocation().getAbsoluteFile();
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

    String getMP3Location() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
