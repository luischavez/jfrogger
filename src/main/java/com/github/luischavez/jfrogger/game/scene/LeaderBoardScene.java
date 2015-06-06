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

import com.github.luischavez.jfrogger.core.Game;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.resource.ResourceManager;

import com.github.luischavez.jfrogger.game.component.Buttonable;
import com.github.luischavez.jfrogger.game.component.Drawable;
import com.github.luischavez.jfrogger.game.component.Positionable;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Font;
import java.util.Properties;

/**
 *
 * @author Luis
 */
public class LeaderBoardScene extends UIScene {

    /**
     * Renderers
     */
    final private TextRenderer renderer = new TextRenderer(new Font("Serif", Font.PLAIN, 16), true, true);

    /**
     * leaderboard
     */
    private Properties leaderboard;

    @Override
    protected void init() {
        super.init();

        this.leaderboard = ResourceManager.load("leaderboard", "leaderboard.properties");
    }

    private void drawInfo(GL2 gl2) {
        this.renderer.beginRendering(Game.getWidth(), Game.getHeight());

        this.renderer.setColor(0.5f, 0.9f, 1.0f, 1);

        this.renderer.draw("PUNTUACIONES MAXIMAS", 32 * 8, 32 * 14);

        this.renderer.setColor(1.0f, 1.0f, 1.0f, 1);

        this.renderer.draw("Facil: " + leaderboard.getProperty("easy", "0"), 32 * 5, 32 * 11);
        this.renderer.draw("Normal: " + leaderboard.getProperty("normal", "0"), 32 * 5, 32 * 9);
        this.renderer.draw("Dificil: " + leaderboard.getProperty("hard", "0"), 32 * 5, 32 * 7);

        this.renderer.flush();

        this.renderer.endRendering();
    }

    @Override
    protected void initializeComponents() {
        artemis.make("button", (entity) -> {
            entity.getComponent(Buttonable.class).setListener(() -> Game.scene("title"));

            Image image = ResourceManager.load("image", "ui/back_button.ui");
            entity.getComponent(Drawable.class).setImage(image);

            entity.getComponent(Positionable.class).setX((Game.getWidth() / 2) - (image.width / 2));
            entity.getComponent(Positionable.class).setY(32 * 1);
        });
    }

    @Override
    protected void paint(GL gl) {
        super.paint(gl);

        this.drawInfo(gl.getGL2());
    }
}
