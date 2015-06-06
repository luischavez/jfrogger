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

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 *
 * @author Luis (Leviathan)
 */
public class Clock {

    /**
     * CONSTANTS
     */
    final static public long SECOND_IN_NANOS = 1_000_000_000;

    /**
     * Updates per second
     */
    final public int ups;

    /**
     * Frame limit
     */
    final public int frameLimit;

    /**
     * Clock flags
     */
    private boolean running;
    private boolean paused;
    private boolean requestPause;
    private boolean requestResume;

    /**
     * Listeners
     */
    private BiConsumer<Integer, Integer> clockListener;

    public Clock(int ups, int frameLimit) {
        if (0 >= ups) {
            throw new RuntimeException("ups must be greater than 0");
        }

        if (0 > frameLimit) {
            throw new RuntimeException("Frame limit must be greater than 0 or 0");
        }

        this.ups = ups;
        this.frameLimit = frameLimit;
    }

    public void setListener(BiConsumer<Integer, Integer> clockListener) {
        this.clockListener = clockListener;
    }

    public void start(Tick tick, Frame frame) {
        long lastUpdateTime = System.nanoTime();
        long currentTime;

        double tickStep = (Clock.SECOND_IN_NANOS * 1.0) / this.ups;

        double lastUpdatedDeltaTick = 0D;
        double deltaTick = 0D;

        int updates = 0, frames = 0;

        long lastTimer = System.nanoTime();
        long actualTimer;

        this.running = true;
        this.paused = false;
        this.requestPause = false;
        this.requestResume = false;

        while (this.running) {
            currentTime = System.nanoTime();

            deltaTick += (currentTime - lastUpdateTime) / tickStep;

            if (!this.paused) {
                lastUpdatedDeltaTick = deltaTick;
            }

            if (this.requestResume) {
                deltaTick = lastUpdatedDeltaTick;

                this.paused = false;
                this.requestResume = false;
            }

            if (this.requestPause) {
                this.paused = true;
                this.requestPause = false;
            }

            if (1 <= deltaTick) {
                tick.update(this.paused ? 0 : deltaTick * tickStep);

                deltaTick = 0;
                //deltaTick--;
                updates++;

                frame.update();
                frames++;
            }

            //frame.update();
            //frames++;
            if ((actualTimer = System.nanoTime()) - lastTimer >= Clock.SECOND_IN_NANOS) {
                lastTimer = actualTimer;

                if (Objects.nonNull(this.clockListener)) {
                    this.clockListener.accept(updates, frames);
                }

                updates = 0;
                frames = 0;
            }

            lastUpdateTime = currentTime;
        }
    }

    public void stop() {
        this.running = false;
    }

    public void pause() {
        if (!this.paused) {
            this.requestPause = true;
        }
    }

    public void resume() {
        if (this.paused) {
            this.requestResume = true;
        }
    }

    public boolean isPaused() {
        return this.paused;
    }
}
