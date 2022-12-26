package net.defade.dungeons.zombies.classic;

import net.defade.dungeons.zombies.DungeonsEntity;
import net.defade.dungeons.zombies.ai.ClassicZombieGoal;
import net.defade.dungeons.zombies.ai.PlayerTargetSelector;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ZombieII extends DungeonsEntity {
    public ZombieII() {
        super(EntityType.ZOMBIE, "Lvl 2", 24, 1, 5 * 20, 7, 0.15, 0, 3);

        setHelmet(ItemStack.builder(Material.LEATHER_HELMET).build());
        setChestplate(ItemStack.builder(Material.LEATHER_CHESTPLATE).build());

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new ClassicZombieGoal(this, 2, 25))
                        .addTargetSelector(new PlayerTargetSelector(this))
                        .build()
        );
    }
}
