package net.defade.dungeons.zombies.classic;

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

public class ZombieVI extends DungeonsEntity {
    public ZombieVI() {
        super(EntityType.ZOMBIE, "Lvl 6", 44, 1, 2 * 20, 20, 0.15, 15, 8);

        setHelmet(ItemList.VERY_BLOODY_HEAD);
        setChestplate(
            ItemStack.builder(Material.LEATHER_CHESTPLATE)
                .meta(LeatherArmorMeta.class, leatherArmorMeta -> {
                    leatherArmorMeta.color(new Color(145, 0, 15));
                }).build()
        );
        setLeggings(ItemStack.of(Material.IRON_LEGGINGS));
        setBoots(ItemStack.of(Material.IRON_BOOTS));

        setItemInMainHand(ItemStack.of(Material.IRON_AXE));

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new ClassicZombieGoal(this, 2, 25))
                        .addTargetSelector(new PlayerTargetSelector(this))
                        .build()
        );
    }
}
