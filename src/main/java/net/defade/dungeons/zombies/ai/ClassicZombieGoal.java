package net.defade.dungeons.zombies.ai;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

public class ClassicZombieGoal extends FollowTargetGoal {
    private final double squaredReach;
    private final int attackCooldown;

    private int ticks = 0;

    public ClassicZombieGoal(@NotNull DungeonsEntity entity, double reach, int attackCooldown) {
        super(entity, reach - 1);
        this.squaredReach = reach * reach;
        this.attackCooldown = attackCooldown;
    }

    @Override
    public void tick(long time) {
        super.tick(time);
        entityCreature.lookAt(new Pos(target.getPosition().x(), target.getPosition().y() + target.getEyeHeight(), target.getPosition().z()));
        if (entityCreature.getDistanceSquared(target) <= squaredReach) {
            if (ticks == attackCooldown) {
                entityCreature.attack(target, true);
                ticks = 0;
            } else {
                ticks++;
            }
        }
    }
}
