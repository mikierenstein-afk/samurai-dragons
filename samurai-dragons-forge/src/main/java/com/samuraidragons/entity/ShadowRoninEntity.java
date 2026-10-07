package com.samuraidragons.entity;

import com.samuraidragons.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Shadow Ronin: fast duelist. Teleports behind you and cuts; later unleashes sweeping slashes. */
public class ShadowRoninEntity extends SamuraiBossEntity {
    private int teleportCooldown = 60;
    private int slashCooldown = 60;

    public ShadowRoninEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, BossEvent.BossBarColor.PURPLE, "entity.samuraidragons.shadow_ronin", 150);
        setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.DRAGON_KATANA.get()));
        setDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.MOVEMENT_SPEED, 0.38)
                .add(Attributes.ATTACK_DAMAGE, 9.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override protected double meleeSpeed() { return 1.25; }

    @Override
    protected Component phaseMessage(int phase) {
        return Component.translatable("message.samuraidragons.shadow_ronin.phase" + phase);
    }

    @Override
    protected void onPhaseChange(int newPhase) {
        if (newPhase == 2) setAttr(Attributes.ATTACK_DAMAGE, 12.0);
        if (newPhase == 3) {
            setAttr(Attributes.ATTACK_DAMAGE, 16.0);
            setAttr(Attributes.MOVEMENT_SPEED, 0.45);
        }
    }

    @Override
    protected void tickAbilities(ServerLevel sl, LivingEntity target) {
        if (teleportCooldown > 0) teleportCooldown--;
        if (slashCooldown > 0) slashCooldown--;
        if (target == null) return;

        double distSq = distanceToSqr(target);
        if (teleportCooldown == 0 && distSq > 25 && distSq < 1600) {
            if (teleportBehind(sl, target)) doHurtTarget(target);
            teleportCooldown = phase >= 3 ? 40 : phase == 2 ? 60 : 100;
        }
        if (phase >= 2 && slashCooldown == 0 && distSq < 16) {
            sweepSlash(sl);
            slashCooldown = phase >= 3 ? 40 : 60;
        }
    }

    private boolean teleportBehind(ServerLevel sl, LivingEntity target) {
        Vec3 look = target.getLookAngle();
        double x = target.getX() - look.x * 1.5;
        double z = target.getZ() - look.z * 1.5;
        AABB moved = getBoundingBox().move(x - getX(), target.getY() - getY(), z - getZ());
        if (!sl.noCollision(this, moved)) return false;

        sl.sendParticles(ParticleTypes.PORTAL, getX(), getY(0.5), getZ(), 30, 0.4, 0.8, 0.4, 0.2);
        moveTo(x, target.getY(), z, getYRot(), getXRot());
        getNavigation().stop();
        sl.sendParticles(ParticleTypes.PORTAL, x, target.getY() + 1, z, 30, 0.4, 0.8, 0.4, 0.2);
        playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0f, 1.2f);
        return true;
    }

    private void sweepSlash(ServerLevel sl) {
        playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2.0f, 0.8f);
        sl.sendParticles(ParticleTypes.SWEEP_ATTACK, getX(), getY(0.6), getZ(), 6, 1.5, 0.2, 1.5, 0.0);
        for (Player p : sl.getEntitiesOfClass(Player.class, getBoundingBox().inflate(4, 1, 4),
                pl -> pl.isAlive() && !pl.isSpectator())) {
            p.hurt(damageSources().mobAttack(this), 8.0f);
            p.knockback(0.9, getX() - p.getX(), getZ() - p.getZ());
            p.hurtMarked = true;
        }
    }

    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WITHER_SKELETON_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.WITHER_SKELETON_DEATH; }
}
