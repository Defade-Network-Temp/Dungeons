package net.defade.dungeons.gui.shop;

import net.defade.dungeons.game.CoinsManager;
import net.defade.dungeons.shop.swords.Sword;
import net.defade.dungeons.shop.swords.SwordType;
import net.defade.dungeons.shop.swords.Swords;
import net.defade.dungeons.utils.ItemList;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class SwordShopGUI extends Inventory {
    private final Map<Integer, Runnable> slotsActions = new HashMap<>();
    private final CoinsManager coinsManager;
    private final Player player;

    public SwordShopGUI(CoinsManager coinsManager, Player player) {
        super(InventoryType.CHEST_5_ROW, Component.text("Shop > Épées"));
        this.coinsManager = coinsManager;
        this.player = player;

        for (int slot = 0; slot < getSize(); slot++) {
            setItemStack(slot, ItemList.INVENTORY_FILLER);
        }

        SwordType playerCurrentSwordType = player.getTag(SwordType.SWORD_TYPE_TAG);

        fillSword(Swords.WOODEN_SWORD.getSword(), playerCurrentSwordType, player.getTag(SwordType.SWORD.getSwordTag()), 10);
        fillSword(Swords.WOODEN_BROADSWORD.getSword(), playerCurrentSwordType, player.getTag(SwordType.BROADSWORD.getSwordTag()), 28);

        addInventoryCondition((playerClicking, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);

            Runnable action = slotsActions.get(slot);
            if(action != null) {
                action.run();
                playerClicking.openInventory(new SwordShopGUI(coinsManager, playerClicking));
            }
        });
    }

    private ItemStack formatSword(int slot, Sword sword, boolean hasBoughtSword, boolean isEquipped, boolean canEquip, boolean canBuy, boolean hasEnoughCoins) {
        ItemStack itemStack = sword.getAsItemStack();
        if(isEquipped) {
            List<Component> lore = new ArrayList<>(itemStack.getLore());
            lore.add(text(""));
            lore.add(text("Équipée").color(GREEN).decoration(ITALIC, false));

            itemStack = ItemStack.builder(Material.LIME_DYE)
                    .meta(itemStack.meta())
                    .lore(lore)
                    .build();
        } else if(hasBoughtSword) {
            List<Component> lore = new ArrayList<>(itemStack.getLore());
            lore.add(text(""));
            if(canEquip) {
                lore.add(text("Équipper").color(GREEN).decoration(ITALIC, false));
                slotsActions.put(slot, () -> Swords.equipSwordForPlayer(player, sword));
            } else {
                lore.add(text("Achetée").color(GREEN).decoration(ITALIC, false));
            }

            itemStack = itemStack.withLore(lore);
        } else if(canBuy) {
            List<Component> lore = new ArrayList<>(itemStack.getLore());
            lore.add(text(""));
            if(hasEnoughCoins) {
                lore.add(text("Cliquez pour acheter").color(YELLOW).decoration(ITALIC, false));
            } else {
                lore.add(text("Pas assez d'argent.").color(RED));
            }

            itemStack = itemStack.withLore(lore);

            slotsActions.put(slot, () -> {
                if(!coinsManager.hasEnoughCoins(player, sword.getPrice())) {
                    player.sendMessage(text("Vous n'avez pas assez d'argent.").color(RED));
                    return;
                }

                coinsManager.removeCoins(player, sword.getPrice());
                Swords.equipSwordForPlayer(player, sword);
            });
        } else {
            List<Component> lore = new ArrayList<>(itemStack.getLore());
            lore.add(text(""));
            lore.add(text("Non débloqué").color(GRAY).decoration(ITALIC, false));

            itemStack = ItemStack.builder(Material.GRAY_DYE)
                    .meta(itemStack.meta())
                    .lore(lore)
                    .build();
        }

        return itemStack;
    }

    private void fillSword(Sword sword, SwordType playerCurrentSwordType, Sword currentPlayerSword, int slot) {
        boolean canBuy = false;
        boolean hasBought = true;

        while (sword != null) {
            setItemStack(slot, formatSword(slot, sword, hasBought,
                    currentPlayerSword == sword && currentPlayerSword.getSwordType() == playerCurrentSwordType,
                    currentPlayerSword == sword, canBuy, coinsManager.hasEnoughCoins(player, sword.getPrice())));
            if(slot == 12 || slot == 30) slot++; // Add a margin
            slot++;

            if(canBuy) {
                canBuy = false;
            } else if(sword == currentPlayerSword) {
                canBuy = true;
                hasBought = false;
            }
            sword = sword.getNextSword();
        }
    }
}
