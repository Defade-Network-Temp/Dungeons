package net.defade.dungeons.zombies.ai;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.utils.time.Cooldown;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class FollowTargetGoal extends GoalSelector {
    private final double stopPoint;
    private final Cooldown pathCooldown = new Cooldown(Duration.ofSeconds(1));
    protected Entity target;

    public FollowTargetGoal(@NotNull DungeonsEntity entityCreature, double stopPoint) {
        super(entityCreature);
        this.stopPoint = stopPoint;
    }

    @Override
    public boolean shouldStart() {
        target = findTarget();
        return target != null;
    }

    @Override
    public void start() {

    }

    @Override
    public void tick(long time) {
        double distance = entityCreature.getDistanceSquared(target);
        if (distance > stopPoint && pathCooldown.isReady(time)) {
            entityCreature.getNavigator().setPathTo(target.getPosition(), true);
            pathCooldown.refreshLastUpdate(time);
        } else if (distance <= stopPoint) {
            entityCreature.getNavigator().setPathTo(null);
        }
    }

    @Override
    public boolean shouldEnd() {
        return target == null;
    }

    @Override
    public void end() {
        entityCreature.getNavigator().setPathTo(null);
    }
}
