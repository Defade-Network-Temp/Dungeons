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

public class ZombieVIII extends DungeonsEntity {
    private static final Color PURPLE_COLOR = new Color(138, 43, 226);

    public ZombieVIII() {
        super(EntityType.ZOMBIE, "Lvl 8", 60, 1, 2 * 20, 20, 0.15, 20, 13);

        setHelmet(ItemList.CORRUPTED_HEAD);
        setChestplate(
                ItemStack.builder(Material.LEATHER_CHESTPLATE)
                        .meta(LeatherArmorMeta.class, leatherArmorMeta -> {
                            leatherArmorMeta.color(PURPLE_COLOR);
                        }).build()
        );
        setLeggings(
                ItemStack.builder(Material.LEATHER_LEGGINGS)
                        .meta(LeatherArmorMeta.class, leatherArmorMeta -> {
                            leatherArmorMeta.color(PURPLE_COLOR);
                        }).build()
        );
        setBoots(
                ItemStack.builder(Material.LEATHER_BOOTS)
                        .meta(LeatherArmorMeta.class, leatherArmorMeta -> {
                            leatherArmorMeta.color(PURPLE_COLOR);
                        }).build()
        );

        setItemInMainHand(ItemStack.of(Material.STONE_HOE));

        addAIGroup(
                new EntityAIGroupBuilder()
                        .addGoalSelector(new ClassicZombieGoal(this, 2, 25))
                        .addTargetSelector(new PlayerTargetSelector(this))
                        .build()
        );
    }
}
