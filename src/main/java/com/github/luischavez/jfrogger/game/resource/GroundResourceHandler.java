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
package com.github.luischavez.jfrogger.game.resource;

import com.github.luischavez.jfrogger.core.resource.ResourceHandler;

import com.github.luischavez.jfrogger.game.Ground;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Luis
 */
public class GroundResourceHandler implements ResourceHandler<Ground> {

    @Override
    public Ground handle(InputStream stream) {
        Ground ground = new Ground();

        int count = 0;
        int max = 13;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String lines = reader.lines().collect(Collectors.joining());

            Pattern pattern = Pattern.compile("\\[(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d),\\s?(\\d)\\s?\\]");
            Matcher matcher = pattern.matcher(lines);

            while (matcher.find() && max > count) {
                for (int i = 0; i < 15; i++) {
                    ground.tiles[count][i] = Integer.valueOf(matcher.group(i + 1));
                }

                count++;
            }

            while (max > count) {
                for (int i = 0; i < 15; i++) {
                    ground.tiles[count][i] = 0;
                }

                count++;
            }
        } catch (IOException ex) {
            Logger.getLogger(GroundResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ground;
    }
}
