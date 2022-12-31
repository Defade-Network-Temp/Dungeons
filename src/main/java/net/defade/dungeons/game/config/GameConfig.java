package net.defade.dungeons.game.config;

import net.defade.dungeons.difficulty.Difficulty;
import net.defade.dungeons.difficulty.GameDifficulty;
import net.defade.dungeons.game.config.map.RoomConfig;
import net.defade.dungeons.game.config.map.door.DoorConfig;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class GameConfig {
    private final JSONObject jsonConfig;
    private final Pos spawnPoint;
    private final RoomConfig roomConfig;
    private final DoorConfig doorConfig;

    private final Map<Difficulty, Float> spawnMultiplier = new HashMap<>();
    private final Map<Difficulty, Float> damageMultiplier = new HashMap<>();
    private final Map<Difficulty, Float> healthMultiplier = new HashMap<>();
    private final Map<Difficulty, Float> priceMultiplier = new HashMap<>();
    private final Map<Difficulty, Integer> weaponDifficultyReducer = new HashMap<>();

    public GameConfig(String config, Instance instance) {
        jsonConfig = new JSONObject(config);
        spawnPoint = getPos(jsonConfig.getJSONObject("spawn"));
        this.roomConfig = new RoomConfig(jsonConfig.getJSONObject("rooms"));
        this.doorConfig = new DoorConfig(instance, roomConfig, jsonConfig.getJSONObject("doors"));

        JSONObject spawnMultiplierObject = jsonConfig.getJSONObject("SpawnMultiplier");
        for(Difficulty difficulty : Difficulty.values()) {
            spawnMultiplier.put(difficulty, spawnMultiplierObject.getFloat(difficulty.toString()));
        }

        JSONObject damageMultiplierObject = jsonConfig.getJSONObject("DamageMultiplier");
        for(Difficulty difficulty : Difficulty.values()) {
            damageMultiplier.put(difficulty, damageMultiplierObject.getFloat(difficulty.toString()));
        }

        JSONObject healthMultiplierObject = jsonConfig.getJSONObject("HealthMultiplier");
        for(Difficulty difficulty : Difficulty.values()) {
            healthMultiplier.put(difficulty, healthMultiplierObject.getFloat(difficulty.toString()));
        }

        JSONObject priceMultiplierObject = jsonConfig.getJSONObject("PriceMultiplier");
        for(Difficulty difficulty : Difficulty.values()) {
            priceMultiplier.put(difficulty, priceMultiplierObject.getFloat(difficulty.toString()));
        }

        JSONObject weaponDifficultyReducerObject = jsonConfig.getJSONObject("WeaponDifficultyReducer");
        for(Difficulty difficulty : Difficulty.values()) {
            weaponDifficultyReducer.put(difficulty, weaponDifficultyReducerObject.getInt(difficulty.toString()));
        }
    }

    public Pos getSpawnPoint() {
        return spawnPoint;
    }

    public GameDifficulty getDifficulty(Difficulty difficulty) {
        return new GameDifficulty(difficulty, spawnMultiplier.get(difficulty), damageMultiplier.get(difficulty),
                healthMultiplier.get(difficulty), priceMultiplier.get(difficulty),
                weaponDifficultyReducer.get(difficulty));
    }

    public WaveConfig getWaveConfig(Difficulty difficulty) {
        return new WaveConfig(jsonConfig.getJSONArray("waves"), spawnMultiplier.get(difficulty));
    }

    public RoomConfig getRoomConfig() {
        return roomConfig;
    }

    public DoorConfig getDoorConfig() {
        return doorConfig;
    }

    private static Pos getPos(JSONObject jsonObject) {
        return new Pos(jsonObject.getDouble("x"), jsonObject.getDouble("y"), jsonObject.getDouble("z"));
    }
}
