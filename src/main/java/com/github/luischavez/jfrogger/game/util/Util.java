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
package com.github.luischavez.jfrogger.game.util;

import com.github.luischavez.jfrogger.core.Interpolator;
import com.github.luischavez.jfrogger.core.graphics.Image;
import com.github.luischavez.jfrogger.core.graphics.Sprite;
import com.github.luischavez.jfrogger.core.graphics.animation.Actor;
import com.github.luischavez.jfrogger.core.graphics.animation.Animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

/**
 *
 * @author Luis Chávez {@literal <https://github.com/luischavez>}
 */
public class Util {

    public static Image image(String name) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(Util.class.getResource("/raw/" + name + ".png"));
        Image image = new Image(bufferedImage.getWidth(), bufferedImage.getHeight());
        for (int i = 0; i < image.width; i++) {
            for (int j = 0; j < image.height; j++) {
                int pixel = bufferedImage.getRGB(i, j);
                image.setPixel(i, image.height - 1 - j,
                        (pixel >> 16) & 0xFF,
                        (pixel >> 8) & 0xFF,
                        (pixel >> 0) & 0xFF,
                        (pixel >> 24) & 0xFF);
            }
        }
        return image;
    }

    public static void write(Object object, String name, String folder, String type) throws IOException {
        String path = System.getProperty("user.dir");
        File file = new File(path + "/" + folder + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path + "/" + folder + "/" + name + "." + type));
        outputStream.writeObject(object);
    }

    public static void writeImage(String name, String folder, String type) throws IOException {
        Image image = Util.image(name);
        Util.write(image, name, folder, type);
    }

    public static void writeSprite(String name, String[] parts, String folder) throws IOException {
        Image[] images = new Image[parts.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = Util.image(parts[i]);
        }
        Sprite sprite = Sprite.create(images);
        Util.write(sprite, name, folder, "spr");
    }

    public static void writeActor(String name, String folder, Consumer<Actor> consumer) throws IOException {
        Actor actor = new Actor();
        consumer.accept(actor);
        Util.write(actor, name, folder, "act");
    }

    public static void main(String... args) throws IOException {
        Util.writeImage("back_button", "res/ui", "ui");
        Util.writeImage("bg", "res/ui", "ui");
        Util.writeImage("button_b", "res/ui", "ui");
        Util.writeImage("button_m", "res/ui", "ui");
        Util.writeImage("easy_button", "res/ui", "ui");
        Util.writeImage("hard_button", "res/ui", "ui");
        Util.writeImage("leaderboard_button", "res/ui", "ui");
        Util.writeImage("normal_button", "res/ui", "ui");
        Util.writeImage("play_button", "res/ui", "ui");
        Util.writeImage("select_bl", "res/ui", "ui");
        Util.writeImage("select_br", "res/ui", "ui");
        Util.writeImage("select_ul", "res/ui", "ui");
        Util.writeImage("select_ur", "res/ui", "ui");

        Util.writeSprite("gnd", new String[]{
            "gnd_1", "gnd_2", "gnd_3", "gnd_4", "gnd_5"
        }, "res/level");
        Util.writeSprite("frog", new String[]{
            "frog_jump_down", "frog_jump_left", "frog_jump_right", "frog_jump_up",
            "frog_land_down", "frog_land_left", "frog_land_right", "frog_land_up",
            "frog_stand_down", "frog_stand_left", "frog_stand_right", "frog_stand_up"
        }, "res");
        Util.writeSprite("objects", new String[]{
            "car_1_left", "car_1_right",
            "car_2_left", "car_2_right",
            "car_3_left", "car_3_right",
            "leaf_1", "leaf_2",
            "trunk", "life"
        }, "res");

        Util.writeActor("frog", "res", actor -> {
            actor.addAnimation("jump.down", new Animation(Interpolator.SECOND_IN_NANOS / 3, 8, 0, 4, 8));
            actor.addAnimation("jump.left", new Animation(Interpolator.SECOND_IN_NANOS / 3, 9, 1, 5, 9));
            actor.addAnimation("jump.right", new Animation(Interpolator.SECOND_IN_NANOS / 3, 10, 2, 6, 10));
            actor.addAnimation("jump.up", new Animation(Interpolator.SECOND_IN_NANOS / 3, 11, 3, 7, 11));

            actor.addAnimation("stand.down", new Animation(Interpolator.SECOND_IN_NANOS, 8));
            actor.addAnimation("stand.left", new Animation(Interpolator.SECOND_IN_NANOS, 9));
            actor.addAnimation("stand.right", new Animation(Interpolator.SECOND_IN_NANOS, 10));
            actor.addAnimation("stand.up", new Animation(Interpolator.SECOND_IN_NANOS, 11));
        });
    }
}
