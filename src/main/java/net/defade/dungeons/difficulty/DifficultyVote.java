package net.defade.dungeons.difficulty;

import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DifficultyVote {
    private final Map<Player, Difficulty> votes = new HashMap<>();

    public void castVote(Player player, Difficulty difficulty) {
        votes.put(player, difficulty);
    }

    public Difficulty getVotedDifficulty() {
        Map<Difficulty, Integer> votedDifficulties = new HashMap<>();

        votes.forEach((player, difficulty) -> {
            votedDifficulties.put(difficulty, votedDifficulties.getOrDefault(difficulty, 0) + 1);
        });

        Difficulty votedDifficulty = null;
        for (Map.Entry<Difficulty, Integer> difficultyVote : votedDifficulties.entrySet()) {
            if(votedDifficulty == null || difficultyVote.getValue() > votedDifficulties.get(votedDifficulty)) {
                votedDifficulty = difficultyVote.getKey();
            }
        }

        return votedDifficulty;
    }
}
