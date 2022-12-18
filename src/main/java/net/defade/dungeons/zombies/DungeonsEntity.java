package net.defade.dungeons.zombies;

import net.defade.dungeons.game.GameInstance;
import net.defade.dungeons.shop.Armors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
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
    private final float damageResistance;
    private final float attackDamage;

    public DungeonsEntity(@NotNull EntityType entityType, String name, float health, int regen, int ticksPerRegen,
                          int dungeonsCoins, double speed, float damageResistance, float attackDamage) {
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

    @Override
    public void attack(@NotNull Entity target, boolean swingHand) {
        super.attack(target, swingHand);

        if(target instanceof Player player && getInstance() != null) {
            int playerDamageResistance = player.getTag(Armors.DAMAGE_RESISTANCE_PERCENTAGE_TAG);
            player.damage(DamageType.fromEntity(this),
                (attackDamage * getInstance().getConfig().getDamageMultiplier(getInstance().getDifficulty())) * (1 - (playerDamageResistance * 0.01F)));
        }
    }

    @Override
    public GameInstance getInstance() {
        return ((GameInstance) super.getInstance());
    }

    public void setInstance(@NotNull GameInstance instance, @NotNull Pos spawnPosition) {
        float newHealth = getMaxHealth() + instance.getConfig().getHealthMultiplier(instance.getDifficulty());

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

    public float getDamageResistance() {
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
