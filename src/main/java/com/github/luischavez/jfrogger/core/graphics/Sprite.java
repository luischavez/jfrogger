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
package com.github.luischavez.jfrogger.core.graphics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Luis
 */
public class Sprite implements Serializable {

    final private List<Image> images = new ArrayList<>();

    private Sprite() {
    }

    public int size() {
        return this.images.size();
    }

    public Image get(int index) {
        if (0 > index || this.images.size() <= index) {
            throw new RuntimeException("invalid index " + index);
        }

        return this.images.get(index);
    }

    static public Sprite create(Image... images) {
        Sprite sprite = new Sprite();

        sprite.images.addAll(Arrays.asList(images));

        return sprite;
    }
}
