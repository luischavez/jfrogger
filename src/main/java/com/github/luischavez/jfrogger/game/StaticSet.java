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
package com.github.luischavez.jfrogger.game;

import com.github.luischavez.jfrogger.core.Game;

import com.github.luischavez.jfrogger.game.component.Movable;
import com.github.luischavez.jfrogger.game.component.Positionable;
import com.github.luischavez.jfrogger.game.component.Tileable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis
 */
public class StaticSet {

    final private List<Set> sets = new ArrayList<>();

    public void add(Set set) {
        this.sets.add(set);
    }

    public void init() {
        this.sets.forEach((set) -> set.addToWorld());
    }

    static public class Set {

        final private int x;
        final private int y;
        final private int tile;
        final private boolean enemy;

        public Set(int x, int y, int tile, boolean enemy) {
            this.x = x;
            this.y = y;
            this.tile = tile;
            this.enemy = enemy;
        }

        public void addToWorld() {
            Game.getArtemis().make("object", (entity) -> {
                if (this.enemy) {
                    Game.getArtemis().group("enemy", entity);
                } else {
                    Game.getArtemis().group("platform", entity);
                }

                entity.getComponent(Tileable.class).setTile(this.tile);
                entity.getComponent(Positionable.class).setPosition(this.x, this.y);
                entity.getComponent(Movable.class).setDirection("x");
                entity.getComponent(Movable.class).setInterpolator(null);
            });
        }
    }
}
