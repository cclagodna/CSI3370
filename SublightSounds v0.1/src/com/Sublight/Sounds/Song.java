/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

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
   
    //File containing mp3
    private File mp3Location;
    //Song name
    private String songName;
    //Song artist's name
    private String artistName;
    //Song album's name
    private String albumName;
    //Song album art
    private Image albumArt;
    
    
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
    }
    
    //CONSTRUCTORS #####################################################

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
