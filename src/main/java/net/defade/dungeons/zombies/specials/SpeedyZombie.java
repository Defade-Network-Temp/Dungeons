package net.defade.dungeons.zombies.specials;

import net.defade.dungeons.utils.ItemList;
import net.defade.dungeons.zombies.DungeonsEntity;
import net.defade.dungeons.zombies.ai.ClassicZombieGoal;
import net.defade.dungeons.zombies.ai.PlayerTargetSelector;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.LeatherArmorMeta;

public class SpeedyZombie extends DungeonsEntity {
    private static final Color WHITE_COLOR = new Color(255, 255, 255);

    public SpeedyZombie() {
        super(EntityType.ZOMBIE, "Speed Zombie", 28, 1, 3 * 20, 10, 0.15, 10, 4);

        setHelmet(ItemList.SPEEDY_ZOMBIE_HEAD);
        setChestplate(
                ItemStack.builder(Material.LEATHER_CHESTPLATE)
                        .meta(LeatherArmorMeta.class, leatherArmorMeta -> {
                            leatherArmorMeta.color(WHITE_COLOR);
                        }).build()
        );
        setLeggings(
                ItemStack.builder(Material.LEATHER_LEGGINGS)
                        .meta(LeatherArmorMeta.class, leatherArmorMeta -> {
                            leatherArmorMeta.color(WHITE_COLOR);
                        }).build()
        );
        setBoots(
                ItemStack.builder(Material.LEATHER_BOOTS)
                        .meta(LeatherArmorMeta.class, leatherArmorMeta -> {
                            leatherArmorMeta.color(WHITE_COLOR);
                        }).build()
        );

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new ClassicZombieGoal(this, 2, 25))
                        .addTargetSelector(new PlayerTargetSelector(this))
                        .build()
        );
    }

    @Override
    public void tick(long time) {
        super.tick(time);
        // TODO do end rod particles
    }
}
