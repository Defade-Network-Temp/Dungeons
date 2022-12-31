package net.defade.dungeons.game.config.map.door;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import java.util.List;
import java.util.Map;

public record ProtoDoor(Pos minCorner, Pos maxCorner, int price, Map<String, Entity> armorStands,
                        List<Entity> defaultArmorStands, Map<String, String> nextArmorStands,
                        Pos antennaPos, Pos enchantTablePos) {}
