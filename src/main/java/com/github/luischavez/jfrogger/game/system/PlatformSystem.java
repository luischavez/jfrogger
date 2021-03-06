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
package com.github.luischavez.jfrogger.game.system;

import com.github.luischavez.jfrogger.core.Event;
import com.github.luischavez.jfrogger.core.Game;
import com.github.luischavez.jfrogger.core.Interpolator;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.graphics.Sprite;
import com.github.luischavez.jfrogger.core.graphics.animation.Actor;
import com.github.luischavez.jfrogger.core.math.Rectangle;

import com.github.luischavez.jfrogger.game.component.Animable;
import com.github.luischavez.jfrogger.game.component.Movable;
import com.github.luischavez.jfrogger.game.component.Platformable;
import com.github.luischavez.jfrogger.game.component.Positionable;
import com.github.luischavez.jfrogger.game.component.Tileable;

import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.ImmutableBag;

import java.util.Objects;

/**
 *
 * @author Luis
 */
public class PlatformSystem extends VoidEntitySystem {

    final private Sprite objectsSprite;

    public PlatformSystem(Sprite objectsSprite) {
        this.objectsSprite = objectsSprite;
    }

    private boolean inPlatform(Entity platform) {
        Entity player = Game.getArtemis().getEntityByTag("player");

        int x1 = player.getComponent(Positionable.class).getX();
        int y1 = player.getComponent(Positionable.class).getY();

        int x2 = platform.getComponent(Positionable.class).getX();
        int y2 = platform.getComponent(Positionable.class).getY();

        Sprite sprite = player.getComponent(Animable.class).getSprite();
        Actor actor = player.getComponent(Animable.class).getActor();

        Image playerImage = sprite.get(actor.getCurrentAnimation().getIndex());
        Image image = this.objectsSprite.get(platform.getComponent(Tileable.class).getTile());

        int width1 = playerImage.width * 2;
        int height1 = playerImage.height * 2;

        int width2 = image.width * 2;
        int height2 = image.height * 2;

        Rectangle rectangle1 = new Rectangle(x1, y1, width1, height1);
        Rectangle rectangle2 = new Rectangle(x2, y2, width2, height2);

        return rectangle1.overlaps(rectangle2);
    }

    @Override
    protected void processSystem() {
        Entity player = Game.getArtemis().getEntityByTag("player");

        ImmutableBag<Entity> platforms = Game.getArtemis().getEntitiesByGroup("platform");

        Interpolator interpolator = player.getComponent(Movable.class).getInterpolator();

        if (Objects.nonNull(interpolator)) {
            if (interpolator.finished()) {
                boolean inPlatform = false;

                for (int i = 0; i < platforms.size(); i++) {
                    Entity platform = platforms.get(i);

                    if (this.inPlatform(platform)) {
                        Event.fire("player.collision.platform", platform);

                        player.getComponent(Platformable.class).setPlatform(platform);

                        inPlatform = true;

                        break;
                    }
                }

                if (!inPlatform) {
                    player.getComponent(Platformable.class).setPlatform(null);
                }
            }
        }
    }
}
