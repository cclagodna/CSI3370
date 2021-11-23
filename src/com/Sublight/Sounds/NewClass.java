package com.Sublight.Sounds;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Carl
 */
public class NewClass {
    
    
    
    public static void main(String[] args) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
        
        
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        
        String path = "C:\\Users\\Carl\\Downloads\\Plok Boss Theme.mp3";
        File f = new File(path);
        Mp3File mp3 = new Mp3File(f);
        ID3v2 tag;
        
        if ( mp3.hasId3v2Tag()) {
            mp3.removeId3v2Tag();    
        }
        
        tag = new ID3v24Tag();
        tag.setTitle("Test");
        
        System.out.println( tag.getTitle() );
        
        mp3.setId3v2Tag(tag);
        
        //f.delete();
        mp3.save(path + ".mp3");
        String s = path + ".mp3";
        File newf = new File(s);
        f.delete();
        newf.renameTo(f);
    }
}
