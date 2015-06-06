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
package com.github.luischavez.jfrogger.core.math;

import java.io.Serializable;

/**
 * Encapsulates a 2D rectangle defined by it's bottom corner point and its
 * extends in x (width) and y (height).
 *
 * @author badlogicgames@gmail.com
 */
public class Rectangle implements Serializable {

    /**
     * Static temporary rectangle. Use with care! Use only when sure other code
     * will not also use this.
     */
    static public final Rectangle tmp = new Rectangle();

    /**
     * Static temporary rectangle. Use with care! Use only when sure other code
     * will not also use this.
     */
    static public final Rectangle tmp2 = new Rectangle();

    private static final long serialVersionUID = 5733252015138115702L;
    public int x, y;
    public int width, height;

    /**
     * Constructs a new rectangle with all values set to zero
     */
    public Rectangle() {

    }

    /**
     * Constructs a new rectangle with the given corner point in the bottom left
     * and dimensions.
     *
     * @param x The corner point x-coordinate
     * @param y The corner point y-coordinate
     * @param width The width
     * @param height The height
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a rectangle based on the given rectangle
     *
     * @param rect The rectangle
     */
    public Rectangle(Rectangle rect) {
        x = rect.x;
        y = rect.y;
        width = rect.width;
        height = rect.height;
    }

    /**
     * @param x bottom-left x coordinate
     * @param y bottom-left y coordinate
     * @param width width
     * @param height height
     * @return this rectangle for chaining
     */
    public Rectangle set(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        return this;
    }

    /**
     * @return the x-coordinate of the bottom left corner
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the bottom left corner
     *
     * @param x The x-coordinate
     * @return this rectangle for chaining
     */
    public Rectangle setX(int x) {
        this.x = x;

        return this;
    }

    /**
     * @return the y-coordinate of the bottom left corner
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the bottom left corner
     *
     * @param y The y-coordinate
     * @return this rectangle for chaining
     */
    public Rectangle setY(int y) {
        this.y = y;

        return this;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of this rectangle
     *
     * @param width The width
     * @return this rectangle for chaining
     */
    public Rectangle setWidth(int width) {
        this.width = width;

        return this;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of this rectangle
     *
     * @param height The height
     * @return this rectangle for chaining
     */
    public Rectangle setHeight(int height) {
        this.height = height;

        return this;
    }

    /**
     * return the Vector with coordinates of this rectangle
     *
     * @param position The Vector
     */
    public Vector getPosition(Vector position) {
        return position.set(x, y);
    }

    /**
     * Sets the x and y-coordinates of the bottom left corner from vector
     *
     * @param position The position vector
     * @return this rectangle for chaining
     */
    public Rectangle setPosition(Vector position) {
        this.x = position.x;
        this.y = position.y;

        return this;
    }

    /**
     * Sets the x and y-coordinates of the bottom left corner
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return this rectangle for chaining
     */
    public Rectangle setPosition(int x, int y) {
        this.x = x;
        this.y = y;

        return this;
    }

    /**
     * Sets the width and height of this rectangle
     *
     * @param width The width
     * @param height The height
     * @return this rectangle for chaining
     */
    public Rectangle setSize(int width, int height) {
        this.width = width;
        this.height = height;

        return this;
    }

    /**
     * Sets the squared size of this rectangle
     *
     * @param sizeXY The size
     * @return this rectangle for chaining
     */
    public Rectangle setSize(int sizeXY) {
        this.width = sizeXY;
        this.height = sizeXY;

        return this;
    }

    /**
     * @return the Vector with size of this rectangle
     * @param size The Vector
     */
    public Vector getSize(Vector size) {
        return size.set(width, height);
    }

    /**
     * @param x point x coordinate
     * @param y point y coordinate
     * @return whether the point is contained in the rectangle
     */
    public boolean contains(int x, int y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    /**
     * @param point The coordinates vector
     * @return whether the point is contained in the rectangle
     */
    public boolean contains(Vector point) {
        return contains(point.x, point.y);
    }

    /**
     * @param rectangle the other {@link Rectangle}.
     * @return whether the other rectangle is contained in this rectangle.
     */
    public boolean contains(Rectangle rectangle) {
        int xmin = rectangle.x;
        int xmax = xmin + rectangle.width;

        int ymin = rectangle.y;
        int ymax = ymin + rectangle.height;

        return ((xmin > x && xmin < x + width) && (xmax > x && xmax < x + width))
                && ((ymin > y && ymin < y + height) && (ymax > y && ymax < y + height));
    }

    /**
     * @param r the other {@link Rectangle}
     * @return whether this rectangle overlaps the other rectangle.
     */
    public boolean overlaps(Rectangle r) {
        return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
    }

    /**
     * Sets the values of the given rectangle to this rectangle.
     *
     * @param rect the other rectangle
     * @return this rectangle for chaining
     */
    public Rectangle set(Rectangle rect) {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;

        return this;
    }

    /**
     * Merges this rectangle with the other rectangle. The rectangle should not
     * have negative width or negative height.
     *
     * @param rect the other rectangle
     * @return this rectangle for chaining
     */
    public Rectangle merge(Rectangle rect) {
        int minX = Math.min(x, rect.x);
        int maxX = Math.max(x + width, rect.x + rect.width);
        x = minX;
        width = maxX - minX;

        int minY = Math.min(y, rect.y);
        int maxY = Math.max(y + height, rect.y + rect.height);
        y = minY;
        height = maxY - minY;

        return this;
    }

    /**
     * Merges this rectangle with a point The rectangle should not have negative
     * width or negative height.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return this rectangle for chaining
     */
    public Rectangle merge(int x, int y) {
        int minX = Math.min(this.x, x);
        int maxX = Math.max(this.x + width, x);
        this.x = minX;
        this.width = maxX - minX;

        int minY = Math.min(this.y, y);
        int maxY = Math.max(this.y + height, y);
        this.y = minY;
        this.height = maxY - minY;

        return this;
    }

    /**
     * Merges this rectangle with a point The rectangle should not have negative
     * width or negative height.
     *
     * @param vec the vector describing the point
     * @return this rectangle for chaining
     */
    public Rectangle merge(Vector vec) {
        return merge(vec.x, vec.y);
    }

    /**
     * Merges this rectangle with a list of points The rectangle should not have
     * negative width or negative height.
     *
     * @param vecs the vectors describing the points
     * @return this rectangle for chaining
     */
    public Rectangle merge(Vector[] vecs) {
        int minX = x;
        int maxX = x + width;
        int minY = y;
        int maxY = y + height;
        for (int i = 0; i < vecs.length; ++i) {
            Vector v = vecs[i];
            minX = Math.min(minX, v.x);
            maxX = Math.max(maxX, v.x);
            minY = Math.min(minY, v.y);
            maxY = Math.max(maxY, v.y);
        }
        x = minX;
        width = maxX - minX;
        y = minY;
        height = maxY - minY;
        return this;
    }

    /**
     * Calculates the aspect ratio ( width / height ) of this rectangle
     *
     * @return the aspect ratio of this rectangle. Returns Float.NaN if height
     * is 0 to avoid ArithmeticException
     */
    public int getAspectRatio() {
        return (height == 0) ? 0 : width / height;
    }

    /**
     * Calculates the center of the rectangle. Results are located in the given
     * Vector
     *
     * @param vector the Vector to use
     * @return the given vector with results stored inside
     */
    public Vector getCenter(Vector vector) {
        vector.x = x + width / 2;
        vector.y = y + height / 2;
        return vector;
    }

    /**
     * Moves this rectangle so that its center point is located at a given
     * position
     *
     * @param x the position's x
     * @param y the position's y
     * @return this for chaining
     */
    public Rectangle setCenter(int x, int y) {
        setPosition(x - width / 2, y - height / 2);
        return this;
    }

    /**
     * Moves this rectangle so that its center point is located at a given
     * position
     *
     * @param position the position
     * @return this for chaining
     */
    public Rectangle setCenter(Vector position) {
        setPosition(position.x - width / 2, position.y - height / 2);
        return this;
    }

    /**
     * Fits this rectangle around another rectangle while maintaining aspect
     * ratio This scales and centers the rectangle to the other rectangle (e.g.
     * Having a camera translate and scale to show a given area)
     *
     * @param rect the other rectangle to fit this rectangle around
     * @return this rectangle for chaining
     */
    public Rectangle fitOutside(Rectangle rect) {
        int ratio = getAspectRatio();

        if (ratio > rect.getAspectRatio()) {
            // Wider than tall
            setSize(rect.height * ratio, rect.height);
        } else {
            // Taller than wide
            setSize(rect.width, rect.width / ratio);
        }

        setPosition((rect.x + rect.width / 2) - width / 2, (rect.y + rect.height / 2) - height / 2);
        return this;
    }

    /**
     * Fits this rectangle into another rectangle while maintaining aspect
     * ratio. This scales and centers the rectangle to the other rectangle (e.g.
     * Scaling a texture within a arbitrary cell without squeezing)
     *
     * @param rect the other rectangle to fit this rectangle inside
     * @return this rectangle for chaining
     */
    public Rectangle fitInside(Rectangle rect) {
        int ratio = getAspectRatio();

        if (ratio < rect.getAspectRatio()) {
            // Taller than wide
            setSize(rect.height * ratio, rect.height);
        } else {
            // Wider than tall
            setSize(rect.width, rect.width / ratio);
        }

        setPosition((rect.x + rect.width / 2) - width / 2, (rect.y + rect.height / 2) - height / 2);
        return this;
    }

    public String toString() {
        return x + "," + y + "," + width + "," + height;
    }
}
