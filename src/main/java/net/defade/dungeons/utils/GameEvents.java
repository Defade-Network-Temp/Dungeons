package net.defade.dungeons.utils;

import net.defade.dungeons.game.GameInstance;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.PlayerEvent;

public class GameEvents {
    private final EventNode<Event> parentNode;

    private final EventNode<Event> instanceEventNode = EventNode.all("game");
    private final EventNode<EntityEvent> entityInstanceEventNode;
    private final EventNode<PlayerEvent> playerInstanceEventNode;

    public GameEvents(GameInstance gameInstance, EventNode<Event> parentNode) {
        this.parentNode = parentNode;
        this.entityInstanceEventNode = EventNode.type("entities", EventFilter.ENTITY, (playerEvent, entity) -> gameInstance.getEntities().contains(entity));
        this.playerInstanceEventNode = EventNode.type("players", EventFilter.PLAYER, (playerEvent, player) -> gameInstance.getPlayers().contains(player));

        parentNode.addChild(instanceEventNode);
        instanceEventNode.addChild(entityInstanceEventNode);
        instanceEventNode.addChild(playerInstanceEventNode);
    }

    public EventNode<Event> getGlobalEventNode() {
        return instanceEventNode;
    }

    public EventNode<EntityEvent> getEntityEventNode() {
        return entityInstanceEventNode;
    }

    public EventNode<PlayerEvent> getPlayerEventNode() {
        return playerInstanceEventNode;
    }

    public void unregister() {
        parentNode.removeChild(instanceEventNode);
    }
}
