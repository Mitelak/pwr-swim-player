package pl.mitelski.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Songs {
    public static List<Song> SONGS = new ArrayList<>(Arrays.asList(
            new Song("Tabaluga", "nieznany", R.raw.tabaluga, "1", "30"),
            new Song("Nostalgia", "Taco Hemingway", R.raw.nostalgia, "3", "49"),
            new Song("Coco Jambo", "Mr. President", R.raw.coco, "3", "42"),
            new Song("Weź pigułke", "DJ Hazel", R.raw.hazel, "6", "14"),
            new Song("Chodź", "Taco Hemingway", R.raw.chodz, "3", "28"),
            new Song("narkotik kal", "Hard Bass School", R.raw.narkotik, "3", "55"),
            new Song("Chciałem być", "Krzysztof Krawczyk", R.raw.chcialem, "3", "05"),
            new Song("Co ty mi dasz?", "MIG", R.raw.mig, "2", "28"),
            new Song("Małomiasteczkowy", "Dawid Podsiadło", R.raw.podsiadlo, "3", "25")));

    public int size() {
        return SONGS.size();
    }

    public Song get(int id) {
        return SONGS.get(id);
    }
}
