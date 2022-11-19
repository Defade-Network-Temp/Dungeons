package net.defade.dungeons.gui;

import net.defade.dungeons.difficulty.Difficulty;
import net.defade.dungeons.difficulty.DifficultyVote;
import net.defade.dungeons.utils.ItemList;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

import static net.kyori.adventure.text.Component.text;

public class DifficultySelectorGUI extends Inventory {
    public DifficultySelectorGUI(DifficultyVote difficultyVote) {
        super(InventoryType.CHEST_3_ROW, text("Difficulté"));

        for(int i = 0; i < getSize(); i++) {
            setItemStack(i, ItemList.INVENTORY_FILLER);
        }

        setItemStack(11, ItemList.NORMAL_DIFFICULTY);
        setItemStack(13, ItemList.HARD_DIFFICULTY);
        setItemStack(15, ItemList.INSANE_DIFFICULTY);

        inventoryConditions.add((player, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);

            switch (slot) {
                case 11 -> difficultyVote.castVote(player, Difficulty.NORMAL);
                case 13 -> difficultyVote.castVote(player, Difficulty.HARD);
                case 15 -> difficultyVote.castVote(player, Difficulty.INSANE);
            }
        });
    }
}
