Carl  LaGodna
UPDATE LOG
-------------------------------------------------------------------------------------------------
2021/11/12
-Removed playlistTest functions, as they appeared to be testing functions
-Note: MusicPlayer may need to be removed and it's functionality should be absorbed into a MediaPlayer object
	-At of 04:28pm, Roger is working on the MusicPlayer class

-MUSICPLAYER class:
	-Added songFolder and pathToSongFolder variables
	-Added currSong and currPlaylist variables
	-Created loadSong function, that calls setSong to load the first song in file
	-Created several setSong functions to handle different possible passed types (song, file, string)

-SONG class:
	-Added new constructors: for when just String of file location is passed, just File object is
#TO DO
-Add song library file. This will house every song the music player will play from.
-Add playlist subfolder where playlist objects can be used to store lists of specific songs
-------------------------------------------------------------------------------------------------
2021/11/14
-Changed version: 0.1 -> 0.2
-SONG class:
	-Added Image 'albumArt' field, inherited from javafx.scene.image.Image
-MusicPlayer class:
	-Clarified some/most comments (sorry -_-)
	-Created funciton setDefaultSong()
		-If a song file not passed during constructor, gets first song in Song folder
	-Added setSong and getSong methods
	-Created function updateMusicPlayer()
		-Since only the current song will change, all other fields (path, media, mediaplayer) will change along with it
		-Thus this method will be used to set all other fields so they won't have to be called individually
	-Changed all SET methods to be private, except for updateMusicPlayer()
	-Changed data type of 'currSong': Song -> File
		-NOTE: SONG class may need to be deleted, superfluous
		-SONG class should be kept if keeping track of song metadata proves difficult without it
	-Renamed function get/setMediaPlayer() -> get/setPlayer()
	-NOTE: I changed this class a LOT. If any errors arise, this would be a good first place to check
	
-MainScreenController class:
	-Removed <MediaPlayer> field 'mp' this field would have to be updated everytime a song was to change
	-Renamed <MusicPlayer> object to 'mp' instead
		-NOTE: If a song was to be paused, syntax is: mp.getPlayer().pause();
	-Added function btnSkipSong() that skips to next song in list

-MainScreen fxml:
	-Added TEMPORARY button to show off next song feature
		-Create button with same code using SceneBuilder

#TODO
-Make program play next song in file, or next song in playlist.
-Make this LOG a docx or something

