package net.defade.dungeons.shop.swords;

import net.kyori.adventure.text.Component;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagSerializer;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum SwordType {
    SWORD(Component.text("Sword"), Tag.Structure("Sword", new TagSerializer<>() {
        @Override
        public @Nullable Sword read(@NotNull TagReadable reader) {
            return Swords.valueOf(reader.getTag(Tag.String("Sword"))).getSword();
        }

        @Override
        public void write(@NotNull TagWritable writer, @NotNull Sword value) {
            writer.setTag(Tag.String("Sword"), value.toString());
        }
    })),
    BROADSWORD(Component.text("Broadsword"), Tag.Structure("BroadSword", new TagSerializer<>() {
        @Override
        public @Nullable Sword read(@NotNull TagReadable reader) {
            return Swords.valueOf(reader.getTag(Tag.String("BroadSword"))).getSword();
        }

        @Override
        public void write(@NotNull TagWritable writer, @NotNull Sword value) {
            writer.setTag(Tag.String("BroadSword"), value.toString());
        }
    }));

    public final static Tag<SwordType> SWORD_TYPE_TAG = Tag.Structure("SwordType", new TagSerializer<>() {
        @Override
        public SwordType read(@NotNull TagReadable reader) {
            return SwordType.valueOf(reader.getTag(Tag.String("SwordType")));
        }

        @Override
        public void write(@NotNull TagWritable writer, @NotNull SwordType value) {
            writer.setTag(Tag.String("SwordType"), value.toString());
        }
    });

    private final Component name;
    private final Tag<Sword> swordTag;

    SwordType(Component name, Tag<Sword> swordTag) {
        this.name = name;
        this.swordTag = swordTag;
    }

    public Component getName() {
        return name;
    }

    public Tag<Sword> getSwordTag() {
        return swordTag;
    }
}
