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
package com.github.luischavez.jfrogger.game.scene;

import com.github.luischavez.jfrogger.core.Artemis;
import com.github.luischavez.jfrogger.core.Scene;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.input.Input;
import com.github.luischavez.jfrogger.core.input.Mouse;
import com.github.luischavez.jfrogger.core.resource.ResourceManager;

import com.github.luischavez.jfrogger.game.component.Buttonable;
import com.github.luischavez.jfrogger.game.component.Drawable;
import com.github.luischavez.jfrogger.game.component.Positionable;
import com.github.luischavez.jfrogger.game.entity.ui.BigButton;
import com.github.luischavez.jfrogger.game.entity.ui.Button;

import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/**
 *
 * @author Luis
 */
abstract public class UIScene extends Scene {

    /**
     * UI components
     */
    private Image bg;
    private Image selectul, selectur, selectbl, selectbr;

    abstract protected void initializeComponents();

    private void drawCorners(GL gl, int x, int y, int width, int height) {
        GL2 gl2 = gl.getGL2();

        int x2 = x + width;
        int y2 = y + height;

        gl2.glRasterPos2i(x, y);
        gl2.glDrawPixels(this.selectbl.width, this.selectbl.height, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, this.selectbl.getData());

        gl2.glRasterPos2i(x2 - this.selectbr.width, y);
        gl2.glDrawPixels(this.selectbr.width, this.selectbr.height, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, this.selectbr.getData());

        gl2.glRasterPos2i(x, y2 - this.selectul.height);
        gl2.glDrawPixels(this.selectul.width, this.selectul.height, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, this.selectul.getData());

        gl2.glRasterPos2i(x2 - this.selectur.width, y2 - this.selectur.height);
        gl2.glDrawPixels(this.selectur.width, this.selectur.height, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, this.selectur.getData());
    }

    @Override
    protected void init() {
        this.bg = ResourceManager.load("image", "ui/bg.ui");
        this.selectul = ResourceManager.load("image", "ui/select_ul.ui");
        this.selectur = ResourceManager.load("image", "ui/select_ur.ui");
        this.selectbl = ResourceManager.load("image", "ui/select_bl.ui");
        this.selectbr = ResourceManager.load("image", "ui/select_br.ui");
    }

    @Override
    protected void configure(Artemis artemis) {
        artemis.entity("button", new Button());
        artemis.entity("big_button", new BigButton());

        this.initializeComponents();
    }

    @Override
    protected void handle(Input input) {
        input.handleMouse((mouse) -> {
            ImmutableBag<Entity> entities = this.artemis.getEntitiesByGroup("ui");

            if (Mouse.Action.CLICK == mouse.action) {
                int x = mouse.x;
                int y = mouse.y;

                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = entities.get(i);

                    int entityX = entity.getComponent(Positionable.class).getX();
                    int entityY = entity.getComponent(Positionable.class).getY();

                    Image image = entity.getComponent(Drawable.class).getImage();

                    if ((x >= entityX && x <= entityX + image.width)
                            && (y >= entityY && y <= entityY + image.height)) {
                        entity.getComponent(Buttonable.class).fireEvent();
                    }
                }
            }

            if (Mouse.Action.MOVED == mouse.action) {
                int x = mouse.x;
                int y = mouse.y;

                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = entities.get(i);

                    int entityX = entity.getComponent(Positionable.class).getX();
                    int entityY = entity.getComponent(Positionable.class).getY();

                    Image image = entity.getComponent(Drawable.class).getImage();

                    if ((x >= entityX && x <= entityX + image.width)
                            && (y >= entityY && y <= entityY + image.height)) {
                        entity.getComponent(Buttonable.class).setSelected(true);
                    } else {
                        entity.getComponent(Buttonable.class).setSelected(false);
                    }
                }
            }

            input.flushMouseEvents();
        });
    }

    @Override
    protected void paint(GL gl) {
        GL2 gl2 = gl.getGL2();

        gl2.glPixelZoom(1f, 1f);

        gl2.glRasterPos2i(0, 0);
        gl2.glDrawPixels(this.bg.width, this.bg.height, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, this.bg.getData());

        ImmutableBag<Entity> entities = this.artemis.getEntitiesByGroup("ui");

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            int x = entity.getComponent(Positionable.class).getX();
            int y = entity.getComponent(Positionable.class).getY();

            Image image = entity.getComponent(Drawable.class).getImage();

            gl2.glRasterPos2i(x, y);
            gl2.glDrawPixels(image.width, image.height, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, image.getData());

            boolean selected = entity.getComponent(Buttonable.class).isSelected();

            if (selected) {
                this.drawCorners(gl, x, y, image.width, image.height);
            }
        }
    }
}
