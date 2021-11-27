/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.stage.FileChooser;

public class Helpers
{
    public final static String p = System.getProperty("file.separator");
    public static String songResourcePath = "Resources" + p + "Songs" + p;
    public static String uploadSongPath = "resources" + p + "Songs" + p;
    public static String uploadImagePath = "resources" + p + "Albums" + p;
    //Stores message about the status of upload functionality
    static String uploadStatus = "";
    
    public static Boolean uploadFile(Song s) throws IOException, UnsupportedTagException, InvalidDataException {
        return uploadFile(new File(s.getMP3Location()));
    }
    
    public static Boolean uploadFile(File origFile) throws IOException, UnsupportedTagException, InvalidDataException {
        return uploadFile(origFile, songResourcePath);
    }
    
    public static boolean uploadFile(File origFile, String newLoc) 
    {
        Path oldPath = Paths.get(origFile.getAbsolutePath());
        Path newPath = Paths.get(newLoc);
        try {
            Files.move(oldPath, newPath, REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    // This function is used to make sure all the details of Song are included in the file upload.
    public static String uploadCheck(Song s) 
    {
        File f = s.getmp3Location();
        String sName = s.getSongName();
        String artistName = s.getArtistName();
        String albumName = s.getAlbumName();
        File image = s.getAlbumArt();
        
        if (f.exists()) 
        {
            if (sName != null) 
            {
                if (artistName != null) 
                {
                    if (albumName != null) 
                    {
                        String mp3filepath = uploadSongPath + sName + "+" + artistName + ".mp3";
                        File newFileLoc = new File(mp3filepath);
                        Song temp;
                        if (Helpers.checkForAlbumCover(artistName, albumName) != null) 
                        {
                            File existingAlbumArt = Helpers.checkForAlbumCover(artistName, albumName);
                            temp = new Song(newFileLoc, sName, artistName, albumName, existingAlbumArt);
                            temp.createJSONFile(temp);
                            System.out.println("Album Art already detected.");
                        }
                        else if (image != null) 
                        {
                            File oldImageLoc = new File(s.getAlbumArt().getAbsolutePath()); // getting the File of the Image
                            int dot = oldImageLoc.getAbsolutePath().lastIndexOf(".");
                            String imageExt = oldImageLoc.getAbsolutePath().substring(dot, oldImageLoc.getAbsolutePath().length()); // getting the image extension
                            String albumArtPath = uploadImagePath + artistName + "+" + albumName + imageExt;
                            File newArtLoc = new File(albumArtPath);
                            Helpers.uploadFile(oldImageLoc, albumArtPath); // uploading the albumArt to its respective folder
                            temp = new Song(newFileLoc, sName, artistName, albumName, newArtLoc);
                            temp.createJSONFile(temp);
                            System.out.println("No Album Art previously detected - using provided image.");
                        } 
                        else 
                        {
                            temp = new Song(newFileLoc, sName, artistName, albumName);
                            temp.createJSONFile(temp);
                            System.out.println("No album art previously detected or provided.");
                        }
                        Helpers.uploadFile(f, mp3filepath);
                        return "MP3 Uploaded Successfully!";
                    } else {
                        return "No Album specified!";
                    }
                } else {
                    return "No Artist specified!";
                }
            } else {
                return "No Song Name specified!";
            }
        } else {
            return "No MP3 File to upload!";
        }
    }
    
    /* This converts file paths between Mac and Windows so the program can load
    the songs properly despite who uploads them with the program.
    Example: "resources/Songs/abc.mp3" becomes "resources\Songs\abc.mp3" for Windows users.
    */
    public static File convertFilePath(File f) 
    {
        String s = f.getPath();
        char c = s.charAt(9);
        // if the / after "resources" is not equal to your path separator, change it for the whole string.
        if (!(s.charAt(9) == p.charAt(0))) 
        {
            s = s.replace(s.charAt(9), p.charAt(0));
        }
        return new File(s);
    }
    
    private static void setUploadStatus(String s) {
        uploadStatus = s;
    }
    
    public static String getUploadStatus() {
        return uploadStatus;
    }
    
    // this will be called when choosing a file for album art
    public static File imageFileChooser() 
    {
        FileChooser choose = new FileChooser();
        choose.setTitle("Uploading Album Art");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"));
        File f = choose.showOpenDialog(null);
        return f;
    }
    
    public static File mp3FileChooser() 
    {
        FileChooser choose = new FileChooser();
        choose.setTitle("Uploading MP3");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.mp3"));
        File f = choose.showOpenDialog(null);
        return f;
    }
    
    public static ArrayList<Playlist> onStartLoadPlaylists() 
    {
        ArrayList<Playlist> list = new ArrayList<Playlist>();
        ArrayList<Playlist> playlists = Playlist.loadPlaylists();
        if (!playlists.isEmpty()) 
        {
            for (Playlist p : playlists) {
                list.add(p);
            }
        }
        return list;
    }
    
    // this checks during uploading a song if there's album art already specified for the artist and album.
    public static File checkForAlbumCover(String artist, String album) 
    {
        File value = null;
        File f = new File("resources" + p + "Albums");
        if (f.exists() && f.isDirectory()) 
        {
            if (f.length() > 0) // getting the files of the playlist folder (minus DS_Store files)
            {
                File[] dirContents = Helpers.filterMacOS(f);
                for (File temp : dirContents) // for all files in the playlists folder
                {
                    if (temp.isFile()) // if the file is a file rather than a directory.
                    {
                        int extension = temp.getName().lastIndexOf(".");
                        String filename = temp.getName().substring(0, extension); // removing extension from file name
                        String[] parts = filename.split(Pattern.quote("+")); // splitting the string by artist and album name
                        if (parts[0].equalsIgnoreCase(artist) && parts[1].equalsIgnoreCase(album)) // if the provided artist & album are the same as current file
                        {
                            value = temp; // set the return value equal to this file
                            return value;
                        }
                    }
                }
            }
        } else {
            System.out.println("Albums directory not found, cannot check album covers.");
        }
        return value;
    }
    
    // this removes any .DS_Store files from specified directory.
    public static File[] filterMacOS(File f) 
    {
        File[] dirContents = f.listFiles(new FilenameFilter() { 
        @Override
        public boolean accept(File file, String string) {
            return !string.equals(".DS_Store");
            }
        }); 
        return dirContents;
    }
}