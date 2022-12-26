package net.defade.dungeons.utils;

import net.defade.dungeons.difficulty.Difficulty;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

@SuppressWarnings("UnstableApiUsage")
public class ItemList {
    public static ItemStack INVENTORY_FILLER = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
            .displayName(text(""))
            .build();

    public static ItemStack DIFFICULTY_SELECTOR = ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(
                    text("» ").color(color(GRAY))
                            .append(text("Choisir la difficulté").color(color(215, 10, 10))
                            .append(text(" «").color(color(GRAY)))).decoration(ITALIC, false)
            )
            .meta(PlayerHeadMeta.class, headMeta -> {
                headMeta.skullOwner(UUID.randomUUID());
                headMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYyNGVkNjg3NTMwNGZhNG" +
                        "ExZjBjNzg1YjJjYjZhNmE3MjU2M2U5ZjNlMjRlYTU1ZTE4MTc4NDUyMTE5YWE2NiJ9fX0=", ""));
            })
            .build();

    public static ItemStack NORMAL_DIFFICULTY = ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(Difficulty.NORMAL.getName())
            .meta(PlayerHeadMeta.class, headMeta -> {
                headMeta.skullOwner(UUID.randomUUID());
                headMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJlY2M2NDVmMTI5YzhiYzJmY" +
                        "WE0ZDgxNDU0ODFmYWIxMWFkMmVlNzU3NDlkNjI4ZGNkOTk5YWE5NGU3In19fQ==", ""));
            })
            .build();

    public static ItemStack HARD_DIFFICULTY = ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(Difficulty.HARD.getName())
            .meta(PlayerHeadMeta.class, headMeta -> {
                headMeta.skullOwner(UUID.randomUUID());
                headMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I4NTJiYTE1ODRkYTllNTcxNDg1OTk5NTQ1MWU0Yjk0NzQ4YzRkZDYzYWU0NTQzYzE1ZjlmOGFlYzY1YzgifX19", ""));
            })
            .build();

    public static ItemStack INSANE_DIFFICULTY = ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(Difficulty.INSANE.getName())
            .meta(PlayerHeadMeta.class, headMeta -> {
                headMeta.skullOwner(UUID.randomUUID());
                headMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ5ZDgwYjc5NDQyY2YxYTNhZmVhYTIz" +
                        "N2JkNmFkYWFhY2FiMGMyODgzMGZiMzZiNTcwNGNmNGQ5ZjU5MzdjNCJ9fX0=", ""));
            })
            .build();

    public static ItemStack SWORD_GUI = ItemStack.builder(Material.STONE_SWORD)
            .displayName(text("Épées").color(LIGHT_PURPLE).decoration(ITALIC, false))
            .meta(builder -> builder.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES))
            .build();

    public static ItemStack ARMORS_GUI = ItemStack.builder(Material.IRON_CHESTPLATE)
            .displayName(text("Armures").color(AQUA).decoration(ITALIC, false))
            .meta(builder -> builder.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES))
            .build();

    public static ItemStack RADIO_ON = ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(text("» ").color(color(GRAY)).decoration(ITALIC, false)
                    .append(text("Radio").color(color(70, 170, 35))))
            .meta(PlayerHeadMeta.class, headMeta -> {
                headMeta.skullOwner(UUID.randomUUID());
                headMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZn" +
                        "QubmV0L3RleHR1cmUvZGViZGVhMmI1YzRkYjVmYTQ0YTRlYzQwMzI2NTgzMmZhN2QxY2FmYThjNGE2Y2Y3ZmE2OTYwYmJhY2Q3In19fQ==", ""));
            })
            .build();

    public static ItemStack RADIO_OFF = ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(text("e ").color(color(GRAY)).decoration(OBFUSCATED, true)
                    .append(text("Radio brouillée").color(color(90, 90, 90)).decoration(OBFUSCATED, false))
                    .append(text(" e").decoration(OBFUSCATED, true)).decoration(ITALIC,false))
            .meta(PlayerHeadMeta.class, headMeta -> {
                headMeta.skullOwner(UUID.randomUUID());
                headMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5l" +
                        "Y3JhZnQubmV0L3RleHR1cmUvNGMzYzg1MTc1MTZmOGQ4ZTgwNjc3ODFlN2M2MmVlYTI3ZGU0NzhiMTRjNGE2OGM4ZThjMWFkOGFmMWJhZTIxIn19fQ==", ""));
            })
            .build();

    public static ItemStack ACTUAL_ARMOR = ItemStack.builder(Material.LIME_DYE)
            .displayName(text("Armure Actuelle").color(GREEN).decoration(ITALIC, false))
            .build();

    public static BiFunction<Integer, Integer, ItemStack> BUY_ARMOR = (price, minWave) -> ItemStack.builder(Material.LIGHT_BLUE_DYE)
            .displayName(text("Acheter ").color(AQUA).decoration(ITALIC, false)
                    .append(text("(" + price + " coins)").color(GRAY)))
            .lore(minWave == -1 ? List.of() : List.of(Component.text("Débloquable à la vague " + minWave + ".").color(RED).decoration(ITALIC, false)))
            .build();

    public static ItemStack LESS_INJURED_ZOMBIE_HEAD = ItemStack.builder(Material.PLAYER_HEAD)
            .meta(PlayerHeadMeta.class, headMeta -> {
                headMeta.skullOwner(UUID.randomUUID());
                headMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J" +
                        "hZnQubmV0L3RleHR1cmUvYzNmYWI3NmY3MTgwOTJkNjRiN2IwMGRkYjcyOTMxYzBiMDVlOGNiYjY4NzEwNmMzYzZmMzU2MGY5MzNmOTkzMCJ9fX0=", ""));
            })
            .build();

    public static ItemStack INJURED_ZOMBIE_HEAD = ItemStack.builder(Material.PLAYER_HEAD)
            .meta(PlayerHeadMeta.class, playerHeadMeta -> {
                playerHeadMeta.skullOwner(UUID.randomUUID());
                playerHeadMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5l" +
                        "Y3JhZnQubmV0L3RleHR1cmUvZmFhYzIyMzAxNTlhODAzZDI4Y2ZkZTY2NjJlYWYzNzlkYTg5YThhMDczYzdiZTIwYzZlN2U0MDhkZDg4NjFkMSJ9fX0=", ""));
            })
            .build();

    public static ItemStack BLOOD_ZOMBIE_HEAD = ItemStack.builder(Material.PLAYER_HEAD)
            .meta(PlayerHeadMeta.class, playerHeadMeta -> {
                playerHeadMeta.skullOwner(UUID.randomUUID());
                playerHeadMeta.playerSkin(new PlayerSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J" +
                        "hZnQubmV0L3RleHR1cmUvNDVlOTZlMTJjN2ZhZTcwZjU0Mjc4ZGUxODA5YjM4OGExNjcyZjM5YmFiYTM0YTI5ODYxNDRmZTJkNjJiNTBmYSJ9fX0=", ""));
            })
            .build();
}
