package com.samuraidragons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonKatanaItem extends SwordItem {
    public DragonKatanaItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    /** Every hit sets the target ablaze. */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setSecondsOnFire(6);
        return super.hurtEnemy(stack, target, attacker);
    }

    /** Right click: breathe a dragon fireball (5 second cooldown). */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            Vec3 look = player.getLookAngle();
            DragonFireball fireball = new DragonFireball(level, player, look.x, look.y, look.z);
            fireball.setPos(player.getEyePosition().add(look.scale(1.5)));
            level.addFreshEntity(fireball);
            level.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_SHOOT,
                    SoundSource.PLAYERS, 1.0f, 1.0f);
            stack.hurtAndBreak(4, player, p -> p.broadcastBreakEvent(hand));
        }
        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.samuraidragons.dragon_katana.tooltip1").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.samuraidragons.dragon_katana.tooltip2").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
