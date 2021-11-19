/*
 * @author Sublight Development
 */
package com.Sublight.Sounds;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/*
This is a helper class that allows us to make an object
that allows us to change songs and such in our MainScreenController class
*/
public class MusicPlayer {
    
    private String path; // the string literal of the path of our file
    private Media mediaBefore; // creating Media object for our path
    private MediaPlayer player; // creating mediaPlayer for interacting w/ songs
    
    // constructor to be called in MainScreenController on startup
    public MusicPlayer(String pathname) 
    {
        this.path = new File(pathname).getAbsolutePath();
        this.mediaBefore = new Media(new File(this.path).toURI().toString());
        this.player = new MediaPlayer(mediaBefore);
    }
    
    // path getter
    public String getPath() {
        return path;
    }
    
    // path setter
    public void setPath(String newPath) {
        this.path = newPath;
    }
    
    // media getter
    public Media getMedia() {
        return mediaBefore;
    }
    
    // media setter
    public void setMedia(Media newMedia) {
        this.mediaBefore = newMedia;
    }
    
    // mediaPlayer getter
    public MediaPlayer getMediaPlayer() {
        return player;
    }
    
    // mediaPlayer setter
    public void setMediaPlayer(MediaPlayer newPlayer) {
        this.player = newPlayer;
    }
    
}
