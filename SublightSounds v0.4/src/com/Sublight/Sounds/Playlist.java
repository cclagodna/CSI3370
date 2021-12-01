/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Playlist {
    public static String p = System.getProperty("file.separator"); 
    public String playlistFolderPath = "resources" + p + "Playlists" + p;
    private ArrayList<Song> playlist; // arrayList for specified playlist
    private String name; // name for specified Playlist
    
    //CONSTRUCTORS #####################################################
    
    //Instantiate Playlist with a default name
    public Playlist() {
        this("New Playlist");
    }
    
    public Playlist(String playlistName) {
        this.playlist = new ArrayList<Song>();
        this.name = playlistName;
    }
    
    //CONSTRUCTORS #####################################################
    
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
    
    // call this function when updating a playlists text file (so whenever a playlist changes in any way)
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
                    String songPath = s.getJSONLocation().getPath(); // get its JSON file location
                    //fr.write(songPath + "\n"); // add it to this text file
                    fr.write(songPath + System.getProperty("line.separator"));
                }
            }
            fr.close(); // close the FileWriter after you're done
        }   catch (IOException ex) {
            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    // this function will be called at the start of the programs runtime so all the playlists are loaded into it.
    public static ArrayList<Playlist> loadPlaylists() 
    {
        ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();
        File f = new File("resources" + p + "Playlists");
        if (f.exists() && f.isDirectory()) 
        {
            if (f.length() > 0) // getting the files of the playlist folder (minus DS_Store files)
            {
                File[] dirContents = f.listFiles(new FilenameFilter() { 
                    @Override
                    public boolean accept(File file, String string) {
                        return !string.equals(".DS_Store");
                    }
                    
                }); 
                for (File temp : dirContents) // for all files in the playlists folder
                {
                    if (temp.isFile()) // if the file is a file rather than a directory.
                    {
                        try 
                        {
                            Scanner scan = new Scanner(temp); // Creating a scanner for the text file
                            Gson gson = new Gson();
                            int dot = temp.getName().lastIndexOf(".");
                            Playlist p = new Playlist(temp.getName().substring(0, dot));
                            while (scan.hasNextLine()) // while the text file has more JSON file locations to read
                            {
                                String filePath = scan.nextLine(); // go to the next line of the textfile
                                filePath = filePath.replace("\0", ""); // removing null from filename
                                try (Reader reader = new FileReader(filePath)) // reading at the filepath
                                {
                                    Song s = gson.fromJson(reader, Song.class); // converting the JSON File into a Song Object.
                                    s.setmp3Location(Helpers.convertFilePath(s.getmp3Location())); // this makes sure the filepath is correct for the OS
                                    p.addSong(s); // adding the song to that playlist
                                }
                                catch (IOException ex) {
                                    Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            allPlaylists.add(p); // adding playlist to the arraylist of all playlists
                        } catch (FileNotFoundException ex) 
                        {
                            Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } else {
            System.out.println("Playlists directory not found, cannot initialize playlists.");
        }
        return allPlaylists;
    }
}