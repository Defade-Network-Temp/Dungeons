package net.defade.dungeons.game;

import net.defade.yokura.amethyst.AmethystChunkLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;
import java.util.UUID;

public class GameInstance extends InstanceContainer {
    private final GameManager gameManager;

    private final String config;
    private boolean acceptsPlayers = true; // If the game can receive new players

    public GameInstance(GameManager gameManager) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD);
        this.gameManager = gameManager;

        AmethystChunkLoader chunkLoader = new AmethystChunkLoader(new AmethystMapSource());
        this.config = chunkLoader.getConfig();

        setChunkLoader(chunkLoader);
        chunkLoader.loadInstance(this);
    }

    public boolean canAcceptPlayers() {
        return acceptsPlayers;
    }

    public void setAcceptsPlayers(boolean acceptsPlayers) {
        this.acceptsPlayers = acceptsPlayers;
    }

    public void unregisterGame() {
        gameManager.unregisterGame(this);
    }
}
