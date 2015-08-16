/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import java.util.ArrayList;
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
    }

    public void play(Sounds type) {
        if (type == Sounds.MOVEMENT_PLAYER && !sounds.get(type).isRunning()) {
            sounds.get(type).loop();
        }
    }

    public void stop(Sounds type) {
        if (type == Sounds.MOVEMENT_PLAYER && sounds.get(type).isRunning()) {
            sounds.get(type).stop();
        }
    }

    public void changeVolume(Sounds type, float value) {
            sounds.get(type).setVolume(sounds.get(type).getVolume() + value);
    }
}
