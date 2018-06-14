package pl.mitelski.player;

public class Song {
    String title;
    String artist;
    int file_id;
    String min, sec;

    public Song(String title, String artist, int file_id, String min, String sec) {
        this.title = title;
        this.artist = artist;
        this.file_id = file_id;
        this.min = min;
        this.sec = sec;
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
