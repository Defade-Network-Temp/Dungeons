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

public class ZombieIV extends DungeonsEntity {
    public ZombieIV() {
        super(EntityType.ZOMBIE, "Lvl 4", 36, 1, 3 * 20, 13, 0.15, 7, 5);

        setHelmet(ItemList.INJURED_ZOMBIE_HEAD);
        setChestplate(
                ItemStack.builder(Material.LEATHER_CHESTPLATE)
                        .meta(LeatherArmorMeta.class, leatherArmorMeta -> leatherArmorMeta.color(new Color(85, 0, 0)))
                        .build()
        );
        setLeggings(ItemStack.of(Material.CHAINMAIL_LEGGINGS));
        setBoots(ItemStack.of(Material.CHAINMAIL_BOOTS));

        setItemInMainHand(ItemStack.of(Material.WOODEN_AXE));

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new ClassicZombieGoal(this, 2, 25))
                        .addTargetSelector(new PlayerTargetSelector(this))
                        .build()
        );
    }
}
