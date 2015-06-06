/* 
 * Copyright (C) 2015 Luis Chávez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.luischavez.jfrogger.core;

import com.github.luischavez.jfrogger.core.resource.ResourceManager;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 *
 * @author Luis
 */
final public class Sound {

    final static private HashMap<String, Sequencer> SEQUENCERS = new HashMap<>();

    static public void register(String sound) {
        Sequence sequence = ResourceManager.load("sound", "sound/" + sound + ".mid");

        try {
            Sequencer sequencer = MidiSystem.getSequencer();

            sequencer.setSequence(sequence);

            Sound.SEQUENCERS.put(sound, sequencer);
        } catch (MidiUnavailableException | InvalidMidiDataException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public void stopAll() {
        Sound.SEQUENCERS.forEach((id, sequencer) -> {
            if (sequencer.isRunning()) {
                sequencer.stop();
            }
        });
    }

    static public void closeAll() {
        Sound.SEQUENCERS.forEach((id, sequencer) -> {
            if (sequencer.isRunning()) {
                sequencer.stop();
            }
            sequencer.close();
        });
    }

    static public void stop(String sound) {
        Sequencer sequencer = Sound.SEQUENCERS.get(sound);

        if (Objects.nonNull(sequencer)) {
            if (sequencer.isRunning()) {
                sequencer.stop();
            }
        }
    }

    static public boolean isRunning(String sound) {
        Sequencer sequencer = Sound.SEQUENCERS.get(sound);

        if (Objects.nonNull(sequencer)) {
            return sequencer.isRunning();
        }

        return false;
    }

    static public void play(String sound, int loopCount) {
        Sequencer sequencer = Sound.SEQUENCERS.get(sound);

        if (!sequencer.isOpen()) {
            try {
                sequencer.open();
            } catch (MidiUnavailableException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!sequencer.isRunning()) {
            sequencer.setLoopCount(loopCount);

            sequencer.setTickPosition(0);

            sequencer.start();
        }
    }

    static public void background(String sound) {
        if (!Sound.isRunning(sound)) {
            Sound.play(sound, -1);
        }
    }

    static public void sound(String sound) {
        Sound.play(sound, 0);
    }
}
