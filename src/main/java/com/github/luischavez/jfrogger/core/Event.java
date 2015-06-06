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

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Luis
 */
public class Event {

    final static Map<String, GameEventListener> events = new HashMap<>();

    static public void fire(String event, Object... args) {
        GameEventListener listener = Event.events.get(event);

        if (Objects.nonNull(listener)) {
            listener.success(args);
        }
    }

    static public void fire(String event) {
        Event.fire(event, new Object[0]);
    }

    static public void listen(String event, GameEventListener listener) {
        Event.events.put(event, listener);
    }

    public interface GameEventListener extends EventListener {

        public void success(Object... args);
    }
}
