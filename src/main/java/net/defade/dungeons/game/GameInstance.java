package net.defade.dungeons.game;

import net.defade.dungeons.difficulty.Difficulty;
import net.defade.dungeons.game.config.GameConfig;
import net.defade.dungeons.game.utils.AmethystMapSource;
import net.defade.dungeons.utils.GameEvents;
import net.defade.dungeons.game.utils.GameStartManager;
import net.defade.yokura.amethyst.AmethystChunkLoader;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;
import java.util.UUID;

public class GameInstance extends InstanceContainer {
    private final GameManager gameManager;

    private final GameConfig config;
    private boolean acceptsPlayers = true; // If the game can receive new players

    private final GameEvents gameEvents = new GameEvents(this);

    private final BossBar bossBar = BossBar.bossBar(
            Component.text("Démarrage... ").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                    .append(Component.text("(").color(NamedTextColor.GRAY))
                    .append(Component.text("10s").color(NamedTextColor.WHITE))
                    .append(Component.text(")").color(NamedTextColor.GRAY)),
            0f,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
    );

    public GameInstance(GameManager gameManager) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD);
        this.gameManager = gameManager;

        AmethystChunkLoader chunkLoader = new AmethystChunkLoader(new AmethystMapSource());
        this.config = new GameConfig(chunkLoader.getConfig());

        setChunkLoader(chunkLoader);
        chunkLoader.loadInstance(this);

        gameEvents.getPlayerEventNode().addListener(PlayerSpawnEvent.class, playerSpawnEvent -> {
            Player player = playerSpawnEvent.getPlayer();

            if(!acceptsPlayers) {
                // TODO check if the player was previously in the game and make him re-join
                return;
            }

            player.teleport(config.getSpawnPoint());
            player.showBossBar(bossBar);
        });

        new GameStartManager(this);
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

    public void start(Difficulty difficulty) {
        setAcceptsPlayers(false);
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public GameEvents getGameEvents() {
        return gameEvents;
    }
}
