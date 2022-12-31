package net.defade.dungeons.map.doors;

import net.defade.dungeons.game.GameInstance;
import net.defade.dungeons.utils.GameEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.tag.Tag;
import java.util.Map;

public class DoorsManager {
    private static final Tag<String> DOOR_NAME_TAG = Tag.String("DoorName");

    private final GameInstance gameInstance;
    private final Map<String, Door> doors;

    private final GameEvents events;

    public DoorsManager(GameInstance gameInstance, Map<String, Door> doors) {
        this.gameInstance = gameInstance;
        this.doors = doors;

        this.events = new GameEvents(gameInstance, gameInstance.getGameEvents().getGlobalEventNode());
    }

    public void init() {
        initArmorStandsDoorNameTag();
        registerBlockClickEvent();
        registerArmorStandInteractEvent();
    }

    public void stop() {
        events.unregister();
    }

    private void initArmorStandsDoorNameTag() {
        for (Map.Entry<String, Door> door : doors.entrySet()) {
            for (Entity armorStand : door.getValue().getArmorStands()) {
                armorStand.setTag(DOOR_NAME_TAG, door.getKey());
            }
        }
    }

    private void registerBlockClickEvent() {
        events.getPlayerEventNode().addListener(PlayerBlockInteractEvent.class, playerBlockInteractEvent -> {
            if(playerBlockInteractEvent.getHand() == Player.Hand.OFF) return; // The event is called twice, each time with a different hand

            Player player = playerBlockInteractEvent.getPlayer();
            Point blockPos = playerBlockInteractEvent.getBlockPosition();

            for (Door door : doors.values()) {
                if(door.isOpened()) continue;

                if(blockPos.blockX() >= door.getMinCorner().blockX() && blockPos.blockY() >= door.getMinCorner().blockY() && blockPos.blockZ() >= door.getMinCorner().blockZ() &&
                        blockPos.blockX() <= door.getMaxCorner().blockX() && blockPos.blockY() <= door.getMaxCorner().blockY() && blockPos.blockZ() <= door.getMaxCorner().blockZ()) {
                    openDoor(player, door);
                    break;
                }
            }
        });
    }

    private void registerArmorStandInteractEvent() {
        events.getPlayerEventNode().addListener(PlayerEntityInteractEvent.class, playerEntityInteractEvent -> {
            Player player = playerEntityInteractEvent.getPlayer();
            Entity entity = playerEntityInteractEvent.getTarget();

            if(entity.hasTag(DOOR_NAME_TAG)) {
                Door door = doors.get(entity.getTag(DOOR_NAME_TAG));
                if(door == null) return;

                openDoor(player, door);
            }
        });
    }

    private void openDoor(Player player, Door door) {
        if(!gameInstance.getCoinsManager().hasEnoughCoins(player, door.getPrice())) {
            player.sendMessage(Component.text("Vous n'avez pas assez d'argent pour ouvrir cette porte.").color(NamedTextColor.RED));
            return;
        }

        gameInstance.getCoinsManager().removeCoins(player, door.getPrice());
        player.sendMessage(Component.text("Vous avez ouvert une porte !").color(NamedTextColor.GREEN));

        door.open();
    }
}
