package net.defade.dungeons.zombies.ai;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;

public class ClassicZombieGoal extends GoalSelector {

    private final Cooldown cooldown = new Cooldown(Duration.of(5, TimeUnit.SERVER_TICK));

    private long lastHit;
    private final double squaredRange;
    private final Duration delay;

    private boolean stop;
    private Player target;

    public ClassicZombieGoal(@NotNull DungeonsEntity entity, double range, Duration delay) {
        super(entity);
        this.squaredRange = range * range;
        this.delay = delay;
    }

    @Override
    public boolean shouldStart() {
        findAndSetTarget();
        return target != null;
    }

    @Override
    public void start() {
        final Point targetPosition = this.target.getPosition();
        entityCreature.getNavigator().setPathTo(targetPosition, true);
    }

    @Override
    public void tick(long time) {
        Entity target;
        if (this.target != null) {
            target = this.target;
        } else {
            target = findTarget();
        }

        this.stop = target == null;

        if (!stop) {
            entityCreature.lookAt(target);
            if (entityCreature.getDistanceSquared(target) <= squaredRange) {
                if (!Cooldown.hasCooldown(time, lastHit, delay)) {
                    entityCreature.attack(target, true);
                    this.lastHit = time;
                }
                return;
            }

            Navigator navigator = entityCreature.getNavigator();
            final var pathPosition = navigator.getPathPosition();
            final var targetPosition = target.getPosition();
            if (pathPosition == null || !pathPosition.samePoint(targetPosition)) {
                if (this.cooldown.isReady(time)) {
                    this.cooldown.refreshLastUpdate(time);
                    navigator.setPathTo(targetPosition);
                }
            }
        }
    }

    @Override
    public boolean shouldEnd() {
        return stop;
    }

    @Override
    public void end() {
        entityCreature.getNavigator().setPathTo(null);
    }

    private void findAndSetTarget() {
        Instance instance = entityCreature.getInstance();
        if (instance == null) {
            return;
        }

        Player target = null;
        double squaredDistance = -1;
        for (Player player : entityCreature.getInstance().getPlayers()) {
            if(target == null) {
                target = player;
                squaredDistance = target.getDistanceSquared(entityCreature);
                continue;
            }

            double newDistance = player.getDistanceSquared(entityCreature);
            if(newDistance < squaredDistance) {
                target = player;
                squaredDistance = newDistance;
            }
        }

        entityCreature.setTarget(target);
        this.target = target;
    }
}
