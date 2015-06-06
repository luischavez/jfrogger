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
import com.github.luischavez.jfrogger.core.Sound;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.resource.ResourceManager;

import com.github.luischavez.jfrogger.game.component.Buttonable;
import com.github.luischavez.jfrogger.game.component.Drawable;
import com.github.luischavez.jfrogger.game.component.Positionable;

/**
 *
 * @author Luis
 */
public class TitleScene extends UIScene {

    @Override
    protected void init() {
        super.init();

        Sound.stopAll();
        Sound.background("intro");
    }

    @Override
    protected void initializeComponents() {
        artemis.make("big_button", (entity) -> {
            entity.getComponent(Buttonable.class).setListener(() -> Game.scene("difficulty"));

            Image image = ResourceManager.load("image", "ui/play_button.ui");
            entity.getComponent(Drawable.class).setImage(image);

            entity.getComponent(Positionable.class).setX((Game.getWidth() / 2) - (image.width / 2));
            entity.getComponent(Positionable.class).setY(32 * 10);
        });

        artemis.make("big_button", (entity) -> {
            entity.getComponent(Buttonable.class).setListener(() -> Game.scene("leaderboard"));

            Image image = ResourceManager.load("image", "ui/leaderboard_button.ui");
            entity.getComponent(Drawable.class).setImage(image);

            entity.getComponent(Positionable.class).setX((Game.getWidth() / 2) - (image.width / 2));
            entity.getComponent(Positionable.class).setY(32 * 7);
        });
    }
}
