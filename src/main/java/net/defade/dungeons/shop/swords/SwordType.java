package net.defade.dungeons.shop.swords;

import net.kyori.adventure.text.Component;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagSerializer;
import net.minestom.server.tag.TagWritable;
import org.jetbrains.annotations.NotNull;

public enum SwordType {
    SWORD(Component.text("Sword"), Tag.Structure("Sword", new TagSerializer<>() {
        @Override
        public Swords read(@NotNull TagReadable reader) {
            return Swords.valueOf(reader.getTag(Tag.String("Sword")));
        }

        @Override
        public void write(@NotNull TagWritable writer, @NotNull Swords value) {
            writer.setTag(Tag.String("Sword"), value.name());
        }
    })),
    BROADSWORD(Component.text("Broadsword"), Tag.Structure("Broadsword", new TagSerializer<>() {
        @Override
        public Swords read(@NotNull TagReadable reader) {
            return Swords.valueOf(reader.getTag(Tag.String("Broadsword")));
        }

        @Override
        public void write(@NotNull TagWritable writer, @NotNull Swords value) {
            writer.setTag(Tag.String("Broadsword"), value.name());
        }
    }));

    private final Component name;
    private final Tag<Swords> swordTag;

    SwordType(Component name, Tag<Swords> swordTag) {
        this.name = name;
        this.swordTag = swordTag;
    }

    public Component getName() {
        return name;
    }

    public Tag<Swords> getSwordTag() {
        return swordTag;
    }
}
