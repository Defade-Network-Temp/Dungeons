package net.defade.dungeons.zombies;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.text.DecimalFormat;
import java.util.List;

public abstract class DungeonsEntity extends EntityCreature {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    private final String name;
    private final int regen;
    private final int ticksPerRegen;
    private final int dungeonsCoins;
    private final double speed;
    private final double damageResistance;
    private final double attackDamage;

    public DungeonsEntity(@NotNull EntityType entityType, String name, float health, int regen, int ticksPerRegen,
                          int dungeonsCoins, double speed, double damageResistance, double attackDamage) {
        super(entityType);

        this.name = name;
        this.regen = regen;
        this.ticksPerRegen = ticksPerRegen;
        this.dungeonsCoins = dungeonsCoins;
        this.speed = speed;
        this.damageResistance = damageResistance;
        this.attackDamage = attackDamage;

        getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
        setHealth(health);

        setCustomNameVisible(true);
    }

    @Override
    public void tick(long time) {
        if(ticksPerRegen != 0 && time % ticksPerRegen == 0) {
            float newHealth = getHealth() + regen;
            if(newHealth > getAttributeValue(Attribute.MAX_HEALTH)) {
                newHealth = getAttributeValue(Attribute.MAX_HEALTH);
            }

            setHealth(newHealth);
        }

        setCustomName(Component.text("[").color(NamedTextColor.DARK_GRAY)
                .append(Component.text(name).color(NamedTextColor.GRAY))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(DECIMAL_FORMAT.format(getHealth()) + "/" + DECIMAL_FORMAT.format(getMaxHealth())).color(NamedTextColor.WHITE))
                .append(Component.text("❤").color(TextColor.color(200, 35, 35))));

        super.tick(time);
    }

    @Override
    public void kill() {
        super.kill();

        // TODO drop the items
    }

    public List<ItemStack> getDrops() {
        return List.of();
    }

    public int getDungeonsCoins() {
        return dungeonsCoins;
    }

    public double getDamageResistance() {
        return damageResistance;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    @Override
    public double getMovementSpeed() {
        return speed;
    }
}
