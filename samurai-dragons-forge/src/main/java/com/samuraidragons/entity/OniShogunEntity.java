package com.samuraidragons.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Oni Shogun: huge demon warlord. Ground slam -> fireballs -> lightning rage. */
public class OniShogunEntity extends SamuraiBossEntity {
    private int slamCooldown = 60;

    public OniShogunEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, BossEvent.BossBarColor.RED, "entity.samuraidragons.oni_shogun", 250);
        setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_AXE));
        setDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.MOVEMENT_SPEED, 0.27)
                .add(Attributes.ATTACK_DAMAGE, 14.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override protected double meleeSpeed() { return 1.0; }

    @Override
    protected Component phaseMessage(int phase) {
        return Component.translatable("message.samuraidragons.oni_shogun.phase" + phase);
    }

    @Override
    protected void onPhaseChange(int newPhase) {
        if (newPhase == 2) {
            setAttr(Attributes.MOVEMENT_SPEED, 0.32);
        } else if (newPhase == 3) {
            setAttr(Attributes.MOVEMENT_SPEED, 0.36);
            setAttr(Attributes.ATTACK_DAMAGE, 20.0);
        }
    }

    @Override
    protected void tickAbilities(ServerLevel sl, LivingEntity target) {
        if (slamCooldown > 0) slamCooldown--;
        if (target == null) return;

        if (slamCooldown == 0 && distanceToSqr(target) < 36) {
            groundSlam(sl);
            slamCooldown = phase >= 3 ? 70 : 120;
        }
        if (phase >= 2 && tickCount % 90 == 0) {
            Vec3 d = target.getEyePosition().subtract(getEyePosition()).normalize();
            SmallFireball fb = new SmallFireball(sl, this, d.x, d.y, d.z);
            fb.setPos(getX() + d.x * 2, getEyeY(), getZ() + d.z * 2);
            sl.addFreshEntity(fb);
        }
        if (phase >= 3 && tickCount % 100 == 50) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sl);
            if (bolt != null) {
                bolt.moveTo(target.getX(), target.getY(), target.getZ());
                sl.addFreshEntity(bolt);
            }
        }
    }

    private void groundSlam(ServerLevel sl) {
        playSound(SoundEvents.GENERIC_EXPLODE, 2.0f, 0.7f);
        sl.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + 0.2, getZ(), 15, 3.0, 0.2, 3.0, 0.1);
        for (Player p : sl.getEntitiesOfClass(Player.class, getBoundingBox().inflate(6, 2, 6),
                pl -> pl.isAlive() && !pl.isSpectator())) {
            p.hurt(damageSources().mobAttack(this), 10.0f);
            p.knockback(1.4, getX() - p.getX(), getZ() - p.getZ());
            p.hurtMarked = true;
        }
    }

    @Override public boolean fireImmune() { return true; }
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.RAVAGER_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.RAVAGER_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.RAVAGER_DEATH; }
}
