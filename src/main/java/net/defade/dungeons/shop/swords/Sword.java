package net.defade.dungeons.shop.swords;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    public static Tag<Float> MOVEMENT_SPEED_MODIFIER_TAG = Tag.Float("MovementSpeedModifier");
    public static Tag<Integer> DURABILITY_TAG = Tag.Integer("Durability");

    private final SwordType swordType;
    private final Material material;
    private final Component name;
    private final int price;
    private final int attackDamage;
    private final float attackSpeed;
    private final float movementSpeedModifier;
    private final int durability;
    private Sword nextSword;

    public Sword(SwordType swordType, Material material, Component name, int price, int attackDamage, float attackSpeed, float movementSpeedModifier, int durability) {
        this.swordType = swordType;
        this.material = material;
        this.name = name;
        this.price = price;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.movementSpeedModifier = movementSpeedModifier;
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

    public float getMovementSpeedModifier() {
        return movementSpeedModifier;
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
        NamedTextColor color = swordType == SwordType.SWORD ? GREEN : YELLOW;

        return ItemStack.builder(getMaterial())
                .displayName(getName())
                .lore(List.of(
                        text("Damage: ").color(GRAY).append(text(getAttackDamage()).color(color)).decoration(ITALIC, false),
                        text("Durability: ").color(GRAY).append(text(getDurability()).color(color)).decoration(ITALIC, false),
                        text("Atk Speed: ").color(GRAY).append(text(getAttackSpeed()).color(color)).decoration(ITALIC, false),
                        text("Movement Speed: ").color(GRAY).append(text(
                                        (getMovementSpeedModifier() < 0 ? "" : "+") + getMovementSpeedModifier() + "%").color(getMovementSpeedModifier() < 0 ? RED : GREEN)                        ).decoration(ITALIC, false),
                        text(""),
                        text("Type: ").color(GRAY).append(getSwordType().getName().color(color)).decoration(ITALIC, false)
                ))
                .set(ATTACK_DAMAGE_TAG, getAttackDamage())
                .set(ATTACK_SPEED_TAG, getAttackSpeed())
                .set(MOVEMENT_SPEED_MODIFIER_TAG, getMovementSpeedModifier())
                .set(DURABILITY_TAG, getDurability())
                .meta(builder -> builder.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES))
                .build();
    }
}
