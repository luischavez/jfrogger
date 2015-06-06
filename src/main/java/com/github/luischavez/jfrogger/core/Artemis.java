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

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 * @author Luis
 */
final public class Artemis implements Tick {

    /**
     * Builders
     */
    final private Map<String, Consumer<Entity>> builders;

    /**
     * World instance
     */
    final private World world;

    public Artemis() {
        this.builders = new HashMap<>();
        this.world = new World();

        this.setup();
    }

    private void setup() {
        this.world.setManager(new TagManager());
        this.world.setManager(new GroupManager());
    }

    protected void initialize() {
        this.world.initialize();
    }

    public Entity make(String entityName) {
        Consumer<Entity> builder = this.builders.get(entityName);

        if (Objects.nonNull(builder)) {
            Entity entity = this.world.createEntity();

            entity.addToWorld();

            builder.accept(entity);

            return entity;
        }

        return null;
    }

    public void make(String entityName, Consumer<Entity> entityConsumer) {
        Entity entity = this.make(entityName);

        if (Objects.nonNull(entity)) {
            entityConsumer.accept(entity);
        }
    }

    public void entity(String entityName, Consumer<Entity> builder) {
        this.builders.put(entityName, builder);
    }

    public void system(EntitySystem system) {
        this.world.setSystem(system);
    }

    public Entity getEntityByTag(String tag) {
        return this.world.getManager(TagManager.class).getEntity(tag);
    }

    public ImmutableBag<Entity> getEntitiesByGroup(String group) {
        return this.world.getManager(GroupManager.class).getEntities(group);
    }

    public void tag(String tag, Entity entity) {
        this.world.getManager(TagManager.class).register(tag, entity);
    }

    public void group(String group, Entity entity) {
        this.world.getManager(GroupManager.class).add(entity, group);
    }

    @Override
    public void update(double delta) {
        this.world.setDelta(delta);
        this.world.process();
    }
}
