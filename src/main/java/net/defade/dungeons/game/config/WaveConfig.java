package net.defade.dungeons.game.config;

import net.defade.dungeons.waves.Wave;
import net.defade.dungeons.zombies.DungeonsEntity;
import net.defade.dungeons.zombies.classic.ZombieI;
import net.defade.dungeons.zombies.classic.ZombieII;
import net.defade.dungeons.zombies.classic.ZombieIII;
import net.defade.dungeons.zombies.classic.ZombieIV;
import net.defade.dungeons.zombies.classic.ZombieV;
import net.defade.dungeons.zombies.classic.ZombieVI;
import net.defade.dungeons.zombies.classic.ZombieVII;
import net.defade.dungeons.zombies.classic.ZombieVIII;
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

            List<Class<? extends DungeonsEntity>> zombiesToSpawn = new ArrayList<>();
            for (String zombie : waveObject.keySet()) {
                Class<? extends DungeonsEntity> zombieClass = parseZombie(zombie);
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

    private static Class<? extends DungeonsEntity> parseZombie(String zombieName) {
        return switch (zombieName.toLowerCase()) {
            case "level1" -> ZombieI.class;
            case "level2" -> ZombieII.class;
            case "level3" -> ZombieIII.class;
            case "level4" -> ZombieIV.class;
            case "level5" -> ZombieV.class;
            case "level6" -> ZombieVI.class;
            case "level7" -> ZombieVII.class;
            case "level8" -> ZombieVIII.class;
            default -> throw new IllegalArgumentException("The zombie " + zombieName + " does not exists.");
        };
    }
}
