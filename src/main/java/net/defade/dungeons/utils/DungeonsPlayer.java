package net.defade.dungeons.utils;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DungeonsPlayer extends Player {
    public DungeonsPlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    @Override
    public void setHealth(float health) {
        if (health <= 0) {
            health = getMaxHealth();

            PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(this, null, null);
            EventDispatcher.call(playerDeathEvent);

            Component chatMessage = playerDeathEvent.getChatMessage();

            if (chatMessage != null && getInstance() != null) {
                getInstance().sendMessage(chatMessage);
            }
        }

        super.setHealth(health);
    }

    @Override
    public void setGameMode(@NotNull GameMode gameMode) {
        super.setGameMode(gameMode);
        setInvisible(gameMode == GameMode.SPECTATOR);
    }
}
