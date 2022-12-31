package net.defade.dungeons.game.config.map;

import net.defade.dungeons.map.Room;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minestom.server.coordinate.Pos;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomConfig {
    private final Map<String, Room> rooms = new HashMap<>();

    public RoomConfig(JSONObject roomsConfig) {
        for (String room : roomsConfig.keySet()) {
            JSONObject roomObject = roomsConfig.getJSONObject(room);

            Component name = GsonComponentSerializer.gson().deserialize(roomObject.getString("name"));

            JSONArray spawningPositionsArray = roomObject.getJSONArray("SpawningPositions");
            List<Pos> spawningPositions = new ArrayList<>();
            for (int i = 0; i < spawningPositionsArray.length(); i++) {
                JSONArray positionArray = spawningPositionsArray.getJSONArray(i);
                spawningPositions.add(new Pos(
                        positionArray.getDouble(0),
                        positionArray.getDouble(1),
                        positionArray.getDouble(2)
                ));
            }

            rooms.put(room, new Room(name, spawningPositions));
        }
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }
}
