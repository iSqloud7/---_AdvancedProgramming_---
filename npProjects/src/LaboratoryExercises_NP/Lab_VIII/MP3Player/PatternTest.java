package LaboratoryExercises_NP.Lab_VIII.MP3Player;

import java.util.ArrayList;
import java.util.List;

class Song {
    private final String title;
    private final String artist;

    public Song(String title, String author) {
        this.title = title;
        this.artist = author;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Song{");
        sb.append("title=").append(title);
        sb.append(", artist=").append(artist);
        sb.append('}');
        return sb.toString();
    }
}

class MP3Player {

    private int currentSong;
    private final List<Song> songList;
    private int playing;

    MP3Player(List<Song> songList) {
        this.songList = songList;
        this.currentSong = 0;
        this.playing = 0;
    }

    public void pressPlay() {
        if (playing == 1) {
            System.out.println("Song is already playing");
        } else {
            System.out.printf("Song %d is playing%n", currentSong);

            playing = 1;
        }
    }

    public void printCurrentSong() {
        System.out.println(songList.get(currentSong).toString());
    }

    public void pressStop() {
        if (playing == 1) {
            System.out.printf("Song %d is paused%n", currentSong);

            playing = 0;
        } else if (playing == 0) {
            System.out.println("Songs are stopped");

            playing = -1;
            currentSong = 0;
        } else {
            System.out.println("Songs are already stopped");
        }
    }

    public void pressFWD() {
        if (currentSong + 1 == songList.size()) {
            currentSong = 0;
        } else {
            currentSong++;
        }

        if (playing != -1) {
            playing = 0;
        }

        System.out.println("Forward...");
    }

    public void pressREW() {
        if (currentSong - 1 == -1) {
            currentSong = songList.size() - 1;
        } else {
            currentSong--;
        }

        if (playing != -1) {
            playing = 0;
        }

        System.out.println("Reward...");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MP3Player{");
        sb.append("currentSong = ").append(currentSong);
        sb.append(", songList = ").append(songList);
        sb.append('}');
        return sb.toString();
    }
}

public class PatternTest {

    public static void main(String args[]) {

        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}