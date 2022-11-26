package net.defade.dungeons.waves;

import net.defade.dungeons.game.GameInstance;
import net.defade.dungeons.gui.shop.ShopGUI;
import net.defade.dungeons.utils.GameEvents;
import net.defade.dungeons.utils.ItemList;
import net.defade.dungeons.zombies.DungeonsEntity;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WaveManager implements Runnable {
    private final GameInstance gameInstance;

    private WaveStatus waveStatus = WaveStatus.PLAYING;

    private final List<Task> registeredTasks = new ArrayList<>();

    private final List<Wave> waves;
    private int currentWave = 0;

    private final List<DungeonsEntity> spawnedZombies = new ArrayList<>();
    private final GameEvents eventNode;

    private int timer = 6;
    private int timerTicks = 0;

    public WaveManager(GameInstance gameInstance, List<Wave> waves) {
        this.gameInstance = gameInstance;
        this.waves = waves;
        this.eventNode = new GameEvents(gameInstance, gameInstance.getGameEvents().getGlobalEventNode());

        updateWaveStatus();
        announceNewWave();
        registeredTasks.add(gameInstance.scheduler().scheduleTask(this, TaskSchedule.immediate(), TaskSchedule.seconds(1)));

        gameInstance.scheduler().scheduleTask(() -> {
            if(waveStatus == WaveStatus.PAUSE_TIME) {
                timerTicks++;
                gameInstance.getBossBar().progress((((30 * 1000) - (timer * 1000)) / 30000f) + (timerTicks * 50) / 30000f);
            } else {
                timerTicks = 0;
            }
        }, TaskSchedule.immediate(), TaskSchedule.tick(1));

        eventNode.getPlayerEventNode().addListener(PlayerUseItemEvent.class, playerUseItemEvent -> {
            if(waveStatus == WaveStatus.PAUSE_TIME && playerUseItemEvent.getItemStack().isSimilar(ItemList.RADIO_ON)) {
                playerUseItemEvent.getPlayer().openInventory(new ShopGUI(this));
            }
        });
    }

    @Override
    public void run() {
        Wave wave = waves.get(currentWave);

        if(waveStatus == WaveStatus.PLAYING) {
            if(spawnedZombies.size() == 0 && !wave.hasZombiesLeft()) {
                currentWave++;
                if(waves.size() == currentWave) {
                    stop();
                    return;
                }

                waveStatus = WaveStatus.PAUSE_TIME;
                timer = 30;
                updateWaveStatus();

                return;
            }

            timer--;
            if(timer == 0) {
                timer = 3;
                if(wave.hasZombiesLeft()) {
                    updateWaveStatus();
                    spawnZombie();
                }
            }
        } else if(waveStatus == WaveStatus.PAUSE_TIME) {
            timer--;
            timerTicks = 0;
            if(timer == 0) {
                timer = 6;
                waveStatus = WaveStatus.PLAYING;
                updateWaveStatus();
                announceNewWave();
            }
        }
    }

    public int getCurrentWave() {
        return currentWave;
    }

    private void spawnZombie() {
        DungeonsEntity entity = waves.get(currentWave).getZombie();
        spawnedZombies.add(entity);
        entity.setInstance(gameInstance, gameInstance.getConfig().getSpawnPoint()); // TODO change the spawn point
        updateWaveStatus();
    }

    private void updateWaveStatus() {
        BossBar bossBar = gameInstance.getBossBar();

        if(waveStatus == WaveStatus.PLAYING) {
            bossBar.color(BossBar.Color.RED);
            bossBar.name(
                    Component.text("Vague " + (currentWave + 1)).color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
                            .append(Component.text(" | ").color(NamedTextColor.GRAY))
                            .append(Component.text("Zombies Restant: ").color(NamedTextColor.WHITE))
                            .append(Component.text(spawnedZombies.size() + waves.get(currentWave).getZombiesLeft()).color(NamedTextColor.RED))
            );

            gameInstance.getPlayers().forEach(player -> player.getInventory().setItemStack(8, ItemList.RADIO_OFF));
        } else if(waveStatus == WaveStatus.PAUSE_TIME) {
            bossBar.color(BossBar.Color.YELLOW);
            bossBar.name(
                    Component.text("Pause ").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                            .append(Component.text("(").color(NamedTextColor.GRAY))
                            .append(Component.text(timer + "s").color(NamedTextColor.WHITE))
                            .append(Component.text(")").color(NamedTextColor.GRAY))
            );
            gameInstance.getPlayers().forEach(player -> player.getInventory().setItemStack(8, ItemList.RADIO_ON));
        }
    }

    private void announceNewWave() {
        Title.Times times = Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(5000), Duration.ofMillis(1000));

        Component subtitle = Component.text("");
        if(currentWave == 0) {
            subtitle = Component.text("Difficulté: ").color(TextColor.color(255, 255, 75))
                    .append(gameInstance.getDifficulty().getName());
        }

        Title title = Title.title((Component.text("Vague " + currentWave + 1).color(TextColor.color(200, 20, 20))
                .decoration(TextDecoration.BOLD, true)), subtitle, times);

        gameInstance.showTitle(title);
        gameInstance.playSound(Sound.sound(Key.key("minecraft:entity.wither.spawn"), Sound.Source.HOSTILE, 1.0F, 0F));
    }

    private void stop() {
        eventNode.unregister();
        for (Task task : registeredTasks) {
            task.cancel();
        }
    }
}
