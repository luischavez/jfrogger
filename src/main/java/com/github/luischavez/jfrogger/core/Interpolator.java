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
package com.github.luischavez.jfrogger.core;

/**
 *
 * @author Luis
 */
public class Interpolator implements Tick {

    final static public long SECOND_IN_NANOS = 1_000_000_000;

    final public long from;
    final public long to;
    final public long time;
    private double accumulator;
    private long current;

    public Interpolator(long from, long to, long time) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.accumulator = -1;
        this.current = from;
    }

    public boolean finished() {
        return this.to == this.getCurrent();
    }

    public void reset() {
        if (this.finished()) {
            this.accumulator = -1;
            this.current = this.from;
        }
    }

    public long getCurrent() {
        if (-1 != this.accumulator) {
            long d = this.from - this.to;

            double value = this.from - ((this.accumulator * d) / this.time);

            this.current = (long) Math.floor(value);

            if (this.accumulator > this.time) {
                this.current = this.to;
            }
        }

        return this.current;
    }

    @Override
    public void update(double delta) {
        if (-1 == this.accumulator) {
            this.accumulator++;
        }

        if (!this.finished()) {
            this.accumulator += delta;
        }
    }
}
