package net.defade.dungeons.game;

import net.defade.bismuth.core.servers.ServerStatus;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import java.util.HashSet;
import java.util.Set;

public class GameManager {
    private static final int MAX_GAMES_PER_SERVER = 15;
    private final Set<GameInstance> gameInstances = new HashSet<>();

    public GameManager() {
        refreshAvailableGames();

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, playerLoginEvent -> {
            Player player = playerLoginEvent.getPlayer();

            playerLoginEvent.setSpawningInstance(getAvailableGame());
        });
    }

    private void refreshAvailableGames() {
        int totalGames = gameInstances.size();
        if(totalGames == MAX_GAMES_PER_SERVER) {
            if(MinecraftServer.getServerAPI().getServerStatus() != ServerStatus.FULL) {
                MinecraftServer.getServerAPI().setServerStatus(ServerStatus.FULL);
            }
            return;
        } else if(MinecraftServer.getServerAPI().getServerStatus() != ServerStatus.ACCEPTING_PLAYERS) {
            MinecraftServer.getServerAPI().setServerStatus(ServerStatus.ACCEPTING_PLAYERS);
        }

        int availableGames = 0;
        for (GameInstance gameInstance : gameInstances) {
            if(gameInstance.canAcceptPlayers()) {
                availableGames++;
            }
        }

        while (availableGames < 2 && gameInstances.size() < MAX_GAMES_PER_SERVER) {
            availableGames++;
            createGame();
        }
    }

    private void createGame() {
        GameInstance gameInstance = new GameInstance(this);
        MinecraftServer.getInstanceManager().registerInstance(gameInstance);
        gameInstance.postInit();

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
