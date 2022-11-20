package net.defade.dungeons.zombies.classic;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.minestom.server.entity.EntityType;

public class ZombieI extends DungeonsEntity {

    public ZombieI() {
        super(EntityType.ZOMBIE, "Lvl 1", 20, 0, 0, 5, 1.2f, 0, 2);
    }
}
