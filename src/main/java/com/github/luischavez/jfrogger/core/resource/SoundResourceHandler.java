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
package com.github.luischavez.jfrogger.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

/**
 *
 * @author Luis
 */
public class SoundResourceHandler implements ResourceHandler<Sequence> {

    @Override
    public Sequence handle(InputStream stream) {
        try {
            return MidiSystem.getSequence(stream);
        } catch (InvalidMidiDataException | IOException ex) {
            Logger.getLogger(SoundResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
