package net.defade.dungeons.game.utils;

import net.defade.dungeons.game.GameInstance;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.PlayerEvent;

public class GameEvents {
    private final EventNode<Event> instanceEventNode = EventNode.all("game");
    private final EventNode<PlayerEvent> playerInstanceEventNode;

    public GameEvents(GameInstance gameInstance) {
        this.playerInstanceEventNode = EventNode.type("players", EventFilter.PLAYER, (playerEvent, player) -> gameInstance.getPlayers().contains(player));

        MinecraftServer.getGlobalEventHandler().addChild(instanceEventNode);
        instanceEventNode.addChild(playerInstanceEventNode);
    }

    public EventNode<Event> getGlobalEventNode() {
        return instanceEventNode;
    }

    public EventNode<PlayerEvent> getPlayerEventNode() {
        return playerInstanceEventNode;
    }
}
