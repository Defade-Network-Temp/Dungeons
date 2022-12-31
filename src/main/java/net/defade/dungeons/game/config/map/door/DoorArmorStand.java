package net.defade.dungeons.game.config.map.door;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.CompletableFuture;

class DoorArmorStand extends Entity {
    private final Entity priceArmorStand;
    private final Pos position;

    protected DoorArmorStand(Component name, Pos position, int doorPrice) {
        super(EntityType.ARMOR_STAND);
        this.position = position;
        this.priceArmorStand = createPriceArmorStand(doorPrice);

        setCustomName(name);
        setCustomNameVisible(false);

        setInvisible(true);
        ArmorStandMeta armorStandMeta = (ArmorStandMeta) getEntityMeta();
        armorStandMeta.setInvisible(true);
        armorStandMeta.setSmall(true);
        armorStandMeta.setHasNoGravity(true);
    }

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance) {
        priceArmorStand.setInstance(instance, position.withY(y -> y - 0.25));
        return super.setInstance(instance, position);
    }

    @Override
    public void remove() {
        priceArmorStand.remove();
        super.remove();
    }

    @Override
    public void setCustomNameVisible(boolean customNameVisible) {
        priceArmorStand.setCustomNameVisible(customNameVisible);
        super.setCustomNameVisible(customNameVisible);
    }

    private static Entity createPriceArmorStand(int doorPrice) {
        Entity priceArmorStand = new Entity(EntityType.ARMOR_STAND);
        priceArmorStand.setCustomName(Component.text(doorPrice + " Coins").color(NamedTextColor.GOLD));
        priceArmorStand.setCustomNameVisible(false);

        priceArmorStand.setInvisible(true);
        ArmorStandMeta armorStandMeta = (ArmorStandMeta) priceArmorStand.getEntityMeta();
        armorStandMeta.setInvisible(true);
        armorStandMeta.setSmall(true);
        armorStandMeta.setHasNoGravity(true);

        return priceArmorStand;
    }
}
