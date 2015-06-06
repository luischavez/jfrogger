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
package com.github.luischavez.jfrogger.core.input;

import com.github.luischavez.jfrogger.core.Game;
import com.github.luischavez.jfrogger.core.graphics.Surface;

import com.jogamp.opengl.awt.GLCanvas;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.Stack;
import java.util.function.Consumer;

/**
 *
 * @author Luis
 */
final public class Input {

    final static public int MODIFIER_SHIFT = 1;
    final static public int MODIFIER_CTRL = 2;
    final static public int MODIFIER_ALT = 8;

    final static public int MOUSE_NO_BUTTON = 0;
    final static public int MOUSE_BUTTON_1 = 1;
    final static public int MOUSE_BUTTON_2 = 2;
    final static public int MOUSE_BUTTON_3 = 3;

    final static public int KEY_ENTER = 10;
    final static public int KEY_SHIFT = 16;
    final static public int KEY_CTRL = 17;
    final static public int KEY_ALT = 18;
    final static public int KEY_MAYUS = 20;
    final static public int KEY_SPACE = 32;

    final static public int KEY_LEFT = 37;
    final static public int KEY_UP = 38;
    final static public int KEY_RIGHT = 39;
    final static public int KEY_DOWN = 40;

    final static public int KEY_0 = 48;
    final static public int KEY_1 = 49;
    final static public int KEY_2 = 50;
    final static public int KEY_3 = 51;
    final static public int KEY_4 = 52;
    final static public int KEY_5 = 53;
    final static public int KEY_6 = 54;
    final static public int KEY_7 = 55;
    final static public int KEY_8 = 56;
    final static public int KEY_9 = 57;

    final static public int KEY_A = 65;
    final static public int KEY_B = 66;
    final static public int KEY_C = 67;
    final static public int KEY_D = 68;
    final static public int KEY_E = 69;
    final static public int KEY_F = 70;
    final static public int KEY_G = 71;
    final static public int KEY_H = 72;
    final static public int KEY_I = 73;
    final static public int KEY_J = 74;
    final static public int KEY_K = 75;
    final static public int KEY_L = 76;
    final static public int KEY_M = 77;
    final static public int KEY_N = 78;
    final static public int KEY_O = 79;
    final static public int KEY_P = 80;
    final static public int KEY_Q = 81;
    final static public int KEY_R = 82;
    final static public int KEY_S = 83;
    final static public int KEY_T = 84;
    final static public int KEY_U = 85;
    final static public int KEY_V = 86;
    final static public int KEY_W = 87;
    final static public int KEY_X = 88;
    final static public int KEY_Y = 89;
    final static public int KEY_Z = 90;

    final static public int KEY_F1 = 112;
    final static public int KEY_F2 = 113;
    final static public int KEY_F3 = 114;
    final static public int KEY_F4 = 115;
    final static public int KEY_F5 = 116;
    final static public int KEY_F6 = 117;
    final static public int KEY_F7 = 118;
    final static public int KEY_F8 = 119;
    final static public int KEY_F9 = 120;
    final static public int KEY_F10 = 121;
    final static public int KEY_F11 = 122;
    final static public int KEY_F12 = 123;

    final private GLCanvas canvas;
    final private Stack<Key> keyEvents;
    final private Stack<Mouse> mouseEvents;

    public Input(Surface surface) {
        this.canvas = surface.getCanvas();
        this.keyEvents = new Stack<>();
        this.mouseEvents = new Stack<>();

        this.setup();
    }

    private void removeListeners() {
        KeyListener[] keyListeners = this.canvas.getListeners(KeyListener.class);
        for (KeyListener keyListener : keyListeners) {
            this.canvas.removeKeyListener(keyListener);
        }

        MouseListener[] mouseListeners = this.canvas.getListeners(MouseListener.class);
        for (MouseListener mouseListener : mouseListeners) {
            this.canvas.removeMouseListener(mouseListener);
        }

        MouseMotionListener[] mouseMotionListeners = this.canvas.getListeners(MouseMotionListener.class);
        for (MouseMotionListener mouseMotionListener : mouseMotionListeners) {
            this.canvas.removeMouseMotionListener(mouseMotionListener);
        }

        MouseWheelListener[] mouseWheelListeners = this.canvas.getListeners(MouseWheelListener.class);
        for (MouseWheelListener mouseWheelListener : mouseWheelListeners) {
            this.canvas.removeMouseWheelListener(mouseWheelListener);
        }
    }

    private Point translatePoint(int x, int y) {
        if (0 > y) {
            y = Game.getHeight() + -y;
        } else {
            y -= Game.getHeight();
            y = 0 > y ? -y : y;
        }

        return new Point(x, y);
    }

    private void setup() {
        this.removeListeners();

        this.canvas.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                keyEvents.add(new Key(e.getKeyCode(), e.getModifiers()));
            }
        });

        this.canvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Point point = translatePoint(e.getX(), e.getY());

                mouseEvents.add(new Mouse(Mouse.Action.CLICK, e.getButton(), point.x, point.y));
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                Point point = translatePoint(e.getX(), e.getY());

                mouseEvents.add(new Mouse(Mouse.Action.PRESSED, e.getButton(), point.x, point.y));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                Point point = translatePoint(e.getX(), e.getY());

                mouseEvents.add(new Mouse(Mouse.Action.RELEASED, e.getButton(), point.x, point.y));
            }
        });

        this.canvas.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = translatePoint(e.getX(), e.getY());

                mouseEvents.add(new Mouse(Mouse.Action.DRAGGED, e.getButton(), point.x, point.y));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point point = translatePoint(e.getX(), e.getY());

                mouseEvents.add(new Mouse(Mouse.Action.MOVED, e.getButton(), point.x, point.y));
            }
        });
    }

    public void flushKeyEvents() {
        this.keyEvents.removeAllElements();
    }

    public void flushMouseEvents() {
        this.mouseEvents.removeAllElements();
    }

    public void flush() {
        this.flushKeyEvents();
        this.flushMouseEvents();
    }

    public void handleKey(Consumer<Key> keyConsumer) {
        if (!this.keyEvents.isEmpty()) {
            keyConsumer.accept(this.keyEvents.pop());
        }
    }

    public void handleMouse(Consumer<Mouse> mouseConsumer) {
        if (!this.mouseEvents.isEmpty()) {
            mouseConsumer.accept(this.mouseEvents.pop());
        }
    }
}
