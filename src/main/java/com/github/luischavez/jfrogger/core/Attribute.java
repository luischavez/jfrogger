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

import java.util.Objects;

/**
 *
 * @author Luis
 */
public class Attribute {

    final public Class<?> type;
    final public String name;
    private Object value;

    public Attribute(String name, Object defaultValue) {
        this.type = defaultValue.getClass();
        this.name = name;
        this.value = defaultValue;
    }

    public Attribute(String name, Class<?> type) {
        this.type = type;
        this.name = name;
        this.value = null;
    }

    public <T> T getValue() {
        return (T) this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Attribute) {
            Attribute attribute = Attribute.class.cast(obj);
            return attribute.name.equals(this.name);
        }

        if (obj instanceof String) {
            return obj.equals(this.name);
        }

        return Objects.equals(this, obj);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
