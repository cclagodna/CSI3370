package com.Sublight.Sounds;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;

/**
 *
 * @author rogervalade
 */
public class Helpers 
{
    public static String p = System.getProperty("file.separator");
    public static String uploadSongPath = "resources" + p + "Songs" + p;
    public static String uploadImagePath = "resources" + p + "Albums" + p;
    
    /* Use this function when wanting to move a selected file to a target directory.
    */
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
                        if (image != null) 
                        {
                            File oldImageLoc = new File(s.getAlbumArt().getAbsolutePath()); // getting the File of the Image
                            int dot = oldImageLoc.getAbsolutePath().lastIndexOf(".");
                            String imageExt = oldImageLoc.getAbsolutePath().substring(dot, oldImageLoc.getAbsolutePath().length()); // getting the image extension
                            String albumArtPath = uploadImagePath + artistName + "+" + albumName + imageExt;
                            File newArtLoc = new File(albumArtPath);
                            Helpers.uploadFile(oldImageLoc, albumArtPath); // uploading the albumArt to it's respective folder
                            temp = new Song(newFileLoc, sName, artistName, albumName, newArtLoc);
                            temp.createJSONFile(temp);
                        } else 
                        {
                            temp = new Song(newFileLoc, sName, artistName, albumName);
                            temp.createJSONFile(temp);
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
    
    // this will be called when choosing a file for album art
    public static File imageFileChooser() 
    {
        FileChooser choose = new FileChooser();
        choose.setTitle("Uploading Album Art");
        choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpeg", "*.png", "*.bmp", "*.gif"));
        File f = choose.showOpenDialog(null);
        return f;
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
    
}
