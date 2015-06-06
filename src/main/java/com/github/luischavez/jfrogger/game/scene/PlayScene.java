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
import com.github.luischavez.jfrogger.core.Event;
import com.github.luischavez.jfrogger.core.Game;
import com.github.luischavez.jfrogger.core.Interpolator;
import com.github.luischavez.jfrogger.core.Scene;
import com.github.luischavez.jfrogger.core.Sound;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.graphics.Sprite;
import com.github.luischavez.jfrogger.core.graphics.animation.Actor;
import com.github.luischavez.jfrogger.core.input.Input;
import com.github.luischavez.jfrogger.core.input.Key;
import com.github.luischavez.jfrogger.core.resource.ResourceManager;

import com.github.luischavez.jfrogger.game.Ground;
import com.github.luischavez.jfrogger.game.GroundData;
import com.github.luischavez.jfrogger.game.Spawn;
import com.github.luischavez.jfrogger.game.StaticSet;
import com.github.luischavez.jfrogger.game.component.Animable;
import com.github.luischavez.jfrogger.game.component.Movable;
import com.github.luischavez.jfrogger.game.component.Platformable;
import com.github.luischavez.jfrogger.game.component.Positionable;
import com.github.luischavez.jfrogger.game.component.Tileable;
import com.github.luischavez.jfrogger.game.entity.FrogEntity;
import com.github.luischavez.jfrogger.game.entity.ObjectEntity;
import com.github.luischavez.jfrogger.game.system.AnimationSystem;
import com.github.luischavez.jfrogger.game.system.CollisionSystem;
import com.github.luischavez.jfrogger.game.system.LevelSystem;
import com.github.luischavez.jfrogger.game.system.MovementSystem;
import com.github.luischavez.jfrogger.game.system.PlatformSystem;
import com.github.luischavez.jfrogger.game.system.SpawnSystem;

import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class PlayScene extends Scene {

    final static public int EASY = 0;
    final static public int NORMAL = 1;
    final static public int HARD = 2;

    /**
     * Movement limits
     */
    final static public int MIN_X = 0, MAX_X = Game.getWidth();
    final static public int MIN_Y = 32, MAX_Y = Game.getHeight() - 32;
    final static public int INITIAL_X = 32 * 7, INITIAL_Y = 32 * 1;

    /**
     * Current level data
     */
    private int level = 1;
    private int difficulty = PlayScene.EASY;
    private int intents = 5;
    private int points = 0;
    private int time = 0;

    /**
     * Time interpolator
     */
    private Interpolator timeInterpolator;

    /**
     * Renderers
     */
    final private TextRenderer renderer = new TextRenderer(new Font("Serif", Font.PLAIN, 16), true, true);

    /**
     * Ground tiles
     */
    private Sprite groundSprite;

    /**
     * Objects Sprite
     */
    private Sprite objectsSprite;

    /**
     * Ground
     */
    private Ground ground;

    /**
     * Ground tiles
     */
    private GroundData groundData;

    /**
     * Spawns
     */
    private List<Spawn> spawns;
    private StaticSet staticSet;

    private void loadLevel(int level) {
        this.ground = ResourceManager.load("gnd", "level/level_" + level + ".gnd");
        this.groundData = ResourceManager.load("dat", "level/level_" + level + ".dat");
    }

    private void handle(Key key, Entity entity) {
        Positionable positionable = entity.getComponent(Positionable.class);
        Movable movable = entity.getComponent(Movable.class);
        Animable animable = entity.getComponent(Animable.class);

        Interpolator interpolator = movable.getInterpolator();
        if (Objects.isNull(interpolator) || interpolator.finished()) {
            int x = positionable.getX();
            int y = positionable.getY();

            Actor actor = animable.getActor();

            switch (key.keyCode) {
                case Input.KEY_UP:
                    actor.action(PlayScene.MAX_Y > y + 32 ? "jump.up" : "stand.up");
                    break;
                case Input.KEY_DOWN:
                    actor.action(PlayScene.MIN_Y < y ? "jump.down" : "stand.down");
                    break;
                case Input.KEY_LEFT:
                    actor.action(PlayScene.MIN_X < x ? "jump.left" : "stand.left");
                    break;
                case Input.KEY_RIGHT:
                    actor.action(PlayScene.MAX_X > x + 32 ? "jump.right" : "stand.right");
                    break;
            }

            if (!actor.getCurrentAction().contains("stand")) {
                int from = key.isKey(Input.KEY_UP) | key.isKey(Input.KEY_DOWN) ? y : x;
                int to = from + (key.isKey(Input.KEY_UP) | key.isKey(Input.KEY_RIGHT) ? +32 : -32);

                String direction = key.isKey(Input.KEY_UP) | key.isKey(Input.KEY_DOWN) ? "y" : "x";
                interpolator = new Interpolator(from, to, actor.getCurrentAnimation().step);

                movable.setDirection(direction);
                movable.setInterpolator(interpolator);
            }
        }
    }

    private void drawWorld(GL2 gl2) {
        Image gnd;
        int index;

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 15; j++) {
                index = this.ground.tiles[i][j];

                if (1 > index) {
                    continue;
                }

                gnd = this.groundSprite.get(--index);

                gl2.glRasterPos2i(j * 32, Game.getHeight() - (32 * 2) - (i * 32));
                gl2.glDrawPixels(gnd.width, gnd.height, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, gnd.getData());
            }
        }

        gl2.glBegin(GL2.GL_LINES);
        gl2.glColor3f(1.0f, 1.0f, 1.0f);
        gl2.glVertex2i(PlayScene.MIN_X, PlayScene.MAX_Y);
        gl2.glVertex2i(PlayScene.MAX_X, PlayScene.MAX_Y);

        gl2.glVertex2i(PlayScene.MIN_X, PlayScene.MIN_Y);
        gl2.glVertex2i(PlayScene.MAX_X, PlayScene.MIN_Y);
        gl2.glEnd();
    }

    private void drawPause(GL2 gl2) {
        this.renderer.beginRendering(Game.getWidth(), Game.getHeight());

        this.renderer.setColor(1.0f, 1.0f, 1.0f, 1);
        this.renderer.draw("PAUSA", Game.getWidth() / 4, Game.getHeight() / 2);
        this.renderer.draw("Presiona s para volver al menu", Game.getWidth() / 4, Game.getHeight() / 4);

        this.renderer.flush();

        this.renderer.endRendering();
    }

    private void drawInfo(GL2 gl2) {
        this.renderer.beginRendering(Game.getWidth(), Game.getHeight());

        this.renderer.setColor(1.0f, 1.0f, 1.0f, 1);
        this.renderer.draw("Nivel: " + this.level, 32, Game.getHeight() - 16);

        this.renderer.setColor(1.0f, 1.0f, 1.0f, 1);

        switch (this.difficulty) {
            case PlayScene.EASY:
                this.renderer.draw("Dificultad: Facil", 32 * 4, Game.getHeight() - 16);
                break;
            case PlayScene.NORMAL:
                this.renderer.draw("Dificultad: Normal", 32 * 4, Game.getHeight() - 16);
                break;
            case PlayScene.HARD:
                this.renderer.draw("Dificultad: Dificil", 32 * 4, Game.getHeight() - 16);
                break;
        }

        this.renderer.draw("Intentos: " + this.intents, 32, 0 + 16);
        this.renderer.draw("Puntos: " + this.points, 32 * 4, 0 + 16);
        this.renderer.draw("Tiempo Restante: " + this.time + " segundos", 32 * 8, 0 + 16);

        this.renderer.flush();

        this.renderer.endRendering();
    }

    private void drawPlayer(GL2 gl2) {
        Entity player = this.artemis.getEntityByTag("player");

        Positionable positionable = player.getComponent(Positionable.class);
        Animable animable = player.getComponent(Animable.class);

        int x = positionable.getX();
        int y = positionable.getY();

        Sprite sprite = animable.getSprite();
        Actor actor = animable.getActor();

        gl2.glRasterPos2i(x, y);

        actor.act(sprite, (image) -> {
            gl2.glDrawPixels(image.width, image.height, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, image.getData());
        });
    }

    private void drawEnemies(GL2 gl2) {
        ImmutableBag<Entity> enemies = this.artemis.getEntitiesByGroup("enemy");

        for (int i = 0; i < enemies.size(); i++) {
            Entity entity = enemies.get(i);

            Positionable positionable = entity.getComponent(Positionable.class);
            Tileable tileable = entity.getComponent(Tileable.class);

            int x = positionable.getX();
            int y = positionable.getY();

            int tile = tileable.getTile();

            Image image = this.objectsSprite.get(tile);

            gl2.glRasterPos2i(0, 0);
            gl2.glBitmap(0, 0, 0, 0, x, y, null);

            gl2.glDrawPixels(image.width, image.height, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, image.getData());
        }
    }

    private void drawPlatforms(GL2 gl2) {
        ImmutableBag<Entity> platforms = this.artemis.getEntitiesByGroup("platform");

        for (int i = 0; i < platforms.size(); i++) {
            Entity entity = platforms.get(i);

            Positionable positionable = entity.getComponent(Positionable.class);
            Tileable tileable = entity.getComponent(Tileable.class);

            int x = positionable.getX();
            int y = positionable.getY();

            int tile = tileable.getTile();

            Image image = this.objectsSprite.get(tile);

            gl2.glRasterPos2i(0, 0);
            gl2.glBitmap(0, 0, 0, 0, x, y, null);

            gl2.glDrawPixels(image.width, image.height, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, image.getData());
        }
    }

    @Override
    protected void init() {
        Sound.stopAll();

        this.groundSprite = ResourceManager.load("spr", "level/gnd.spr");
        this.objectsSprite = ResourceManager.load("spr", "objects.spr");

        this.level = Game.get("level");
        this.difficulty = Game.get("difficulty");
        this.intents = Game.get("intents");
        this.points = Game.get("points");
        this.time = Game.get("time");

        this.timeInterpolator = new Interpolator(0, this.time, Interpolator.SECOND_IN_NANOS * this.time);

        this.spawns = ResourceManager.load("spw", "level/level_" + level + ".spw");
        this.staticSet = ResourceManager.load("set", "level/level_" + level + ".spw");

        this.loadLevel(this.level);

        Sound.background("level_" + level);

        Event.listen("player.score", (args) -> {
            Properties leaderboard = ResourceManager.load("leaderboard", "leaderboard.properties");

            String key = PlayScene.EASY == this.difficulty ? "easy" : (PlayScene.NORMAL == this.difficulty ? "normal" : "hard");

            Integer currentPoints = Game.get("points");

            Integer oldPoints = Integer.valueOf(leaderboard.getProperty(key, "0"));

            if (oldPoints < currentPoints) {
                leaderboard.setProperty(key, currentPoints.toString());

                try (FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + "/leaderboard.properties")) {
                    leaderboard.store(fileOutputStream, "");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PlayScene.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(PlayScene.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Event.listen("player.death", (args) -> {
            Game.put("intents", this.intents - 1);

            if (0 < this.intents - 1) {
                Game.scene("play");
            } else {
                Event.fire("player.score");

                this.intents = 0;

                Game.scene("title");
            }
        });

        Event.listen("player.collision", (args) -> {
            Event.fire("player.death");
        });

        Event.listen("player.collision.platform", (args) -> {
        });
    }

    @Override
    protected void configure(Artemis artemis) {
        artemis.system(new SpawnSystem());
        artemis.system(new AnimationSystem());
        artemis.system(new MovementSystem());
        artemis.system(new PlatformSystem(this.objectsSprite));
        artemis.system(new CollisionSystem(this.objectsSprite));
        artemis.system(new LevelSystem());

        artemis.entity("frog", new FrogEntity());
        artemis.entity("object", new ObjectEntity());

        artemis.make("frog", (entity) -> {
            artemis.tag("player", entity);

            entity.getComponent(Positionable.class).setPosition(PlayScene.INITIAL_X, PlayScene.INITIAL_Y);
        });

        this.staticSet.init();
    }

    @Override
    protected void handle(Input input) {
        input.handleKey((key) -> {
            Entity entity = this.artemis.getEntityByTag("player");

            if (key.isKey(Input.KEY_P)) {
                if (Game.getClock().isPaused()) {
                    Game.getClock().resume();
                } else {
                    Game.getClock().pause();
                }
            }

            if (!Game.getClock().isPaused()) {
                if (key.isKey(Input.KEY_UP) || key.isKey(Input.KEY_DOWN)
                        || key.isKey(Input.KEY_LEFT) || key.isKey(Input.KEY_RIGHT)) {
                    this.handle(key, entity);
                }
            } else {
                if (key.isKey(Input.KEY_S)) {
                    Game.getClock().resume();
                    Game.scene("title");
                }
            }
        });
    }

    @Override
    protected void paint(GL gl) {
        GL2 gl2 = gl.getGL2();

        /**
         * Draw pause state
         */
        if (Game.getClock().isPaused()) {
            this.drawPause(gl2);
            return;
        }

        /**
         * Draw World
         */
        this.drawWorld(gl2);

        /**
         * Draw platforms
         */
        this.drawPlatforms(gl2);

        /**
         * Draw player in world
         */
        this.drawPlayer(gl2);

        /**
         * Draw enemies
         */
        this.drawEnemies(gl2);

        /**
         * Draw level info
         */
        this.drawInfo(gl2);

        /**
         * World zoom
         */
        gl2.glPixelZoom(2f, 2f);

        gl2.glFlush();
    }

    @Override
    public void update(double delta) {
        if (0 == this.intents) {
            return;
        }

        super.update(delta);

        this.timeInterpolator.update(delta);
        this.spawns.forEach((spawn) -> spawn.update(delta));

        this.time = (int) (this.timeInterpolator.to - this.timeInterpolator.getCurrent());

        if (this.timeInterpolator.finished()) {
            Event.fire("player.death");
        }

        Entity player = this.artemis.getEntityByTag("player");

        Positionable positionable = player.getComponent(Positionable.class);

        int x = positionable.getX();
        int y = positionable.getY();

        if (0 < y && Game.getHeight() - 32 * 2 > y) {
            Interpolator interpolator = player.getComponent(Movable.class).getInterpolator();

            if (Objects.nonNull(interpolator) && interpolator.finished()) {
                if (!player.getComponent(Platformable.class).hasPlatform()) {
                    if (0 == this.groundData.data[13 - (y / 32)][x / 32]) {
                        Event.fire("player.death");
                    }
                }
            }
        }
    }
}
