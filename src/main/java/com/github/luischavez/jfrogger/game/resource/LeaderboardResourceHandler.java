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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class LeaderboardResourceHandler implements ResourceHandler<Properties> {

    @Override
    public Properties handle(InputStream stream) {
        Properties properties = new Properties();
        
        try {
            properties.load(stream);
        } catch (IOException ex) {
            Logger.getLogger(LeaderboardResourceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return properties;
    }
}
