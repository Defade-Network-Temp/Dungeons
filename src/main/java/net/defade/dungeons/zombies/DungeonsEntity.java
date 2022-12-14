package net.defade.dungeons.zombies;

import net.defade.dungeons.game.GameInstance;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    public void setInstance(@NotNull GameInstance instance, @NotNull Pos spawnPosition) {
        float newHealth = getMaxHealth() + switch (instance.getDifficulty()) {
            case NORMAL -> 1;
            case HARD -> 1.25f;
            case INSANE -> 1.5f;
        };

        getAttribute(Attribute.MAX_HEALTH).setBaseValue(newHealth);
        setHealth(newHealth);

        super.setInstance(instance, spawnPosition);
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

    public Sound getHurtSound() {
        return Sound.sound(Key.key("minecraft:entity.zombie.hurt"), Sound.Source.HOSTILE, 1,
                (ThreadLocalRandom.current().nextFloat() - ThreadLocalRandom.current().nextFloat()) * 0.2F + 1.0F);
    }

    public Sound getDeathSound() {
        return Sound.sound(Key.key("minecraft:entity.zombie.death"), Sound.Source.HOSTILE, 1,
                (ThreadLocalRandom.current().nextFloat() - ThreadLocalRandom.current().nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public double getMovementSpeed() {
        return speed;
    }
}
