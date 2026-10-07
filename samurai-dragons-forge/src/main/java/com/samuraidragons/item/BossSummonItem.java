package com.samuraidragons.item;

import com.samuraidragons.ModEntities;
import com.samuraidragons.entity.SamuraiBossEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Right click to summon a boss in front of you (consumed unless creative). */
public class BossSummonItem extends Item {
    private final boolean oni;

    public BossSummonItem(Properties properties, boolean oni) {
        super(properties);
        this.oni = oni;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel sl) {
            SamuraiBossEntity boss = oni
                    ? ModEntities.ONI_SHOGUN.get().create(sl)
                    : ModEntities.SHADOW_RONIN.get().create(sl);
            if (boss == null) return InteractionResultHolder.fail(stack);

            Vec3 pos = player.position().add(player.getLookAngle().multiply(5, 0, 5));
            boss.moveTo(pos.x, player.getY(), pos.z, player.getYRot() + 180.0f, 0.0f);
            sl.addFreshEntity(boss);

            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sl);
            if (bolt != null) {
                bolt.moveTo(pos.x, player.getY(), pos.z, 0.0f, 0.0f);
                bolt.setVisualOnly(true);
                sl.addFreshEntity(bolt);
            }
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
