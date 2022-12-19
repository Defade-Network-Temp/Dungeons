package net.defade.dungeons.zombies.classic;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.defade.dungeons.zombies.ai.ClassicZombieGoal;
import net.defade.dungeons.zombies.ai.PlayerTargetSelector;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;

public class ZombieI extends DungeonsEntity {

    public ZombieI() {
        super(EntityType.ZOMBIE, "Lvl 1", 20, 0, 0, 5, 0.15, 0, 2);

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new ClassicZombieGoal(this, 2, 25))
                        .addTargetSelector(new PlayerTargetSelector(this))
                        .build()
        );
    }
}
