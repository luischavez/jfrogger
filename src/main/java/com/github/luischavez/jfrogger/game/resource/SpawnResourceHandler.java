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

import com.github.luischavez.jfrogger.game.Spawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Luis
 */
public class SpawnResourceHandler implements ResourceHandler<List<Spawn>> {

    private List<Spawn> loadDynamics(String input) {
        ArrayList<Spawn> spawns = new ArrayList<>();

        int count = 0;
        int max = 11;

        Pattern pattern = Pattern.compile("SPAWN\\s+\\-p\\s+(\\d+)\\s+\\-s\\s+(\\d+)\\s+\\-r\\s+(\\d+)\\s+\\-t\\s+(\\d+)\\s+\\-d\\s+(\\d+)\\s+\\-e\\s+(\\d+)");
        Matcher matcher = pattern.matcher(input);

        int position;
        int speed;
        int delay;
        int tile;
        int direction;
        boolean enemy;

        while (matcher.find() && max > count) {
            position = Integer.valueOf(matcher.group(1));
            speed = Integer.valueOf(matcher.group(2));
            delay = Integer.valueOf(matcher.group(3));
            tile = Integer.valueOf(matcher.group(4));
            direction = Integer.valueOf(matcher.group(5));
            enemy = 1 == Integer.valueOf(matcher.group(6));

            Spawn spawn = new Spawn(position, speed, delay, direction, tile, enemy);
            spawns.add(spawn);

            count++;
        }

        return spawns;
    }

    @Override
    public List<Spawn> handle(InputStream stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String input = reader.lines().collect(Collectors.joining());

            return this.loadDynamics(input);
        } catch (IOException ex) {
            Logger.getLogger(SpawnResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }
}
