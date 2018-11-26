/**
 * Sound.java
 * @Author: Jason Bricco
 */

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;

/**
 * Manages the game's sound and music.
 */
public class Sound
{
    private MediaPlayer musicPlayer;

    private AudioClip soundEffects[] =
    {
        new AudioClip(new File("src/Assets/Pickup_03.mp3").toURI().toString()),
        new AudioClip(new File("src/Assets/Pickup_01.mp3").toURI().toString()),
        new AudioClip(new File("src/Assets/Pickup_02.mp3").toURI().toString()),
        new AudioClip(new File("src/Assets/Pickup_04.mp3").toURI().toString()),
        new AudioClip(new File("src/Assets/Death.mp3").toURI().toString()),
        new AudioClip(new File("src/Assets/Break.mp3").toURI().toString())
    };

    public Sound()
    {
        Media media = new Media(new File("src/Assets/Music.mp3").toURI().toString());
        musicPlayer = new MediaPlayer(media);

        musicPlayer.setOnEndOfMedia(() -> musicPlayer.seek(Duration.ZERO));
        musicPlayer.setVolume(0.75);
    }

    public void play(SoundEffect effect)
    {
        soundEffects[effect.ordinal()].play();
    }

    public void start()
    {
        musicPlayer.play();
    }

    public void stop()
    {
        musicPlayer.stop();
    }
}
