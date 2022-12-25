package net.defade.dungeons.shop.swords;

import net.defade.dungeons.difficulty.GameDifficulty;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

class SwordConfig {
    private final SwordType swordType;
    private final Material material;
    private final Component name;
    private final int price;
    private final int attackDamage;
    private final float attackSpeed;
    private final float movementSpeedModifier;
    private final int maxDurability;
    private final boolean canSweep;

    public SwordConfig(SwordType swordType, Material material, Component name, int price, int attackDamage, float attackSpeed, float movementSpeedModifier, int maxDurability, boolean canSweep) {
        this.swordType = swordType;
        this.material = material;
        this.name = name;
        this.price = price;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.movementSpeedModifier = movementSpeedModifier;
        this.maxDurability = maxDurability;
        this.canSweep = canSweep;
    }

    public SwordType getSwordType() {
        return swordType;
    }

    public Material getMaterial() {
        return material;
    }

    public Component getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public float getMovementSpeedModifier() {
        return movementSpeedModifier;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public boolean canSweep() {
        return canSweep;
    }

    protected Sword getAsSword(GameDifficulty gameDifficulty) {
        return new Sword(gameDifficulty, swordType, material, name, price, attackDamage, attackSpeed, movementSpeedModifier, getMaxDurability(), canSweep);
    }
}
