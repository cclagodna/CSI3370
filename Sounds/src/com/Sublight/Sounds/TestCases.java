package com.Sublight.Sounds;

import java.io.File;
import java.util.ArrayList;

public class TestCases 
{
    public static void playlistOneSong() 
    {
        Playlist p = new Playlist("TestPlaylist");
        Song s1 = new Song(new File("resources/Songs/Test+123.mp3"), "Test", "123", "abc");
        p.addSong(s1);
        p.updateTextFile();
    }
    
    public static void playlistTwoSongs() 
    {
        Song s1 = new Song(new File("resources/Songs/Test+123.mp3"), "Test", "123", "abc");
        Song s2 = new Song(new File("resources/Songs/Hello+World.mp3"), "Hello", "World", "Java");
        Playlist p = new Playlist("TestPlaylist");
        p.addSong(s1);
        p.addSong(s2);
        p.updateTextFile();
    }
    
    public static void loadPlaylistTest() 
    {
        ArrayList<Playlist> aP = Playlist.loadPlaylists();
        for (Playlist p : aP) 
        {
            System.out.println("Playlist Name: " + p.getName());
            ArrayList<Song> songs = p.getPlaylist();
            for (Song s: songs) 
            {
                System.out.println("Song Name: " + s.getSongName());
                System.out.println("Artist Name: " + s.getArtistName());
                System.out.println("Album Name: " + s.getAlbumName());
                System.out.println("File Location: " + s.getmp3Location());
                if (s.getAlbumArt() != null) {
                    System.out.println("Album Art Location: " + s.getAlbumArt());
                }
            }
        }
    }
}
