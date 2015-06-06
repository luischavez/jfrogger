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

import com.github.luischavez.jfrogger.core.graphics.Surface;
import com.github.luischavez.jfrogger.core.input.Input;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 *
 * @author Luis
 */
final public class Game {

    /**
     * Game objects
     */
    final private Surface surface;
    final private Input input;

    /**
     * Clock instance
     */
    private Clock clock;

    /**
     * Game scenes
     */
    final private Map<String, Supplier<Scene>> scenes;

    /**
     * Current scane
     */
    private Scene currentScene;

    /**
     * Game registry
     */
    final private Map<String, Object> registry;

    private Game() {
        this.clock = new Clock(30, 60);
        this.surface = Surface.getSurface();
        this.input = new Input(surface);

        this.scenes = new HashMap<>();

        this.registry = new HashMap<>();

        this.registerEventListeners();
    }

    private void registerEventListeners() {
        Event.listen("Surface.disposed", (args) -> {
            clock.stop();
            Sound.closeAll();
        });
    }

    static public Artemis getArtemis() {
        if (Objects.nonNull(Game.getInstance().currentScene)) {
            return Game.getInstance().currentScene.artemis;
        }

        return null;
    }

    static public Input getInput() {
        return Game.getInstance().input;
    }

    static public int getWidth() {
        return Game.getInstance().surface.getCanvas().getWidth();
    }

    static public int getHeight() {
        return Game.getInstance().surface.getCanvas().getHeight();
    }

    static public void setSize(int width, int height) {
        Game.getInstance().surface.setSize(width, height);
    }

    static public Clock getClock() {
        return Game.getInstance().clock;
    }

    static public void setClock(Clock clock) {
        Game.getInstance().clock = clock;
    }

    static public Surface getSurface() {
        return Game.getInstance().surface;
    }

    static public void start() {
        Game game = Game.getInstance();
        game.clock.start((delta) -> {
            game.currentScene.handle(game.input);
            game.currentScene.update(delta);

            game.input.flush();
        }, game.surface);
    }

    static public void register(String sceneName, Supplier<Scene> sceneSupplier) {
        Game.getInstance().scenes.put(sceneName, sceneSupplier);
    }

    static public void scene(String sceneName) {
        Supplier<Scene> sceneSupplier = Game.getInstance().scenes.get(sceneName);

        if (Objects.nonNull(sceneSupplier)) {
            Scene scene = sceneSupplier.get();

            Game.getInstance().currentScene = scene;

            scene.setup();

            Game.getInstance().surface.gl(scene::paint);
        }
    }

    static public <T> T get(String key) {
        return (T) Game.getInstance().registry.get(key);
    }

    static public void put(String key, Object value) {
        Game.getInstance().registry.put(key, value);
    }

    static private Game getInstance() {
        return Game.GameHolder.INSTANCE;
    }

    final static private class GameHolder {

        final static private Game INSTANCE = new Game();
    }
}
