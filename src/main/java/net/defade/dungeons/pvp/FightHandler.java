package net.defade.dungeons.pvp;

import net.defade.dungeons.game.GameInstance;
import net.defade.dungeons.shop.swords.Sword;
import net.defade.dungeons.utils.GameEvents;
import net.defade.dungeons.zombies.DungeonsEntity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeModifier;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerDeathEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerStartSprintingEvent;
import net.minestom.server.event.player.PlayerStopSprintingEvent;
import net.minestom.server.gamedata.tags.Tag;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.EntityAnimationPacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FightHandler {
    private static final UUID SWORD_MOVEMENT_SPEED_MODIFIER_UUID = UUID.fromString("7AB1E3FF-A61C-46AF-80F1-77A8B649F9E6");
    private static final AttributeModifier MOJANG_SPRINTING_MODIFIER = new AttributeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"),
            "Sprinting speed boost", 0.3F, AttributeOperation.MULTIPLY_TOTAL);

    private final GameInstance gameInstance;
    private final GameEvents events;

    private final Map<Player, Integer> playersAttackStrengthTicker = new HashMap<>();
    private final Map<Player, Long> deadPlayers = new HashMap<>();

    private final List<Task> tasks = new ArrayList<>();

    public FightHandler(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.events = new GameEvents(gameInstance, gameInstance.getGameEvents().getGlobalEventNode());
    }

    public void init() {
        registerSprintAttributeFixEvent();
        registerSwordSelectEvent();
        registerAttackStrengthTicker();
        registerAttackEvent();
        registerEntitySoundsEvent();
        registerDeathEvent();
    }

    public void stop() {
        events.unregister();
        tasks.forEach(Task::cancel);
    }

    private void registerSprintAttributeFixEvent() {
        events.getPlayerEventNode().addListener(PlayerStartSprintingEvent.class, event -> {
            event.getPlayer().getAttribute(Attribute.MOVEMENT_SPEED).addModifier(MOJANG_SPRINTING_MODIFIER);
        });

        events.getPlayerEventNode().addListener(PlayerStopSprintingEvent.class, event -> {
            event.getPlayer().getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(MOJANG_SPRINTING_MODIFIER);
        });
    }

    private void registerSwordSelectEvent() {
        gameInstance.getPlayers().forEach(player -> refreshSwordAttributesForPlayer(player, player.getInventory().getItemStack(player.getHeldSlot())));

        events.getPlayerEventNode().addListener(PlayerChangeHeldSlotEvent.class, event -> {
            refreshSwordAttributesForPlayer(event.getPlayer(), event.getPlayer().getInventory().getItemStack(event.getSlot()));
        });
    }

    private void refreshSwordAttributesForPlayer(Player player, ItemStack sword) {
        if (sword.hasTag(Sword.MOVEMENT_SPEED_MODIFIER_TAG)) {
            player.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(new AttributeModifier(
                    SWORD_MOVEMENT_SPEED_MODIFIER_UUID,
                    "Sword movement speed",
                    sword.getTag(Sword.MOVEMENT_SPEED_MODIFIER_TAG) / 100,
                    AttributeOperation.MULTIPLY_BASE
            ));
        } else {
            player.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(SWORD_MOVEMENT_SPEED_MODIFIER_UUID);
        }

        if (sword.hasTag(Sword.ATTACK_SPEED_TAG)) {
            player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(sword.getTag(Sword.ATTACK_SPEED_TAG));
        } else {
            player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4.0f);
        }

        if(sword.hasTag(Sword.ATTACK_DAMAGE_TAG)) {
            player.setTag(Sword.ATTACK_DAMAGE_TAG, sword.getTag(Sword.ATTACK_DAMAGE_TAG));
        }
    }

    private void registerAttackStrengthTicker() {
        gameInstance.getPlayers().forEach(player -> playersAttackStrengthTicker.put(player, 0));

        events.getPlayerEventNode().addListener(PlayerSpawnEvent.class, event -> {
            playersAttackStrengthTicker.put(event.getPlayer(), 0);
        });

        events.getPlayerEventNode().addListener(PlayerDisconnectEvent.class, event -> {
            playersAttackStrengthTicker.remove(event.getPlayer());
        });

        tasks.add(gameInstance.scheduler().scheduleTask(() -> {
            playersAttackStrengthTicker.replaceAll((player, attackStrength) -> attackStrength + 1);
        }, TaskSchedule.immediate(), TaskSchedule.tick(1)));
    }

    private float getAttackStrengthScale(Player player) {
        return Math.min(Math.max(((float) playersAttackStrengthTicker.get(player) + (float) 0.5) / (1.0F / player.getAttributeValue(Attribute.ATTACK_SPEED) * 20.0F), 0.0F), 1.0F);
    }

    private void resetAttackStrengthTicker(Player player) {
        playersAttackStrengthTicker.put(player, 0);
    }

    private void registerAttackEvent() {
        events.getEntityEventNode().addListener(EntityAttackEvent.class, event -> {
            if (!(event.getTarget() instanceof DungeonsEntity target)) return;

            if (event.getEntity() instanceof Player attacker) {
                if (attacker.getItemInMainHand().hasTag(Sword.ATTACK_DAMAGE_TAG)) {
                    float attackDamage = attacker.getTag(Sword.ATTACK_DAMAGE_TAG) * (1 - target.getDamageResistance() * 0.01F);

                    float attackStrength = this.getAttackStrengthScale(attacker);
                    resetAttackStrengthTicker(attacker);
                    if (attackDamage > 0.0F) {
                        boolean hasEnoughStrength = attackStrength > 0.9F;
                        boolean hasEnoughStrengthAndSprinting = false;
                        if (attacker.isSprinting() && hasEnoughStrength) {
                            gameInstance.playSound(Sound.sound(Key.key("entity.player.attack.knockback"), Sound.Source.PLAYER, 1f, 1f), attacker.getPosition());
                            hasEnoughStrengthAndSprinting = true;
                        }

                        boolean critical = hasEnoughStrength && attacker.getGravityTickCount() > 0.0F && !attacker.isOnGround() && !isPlayerOnClimbable(attacker) && !isPlayerInWater(attacker) && !attacker.isSprinting();
                        boolean sweeping = hasEnoughStrength && !critical && attacker.isOnGround() && !attacker.isSprinting();

                        double xDiff = attacker.getPosition().x() - target.getPosition().x();

                        double zKnockback;
                        for(zKnockback = attacker.getPosition().z() - target.getPosition().z(); xDiff * xDiff + zKnockback * zKnockback < 0.0001; zKnockback = (Math.random() - Math.random()) * 0.01D) {
                            xDiff = (Math.random() - Math.random()) * 0.01D;
                        }

                        target.takeKnockback(0.4F, xDiff, zKnockback);
                        if (hasEnoughStrengthAndSprinting) {
                            target.takeKnockback(
                                    0.5f,
                                    Math.sin(attacker.getPosition().yaw() * ((float) Math.PI / 180F)),
                                    -Math.cos(attacker.getPosition().yaw() * ((float) Math.PI / 180F))
                            );

                            attacker.setVelocity(attacker.getVelocity().mul(0.6D, 1.0D, 0.6D));

                            attacker.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(MOJANG_SPRINTING_MODIFIER);
                            attacker.setSprinting(false);
                        }

                        if (sweeping) {
                            target.damage(DamageType.fromPlayer(attacker), attackDamage * 0.5F);

                            double xRot = Math.sin(attacker.getPosition().yaw() * ((float) Math.PI / 180F));
                            double zRot = Math.cos(attacker.getPosition().yaw() * ((float) Math.PI / 180F));

                            int sweepHits = 0;
                            for (Entity entity : gameInstance.getNearbyEntities(target.getPosition(), target.getBoundingBox().maxX() + 1)) {
                                if (!(entity instanceof LivingEntity livingEntity)) return;
                                if (livingEntity instanceof DungeonsEntity && livingEntity != target && attacker.getPosition().distanceSquared(livingEntity.getPosition()) < 9.0D) {
                                    sweepHits++;
                                    livingEntity.takeKnockback(0.5f, xRot, -zRot);
                                    livingEntity.damage(DamageType.fromPlayer(attacker), attackDamage);
                                }

                                if(sweepHits == 5) break;
                            }

                            gameInstance.playSound(Sound.sound(Key.key("entity.player.attack.sweep"), Sound.Source.PLAYER, 1.0F, 1.0F), attacker.getPosition());

                            Pos playerPos = attacker.getPosition();
                            gameInstance.sendGroupedPacket(ParticleCreator.createParticlePacket(
                                    Particle.SWEEP_ATTACK,
                                    false,
                                    playerPos.x() - xRot,
                                    playerPos.y() + attacker.getBoundingBox().height() * 0.5,
                                    playerPos.z() + zRot,
                                    (float) -xRot,
                                    0f,
                                    (float) zRot,
                                    0f,
                                    0,
                                    null
                            ));
                        } else if (critical) {
                            target.damage(DamageType.fromPlayer(attacker), attackDamage * 1.5F);
                            gameInstance.playSound(Sound.sound(Key.key("entity.player.attack.crit"), Sound.Source.PLAYER, 1.0F, 1.0F), attacker.getPosition());
                            attacker.sendPacketToViewers(new EntityAnimationPacket(target.getEntityId(), EntityAnimationPacket.Animation.CRITICAL_EFFECT));
                        } else {
                            target.damage(DamageType.fromPlayer(attacker), attackDamage);
                        }

                        if (!critical && !sweeping) {
                            if (hasEnoughStrength) {
                                gameInstance.playSound(Sound.sound(Key.key("entity.player.attack.strong"), Sound.Source.PLAYER, 1.0F, 1.0F), attacker.getPosition());
                            } else {
                                gameInstance.playSound(Sound.sound(Key.key("entity.player.attack.weak"), Sound.Source.PLAYER, 1.0F, 1.0F), attacker.getPosition());
                            }
                        }

                        float targetHealth = target.getHealth();
                        if (targetHealth > 2.0F) {
                            int particlesAmount = (int) (targetHealth * 0.5);
                            gameInstance.sendGroupedPacket(ParticleCreator.createParticlePacket(
                                    Particle.DAMAGE_INDICATOR,
                                    false,
                                    target.getPosition().x(),
                                    target.getPosition().y() + target.getBoundingBox().height() * 0.5,
                                    target.getPosition().z(),
                                    0.1f,
                                    0f,
                                    0.1f,
                                    0.2f,
                                    particlesAmount,
                                    null
                            ));
                        } else if(targetHealth <= 0) {
                            gameInstance.getCoinsManager().addCoins(attacker, target.getDungeonsCoins());
                            // TODO send message to player
                        }

                        // TODO attacker.causeFoodExhaustion(0.1F);
                    }
                }
            }
        });

    }

    private void registerEntitySoundsEvent() {
        events.getEntityEventNode().addListener(EntityDamageEvent.class, event -> {
            if(event.getEntity() instanceof DungeonsEntity dungeonsEntity) {
                event.setSound(null); // Minestom doesn't allow us to change the pitch of the sound
                if(dungeonsEntity.getHealth() - event.getDamage() > 0) {
                    gameInstance.playSound(dungeonsEntity.getHurtSound(), dungeonsEntity);
                }
            }
        });

        events.getEntityEventNode().addListener(EntityDeathEvent.class, event -> {
            if(event.getEntity() instanceof DungeonsEntity dungeonsEntity) {
                gameInstance.playSound(dungeonsEntity.getDeathSound(), dungeonsEntity);
            }
        });
    }

    private void registerDeathEvent() {
        gameInstance.getPlayers().forEach(player -> player.setEnableRespawnScreen(false));

        tasks.add(gameInstance.scheduler().scheduleTask(() -> {
            long currentTime = System.currentTimeMillis();

            for (Map.Entry<Player, Long> deadPlayerEntry : deadPlayers.entrySet()) {
                Player player = deadPlayerEntry.getKey();
                long deathTime = deadPlayerEntry.getValue();

                Pos nearestPlayerPos = player.getPosition();
                double nearestDistance = Double.MAX_VALUE;

                for (Player playingPlayers : gameInstance.getPlayers()) {
                    if (playingPlayers == player || deadPlayers.containsKey(playingPlayers)) continue;

                    double distance = player.getPosition().distanceSquared(playingPlayers.getPosition());
                    if (distance < nearestDistance) {
                        if (distance < 25 * 25) {
                            nearestPlayerPos = null;
                            break;
                        }
                        nearestDistance = distance;
                        nearestPlayerPos = playingPlayers.getPosition();
                    }
                }

                if (nearestPlayerPos != null) {
                    player.teleport(nearestPlayerPos);
                    player.sendMessage(Component.text("Ne vous éloignez pas trop de vos équipiers!", NamedTextColor.RED));
                }

                int timeLeft = switch (gameInstance.getDifficulty()) {
                    case NORMAL -> 40 * 1000;
                    case HARD -> 50 * 1000;
                    case INSANE -> Integer.MAX_VALUE; // No respawn for you!
                };

                if(currentTime - deathTime > timeLeft) {
                    player.teleport(gameInstance.getConfig().getSpawnPoint());
                    player.setHealth(player.getMaxHealth());

                    deadPlayers.remove(player);
                }
            }
        }, TaskSchedule.immediate(), TaskSchedule.tick(10)));

        events.getPlayerEventNode().addListener(PlayerDeathEvent.class, event -> {
            Player player = event.getPlayer();
            deadPlayers.put(player, System.currentTimeMillis());

            int lostCoins = (int) (gameInstance.getCoinsManager().getCoins(player) * switch (gameInstance.getDifficulty()) {
                case NORMAL -> 0.10;
                case HARD -> 0.25;
                case INSANE -> 0.50;
            });
            event.setChatMessage(player.getName().append(Component.text(" est mort !").color(NamedTextColor.RED))); // TODO do text formatting
            player.sendMessage(Component.text("(-" + lostCoins + " coins)."));
            gameInstance.getCoinsManager().removeCoins(player, lostCoins);

            player.setGameMode(GameMode.SPECTATOR);

            if(deadPlayers.keySet().containsAll(gameInstance.getPlayers())) {
                gameInstance.finishGame();
            }
        });
    }

    private static boolean isPlayerOnClimbable(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return false;
        } else if(player.getInstance() != null) {
            Pos blockPosition = player.getPosition();
            Block playerBlock = player.getInstance().getBlock(blockPosition);

            if (isBlockInTag(playerBlock, "minecraft:climbable")) {
                return true;
            } else if (isBlockInTag(playerBlock, "minecraft:trapdoor")) {
                Block blockBelow = player.getInstance().getBlock(blockPosition.add(0, -1, 0));
                return blockBelow.id() == Block.LADDER.id() && blockBelow.getProperty("facing").equals(playerBlock.getProperty("facing"));
            } else {
                return false;
            }
        }

        return false;
    }

    private static boolean isPlayerInWater(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return false;
        } else if(player.getInstance() != null){
            Block playerBlock = player.getInstance().getBlock(player.getPosition());
            return playerBlock.isLiquid() && playerBlock.id() == Block.WATER.id();
        }

        return false;
    }

    private static boolean isBlockInTag(Block block, String tagNamespace) {
        Tag blocksForTag = MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, tagNamespace);
        return blocksForTag != null && blocksForTag.contains(block.namespace());
    }
}
