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
    /* Use this function when wanting to move a selected file to a target directory.
    */
    public static boolean upload(File origFile, String newLoc) 
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
    
}
