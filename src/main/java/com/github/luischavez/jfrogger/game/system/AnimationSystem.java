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

import com.github.luischavez.jfrogger.game.component.Animable;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

/**
 *
 * @author Luis
 */
public class AnimationSystem extends EntityProcessingSystem {

    @Mapper
    private ComponentMapper<Animable> animables;

    public AnimationSystem() {
        super(Aspect.getAspectForAll(Animable.class));
    }

    @Override
    protected void process(Entity entitiy) {
        Animable animable = this.animables.get(entitiy);

        animable.getActor().update(entitiy.getWorld().getDelta());
    }
}
