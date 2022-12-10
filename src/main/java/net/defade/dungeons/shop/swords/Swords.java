package net.defade.dungeons.shop.swords;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeModifier;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import java.util.UUID;

import static net.defade.dungeons.shop.swords.SwordType.*;

public enum Swords {
    WOODEN_SWORD(build(SWORD, Material.WOODEN_SWORD, "Wooden Sword", 0, 3, 1.5F, -10, 15)),
    STONE_SWORD(build(SWORD, Material.STONE_SWORD, "Stone Sword", 50, 4, 1.5F, -10, 17)),
    GOLDEN_SWORD(build(SWORD, Material.GOLDEN_SWORD, "Golden Sword", 200, 5, 1.2F, -15, 18)),
    IRON_SWORD(build(SWORD, Material.IRON_SWORD, "Iron Sword", 500, 6, 1.3F, -15, 20)),
    DIAMOND_SWORD(build(SWORD, Material.DIAMOND_SWORD, "Diamond Sword", 750, 8, 1.3F, -15, 21)),
    NETHERITE_SWORD(build(SWORD, Material.NETHERITE_SWORD, "Netherite Sword", 2000, 9, 1.2F, -20, 23)),

    WOODEN_BROADSWORD(build(BROADSWORD, Material.WOODEN_HOE, "Wooden Broadsword", 0, 2, 1.8F, 10, 17)),
    STONE_BROADSWORD(build(BROADSWORD, Material.STONE_HOE, "Stone Broadsword", 50, 2, 1.8F, 10, 20)),
    GOLDEN_BROADSWORD(build(BROADSWORD, Material.GOLDEN_HOE, "Golden Broadsword", 200, 3, 1.7F, 15, 22)),
    IRON_BROADSWORD(build(BROADSWORD, Material.IRON_HOE, "Iron Broadsword", 500, 5, 1.7F, 15, 24)),
    DIAMOND_BROADSWORD(build(BROADSWORD, Material.DIAMOND_HOE, "Diamond Broadsword", 750, 6, 1.7F, 15, 26)),
    NETHERITE_BROADSWORD(build(BROADSWORD, Material.NETHERITE_HOE, "Netherite Broadsword", 2000, 7, 1.6F, 20, 28));

    static {
        WOODEN_SWORD.getSword().setNextSword(STONE_SWORD.getSword());
        STONE_SWORD.getSword().setNextSword(GOLDEN_SWORD.getSword());
        GOLDEN_SWORD.getSword().setNextSword(IRON_SWORD.getSword());
        IRON_SWORD.getSword().setNextSword(DIAMOND_SWORD.getSword());
        DIAMOND_SWORD.getSword().setNextSword(NETHERITE_SWORD.getSword());

        WOODEN_BROADSWORD.getSword().setNextSword(STONE_BROADSWORD.getSword());
        STONE_BROADSWORD.getSword().setNextSword(GOLDEN_BROADSWORD.getSword());
        GOLDEN_BROADSWORD.getSword().setNextSword(IRON_BROADSWORD.getSword());
        IRON_BROADSWORD.getSword().setNextSword(DIAMOND_BROADSWORD.getSword());
        DIAMOND_BROADSWORD.getSword().setNextSword(NETHERITE_BROADSWORD.getSword());
    }

    private final Sword sword;

    Swords(Sword sword) {
        this.sword = sword;
    }

    public Sword getSword() {
        return sword;
    }

    public static void equipSwordForPlayer(Player player, Sword sword) {
        player.setTag(SwordType.SWORD_TYPE_TAG, sword.getSwordType());
        player.setTag(sword.getSwordType().getSwordTag(), sword);

        player.getInventory().setItemStack(0, sword.getAsItemStack());
    }

    private static Sword build(SwordType swordType, Material material, String name, int price, int attackDamage, float attackSpeed, float movementSpeedModifier, int durability) {
        return new Sword(swordType, material, Component.text(name).decoration(TextDecoration.ITALIC, false),
                price, attackDamage, attackSpeed, movementSpeedModifier, durability);
    }

    public static void registerSwordSelectEvent(EventNode<PlayerEvent> eventNode) {
        eventNode.addListener(PlayerChangeHeldSlotEvent.class, event -> {
            Player player = event.getPlayer();
            ItemStack itemStack = player.getInventory().getItemStack(event.getSlot());

            boolean isSprinting = player.isSprinting();
            if(itemStack.hasTag(Sword.MOVEMENT_SPEED_MODIFIER_TAG)) {
                player.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(new AttributeModifier(
                        UUID.fromString("7AB1E3FF-A61C-46AF-80F1-77A8B649F9E6"),
                        "generic.movement_speed",
                        itemStack.getTag(Sword.MOVEMENT_SPEED_MODIFIER_TAG) / 100,
                        AttributeOperation.MULTIPLY_BASE
                ));
            } else {
                player.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(UUID.fromString("7AB1E3FF-A61C-46AF-80F1-77A8B649F9E6"));
            }

            if(isSprinting) {
                /* Mojang are idiots. When a player sprints, they add a modifier to the MOVEMENT_SPEED attribute but doesn't send it to the server...
                   Even worse, when they receive the attribute packet, they remove all of its modifiers and only adds the one the server sent him.
                   So when we add the sword speed modifier... well... the sprint modifier is yeeted out of existence... So we have to send it back to the player.
                   Nice job, mojang. */
                player.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(new AttributeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"),
                        "Sprinting speed boost", 0.3F, AttributeOperation.MULTIPLY_TOTAL));
            }
        });
    }
}
