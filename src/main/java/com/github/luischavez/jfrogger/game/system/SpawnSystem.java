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
package com.github.luischavez.jfrogger.game.system;

import com.github.luischavez.jfrogger.core.Game;
import com.github.luischavez.jfrogger.core.Interpolator;

import com.github.luischavez.jfrogger.game.component.Movable;
import com.github.luischavez.jfrogger.game.component.Platformable;
import com.github.luischavez.jfrogger.game.component.Positionable;

import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.ImmutableBag;

import java.util.Objects;

/**
 *
 * @author Luis
 */
public class SpawnSystem extends VoidEntitySystem {

    private void processEntity(Entity entity) {
        Interpolator interpolator = entity.getComponent(Movable.class).getInterpolator();

        if (Objects.isNull(interpolator)) {
            return;
        }

        interpolator.update(entity.getWorld().getDelta());

        int oldX = entity.getComponent(Positionable.class).getX();
        int x = (int) interpolator.getCurrent();
        int diffX = x - oldX;

        entity.getComponent(Positionable.class).setX(x);

        Entity player = Game.getArtemis().getEntityByTag("player");

        if (player.getComponent(Platformable.class).hasPlatform()) {
            Entity platform = player.getComponent(Platformable.class).getPlatform();

            if (platform.equals(entity)) {
                int playerX = player.getComponent(Positionable.class).getX();

                if (0 < playerX && Game.getWidth() > playerX) {
                    playerX += diffX;

                    player.getComponent(Positionable.class).setX(playerX);
                }
            }
        }

        if (interpolator.finished()) {
            entity.deleteFromWorld();
        }
    }

    @Override
    protected void processSystem() {
        ImmutableBag<Entity> enemies = Game.getArtemis().getEntitiesByGroup("enemy");
        ImmutableBag<Entity> platforms = Game.getArtemis().getEntitiesByGroup("platform");

        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);

            this.processEntity(entity);
        }

        for (int i = 0; i < platforms.size(); i++) {
            Entity entity = platforms.get(i);

            this.processEntity(entity);
        }
    }
}
