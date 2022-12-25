package net.defade.dungeons.shop.swords;

import net.defade.dungeons.difficulty.GameDifficulty;
import net.defade.dungeons.game.GameInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

import static net.defade.dungeons.shop.swords.SwordType.*;

public enum Swords {
    WOODEN_SWORD(build(SWORD, Material.WOODEN_SWORD, "Wooden Sword", 0, 3, 1.4F, -10, 15, true)),
    STONE_SWORD(build(SWORD, Material.STONE_SWORD, "Stone Sword", 50, 4, 1.4F, -10, 17, true)),
    GOLDEN_SWORD(build(SWORD, Material.GOLDEN_SWORD, "Golden Sword", 200, 5, 1.3F, -15, 18, true)),
    IRON_SWORD(build(SWORD, Material.IRON_SWORD, "Iron Sword", 500, 6, 1.2F, -15, 20, true)),
    DIAMOND_SWORD(build(SWORD, Material.DIAMOND_SWORD, "Diamond Sword", 750, 8, 1.2F, -15, 21, true)),
    NETHERITE_SWORD(build(SWORD, Material.NETHERITE_SWORD, "Netherite Sword", 2000, 9, 1.1F, -20, 23, true)),

    WOODEN_BROADSWORD(build(BROADSWORD, Material.WOODEN_HOE, "Wooden Broadsword", 0, 2, 2.0F, 10, 17, false)),
    STONE_BROADSWORD(build(BROADSWORD, Material.STONE_HOE, "Stone Broadsword", 50, 2, 2.0F, 10, 20, false)),
    GOLDEN_BROADSWORD(build(BROADSWORD, Material.GOLDEN_HOE, "Golden Broadsword", 200, 3, 1.9F, 15, 22, false)),
    IRON_BROADSWORD(build(BROADSWORD, Material.IRON_HOE, "Iron Broadsword", 500, 5, 1.9F, 15, 24, false)),
    DIAMOND_BROADSWORD(build(BROADSWORD, Material.DIAMOND_HOE, "Diamond Broadsword", 750, 6, 1.9F, 15, 26, true)),
    NETHERITE_BROADSWORD(build(BROADSWORD, Material.NETHERITE_HOE, "Netherite Broadsword", 2000, 7, 1.7F, 20, 28, true));

    static {
        WOODEN_SWORD.nextSword = STONE_SWORD;
        STONE_SWORD.nextSword = GOLDEN_SWORD;
        GOLDEN_SWORD.nextSword = IRON_SWORD;
        IRON_SWORD.nextSword = DIAMOND_SWORD;
        DIAMOND_SWORD.nextSword = NETHERITE_SWORD;

        WOODEN_BROADSWORD.nextSword = STONE_BROADSWORD;
        STONE_BROADSWORD.nextSword = GOLDEN_BROADSWORD;
        GOLDEN_BROADSWORD.nextSword = IRON_BROADSWORD;
        IRON_BROADSWORD.nextSword = DIAMOND_BROADSWORD;
        DIAMOND_BROADSWORD.nextSword = NETHERITE_BROADSWORD;
    }

    private final SwordConfig sword;
    private Swords nextSword = null;

    Swords(SwordConfig sword) {
        this.sword = sword;
    }

    public Sword getSword(GameDifficulty gameDifficulty) {
        return sword.getAsSword(gameDifficulty);
    }

    public Swords getNextSword() {
        return nextSword;
    }

    public static void equipSwordForPlayer(Player player, Swords sword) {
        GameInstance gameInstance = (GameInstance) player.getInstance();

        if(gameInstance != null) {
            Sword swordToEquip = sword.getSword(gameInstance.getDifficulty());

            player.setTag(swordToEquip.getSwordType().getSwordTag(), sword);
            player.getInventory().setItemStack(0, swordToEquip.getAsItemStack());
        }
    }

    private static SwordConfig build(SwordType swordType, Material material, String name, int price, int attackDamage, float attackSpeed, float movementSpeedModifier, int durability, boolean canSweep) {
        return new SwordConfig(swordType, material, Component.text(name).decoration(TextDecoration.ITALIC, false),
                price, attackDamage, attackSpeed, movementSpeedModifier, durability, canSweep);
    }
}
