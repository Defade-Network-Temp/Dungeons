package net.defade.dungeons.shop.swords;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class Sword {
    public static Tag<Integer> ATTACK_DAMAGE_TAG = Tag.Integer("AttackDamage");
    public static Tag<Float> ATTACK_SPEED_TAG = Tag.Float("AttackSpeed");
    public static Tag<Double> MOVEMENT_SPEED_REDUCTION_TAG = Tag.Double("MovementSpeedReduction");
    public static Tag<Integer> DURABILITY_TAG = Tag.Integer("Durability");

    private final SwordType swordType;
    private final Material material;
    private final Component name;
    private final int price;
    private final int attackDamage;
    private final float attackSpeed;
    private final double movementSpeedReduction;
    private final int durability;
    private Sword nextSword;

    public Sword(SwordType swordType, Material material, Component name, int price, int attackDamage, float attackSpeed, double movementSpeedReduction, int durability) {
        this.swordType = swordType;
        this.material = material;
        this.name = name;
        this.price = price;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.movementSpeedReduction = movementSpeedReduction;
        this.durability = durability;
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

    public double getMovementSpeedReduction() {
        return movementSpeedReduction;
    }

    public int getDurability() {
        return durability;
    }

    public Sword getNextSword() {
        return nextSword;
    }

    protected void setNextSword(Sword nextSword) {
        this.nextSword = nextSword;
    }

    public ItemStack getAsItemStack() { //TODO: Add enchants and power ups here
        return ItemStack.builder(getMaterial())
                .displayName(getName())
                .lore(List.of(
                        text("Damage: ").color(GRAY).append(text(getAttackDamage()).color(GREEN)).decoration(ITALIC, false),
                        text("Durability: ").color(GRAY).append(text(getDurability()).color(GREEN)).decoration(ITALIC, false),
                        text("Atk Speed: ").color(GRAY).append(text(getAttackDamage()).color(GREEN)).decoration(ITALIC, false),
                        text("Movement Speed: ").color(GRAY).append(text("-" + getMovementSpeedReduction() + "%").color(RED)).decoration(ITALIC, false),
                        text(""),
                        text("Type: ").color(GRAY).append(getSwordType().getName().color(GREEN)).decoration(ITALIC, false)
                ))
                .set(ATTACK_DAMAGE_TAG, getAttackDamage())
                .set(ATTACK_SPEED_TAG, getAttackSpeed())
                .set(MOVEMENT_SPEED_REDUCTION_TAG, getMovementSpeedReduction())
                .set(DURABILITY_TAG, getDurability())
                .meta(builder -> builder.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES))
                .build();
    }
}
