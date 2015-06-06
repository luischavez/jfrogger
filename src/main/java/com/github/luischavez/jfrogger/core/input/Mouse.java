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
package com.github.luischavez.jfrogger.core.input;

/**
 *
 * @author Luis
 */
public class Mouse {

    final public Action action;
    final public int button;
    final public int x, y;

    protected Mouse(Action action, int button, int x, int y) {
        this.action = action;
        this.button = button;
        this.x = x;
        this.y = y;
    }

    public enum Action {

        CLICK, PRESSED, RELEASED, DRAGGED, MOVED
    }
}
