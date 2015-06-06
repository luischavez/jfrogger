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
package com.github.luischavez.jfrogger.game.entity;

import com.github.luischavez.jfrogger.game.component.Movable;
import com.github.luischavez.jfrogger.game.component.Positionable;
import com.github.luischavez.jfrogger.game.component.Tileable;

import com.artemis.Entity;

import java.util.function.Consumer;

/**
 *
 * @author Luis
 */
public class ObjectEntity implements Consumer<Entity> {

    @Override
    public void accept(Entity entity) {
        entity.addComponent(new Positionable());
        entity.addComponent(new Movable());
        entity.addComponent(new Tileable(0));
    }
}
