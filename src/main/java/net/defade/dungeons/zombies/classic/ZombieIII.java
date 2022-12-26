package net.defade.dungeons.zombies.classic;

import net.defade.dungeons.utils.ItemList;
import net.defade.dungeons.zombies.DungeonsEntity;
import net.defade.dungeons.zombies.ai.ClassicZombieGoal;
import net.defade.dungeons.zombies.ai.PlayerTargetSelector;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ZombieIII extends DungeonsEntity {
    public ZombieIII() {
        super(EntityType.ZOMBIE, "Lvl 3", 28, 1, 4 * 20, 10, 0.15, 5, 4);

        setHelmet(ItemList.LESS_INJURED_ZOMBIE_HEAD);
        setChestplate(ItemStack.of(Material.LEATHER_CHESTPLATE));
        setLeggings(ItemStack.of(Material.CHAINMAIL_LEGGINGS));
        setBoots(ItemStack.of(Material.LEATHER_BOOTS));

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new ClassicZombieGoal(this, 2, 25))
                        .addTargetSelector(new PlayerTargetSelector(this))
                        .build()
        );
    }
}
