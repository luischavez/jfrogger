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
package com.github.luischavez.jfrogger.game.component;

import com.github.luischavez.jfrogger.core.Interpolator;

import com.artemis.Component;

/**
 *
 * @author Luis
 */
public class Movable extends Component {

    private String direction;
    private Interpolator interpolator;

    public Movable() {
    }

    public Movable(String direction, Interpolator interpolator) {
        this.direction = direction;
        this.interpolator = interpolator;
    }

    public String getDirection() {
        return direction;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }
}
