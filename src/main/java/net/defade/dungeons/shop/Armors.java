package net.defade.dungeons.shop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagSerializer;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minestom.server.item.Material.*;

public enum Armors {
    NOTHING(0, 0, 0, 0, ItemStack.AIR, ItemStack.AIR, ItemStack.AIR, ItemStack.AIR),
    FULL_LEATHER(
            150,
            0,
            7,
            0,
            item(LEATHER_HELMET),
            item(LEATHER_CHESTPLATE),
            item(LEATHER_LEGGINGS),
            item(LEATHER_BOOTS)
    ),
    FULL_GOLD(
            250,
            0,
            10,
            0,
            item(GOLDEN_HELMET),
            item(GOLDEN_CHESTPLATE),
            item(GOLDEN_LEGGINGS),
            item(GOLDEN_BOOTS)
    ),
    FULL_CHAINMAIL(
            500,
            0,
            13,
            0,
            item(CHAINMAIL_HELMET),
            item(CHAINMAIL_CHESTPLATE),
            item(CHAINMAIL_LEGGINGS),
            item(CHAINMAIL_BOOTS)
    ),
    FULL_IRON(
            500,
            0,
            15,
            0,
            item(IRON_HELMET),
            item(IRON_CHESTPLATE),
            item(IRON_LEGGINGS),
            item(IRON_BOOTS)
    ),
    IRON_DIAMOND(
            1000,
            0,
            17,
            0,
            item(IRON_HELMET),
            item(DIAMOND_CHESTPLATE),
            item(IRON_LEGGINGS),
            item(DIAMOND_BOOTS)
    ),
    FULL_DIAMOND(
            1500,
            0,
            20,
            0,
            item(DIAMOND_HELMET),
            item(DIAMOND_CHESTPLATE),
            item(DIAMOND_LEGGINGS),
            item(DIAMOND_BOOTS)
    ),
    DIAMOND_NETHERITE_HELMET(
            2000,
            30,
            25,
            0,
            item(NETHERITE_HELMET),
            item(DIAMOND_CHESTPLATE),
            item(DIAMOND_LEGGINGS),
            item(DIAMOND_BOOTS)
    ),
    DIAMOND_NETHERITE(
            3000,
            30,
            30,
            2,
            item(NETHERITE_HELMET),
            item(DIAMOND_CHESTPLATE),
            item(DIAMOND_LEGGINGS),
            item(NETHERITE_BOOTS)
    ),
    NETHERITE_DIAMOND_CHESTPLATE(
            5000,
            30,
            35,
            2,
            item(NETHERITE_HELMET),
            item(DIAMOND_CHESTPLATE),
            item(NETHERITE_LEGGINGS),
            item(NETHERITE_BOOTS)
    ),
    FULL_NETHERITE(
            10000,
            30,
            40,
            4,
            item(NETHERITE_HELMET),
            item(NETHERITE_CHESTPLATE),
            item(NETHERITE_LEGGINGS),
            item(NETHERITE_BOOTS)
    );

    public static final Tag<Armors> ARMOR_TAG = Tag.Structure("Armor", new TagSerializer<>() {
        @Override
        public Armors read(@NotNull TagReadable reader) {
            return valueOf(reader.getTag(Tag.String("Armor")));
        }

        @Override
        public void write(@NotNull TagWritable writer, @NotNull Armors value) {
            writer.setTag(Tag.String("Armor"), value.toString());
        }
    });

    public static final Tag<Integer> DAMAGE_RESISTANCE_PERCENTAGE_TAG = Tag.Integer("DamageResistancePercentage").defaultValue(0);

    private final int price;
    private final int minWave;
    private final int damageResistancePercentage;
    private final int addedHealth;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    Armors(int price, int minWave, int damageResistancePercentage, int addedHealth, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.price = price;
        this.minWave = minWave;
        this.damageResistancePercentage = damageResistancePercentage;
        this.addedHealth = addedHealth;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public int getPrice() {
        return price;
    }

    public int getMinWave() {
        return minWave;
    }

    public int getDamageResistancePercentage() {
        return damageResistancePercentage;
    }

    public int getAddedHealth() {
        return addedHealth;
    }

    public ItemStack getHelmet() {
        return formatItem(helmet);
    }

    public ItemStack getChestplate() {
        return formatItem(chestplate);
    }

    public ItemStack getLeggings() {
        return formatItem(leggings);
    }

    public ItemStack getBoots() {
        return formatItem(boots);
    }

    public void equipForPlayer(Player player) {
        player.setTag(ARMOR_TAG, this);
        player.setTag(DAMAGE_RESISTANCE_PERCENTAGE_TAG, damageResistancePercentage);
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(Attribute.MAX_HEALTH.defaultValue() + addedHealth);

        player.getInventory().setHelmet(getHelmet());
        player.getInventory().setChestplate(getChestplate());
        player.getInventory().setLeggings(getLeggings());
        player.getInventory().setBoots(getBoots());
    }

    private static ItemStack item(Material material) {
        return ItemStack.builder(material)
                .meta(builder -> builder.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES))
                .build();
    }

    private ItemStack formatItem(ItemStack itemStack) {
        return itemStack.withLore(List.of(
                Component.text("Resistance: ").color(NamedTextColor.GRAY)
                        .append(Component.text(getDamageResistancePercentage() + "%").color(TextColor.color(58, 174, 248))).decoration(TextDecoration.ITALIC, false),
                Component.text("Hearts: ").color(NamedTextColor.GRAY)
                        .append(Component.text("+" + getAddedHealth()).color(TextColor.color(58, 174, 249))).decoration(TextDecoration.ITALIC, false)
        ));
    }
}
