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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis (Leviathan)
 */
public class ResourceManager {

    static final protected Map<String, ResourceHandler<?>> handlers = new HashMap<>();

    static public <T> void register(String resourceType, ResourceHandler<T> handler) {
        ResourceManager.handlers.put(resourceType, handler);
    }

    static public <T> T load(String resourceType, String uri) {
        URL resource = ResourceManager.class.getResource("/" + uri);

        if (null == resource) {
            return null;
        }

        ResourceHandler<?> handler = ResourceManager.handlers.get(resourceType);

        if (null == handler) {
            return null;
        }

        InputStream inputStream = null;

        try {
            inputStream = resource.openStream();
        } catch (IOException ex) {
            Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        Object value = null;

        if (null != inputStream) {
            value = handler.handle(inputStream);

            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return (T) value;
    }
}
