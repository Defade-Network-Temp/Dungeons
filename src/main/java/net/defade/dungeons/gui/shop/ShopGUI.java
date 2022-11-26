package net.defade.dungeons.gui.shop;

import net.defade.dungeons.utils.ItemList;
import net.defade.dungeons.waves.WaveManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class ShopGUI extends Inventory {
    public ShopGUI(WaveManager waveManager) {
        super(InventoryType.CHEST_3_ROW, Component.text("Shop"));

        for(int i = 0; i < getSize(); i++) {
            setItemStack(i, ItemList.INVENTORY_FILLER);
        }

        setItemStack(12, ItemList.SWORD_GUI);
        setItemStack(14, ItemList.ARMORS_GUI);

        addInventoryCondition((player, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);

            if(slot == 12) {
                player.openInventory(new SwordShopGUI(player));
            } else if(slot == 14) {
                player.openInventory(new ArmorShopGUI(waveManager, player));
            }
        });
    }
}
