package com.Sublight.Sounds;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rogervalade
 */
public class Helpers 
{
    public static String p = System.getProperty("file.separator");
    public static String uploadSongPath = "resources" + p + "Songs" + p;
    
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
        
        if (f.exists()) 
        {
            if (sName != null) 
            {
                if (artistName != null) 
                {
                    if (albumName != null) 
                    {
                        String filepath = uploadSongPath + sName + "+" + artistName + ".mp3";
                        File newFileLoc = new File(filepath);
                        Song temp = new Song(newFileLoc, sName, artistName, albumName);
                        Helpers.uploadFile(f, filepath);
                        temp.createJSONFile(temp);
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
        char c = s.charAt(8);
        // if the / after "resources" is not equal to your path separator, change it for the whole string.
        if (!(s.charAt(8) == p.charAt(0))) 
        {
            s = s.replace(s.charAt(8), p.charAt(0));
        }
        return new File(s);
    }
    
}
