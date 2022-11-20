package net.defade.dungeons.waves;

import net.minestom.server.entity.Entity;

import java.util.List;
import java.util.NoSuchElementException;

public class Wave {
    private final List<Class<? extends Entity>> zombiesToSpawn;

    public Wave(List<Class<? extends Entity>> zombiesToSpawn) {
        this.zombiesToSpawn = zombiesToSpawn;
    }

    public Entity getZombie() {
        Class<? extends Entity> zombieClass = zombiesToSpawn.remove(0);

        throw new NoSuchElementException("The class " + zombieClass.getCanonicalName() + " has no supplier.");
    }
}
