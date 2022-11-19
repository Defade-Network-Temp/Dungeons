package net.defade.dungeons.utils;

import net.defade.dungeons.difficulty.Difficulty;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

public class ItemList {
    public static ItemStack INVENTORY_FILLER = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
            .displayName(text(""))
            .build();

    public static ItemStack DIFFICULTY_SELECTOR = ItemStack.builder(Material.PLAYER_HEAD)
            .displayName(
                    text("» ").color(color(NamedTextColor.GRAY))
                            .append(text("Choisir la difficulté").color(color(215, 10, 10))
                            .append(text(" «").color(color(NamedTextColor.GRAY)))).decoration(TextDecoration.ITALIC, false)
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
}
