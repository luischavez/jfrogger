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
package com.github.luischavez.jfrogger.game;

import com.github.luischavez.jfrogger.core.Clock;
import com.github.luischavez.jfrogger.core.Game;
import com.github.luischavez.jfrogger.core.Sound;
import com.github.luischavez.jfrogger.core.resource.ActorResourceHandler;
import com.github.luischavez.jfrogger.core.resource.ImageResourceHandler;
import com.github.luischavez.jfrogger.core.resource.ResourceManager;
import com.github.luischavez.jfrogger.core.resource.SoundResourceHandler;
import com.github.luischavez.jfrogger.core.resource.SpriteResourceHandler;

import com.github.luischavez.jfrogger.game.resource.GroundDataResourceHandler;
import com.github.luischavez.jfrogger.game.resource.GroundResourceHandler;
import com.github.luischavez.jfrogger.game.resource.LeaderboardResourceHandler;
import com.github.luischavez.jfrogger.game.resource.SpawnResourceHandler;
import com.github.luischavez.jfrogger.game.resource.StaticResourceHandler;
import com.github.luischavez.jfrogger.game.scene.DifficultyScene;
import com.github.luischavez.jfrogger.game.scene.LeaderBoardScene;
import com.github.luischavez.jfrogger.game.scene.PlayScene;
import com.github.luischavez.jfrogger.game.scene.TitleScene;

/**
 *
 * @author Luis
 */
public class Main {

    static public void main(String... args) {
        ResourceManager.register("spr", new SpriteResourceHandler());
        ResourceManager.register("act", new ActorResourceHandler());
        ResourceManager.register("image", new ImageResourceHandler());
        ResourceManager.register("sound", new SoundResourceHandler());
        ResourceManager.register("gnd", new GroundResourceHandler());
        ResourceManager.register("dat", new GroundDataResourceHandler());
        ResourceManager.register("spw", new SpawnResourceHandler());
        ResourceManager.register("set", new StaticResourceHandler());
        ResourceManager.register("leaderboard", new LeaderboardResourceHandler());

        Sound.register("intro");
        Sound.register("win");
        Sound.register("level_1");
        Sound.register("level_2");
        Sound.register("level_3");

        Clock clock = new Clock(60, 60);

        clock.setListener((ups, fps) -> {
            Game.getSurface().setTitle("Ranita v6 :: FPS: " + fps);
        });

        Game.setClock(clock);

        Game.setSize(480, 480);

        Game.register("title", TitleScene::new);
        Game.register("difficulty", DifficultyScene::new);
        Game.register("leaderboard", LeaderBoardScene::new);
        Game.register("play", PlayScene::new);

        Game.scene("title");
        Game.start();
    }
}
