package net.defade.dungeons.game.config;

import net.defade.dungeons.waves.Wave;
import net.minestom.server.entity.EntityCreature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaveConfig {
    private final List<Wave> waves = new ArrayList<>();

    public WaveConfig(JSONArray wavesArray, double spawnMultiplier) {
        for(int index = 0; index < wavesArray.length(); index++) {
            JSONObject waveObject = wavesArray.getJSONObject(index);

            List<Class<? extends EntityCreature>> zombiesToSpawn = new ArrayList<>();
            for (String zombie : waveObject.keySet()) {
                Class<? extends EntityCreature> zombieClass = parseZombie(zombie);
                for(int zombiesAmount = (int) Math.ceil(waveObject.getInt(zombie) * spawnMultiplier); zombiesAmount > 0; zombiesAmount--) {
                    zombiesToSpawn.add(zombieClass);
                }
            }

            Collections.shuffle(zombiesToSpawn);

            waves.add(new Wave(zombiesToSpawn));
        }
    }

    public List<Wave> getWaves() {
        return waves;
    }

    private static Class<? extends EntityCreature> parseZombie(String zombieName) {
        return switch (zombieName.toLowerCase()) {
            case "level1" -> EntityCreature.class;
            default -> throw new IllegalArgumentException("The zombie " + zombieName + " does not exists.");
        };
    }
}
