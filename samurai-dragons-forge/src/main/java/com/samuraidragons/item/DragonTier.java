package com.samuraidragons.item;

import com.samuraidragons.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class DragonTier implements Tier {
    public static final DragonTier INSTANCE = new DragonTier();

    @Override public int getUses() { return 2800; }
    @Override public float getSpeed() { return 9.0f; }
    @Override public float getAttackDamageBonus() { return 5.0f; }   // +3 from item +1 base = 9 damage
    @Override public int getLevel() { return 4; }
    @Override public int getEnchantmentValue() { return 20; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.of(ModItems.DRAGON_SCALE.get()); }
}
