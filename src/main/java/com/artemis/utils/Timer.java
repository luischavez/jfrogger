package com.artemis.utils;

public abstract class Timer {

    private double delay;
    private boolean repeat;
    private double acc;
    private boolean done;
    private boolean stopped;

    public Timer(double delay) {
        this(delay, false);
    }

    public Timer(double delay, boolean repeat) {
        this.delay = delay;
        this.repeat = repeat;
        this.acc = 0;
    }

    public void update(double delta) {
        if (!done && !stopped) {
            acc += delta;

            if (acc >= delay) {
                acc -= delay;

                if (repeat) {
                    reset();
                } else {
                    done = true;
                }

                execute();
            }
        }
    }

    public void reset() {
        stopped = false;
        done = false;
        acc = 0;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isRunning() {
        return !done && acc < delay && !stopped;
    }

    public void stop() {
        stopped = true;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public abstract void execute();

    public double getPercentageRemaining() {
        if (done) {
            return 100;
        } else if (stopped) {
            return 0;
        } else {
            return 1 - (delay - acc) / delay;
        }
    }

    public double getDelay() {
        return delay;
    }

}
