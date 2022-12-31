package net.defade.dungeons.game.config.map.door;

import net.defade.dungeons.game.config.map.RoomConfig;
import net.defade.dungeons.map.doors.Door;
import net.defade.dungeons.map.Room;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoorConfig {
    private final Map<String, Door> doors = new HashMap<>();

    public DoorConfig(Instance instance, RoomConfig roomConfig, JSONObject doorsObject) {
        Map<String, ProtoDoor> protoDoors = new HashMap<>();
        for (String door : doorsObject.keySet()) {
            protoDoors.put(door, parseDoor(roomConfig, doorsObject.getJSONObject(door)));
        }

        for (Map.Entry<String, ProtoDoor> protoDoorEntry : protoDoors.entrySet()) {
            String doorId = protoDoorEntry.getKey();
            ProtoDoor protoDoor = protoDoorEntry.getValue();

            List<Entity> nextArmorStands = new ArrayList<>();
            for (Map.Entry<String, String> nextArmorStand : protoDoor.nextArmorStands().entrySet()) {
                nextArmorStands.add(protoDoors.get(nextArmorStand.getKey()).armorStands().get(nextArmorStand.getValue()));
            }

            doors.put(doorId, new Door(
                    instance,
                    protoDoor.minCorner(),
                    protoDoor.maxCorner(),
                    protoDoor.armorStands().values().stream().toList(),
                    protoDoor.defaultArmorStands(),
                    nextArmorStands,
                    protoDoor.price(),
                    protoDoor.antennaPos(),
                    protoDoor.enchantTablePos()
            ));
        }
    }

    public void initDoors() {
        doors.values().forEach(Door::init);
    }

    public Map<String, Door> getDoors() {
        return doors;
    }

    private ProtoDoor parseDoor(RoomConfig roomConfig, JSONObject door) {
        final Map<String, Room> rooms = roomConfig.getRooms();

        JSONArray cornersArray = door.getJSONArray("corners");
        Pos[] corners = new Pos[]{readPos(cornersArray.getJSONArray(0)), readPos(cornersArray.getJSONArray(1))};
        Pos minCorner = getMinPos(corners[0], corners[1]);
        Pos maxCorner = getMaxPos(corners[0], corners[1]);

        int price = door.getInt("price");

        JSONObject armorStandsObject = door.getJSONObject("armorstands");
        Map<String, Entity> armorStands = new HashMap<>();
        for (String roomId : armorStandsObject.keySet()) {
            DoorArmorStand doorArmorStand = new DoorArmorStand(rooms.get(roomId).name(), readPos(armorStandsObject.getJSONArray(roomId)), price);
            armorStands.put(roomId, doorArmorStand);
        }

        JSONArray defaultArmorStandsArray = door.optJSONArray("defaultArmorstands");
        if(defaultArmorStandsArray == null) defaultArmorStandsArray = new JSONArray();
        List<Entity> defaultArmorStands = new ArrayList<>();
        for (int i = 0; i < defaultArmorStandsArray.length(); i++) {
            defaultArmorStands.add(armorStands.get(defaultArmorStandsArray.getString(i)));
        }

        JSONObject nextArmorStandsObject = door.optJSONObject("nextArmorstands", new JSONObject());
        Map<String, String> nextArmorStands = new HashMap<>();
        for (String doorContainingArmorStand : nextArmorStandsObject.keySet()) {
            nextArmorStands.put(doorContainingArmorStand, nextArmorStandsObject.getString(doorContainingArmorStand));
        }

        Pos antennaPos = door.has("antenna") ? readPos(door.getJSONArray("antenna")) : null;
        Pos enchantTablePos = door.has("EnchantTable") ? readPos(door.getJSONArray("EnchantTable")) : null;

        return new ProtoDoor(minCorner, maxCorner, price, armorStands, defaultArmorStands, nextArmorStands, antennaPos, enchantTablePos);
    }

    private static Pos readPos(JSONArray pos) {
        return new Pos(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
    }

    private static Pos getMinPos(Pos pos1, Pos pos2) {
        return new Pos(Math.min(pos1.x(), pos2.x()), Math.min(pos1.y(), pos2.y()), Math.min(pos1.z(), pos2.z()));
    }

    private static Pos getMaxPos(Pos pos1, Pos pos2) {
        return new Pos(Math.max(pos1.x(), pos2.x()), Math.max(pos1.y(), pos2.y()), Math.max(pos1.z(), pos2.z()));
    }
}
