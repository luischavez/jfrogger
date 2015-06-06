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
package com.github.luischavez.jfrogger.core.graphics.animation;

import com.github.luischavez.jfrogger.core.Tick;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.graphics.Sprite;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Luis
 */
public class Actor implements Tick, Serializable {

    final private Map<String, Animation> animations = new HashMap<>();
    transient private String action;

    public Actor() {
    }

    public String getCurrentAction() {
        return this.action;
    }

    public Animation removeAnimation(String action) {
        return this.animations.remove(action);
    }

    public Animation getAnimation(String action) {
        return this.animations.get(action);
    }

    public Animation getCurrentAnimation() {
        return Objects.nonNull(this.action) ? this.getAnimation(this.action) : null;
    }

    public void addAnimation(String action, Animation animation) {
        this.animations.put(action, animation);
    }

    public boolean action(String action) {
        if (this.animations.containsKey(action)) {
            this.action = action;

            return true;
        }

        return false;
    }

    public void act(Sprite sprite, Consumer<Image> imageConsumer) {
        if (Objects.isNull(this.action)) {
            throw new RuntimeException("invalid scene");
        }

        Animation animation = this.getAnimation(this.action);

        if (Objects.nonNull(animation)) {
            animation.animate(sprite, imageConsumer);
        }
    }

    @Override
    public void update(double delta) {
        Animation animation = this.getAnimation(this.action);

        if (Objects.nonNull(animation)) {
            animation.update(delta);
        }
    }
}
