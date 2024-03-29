package net.defade.dungeons.game.utils;

import net.defade.dungeons.difficulty.DifficultyVote;
import net.defade.dungeons.game.GameInstance;
import net.defade.dungeons.gui.DifficultySelectorGUI;
import net.defade.dungeons.utils.GameEvents;
import net.defade.dungeons.utils.ItemList;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import java.time.Duration;
import java.util.Map;

public class GameStartManager implements Runnable {
    private static final Map<Integer, Integer> PLAYER_COUNT_TIMER = Map.ofEntries(
            Map.entry(2, 10), //TODO set to 60
            Map.entry(3, 30),
            Map.entry(4, 10)
    );

    private final GameInstance gameInstance;
    private final DifficultyVote difficultyVote = new DifficultyVote();
    private final Task task;
    private final GameEvents gameEvents;

    private int timer = Integer.MAX_VALUE;
    private int oldPlayerCount = 0;

    private int ticks = 0;

    public GameStartManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.task = gameInstance.scheduler().scheduleTask(this, TaskSchedule.immediate(), TaskSchedule.tick(1));

        this.gameEvents = new GameEvents(gameInstance, gameInstance.getGameEvents().getGlobalEventNode());

        gameEvents.getPlayerEventNode().addListener(PlayerSpawnEvent.class, playerSpawnEvent -> {
            Player player = playerSpawnEvent.getPlayer();

            gameInstance.sendMessage(
                    Component.text("☠").color(TextColor.color(170, 0, 0))
                            .append(Component.text(" | ").color(TextColor.color(107, 107, 107)))
                            .append(player.getName()
                            .append(Component.text(" a rejoint la partie. ")).color(TextColor.color(255, 250, 0)))
                            .append(Component.text("(" + gameInstance.getPlayers().size() + "/4)").color(TextColor.color(157, 157, 157)))
            );

            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().setItemStack(8, ItemList.DIFFICULTY_SELECTOR.withAmount(1));
        });

        gameEvents.getPlayerEventNode().addListener(PlayerDisconnectEvent.class, playerDisconnectEvent -> {
            gameInstance.sendMessage(
                    Component.text("☠").color(TextColor.color(170, 0, 0))
                            .append(Component.text(" | ").color(TextColor.color(107, 107, 107)))
                            .append(playerDisconnectEvent.getPlayer().getName()
                            .append(Component.text(" a quitté la partie. ")).color(TextColor.color(255, 250, 0)))
                            .append(Component.text("(" + (gameInstance.getPlayers().size() - 1) + "/4)").color(TextColor.color(157, 157, 157)))
            );
        });

        gameEvents.getPlayerEventNode().addListener(PlayerUseItemEvent.class, playerUseItemEvent -> {
            if(playerUseItemEvent.getItemStack().isSimilar(ItemList.DIFFICULTY_SELECTOR)) {
                playerUseItemEvent.getPlayer().openInventory(new DifficultySelectorGUI(difficultyVote));
            }
        });
    }

    @Override
    public void run() {
        int connectedPlayers = gameInstance.getPlayers().size();
        if(connectedPlayers <= 1) {
            gameInstance.getBossBar().name(Component.text("En attente de joueurs... ").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
            gameInstance.getBossBar().progress(0f);
            timer = Integer.MAX_VALUE;
            return;
        }

        if(connectedPlayers != oldPlayerCount) {
            oldPlayerCount = connectedPlayers;
            if(timer > PLAYER_COUNT_TIMER.get(connectedPlayers)) timer = PLAYER_COUNT_TIMER.get(connectedPlayers);
            gameInstance.setAcceptsPlayers(connectedPlayers != 4);
        }

        gameInstance.getBossBar().progress(1 - ((float) (timer * 1000 - ticks * 50) / (PLAYER_COUNT_TIMER.get(connectedPlayers) * 1000)));

        if(ticks == 20) {
            timer--;
            ticks = 0;

            gameInstance.getBossBar().name(Component.text("Démarrage... ").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                    .append(Component.text("(").color(NamedTextColor.GRAY))
                    .append(Component.text(timer + "s").color(NamedTextColor.WHITE))
                    .append(Component.text(")").color(NamedTextColor.GRAY))
            );

            switch (timer) {
                case 20, 10, 5, 4, 3, 2, 1 -> {
                    gameInstance.sendMessage(
                            Component.text("» ").color(TextColor.color(NamedTextColor.GRAY))
                                    .append(Component.text("La partie commence dans ").color(TextColor.color(255, 255, 75)))
                                    .append(Component.text(timer).color(TextColor.color(250, 65, 65)))
                                    .append(Component.text(" secondes.").color(TextColor.color(255, 255, 75)).decoration(TextDecoration.BOLD, false))
                    );

                    Title.Times times = Title.Times.times(Duration.ofMillis(100), Duration.ofMillis(1500), Duration.ofMillis(100));
                    Title title = Title.title(Component.text(""), ((Component.text(timer).color(TextColor.color(255,255, 75)))), times);
                    gameInstance.showTitle(title);
                    gameInstance.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), Sound.Source.BLOCK, 1, 1));
                }
                case 0 -> {
                    gameInstance.start(difficultyVote.getVotedDifficulty());
                    task.cancel();
                    gameEvents.unregister();
                }
            }
        }

        ticks++;
    }
}
