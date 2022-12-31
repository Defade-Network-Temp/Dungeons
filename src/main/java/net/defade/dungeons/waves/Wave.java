package net.defade.dungeons.waves;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.defade.dungeons.zombies.classic.ZombieI;
import net.defade.dungeons.zombies.classic.ZombieII;
import net.defade.dungeons.zombies.classic.ZombieIII;
import net.defade.dungeons.zombies.classic.ZombieIV;
import net.defade.dungeons.zombies.classic.ZombieV;
import net.defade.dungeons.zombies.classic.ZombieVI;
import net.defade.dungeons.zombies.classic.ZombieVII;
import net.defade.dungeons.zombies.classic.ZombieVIII;
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
        if(zombieClass == ZombieIV.class) return new ZombieIV();
        if(zombieClass == ZombieV.class) return new ZombieV();
        if(zombieClass == ZombieVI.class) return new ZombieVI();
        if(zombieClass == ZombieVII.class) return new ZombieVII();
        if(zombieClass == ZombieVIII.class) return new ZombieVIII();
        throw new NoSuchElementException("The class " + zombieClass.getCanonicalName() + " has no supplier.");
    }

    public int getZombiesLeft() {
        return zombiesToSpawn.size();
    }

    public boolean hasZombiesLeft() {
        return zombiesToSpawn.size() > 0;
    }
}
