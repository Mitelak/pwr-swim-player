package pl.mitelski.player;

public class Song {
    String title;
    String artist;
    int file_id;

    public Song(String title, String artist, int file_id) {
        this.title = title;
        this.artist = artist;
        this.file_id = file_id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getFileId() {
        return file_id;
    }
}
