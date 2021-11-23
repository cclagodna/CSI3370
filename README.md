Roger Valade Update Log

(Did work before this - will add those at a later time)

11/23/21 
1) Updated songViewInitializer() so it does not error out upon switching to the playlistView ListView.
2) Added ImageView for Album Art.
3) Tried adding TextField for Song Metadata viewing, does not show up right now.

11/22/21
1) Added a way for program to get previous albumArt and use that for the Song in-case it's not provided.
2) Code cleanup - made functions like filterMacOS in Helpers that's used in multiple functions across the program. Made other similar changes like this for QOL. 
3) Changed the way the playlist ListView does things. Instead of looking at the String selected, looks at the index of what's selected to determine Playlist selected. 
4) Added song ListView that loads upon something in the playlist ListView being selected. If a song is selected the music player changes to that song.

11/20/21
1) Added albumArt file to Songs constructor. Added file selection in GUI to accomodate for uploading albumArt. 
2) Made it so program loads first song in Songs folder if there is a song, else, uses everyone's favorite song by default.
3) Added skipSong, changeSong functionality.
4) Added currentSong, currentPlaylist functionality to MusicPlayer.
5) Started working on ListView JavaFX object for dynamically loaded Playlists at beginning of program. 
