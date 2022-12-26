package net.defade.dungeons.waves;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.defade.dungeons.zombies.classic.ZombieI;
import net.defade.dungeons.zombies.classic.ZombieII;
import net.defade.dungeons.zombies.classic.ZombieIII;

import java.util.List;
import java.util.NoSuchElementException;

public class Wave {
    private final List<Class<? extends DungeonsEntity>> zombiesToSpawn;

    public Wave(List<Class<? extends DungeonsEntity>> zombiesToSpawn) {
        this.zombiesToSpawn = zombiesToSpawn;
    }

    public DungeonsEntity getZombie() {
        Class<? extends DungeonsEntity> zombieClass = zombiesToSpawn.remove(0);

        if(zombieClass == ZombieI.class) return new ZombieI();
        if(zombieClass == ZombieII.class) return new ZombieII();
        if(zombieClass == ZombieIII.class) return new ZombieIII();
        throw new NoSuchElementException("The class " + zombieClass.getCanonicalName() + " has no supplier.");
    }

    public int getZombiesLeft() {
        return zombiesToSpawn.size();
    }

    public boolean hasZombiesLeft() {
        return zombiesToSpawn.size() > 0;
    }
}
