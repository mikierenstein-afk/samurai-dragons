package com.samuraidragons.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** Shared boss logic: boss bar, 3 health phases, persistence. */
public abstract class SamuraiBossEntity extends Monster {
    protected final ServerBossEvent bossBar;
    protected int phase = 0;

    protected SamuraiBossEntity(EntityType<? extends Monster> type, Level level,
                                BossEvent.BossBarColor color, String nameKey, int xp) {
        super(type, level);
        this.bossBar = new ServerBossEvent(Component.translatable(nameKey), color, BossEvent.BossBarOverlay.NOTCHED_10);
        this.xpReward = xp;
        this.setPersistenceRequired();
    }

    protected abstract double meleeSpeed();
    protected abstract void onPhaseChange(int newPhase);
    protected abstract void tickAbilities(ServerLevel level, LivingEntity target);
    protected abstract Component phaseMessage(int phase);

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, meleeSpeed(), false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0f));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        float ratio = getHealth() / getMaxHealth();
        bossBar.setProgress(ratio);

        int newPhase = ratio < 0.33f ? 3 : ratio < 0.66f ? 2 : 1;
        if (newPhase > phase) {
            phase = newPhase;
            if (level() instanceof ServerLevel sl && phase > 1) {
                playSound(SoundEvents.ENDER_DRAGON_GROWL, 3.0f, 0.8f);
                Component msg = phaseMessage(phase);
                for (ServerPlayer p : sl.getPlayers(pl -> pl.distanceToSqr(this) < 4096)) {
                    p.displayClientMessage(msg, true);
                }
            }
            onPhaseChange(phase);
        }
        if (level() instanceof ServerLevel sl) {
            tickAbilities(sl, getTarget());
        }
    }

    protected void setAttr(Attribute attribute, double value) {
        AttributeInstance inst = getAttribute(attribute);
        if (inst != null) inst.setBaseValue(value);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossBar.removePlayer(player);
    }

    @Override
    public boolean removeWhenFarAway(double distanceSq) { return false; }
}
