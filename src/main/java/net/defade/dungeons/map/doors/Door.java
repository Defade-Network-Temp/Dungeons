package net.defade.dungeons.map.doors;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import java.util.List;

public class Door {
    private final Instance instance;

    private final Pos minCorner;
    private final Pos maxCorner;
    private final List<Entity> armorStands;
    private final List<Entity> defaultArmorStands;
    private final List<Entity> nextArmorStands;
    private final int price;
    private final Pos antennaPos;
    private final Pos enchantTablePos;
    private boolean isOpened;

    public Door(Instance instance, Pos minCorner, Pos maxCorner, List<Entity> armorStands, List<Entity> defaultArmorStands,
                List<Entity> nextArmorStands, int price, Pos antennaPos, Pos enchantTablePos) {
        this.instance = instance;
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
        this.armorStands = armorStands;
        this.defaultArmorStands = defaultArmorStands;
        this.nextArmorStands = nextArmorStands;
        this.price = price;
        this.antennaPos = antennaPos;
        this.enchantTablePos = enchantTablePos;
    }

    public void init() {
        for (Entity armorStand : armorStands) {
            armorStand.setInstance(instance);
            if(getDefaultArmorStands().contains(armorStand)) {
                armorStand.setCustomNameVisible(true);
            }
        }
    }

    public Pos getMinCorner() {
        return minCorner;
    }

    public Pos getMaxCorner() {
        return maxCorner;
    }

    public List<Entity> getArmorStands() {
        return armorStands;
    }

    public List<Entity> getDefaultArmorStands() {
        return defaultArmorStands;
    }

    public int getPrice() {
        return price;
    }

    public List<Entity> getNextArmorStands() {
        return nextArmorStands;
    }

    public boolean hasAntenna() {
        return antennaPos != null;
    }

    public boolean hasEnchantTable() {
        return enchantTablePos != null;
    }

    public Pos getAntennaPos() {
        return antennaPos;
    }

    public Pos getEnchantTablePos() {
        return enchantTablePos;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void open() {
        isOpened = true;

        for(int x = getMinCorner().blockX(); x <= getMaxCorner().blockX(); x++) {
            for(int y = getMinCorner().blockY(); y <= getMaxCorner().blockY(); y++) {
                for(int z = getMinCorner().blockZ(); z <= getMaxCorner().blockZ(); z++) {
                    instance.setBlock(x, y, z, Block.AIR);
                }
            }
        }

        getArmorStands().forEach(Entity::remove);
        getNextArmorStands().forEach(armorStand -> armorStand.setCustomNameVisible(true));
    }
}
