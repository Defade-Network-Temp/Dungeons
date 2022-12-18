package net.defade.dungeons.shop.swords;

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
        WOODEN_SWORD.getSword().setNextSword(STONE_SWORD.getSword());
        STONE_SWORD.getSword().setNextSword(GOLDEN_SWORD.getSword());
        GOLDEN_SWORD.getSword().setNextSword(IRON_SWORD.getSword());
        IRON_SWORD.getSword().setNextSword(DIAMOND_SWORD.getSword());
        DIAMOND_SWORD.getSword().setNextSword(NETHERITE_SWORD.getSword());

        WOODEN_BROADSWORD.getSword().setNextSword(STONE_BROADSWORD.getSword());
        STONE_BROADSWORD.getSword().setNextSword(GOLDEN_BROADSWORD.getSword());
        GOLDEN_BROADSWORD.getSword().setNextSword(IRON_BROADSWORD.getSword());
        IRON_BROADSWORD.getSword().setNextSword(DIAMOND_BROADSWORD.getSword());
        DIAMOND_BROADSWORD.getSword().setNextSword(NETHERITE_BROADSWORD.getSword());
    }

    private final Sword sword;

    Swords(Sword sword) {
        this.sword = sword;
    }

    public Sword getSword() {
        return sword;
    }

    public static void equipSwordForPlayer(Player player, Sword sword) {
        player.setTag(SwordType.SWORD_TYPE_TAG, sword.getSwordType());
        player.setTag(sword.getSwordType().getSwordTag(), sword);

        player.getInventory().setItemStack(0, sword.getAsItemStack());
    }

    private static Sword build(SwordType swordType, Material material, String name, int price, int attackDamage, float attackSpeed, float movementSpeedModifier, int durability, boolean canSweep) {
        return new Sword(swordType, material, Component.text(name).decoration(TextDecoration.ITALIC, false),
                price, attackDamage, attackSpeed, movementSpeedModifier, durability, canSweep);
    }
}
