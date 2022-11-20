package net.defade.dungeons.waves;

import net.minestom.server.entity.EntityCreature;

import java.util.List;
import java.util.NoSuchElementException;

public class Wave {
    private final List<Class<? extends EntityCreature>> zombiesToSpawn;
    private final int totalZombies;

    public Wave(List<Class<? extends EntityCreature>> zombiesToSpawn) {
        this.zombiesToSpawn = zombiesToSpawn;
        this.totalZombies = zombiesToSpawn.size();
    }

    public EntityCreature getZombie() {
        Class<? extends EntityCreature> zombieClass = zombiesToSpawn.remove(0);

        throw new NoSuchElementException("The class " + zombieClass.getCanonicalName() + " has no supplier.");
    }

    public int getZombiesLeft() {
        return zombiesToSpawn.size();
    }

    public boolean hasZombiesLeft() {
        return zombiesToSpawn.size() > 0;
    }
}
