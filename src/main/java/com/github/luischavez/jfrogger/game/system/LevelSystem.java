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
import com.github.luischavez.jfrogger.core.Sound;

import com.github.luischavez.jfrogger.game.component.Positionable;

import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;

import java.util.Objects;

/**
 *
 * @author Luis
 */
public class LevelSystem extends VoidEntitySystem {

    @Override
    protected void processSystem() {
        Entity player = Game.getArtemis().getEntityByTag("player");

        if (Objects.isNull(player)) {
            return;
        }

        Positionable positionable = player.getComponent(Positionable.class);

        if (positionable.getY() == 32 * 13) {
            Sound.stopAll();
            Sound.sound("win");

            //while (Sound.isRunning("win"));
            int level = Game.get("level");

            if (3 > level) {
                Game.put("level", ++level);
                Game.put("points", ((int) Game.get("points")) + ((int) Game.get("intents") * 100));
                Game.scene("play");
            } else {
                Game.put("points", ((int) Game.get("points")) + ((int) Game.get("intents") * 100));
                Game.scene("title");
            }

            Event.fire("player.score");
        }
    }
}
