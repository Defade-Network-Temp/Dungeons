package net.defade.dungeons.shop.swords;

import net.kyori.adventure.text.Component;
import net.minestom.server.tag.Tag;

public enum SwordType {
    SWORD(Component.text("Sword"), Tag.String("Sword").map(Swords::valueOf, Enum::name)),
    BROADSWORD(Component.text("Broadsword"), Tag.String("Broadsword").map(Swords::valueOf, Enum::name));

    public static final Tag<SwordType> SWORD_TYPE_TAG = Tag.String("SwordType").map(SwordType::valueOf, Enum::name);

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
