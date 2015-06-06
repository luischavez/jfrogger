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

import com.github.luischavez.jfrogger.game.StaticSet;
import com.github.luischavez.jfrogger.game.StaticSet.Set;

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
public class StaticResourceHandler implements ResourceHandler<StaticSet> {

    private StaticSet loadStatics(String input) {
        StaticSet staticSet = new StaticSet();

        Pattern pattern = Pattern.compile("SET\\s+\\-x\\s+(\\d+)\\s+\\-y\\s+(\\d+)\\s+\\-t\\s+(\\d+)\\s+\\-e\\s+(\\d+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            int x = Integer.valueOf(matcher.group(1)) * 32;
            int y = 64 + (Integer.valueOf(matcher.group(2)) * 32);
            int tile = Integer.valueOf(matcher.group(3));
            boolean enemy = 1 == Integer.valueOf(matcher.group(4));

            StaticSet.Set set = new Set(x, y, tile, enemy);
            staticSet.add(set);
        }

        return staticSet;
    }

    @Override
    public StaticSet handle(InputStream stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String input = reader.lines().collect(Collectors.joining());

            return this.loadStatics(input);
        } catch (IOException ex) {
            Logger.getLogger(StaticResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
