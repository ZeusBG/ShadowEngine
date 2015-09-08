/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author pavelcheto
 */
public class SoundClip {

    private Clip clip;
    private FloatControl gainControl;
    private float volume;

    public SoundClip(String path) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);

            clip = AudioSystem.getClip();
            clip.open(dais);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume = Math.abs(gainControl.getMinimum()) + Math.abs(gainControl.getMaximum());
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Failed at SoundClip open.");
        }
    }

    public void play() {
        if (clip == null) {
            return;
        }
        //stop();
        clip.setFramePosition(0);
        while (!clip.isRunning()) {
            clip.start();
        }
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void close() {
        stop();
        clip.drain();
        clip.close();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        while (!clip.isRunning()) {
            clip.start();
        }
    }

    public float getVolume() {
        return gainControl.getValue();
    }

    public void setVolume(float value) {
        if (value < 0 || value > 1) {
            return;
        }
        float newVolume = gainControl.getMinimum() + volume * value;
        if (newVolume >= gainControl.getMinimum() && newVolume <= gainControl.getMaximum()) {
            gainControl.setValue(newVolume);
        }
    }

    public boolean isRunning() {
        return clip.isRunning();
    }
}
