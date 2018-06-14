package pl.mitelski.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Songs {
    public static List<Song> SONGS = new ArrayList<>(Arrays.asList(
            new Song("Tabaluga", "nieznany", R.raw.tabaluga),
            new Song("Nostalgia", "Taco Hemingway", R.raw.nostalgia),
            new Song("Coco Jambo", "Mr. President", R.raw.coco),
            new Song("Weź pigułke", "DJ Hazel", R.raw.hazel),
            new Song("Chodź", "Taco Hemingway", R.raw.chodz),
            new Song("narkotik kal", "Hard Bass School", R.raw.narkotik),
            new Song("Chciałem być", "Krzysztof Krawczyk", R.raw.chcialem)));

    public int size() {
        return SONGS.size();
    }

    public Song get(int id) {
        return SONGS.get(id);
    }
}
