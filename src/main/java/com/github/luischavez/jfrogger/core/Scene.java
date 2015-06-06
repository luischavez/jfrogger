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

import com.github.luischavez.jfrogger.core.input.Input;

import com.jogamp.opengl.GL;

/**
 *
 * @author Luis
 */
abstract public class Scene implements Tick {

    final protected Artemis artemis;

    public Scene() {
        this.artemis = new Artemis();
    }

    protected void setup() {
        this.init();
        this.configure(this.artemis);
        this.artemis.initialize();
    }

    abstract protected void init();

    abstract protected void configure(Artemis artemis);

    abstract protected void handle(Input input);

    abstract protected void paint(GL gl);

    @Override
    public void update(double delta) {
        this.artemis.update(delta);
    }
}
