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
            URL musicUrl = SoundManager.class.getResource("/assets/music.wav");
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toExternalForm());
                musicPlayer = new MediaPlayer(media);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
                musicPlayer.setVolume(0.5); // 50% volume
            } else {
                System.out.println("Music file not found!");
            }

            URL clickUrl = SoundManager.class.getResource("/assets/click.wav");
            if (clickUrl != null) {
                clickSound = new AudioClip(clickUrl.toExternalForm());
                clickSound.setVolume(1.0);
                clickSound.play(0.0);
            } else {
                System.out.println("Click sound not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setMusicMuted(boolean muted) {
        if (musicPlayer != null) {
            musicPlayer.setMute(muted);
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