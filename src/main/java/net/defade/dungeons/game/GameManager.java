package net.defade.dungeons.game;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import java.util.HashSet;
import java.util.Set;

public class GameManager {
    private final Set<GameInstance> gameInstances = new HashSet<>();

    public GameManager() {
        refreshAvailableGames();

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, playerLoginEvent -> {
            Player player = playerLoginEvent.getPlayer();

            playerLoginEvent.setSpawningInstance(getAvailableGame());
        });
    }

    private void refreshAvailableGames() {
        int availableGames = 0;
        for (GameInstance gameInstance : gameInstances) {
            if(gameInstance.canAcceptPlayers()) {
                availableGames++;
            }
        }

        while (availableGames < 2) {
            availableGames++;
            createGame();
        }
    }

    private void createGame() {
        GameInstance gameInstance = new GameInstance(this);
        MinecraftServer.getInstanceManager().registerInstance(gameInstance);
        gameInstances.add(gameInstance);
    }

    public void unregisterGame(GameInstance gameInstance) {
        MinecraftServer.getInstanceManager().unregisterInstance(gameInstance);
        gameInstances.remove(gameInstance);
    }

    private GameInstance getAvailableGame() {
        GameInstance found = null;
        for (GameInstance gameInstance : gameInstances) {
            if(found == null) found = gameInstance;
            if(found.getPlayers().size() < gameInstance.getPlayers().size()) {
                found = gameInstance;
            }
        }

        return found;
    }
}
