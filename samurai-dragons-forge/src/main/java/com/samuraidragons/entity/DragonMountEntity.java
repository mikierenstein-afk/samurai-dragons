package com.samuraidragons.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A dragon you can ride. Right click to mount (the first rider becomes the owner).
 * Controls: W = fly forward, A/D = strafe, look up/down to climb/dive, release W to hover, Shift = dismount.
 * Feed it cooked beef to heal it.
 */
public class DragonMountEntity extends PathfinderMob {
    @Nullable
    private UUID ownerId;

    public DragonMountEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    public void setOwner(@Nullable UUID owner) { this.ownerId = owner; }
    @Nullable public UUID getOwner() { return ownerId; }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (ownerId != null && ownerId.equals(target.getUUID())) return false;
        return super.canAttack(target);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.COOKED_BEEF) && getHealth() < getMaxHealth()) {
            if (!level().isClientSide) {
                heal(20.0f);
                if (!player.getAbilities().instabuild) held.shrink(1);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (isVehicle()) return super.mobInteract(player, hand);

        if (ownerId != null && !ownerId.equals(player.getUUID())) {
            if (!level().isClientSide) {
                player.displayClientMessage(Component.translatable("message.samuraidragons.dragon.not_yours"), true);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        if (!level().isClientSide) {
            ownerId = player.getUUID();
            setTarget(null);
            player.startRiding(this);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    // ---- riding ----

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof Player p ? p : null;
    }

    @Override
    public double getPassengersRidingOffset() {
        return getBbHeight() * 0.8;
    }

    @Override
    public boolean isNoGravity() {
        return super.isNoGravity() || getControllingPassenger() != null;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public void travel(Vec3 travelVector) {
        LivingEntity rider = getControllingPassenger();
        if (isVehicle() && rider != null) {
            setYRot(rider.getYRot());
            yRotO = getYRot();
            setXRot(rider.getXRot() * 0.5f);
            setRot(getYRot(), getXRot());
            yBodyRot = getYRot();
            yHeadRot = yBodyRot;

            float strafe = rider.xxa * 0.5f;
            float forward = rider.zza;
            if (forward <= 0.0f) forward *= 0.25f;

            if (isControlledByLocalInstance()) {
                double vertical = 0.0;
                if (rider.zza > 0.0f) {
                    vertical = -Math.sin(Math.toRadians(rider.getXRot())) * rider.zza;
                }
                float accel = (float) getAttributeValue(Attributes.FLYING_SPEED) * 0.1f;
                moveRelative(accel, new Vec3(strafe, vertical, forward));
                move(MoverType.SELF, getDeltaMovement());
                setDeltaMovement(getDeltaMovement().scale(0.91));
            } else {
                setDeltaMovement(Vec3.ZERO);
            }
        } else {
            super.travel(travelVector);
        }
    }

    // ---- misc ----

    @Override
    public boolean removeWhenFarAway(double distanceSq) { return false; }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (ownerId != null) tag.putUUID("Owner", ownerId);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) ownerId = tag.getUUID("Owner");
    }

    @Override protected SoundEvent getAmbientSound() { return SoundEvents.ENDER_DRAGON_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENDER_DRAGON_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENDER_DRAGON_DEATH; }
}
