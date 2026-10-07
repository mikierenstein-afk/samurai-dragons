package com.samuraidragons.item;

import com.samuraidragons.ModEntities;
import com.samuraidragons.entity.DragonMountEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Right click: a tamed dragon appears in front of you, ready to ride. */
public class DragonWhistleItem extends Item {
    public DragonWhistleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel sl) {
            DragonMountEntity dragon = ModEntities.DRAGON.get().create(sl);
            if (dragon == null) return InteractionResultHolder.fail(stack);

            Vec3 pos = player.position().add(player.getLookAngle().multiply(4, 0, 4));
            dragon.moveTo(pos.x, player.getY(), pos.z, player.getYRot() + 180.0f, 0.0f);
            dragon.setOwner(player.getUUID());
            dragon.setPersistenceRequired();
            sl.addFreshEntity(dragon);
            sl.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL,
                    SoundSource.PLAYERS, 1.0f, 1.0f);
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.samuraidragons.dragon_whistle.tooltip").withStyle(ChatFormatting.GOLD));
    }
}
