package net.defade.dungeons.game;

import net.minestom.server.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class CoinsManager {
    private final Map<Player, Integer> coins = new HashMap<>();

    public void addCoins(Player player, int amount) {
        coins.put(player, coins.getOrDefault(player, 0) + amount);
    }

    public void removeCoins(Player player, int amount) {
        coins.put(player, coins.getOrDefault(player, 0) - amount);
    }

    public int getCoins(Player player) {
        return coins.getOrDefault(player, 0);
    }

    public boolean hasEnoughCoins(Player player, int amount) {
        return coins.getOrDefault(player, 0) >= amount;
    }
}
