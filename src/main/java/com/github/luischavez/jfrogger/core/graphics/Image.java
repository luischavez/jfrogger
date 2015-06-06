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
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Objects;

/**
 *
 * @author Luis
 */
final public class Image implements Serializable {

    final public int width, height;
    final private int[] data;
    transient private IntBuffer buffer;

    public Image(int width, int height) {
        if (0 == width || 0 == height) {
            throw new RuntimeException("El ancho y el alto deben de ser mayores de 0");
        }

        this.width = width;
        this.height = height;

        this.data = new int[width * height];
    }

    public Buffer getData() {
        if (Objects.isNull(this.buffer)) {
            this.buffer = IntBuffer.wrap(this.data);
        }

        return this.buffer;
    }

    public void setPixel(int x, int y, int r, int g, int b, int a) {
        if (0 > x || width < x) {
            throw new RuntimeException("El valor de x se sale del rango, min: 0 max: " + width);
        }

        if (0 > y || height < y) {
            throw new RuntimeException("El valor de y se sale del rango, min: 0 max: " + height);
        }

        if (255 < r || 255 < g || 255 < b || 255 < a) {
            throw new RuntimeException("Color fuera de rango");
        }

        this.data[y * this.width + x] = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public int getPixel(int x, int y) {
        if (0 > x || width < x) {
            throw new RuntimeException("El valor de x se sale del rango, min: 0 max: " + width);
        }

        if (0 > y || height < y) {
            throw new RuntimeException("El valor de y se sale del rango, min: 0 max: " + height);
        }

        return this.data[y * this.width + x];
    }
}
