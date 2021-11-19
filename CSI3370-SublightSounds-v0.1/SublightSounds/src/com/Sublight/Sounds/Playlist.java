/*
 * @author Sublight Development
 */

package com.Sublight.Sounds;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Playlist 
{
    private static ArrayList<ArrayList<Song>> arrayOfAllPlaylists = new ArrayList<ArrayList<Song>>();
    public static String p = System.getProperty("file.separator"); 
    public String playlistFolderPath = "resources" + p + "Playlists" + p;
    private ArrayList<Song> playlist; // arrayList for specified playlist
    private String name; // name for specified Playlist
    
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
    
    //Returns a shuffled version of array
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
            //Pops random stored value from "clone" and adds it to "shuffled"
            r  = (int) (Math.random() * clone.size());
            shuffled.add(clone.get(r));
            clone.remove(r);
        }
        
        return shuffled;
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
        File f = new File(playlistFolderPath + this.name + ".txt");
        // If the file already exists, we want to delete it so we can store new contents to it.
        if (f.exists() && f.isFile()) {
            f.delete();
        }
        // then we create a new file to the same path.
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
        }
        try 
        {
            FileWriter fr = new FileWriter(f, false); // creating the writer that will write to the file we create
            if (!playlist.isEmpty()) // if the playlist isn't empty
            {
                for (Song s : playlist) // for all songs in the playlist
                {
                    String songPath = Song.getJSONLocation(s).getPath(); // get it's JSON file location
                    fr.append(songPath + "\n"); // add it to this text file
                }
            }
            fr.close(); // close the FileWriter after you're done
        }   catch (IOException ex) {
            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    /*
    TODO: Add a function that loads all playlists at the beginning of the programs runtime.
    */
    public static void loadPlaylists() 
    {
        File f = new File("resources" + p + "Playlists");
        if (f.exists() && f.isDirectory()) 
        {
            if (f.length() > 0) 
            {
                File[] dirContents = f.listFiles(); // getting the files of the playlist folder
                for (File temp : dirContents) // for all files in the array
                {
                    if (temp.isFile()) // if the file is a file rather than a directory.
                    {
                        try 
                        {
                            Scanner scan = new Scanner(temp); // Creating a scanner for the text file
                            Gson gson = new Gson();
                            ArrayList<Song> tempPlaylist = new ArrayList<Song>();
                            while (scan.hasNextLine()) // while the text file has more JSON file locations to read
                            {
                                String filePath = scan.nextLine(); // go to the next line of the textfile
                                try 
                                {
                                    Reader reader = Files.newBufferedReader(Paths.get(filePath)); // get the JSON File
                                    Song s = gson.fromJson(reader, Song.class); // converting the JSON File into a Song Object.
                                    s.setmp3Location(Helpers.convertFilePath(s.getmp3Location())); // this makes sure the filepath is correct for the OS
                                    tempPlaylist.add(s); // adding the song to that playlist
                                }
                                catch (IOException ex) {
                                    Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            arrayOfAllPlaylists.add(tempPlaylist); // adding that arraylist of songs to the list of all playlists.
                        } catch (FileNotFoundException ex) 
                        {
                            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } // for the amount of files in the playlist folder
            }
        } else {
            System.out.println("Playlists directory not found, cannot initialize playlists.");
        }
    }
}
