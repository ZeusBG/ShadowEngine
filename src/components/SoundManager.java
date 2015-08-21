/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.util.HashMap;
import utils.Sounds;

/**
 *
 * @author pavelcheto
 */
public class SoundManager {

    private HashMap<Sounds, SoundClip> sounds;

    public SoundManager() {
        sounds = new HashMap<>();
        sounds.put(Sounds.MOVEMENT_PLAYER, new SoundClip("res/walking_player.wav"));
        sounds.put(Sounds.AK47_SHOT, new SoundClip("res/AK47.wav"));
    }

    public void play(Sounds type) {
        if (type == Sounds.MOVEMENT_PLAYER && !sounds.get(type).isRunning()) {
            sounds.get(type).loop();
        } else if (type == Sounds.AK47_SHOT) {
            sounds.get(type).play();
        }
    }

    public void stop(Sounds type) {
        if (type == Sounds.MOVEMENT_PLAYER && sounds.get(type).isRunning()) {
            sounds.get(type).stop();
        }
    }

    public void changeVolume(Sounds type, float value) {
        sounds.get(type).setVolume(value);
    }
}
