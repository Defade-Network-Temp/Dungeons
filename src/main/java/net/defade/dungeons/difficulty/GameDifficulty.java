package net.defade.dungeons.difficulty;

public record GameDifficulty(Difficulty difficulty, float spawnMultiplier, float damageMultiplier,
                             float healthMultiplier, float priceMultiplier) {
}
