package net.defade.dungeons.zombies.ai;

import net.defade.dungeons.game.GameInstance;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.TargetSelector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTargetSelector extends TargetSelector {
    private Player player = null;

    public PlayerTargetSelector(@NotNull EntityCreature entityCreature) {
        super(entityCreature);
    }

    @Override
    public @Nullable Entity findTarget() {
        GameInstance gameInstance = (GameInstance) entityCreature.getInstance();
        if(gameInstance == null) {
            return null;
        }

        if(player == null || gameInstance.getFightHandler().isDead(player)) {
            double closestPlayerDistance = Integer.MAX_VALUE;

            for(Player players : gameInstance.getPlayers()) {
                if(gameInstance.getFightHandler().isDead(players)) continue;

                if(entityCreature.getDistanceSquared(players) < closestPlayerDistance) {
                    closestPlayerDistance = entityCreature.getDistanceSquared(players);
                    player = players;
                }
            }
        }

        return player;
    }
}
