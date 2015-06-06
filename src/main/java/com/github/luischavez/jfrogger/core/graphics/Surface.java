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

import com.github.luischavez.jfrogger.core.Event;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Luis
 */
public class Surface implements com.github.luischavez.jfrogger.core.Frame {

    /**
     * OGL components
     */
    final private GLProfile profile;
    final private GLCapabilities capabilities;
    final private GLCanvas canvas;
    final private GLU glu;
    final private GLUT glut;
    final private GLEventListener gLEventListener;

    /**
     * AWT components
     */
    final private Frame frame;

    /**
     * Consumers
     */
    private Consumer<GL> glConsumer;

    private Surface() {
        this.profile = GLProfile.get(GLProfile.GL2);
        this.capabilities = new GLCapabilities(this.profile);
        this.canvas = new GLCanvas(this.capabilities);
        this.glu = new GLU();
        this.glut = new GLUT();
        this.gLEventListener = new DefaultEventListener();

        this.frame = new Frame();

        this.setup();
    }

    private void setup() {
        this.capabilities.setDoubleBuffered(false);

        this.canvas.addGLEventListener(this.gLEventListener);

        this.frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
                Event.fire("Surface.disposed");
            }
        });

        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.add(this.canvas);
        this.frame.setVisible(true);

        this.canvas.requestFocusInWindow();
    }

    public GLCanvas getCanvas() {
        return this.canvas;
    }

    public boolean isVisible() {
        return this.frame.isVisible();
    }

    public void setVisible(boolean visible) {
        this.frame.setVisible(visible);
    }

    public void hide() {
        this.setVisible(false);
    }

    public void show() {
        this.setVisible(true);
    }

    public void close() {
        this.frame.dispose();
    }

    public void setTitle(String title) {
        this.frame.setTitle(title);
    }

    public void setSize(int width, int height) {
        this.canvas.setSize(width, height);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    synchronized public void gl(Consumer<GL> glConsumer) {
        this.glConsumer = glConsumer;
    }

    @Override
    public void update() {
        this.canvas.display();
    }

    final private class DefaultEventListener implements GLEventListener {

        @Override
        public void init(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();

            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glShadeModel(GL2.GL_FLAT);

            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

            gl.glDisable(GL2.GL_ALPHA_TEST);
            gl.glDisable(GL2.GL_BLEND);
            gl.glDisable(GL2.GL_DEPTH_TEST);
            gl.glDisable(GL2.GL_DITHER);
            gl.glDisable(GL2.GL_FOG);
            gl.glDisable(GL2.GL_LIGHTING);
            gl.glDisable(GL2.GL_LOGIC_OP);
            gl.glDisable(GL2.GL_STENCIL_TEST);
            gl.glDisable(GL2.GL_TEXTURE_1D);
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glPixelTransferi(GL2.GL_MAP_COLOR, GL2.GL_FALSE);
            gl.glPixelTransferi(GL2.GL_RED_SCALE, 1);
            gl.glPixelTransferi(GL2.GL_RED_BIAS, 0);
            gl.glPixelTransferi(GL2.GL_GREEN_SCALE, 1);
            gl.glPixelTransferi(GL2.GL_GREEN_BIAS, 0);
            gl.glPixelTransferi(GL2.GL_BLUE_SCALE, 1);
            gl.glPixelTransferi(GL2.GL_BLUE_BIAS, 0);
            gl.glPixelTransferi(GL2.GL_ALPHA_SCALE, 1);
            gl.glPixelTransferi(GL2.GL_ALPHA_BIAS, 0);
        }

        @Override
        public void dispose(GLAutoDrawable drawable) {

        }

        @Override
        public void display(GLAutoDrawable drawable) {
            GL2 gl = drawable.getGL().getGL2();

            gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL2.GL_BLEND);

            if (Objects.nonNull(glConsumer)) {
                glConsumer.accept(gl);
            }
        }

        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            GL2 gl = drawable.getGL().getGL2();

            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            glu.gluOrtho2D(0, width, 0, height);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
        }
    }

    static public Surface getSurface() {
        return Surface.SurfaceHolder.SURFACE;
    }

    final static private class SurfaceHolder {

        final static private Surface SURFACE = new Surface();
    }
}
