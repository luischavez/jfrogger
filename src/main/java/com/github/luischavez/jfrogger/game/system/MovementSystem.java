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

import com.github.luischavez.jfrogger.core.Interpolator;
import com.github.luischavez.jfrogger.core.graphics.animation.Actor;

import com.github.luischavez.jfrogger.game.component.Animable;
import com.github.luischavez.jfrogger.game.component.Movable;
import com.github.luischavez.jfrogger.game.component.Positionable;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

import java.util.Objects;

/**
 *
 * @author Luis
 */
public class MovementSystem extends EntityProcessingSystem {

    @Mapper
    private ComponentMapper<Positionable> positionables;
    @Mapper
    private ComponentMapper<Movable> movables;
    @Mapper
    private ComponentMapper<Animable> animables;

    public MovementSystem() {
        super(Aspect.getAspectForAll(Positionable.class, Movable.class, Animable.class));
    }

    @Override
    protected void process(Entity entity) {
        Positionable positionable = this.positionables.get(entity);
        Movable movable = this.movables.get(entity);
        Animable animable = this.animables.get(entity);

        String direction = movable.getDirection();

        Actor actor = animable.getActor();

        Interpolator interpolator = movable.getInterpolator();
        if (Objects.nonNull(interpolator) && !interpolator.finished()) {
            interpolator.update(entity.getWorld().getDelta());

            int current = (int) interpolator.getCurrent();

            switch (direction) {
                case "y":
                    positionable.setY(current);
                    break;
                case "x":
                    positionable.setX(current);
                    break;
            }

            if (interpolator.finished()) {
                String action = "stand." + actor.getCurrentAction().split("\\.")[1];

                actor.action(action);
            }
        }
    }
}
