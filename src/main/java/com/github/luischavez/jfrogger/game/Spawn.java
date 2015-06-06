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
import com.github.luischavez.jfrogger.core.Interpolator;
import com.github.luischavez.jfrogger.core.Tick;

import com.github.luischavez.jfrogger.game.component.Movable;
import com.github.luischavez.jfrogger.game.component.Positionable;
import com.github.luischavez.jfrogger.game.component.Tileable;

/**
 *
 * @author Luis
 */
public class Spawn implements Tick {

    final static public int LEFT = 0;
    final static public int RIGHT = 1;

    /**
     * Position: 0 to 10
     */
    final private int position;

    /**
     * Speed, pixel/second
     */
    final private int speed;

    /**
     * Respawn delay in seconds
     */
    final private int delay;

    /**
     * Direction 0 - left 1 - right
     */
    final private int direction;

    /**
     * Entity tile id
     */
    final private int tile;

    /**
     * Enemy flag
     */
    final private boolean enemy;

    /**
     * Respawn time interpolator
     */
    final private Interpolator respawnInterpolator;

    public Spawn(int position, int speed, int delay, int direction, int tile, boolean enemy) {
        this.position = position;
        this.speed = speed;
        this.delay = delay;
        this.direction = direction;
        this.tile = tile;
        this.enemy = enemy;

        this.respawnInterpolator = new Interpolator(0, 1, Interpolator.SECOND_IN_NANOS * delay);
    }

    @Override
    public void update(double delta) {
        this.respawnInterpolator.update(delta);

        if (this.respawnInterpolator.finished()) {
            Game.getArtemis().make("object", (entity) -> {
                if (this.enemy) {
                    Game.getArtemis().group("enemy", entity);
                } else {
                    Game.getArtemis().group("platform", entity);
                }

                int x1, x2;
                int y = 64 + (this.position * 32);

                if (Spawn.LEFT == this.direction) {
                    x1 = Game.getWidth();
                    x2 = -100;
                } else {
                    x1 = 0;
                    x2 = Game.getWidth();
                }

                entity.getComponent(Tileable.class).setTile(this.tile);
                entity.getComponent(Positionable.class).setPosition(x1, y);
                entity.getComponent(Movable.class).setDirection("x");
                entity.getComponent(Movable.class).setInterpolator(new Interpolator(x1, x2, Interpolator.SECOND_IN_NANOS * (Game.getWidth() / this.speed)));
            });

            this.respawnInterpolator.reset();
        }
    }
}
