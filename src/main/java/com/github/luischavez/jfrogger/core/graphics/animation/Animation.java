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

import com.github.luischavez.jfrogger.core.Interpolator;
import com.github.luischavez.jfrogger.core.Tick;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.graphics.Sprite;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Luis
 */
public class Animation implements Tick, Serializable {

    final static public int NOT_ANIMATED_YET = -1;

    final public String sound;
    final private int[] indexes;
    final public long step;

    transient private Interpolator interpolator;
    transient private int lastIndex = 0;

    public Animation(String sound, long step, int... indexes) {
        this.sound = sound;
        this.step = step;
        this.indexes = indexes;
    }

    public Animation(long step, int... indexes) {
        this(null, step, indexes);
    }

    public int getIndex() {
        return this.indexes[this.lastIndex];
    }

    public String getSound() {
        return sound;
    }

    public void animate(Sprite sprite, Consumer<Image> imageConsumer) {
        Image image = sprite.get(this.indexes[this.lastIndex]);

        imageConsumer.accept(image);
    }

    @Override
    public void update(double delta) {
        if (1 == this.indexes.length) {
            return;
        }

        if (Objects.isNull(this.interpolator)) {
            this.interpolator = new Interpolator(0, this.indexes.length - 1, this.step);
        }

        this.interpolator.update(delta);

        this.lastIndex = (int) this.interpolator.getCurrent();

        if (this.interpolator.finished()) {
            this.interpolator.reset();
        }
    }
}
