/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Helpers 
{
    public final static String p = System.getProperty("file.separator");
    public static String songResourcePath = "Resources" + p + "Songs" + p;
    //Stores message about the status of upload functionality
    static String uploadStatus = "";
    
    
    public static String uploadFile(File origFile) throws IOException, UnsupportedTagException, InvalidDataException {
        return uploadFile(origFile, songResourcePath);
    }
    
    //Move a selected file to a target directory.
    public static String uploadFile(File f, String newLoc) throws IOException, UnsupportedTagException, InvalidDataException
    {
        //Current location of file
        Path oldPath = Paths.get(f.getAbsolutePath());
        //Desired location of file, must append f.getName() or else errors arise
        Path newPath = Paths.get(newLoc + f.getName());
        
        boolean canUpload = uploadCheck(f, newLoc);
        
        //If file can be uploaded
        if (canUpload) try {
            //Moves file to new path, replacing existing file of same name (if that exists)
            Files.move(oldPath, newPath, REPLACE_EXISTING);
            //If the file should be copied instead of just moved
            //Files.copy(oldPath, newPath, REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //File cant be uploaded, return error message
        else return uploadStatus;
        
        //Set uploadStatus to a success message
        setUploadStatus("MP3 Uploaded Successfully!");
        return uploadStatus;
    }
    
   
    // This function is used to make sure all the details of mp3 are included in the file upload.
    // Any reason to deny an upload (wrong file type, missing metadata, etc) should be designated here
    public static boolean uploadCheck(File f, String path) throws IOException, UnsupportedTagException, InvalidDataException 
    {
        //Extract ID3 information from mp3
        //Any file passed to this method SHOULD be an mp3 with an ID3v2 tag
        ID3v2 tag = new Mp3File(f).getId3v2Tag();
        //Retrive song name
        String sName = tag.getTitle();
        //Retrieve artist name
        String artistName = tag.getArtist();
        //Retrive album name
        String albumName = tag.getAlbum();
        
        if ( !f.exists() ) {
            setUploadStatus("No MP3 File to upload!");
            return false;
        }
        
        //If file does not have extension .mp3
        if (!f.getName().endsWith(".mp3")){
            setUploadStatus("This file is not an mp3!");
            return false;
        }
        
        if ( sName == null ) {
            setUploadStatus("No Song Name Specified!");
            return false;
        }
        
        if ( artistName == null ) {
            setUploadStatus("No Artist Name Specified!");
            return false;
        }
        
        if ( albumName == null ) {
            setUploadStatus("No Album Name Specified!");
            return false;
        }
        
        if (!(new File(path)).exists()) {
            setUploadStatus("Desired copy path doesn't exist!");
            return false;
        }
        
        setUploadStatus("MP3 viable for upload");
        return true;
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
}