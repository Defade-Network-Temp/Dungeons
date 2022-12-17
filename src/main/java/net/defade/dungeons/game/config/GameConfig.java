package net.defade.dungeons.game.config;

import net.defade.dungeons.difficulty.Difficulty;
import net.minestom.server.coordinate.Pos;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class GameConfig {
    private final JSONObject jsonConfig;
    private final Pos spawnPoint;

    private final Map<Difficulty, Double> spawnMultiplier = new HashMap<>();
    private final Map<Difficulty, Float> damageMultiplier = new HashMap<>();

    public GameConfig(String config) {
        jsonConfig = new JSONObject(config);

        spawnPoint = getPos(jsonConfig.getJSONObject("spawn"));

        JSONObject spawnMultiplierObject = jsonConfig.getJSONObject("SpawnMultiplier");
        for(Difficulty difficulty : Difficulty.values()) {
            spawnMultiplier.put(difficulty, spawnMultiplierObject.getDouble(difficulty.toString()));
        }

        JSONObject damageMultiplierObject = jsonConfig.getJSONObject("DamageMultiplier");
        for(Difficulty difficulty : Difficulty.values()) {
            damageMultiplier.put(difficulty, damageMultiplierObject.getFloat(difficulty.toString()));
        }
    }

    public Pos getSpawnPoint() {
        return spawnPoint;
    }

    public double getSpawnMultiplier(Difficulty difficulty) {
        return spawnMultiplier.get(difficulty);
    }

    public float getDamageMultiplier(Difficulty difficulty) {
        return damageMultiplier.get(difficulty);
    }

    public WaveConfig getWaveConfig(Difficulty difficulty) {
        return new WaveConfig(jsonConfig.getJSONArray("waves"), getSpawnMultiplier(difficulty));
    }

    private static Pos getPos(JSONObject jsonObject) {
        return new Pos(jsonObject.getDouble("x"), jsonObject.getDouble("y"), jsonObject.getDouble("z"));
    }
}
