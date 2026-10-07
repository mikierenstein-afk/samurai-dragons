package com.samuraidragons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

/** Armor piece that grants a status effect while the full set of the same material is worn. */
public class SetBonusArmorItem extends ArmorItem {
    private final ArmorMaterial setMaterial;
    private final Supplier<MobEffect> effect;
    private final String bonusKey;

    public SetBonusArmorItem(ArmorMaterial material, Type type, Properties properties,
                             Supplier<MobEffect> effect, String bonusKey) {
        super(material, type, properties);
        this.setMaterial = material;
        this.effect = effect;
        this.bonusKey = bonusKey;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (level.isClientSide || getType() != Type.CHESTPLATE) return;
        for (ItemStack piece : player.getArmorSlots()) {
            if (!(piece.getItem() instanceof ArmorItem armor) || armor.getMaterial() != setMaterial) return;
        }
        player.addEffect(new MobEffectInstance(effect.get(), 80, 0, false, false, true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(bonusKey).withStyle(ChatFormatting.GRAY));
    }
}
