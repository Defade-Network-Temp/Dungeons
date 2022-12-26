package net.defade.dungeons.shop.swords;

import net.defade.dungeons.difficulty.GameDifficulty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagSerializer;
import net.minestom.server.tag.TagWritable;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class Sword extends SwordConfig {
    private static final Tag<Integer> SWORD_TYPE_TAG = Tag.Integer("SwordType");
    private static final Tag<String> SWORD_MATERIAL_TAG = Tag.String("SwordMaterial");
    private static final Tag<Component> SWORD_NAME_TAG = Tag.Component("SwordName");
    private static final Tag<Integer> PRICE_TAG = Tag.Integer("Price");
    private static final Tag<Integer> ATTACK_DAMAGE_TAG = Tag.Integer("AttackDamage");
    private static final Tag<Float> ATTACK_SPEED_TAG = Tag.Float("AttackSpeed");
    private static final Tag<Float> MOVEMENT_SPEED_MODIFIER_TAG = Tag.Float("MovementSpeedModifier");
    private static final Tag<Integer> MAX_DURABILITY_TAG = Tag.Integer("MaxDurability");
    private static final Tag<Integer> DURABILITY_TAG = Tag.Integer("Durability");
    private static final Tag<Boolean> CAN_SWEEP_TAG = Tag.Boolean("CanSweep").defaultValue(false);

    public static final TagSerializer<Sword> SWORD_TAG_SERIALIZER = new TagSerializer<>() {
        @Override
        public Sword read(TagReadable reader) {
            return new Sword(SwordType.values()[reader.getTag(SWORD_TYPE_TAG)], Material.fromNamespaceId(reader.getTag(SWORD_MATERIAL_TAG)),
                    reader.getTag(SWORD_NAME_TAG), reader.getTag(PRICE_TAG), reader.getTag(ATTACK_DAMAGE_TAG), reader.getTag(ATTACK_SPEED_TAG),
                    reader.getTag(MOVEMENT_SPEED_MODIFIER_TAG), reader.getTag(MAX_DURABILITY_TAG), reader.getTag(DURABILITY_TAG),
                    reader.getTag(CAN_SWEEP_TAG));
        }

        @Override
        public void write(TagWritable writer, Sword value) {
            writer.setTag(SWORD_TYPE_TAG, value.getSwordType().ordinal());
            writer.setTag(SWORD_MATERIAL_TAG, value.getMaterial().name());
            writer.setTag(SWORD_NAME_TAG, value.getName());
            writer.setTag(PRICE_TAG, value.getPrice());
            writer.setTag(ATTACK_DAMAGE_TAG, value.getAttackDamage());
            writer.setTag(ATTACK_SPEED_TAG, value.getAttackSpeed());
            writer.setTag(MOVEMENT_SPEED_MODIFIER_TAG, value.getMovementSpeedModifier());
            writer.setTag(MAX_DURABILITY_TAG, value.getMaxDurability());
            writer.setTag(DURABILITY_TAG, value.getDurability());
            writer.setTag(CAN_SWEEP_TAG, value.canSweep());
        }
    };

    public static Tag<Sword> SWORD_TAG = Tag.Structure("Sword", SWORD_TAG_SERIALIZER);

    private int durability = getMaxDurability();

    public Sword(GameDifficulty gameDifficulty, SwordType swordType, Material material, Component name, int price, int attackDamage,
                 float attackSpeed, float movementSpeedModifier, int maxDurability, boolean canSweep) {
        super(swordType, material, name, (int) Math.ceil(price * gameDifficulty.priceMultiplier()), attackDamage, attackSpeed,
                movementSpeedModifier, maxDurability - gameDifficulty.weaponDurabilityReducer(), canSweep);
    }

    private Sword(SwordType swordType, Material material, Component name, int price, int attackDamage, float attackSpeed, float movementSpeedModifier, int maxDurability, int durability, boolean canSweep) {
        super(swordType, material, name, price, attackDamage, attackSpeed, movementSpeedModifier, maxDurability, canSweep);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public ItemStack getAsItemStack() { //TODO: Add enchants and power ups here
        NamedTextColor color = getSwordType() == SwordType.SWORD ? GREEN : YELLOW;

        return ItemStack.builder(getMaterial())
                .displayName(getName())
                .lore(List.of(
                        text("Damage: ").color(GRAY).append(text(getAttackDamage()).color(color)).decoration(ITALIC, false),
                        text("Durability: ").color(GRAY).append(text(getMaxDurability()).color(color)).decoration(ITALIC, false),
                        text("Atk Speed: ").color(GRAY).append(text(getAttackSpeed()).color(color)).decoration(ITALIC, false),
                        text("Movement Speed: ").color(GRAY).append(text((getMovementSpeedModifier() < 0 ? "" : "+") + getMovementSpeedModifier() + "%")
                                .color(getMovementSpeedModifier() < 0 ? RED : GREEN)).decoration(ITALIC, false),
                        text("Sweep: ").color(GRAY).append(text(canSweep() ? "✔" : "❌").color(canSweep() ? GREEN : RED)).decoration(ITALIC, false),
                        text(""),
                        text("Type: ").color(GRAY).append(getSwordType().getName().color(color)).decoration(ITALIC, false)
                ))
                .set(SWORD_TAG, this)
                .meta(builder -> builder.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES))
                .build();
    }
}
