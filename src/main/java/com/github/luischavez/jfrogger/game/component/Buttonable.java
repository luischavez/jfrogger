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
package com.github.luischavez.jfrogger.game.component;

import com.artemis.Component;

import java.util.EventListener;
import java.util.Objects;

/**
 *
 * @author Luis
 */
public class Buttonable extends Component {

    private boolean selected;
    private ButtonListener listener;

    public Buttonable(boolean selected) {
        this.selected = selected;
    }

    public Buttonable() {
        this(false);
    }

    public boolean isSelected() {
        return selected;
    }

    public ButtonListener getListener() {
        return listener;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setListener(ButtonListener listener) {
        this.listener = listener;
    }

    public void fireEvent() {
        if (Objects.nonNull(this.listener)) {
            this.listener.action();
        }
    }

    public interface ButtonListener extends EventListener {

        public void action();
    }
}
