package net.defade.dungeons.game.config;

import net.minestom.server.coordinate.Pos;
import org.json.JSONObject;

public class GameConfig {
    private final Pos spawnPoint;

    private final double spawnMultiplier;
    private final WaveConfig waveConfig;

    public GameConfig(String config) {
        JSONObject jsonConfig = new JSONObject(config);

        spawnPoint = getPos(jsonConfig.getJSONObject("spawn"));
        spawnMultiplier = jsonConfig.getDouble("SpawnMultiplier");
        waveConfig = new WaveConfig(jsonConfig.getJSONArray("waves"), spawnMultiplier);
    }

    public Pos getSpawnPoint() {
        return spawnPoint;
    }

    public double getSpawnMultiplier() {
        return spawnMultiplier;
    }

    public WaveConfig getWaveConfig() {
        return waveConfig;
    }

    private static Pos getPos(JSONObject jsonObject) {
        return new Pos(jsonObject.getDouble("x"), jsonObject.getDouble("y"), jsonObject.getDouble("z"));
    }
}
