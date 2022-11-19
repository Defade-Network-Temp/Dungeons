package net.defade.dungeons.game.config;

import net.minestom.server.coordinate.Pos;
import org.json.JSONObject;

public class GameConfig {
    private final Pos spawnPoint;

    public GameConfig(String config) {
        JSONObject jsonConfig = new JSONObject(config);

        spawnPoint = getPos(jsonConfig.getJSONObject("spawn"));
    }

    public Pos getSpawnPoint() {
        return spawnPoint;
    }

    private static Pos getPos(JSONObject jsonObject) {
        return new Pos(jsonObject.getDouble("x"), jsonObject.getDouble("y"), jsonObject.getDouble("z"));
    }
}
