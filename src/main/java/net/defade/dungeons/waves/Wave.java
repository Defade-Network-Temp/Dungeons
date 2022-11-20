package net.defade.dungeons.waves;

import net.minestom.server.entity.Entity;

import java.util.List;
import java.util.NoSuchElementException;

public class Wave {
    private final List<Class<? extends Entity>> zombiesToSpawn;
    private final int totalZombies;

    public Wave(List<Class<? extends Entity>> zombiesToSpawn) {
        this.zombiesToSpawn = zombiesToSpawn;
        this.totalZombies = zombiesToSpawn.size();
    }

    public Entity getZombie() {
        Class<? extends Entity> zombieClass = zombiesToSpawn.remove(0);

        throw new NoSuchElementException("The class " + zombieClass.getCanonicalName() + " has no supplier.");
    }

    public int getZombiesLeft() {
        return zombiesToSpawn.size();
    }

    public boolean hasZombiesLeft() {
        return zombiesToSpawn.size() > 0;
    }
}
