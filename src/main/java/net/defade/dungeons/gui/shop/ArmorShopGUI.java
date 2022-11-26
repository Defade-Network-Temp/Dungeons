package net.defade.dungeons.gui.shop;

import net.defade.dungeons.shop.Armors;
import net.defade.dungeons.utils.ItemList;
import net.defade.dungeons.waves.WaveManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class ArmorShopGUI extends Inventory {
    public ArmorShopGUI(WaveManager waveManager, Player player) {
        super(InventoryType.CHEST_5_ROW, Component.text("Shop > Armures"));

        for (int slot = 0; slot < getSize(); slot++) {
            setItemStack(slot, ItemList.INVENTORY_FILLER);
        }

        Armors currentPlayerArmor = player.getTag(Armors.ARMOR_TAG);
        final Armors nextArmor = currentPlayerArmor.ordinal() + 1 != Armors.values().length ? Armors.values()[currentPlayerArmor.ordinal() + 1] : null;

        if(nextArmor != null) { // The player hasn't unlocked all the armors
            placeArmor(currentPlayerArmor, 3);
            setItemStack(39, ItemList.ACTUAL_ARMOR);

            placeArmor(nextArmor, 5);
            setItemStack(41, ItemList.BUY_ARMOR.apply(nextArmor.getPrice(), nextArmor.getMinWave() <= waveManager.getCurrentWave() ? -1 : nextArmor.getMinWave()));
        } else {
            placeArmor(currentPlayerArmor, 4);
            setItemStack(40, ItemList.ACTUAL_ARMOR);
        }

        addInventoryCondition((clickingPlayer, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);

            if(slot == 41 && nextArmor != null) {
                if(nextArmor.getMinWave() <= waveManager.getCurrentWave() + 1) { // Waves starts at 0
                    // TODO Withdraw coins
                    nextArmor.equipForPlayer(clickingPlayer);
                    clickingPlayer.openInventory(new ArmorShopGUI(waveManager, clickingPlayer));
                } else {
                    clickingPlayer.closeInventory();
                    clickingPlayer.sendMessage(Component.text("Cette armure n'est disponible qu'à partir de la vague " + nextArmor.getMinWave() + "."));
                }
            }
        });
    }

    private void placeArmor(Armors armor, int slot) {
        setItemStack(slot, armor.getHelmet());
        setItemStack(slot += 9, armor.getChestplate());
        setItemStack(slot += 9, armor.getLeggings());
        setItemStack(slot + 9, armor.getBoots());
    }
}
