package gui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    private static MediaPlayer musicPlayer;
    private static AudioClip clickSound;
    static {
        try {
            URL musicUrl = SoundManager.class.getResource("/assets/sounds/music.mp3");
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toExternalForm());
                musicPlayer = new MediaPlayer(media);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                musicPlayer.setVolume(0.5); // 50% volume
            } else {
                System.out.println("Music file not found!");
            }

            // 2. Load Click Sound
            URL clickUrl = SoundManager.class.getResource("/assets/sounds/click.wav");
            if (clickUrl != null) {
                clickSound = new AudioClip(clickUrl.toExternalForm());
                clickSound.setVolume(1.0);
            } else {
                System.out.println("Click sound not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playMusic() {
        if (musicPlayer != null) {
            musicPlayer.play();
        }
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    public static void playClick() {
        if (clickSound != null) {
            clickSound.play();
        }
    }
}