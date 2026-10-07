package com.samuraidragons;

import com.samuraidragons.item.BossSummonItem;
import com.samuraidragons.item.DragonKatanaItem;
import com.samuraidragons.item.DragonTier;
import com.samuraidragons.item.DragonWhistleItem;
import com.samuraidragons.item.SetBonusArmorItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SamuraiDragons.MOD_ID);

    // materials
    public static final RegistryObject<Item> DRAGON_SCALE = ITEMS.register("dragon_scale",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRAGON_LEATHER = ITEMS.register("dragon_leather",
            () -> new Item(new Item.Properties().fireResistant()));

    // weapon + summon items
    public static final RegistryObject<Item> DRAGON_KATANA = ITEMS.register("dragon_katana",
            () -> new DragonKatanaItem(DragonTier.INSTANCE, 3, -2.0f, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> ONI_MASK = ITEMS.register("oni_mask",
            () -> new BossSummonItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> RONIN_SCROLL = ITEMS.register("ronin_scroll",
            () -> new BossSummonItem(new Item.Properties().stacksTo(1), false));
    public static final RegistryObject<Item> DRAGON_WHISTLE = ITEMS.register("dragon_whistle",
            () -> new DragonWhistleItem(new Item.Properties().stacksTo(16)));

    // spawn eggs
    public static final RegistryObject<Item> ONI_SHOGUN_SPAWN_EGG = ITEMS.register("oni_shogun_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ONI_SHOGUN, 0x8B0000, 0x111111, new Item.Properties()));
    public static final RegistryObject<Item> SHADOW_RONIN_SPAWN_EGG = ITEMS.register("shadow_ronin_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SHADOW_RONIN, 0x2A2A44, 0x9B30FF, new Item.Properties()));
    public static final RegistryObject<Item> DRAGON_SPAWN_EGG = ITEMS.register("dragon_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DRAGON, 0x1E6E55, 0xE0A020, new Item.Properties()));

    // samurai armor (full set: Speed)
    public static final RegistryObject<Item> SAMURAI_HELMET = armor("samurai_helmet", ModArmorMaterials.SAMURAI,
            ArmorItem.Type.HELMET, () -> MobEffects.MOVEMENT_SPEED, "item.samuraidragons.samurai_set_bonus", false);
    public static final RegistryObject<Item> SAMURAI_CHESTPLATE = armor("samurai_chestplate", ModArmorMaterials.SAMURAI,
            ArmorItem.Type.CHESTPLATE, () -> MobEffects.MOVEMENT_SPEED, "item.samuraidragons.samurai_set_bonus", false);
    public static final RegistryObject<Item> SAMURAI_LEGGINGS = armor("samurai_leggings", ModArmorMaterials.SAMURAI,
            ArmorItem.Type.LEGGINGS, () -> MobEffects.MOVEMENT_SPEED, "item.samuraidragons.samurai_set_bonus", false);
    public static final RegistryObject<Item> SAMURAI_BOOTS = armor("samurai_boots", ModArmorMaterials.SAMURAI,
            ArmorItem.Type.BOOTS, () -> MobEffects.MOVEMENT_SPEED, "item.samuraidragons.samurai_set_bonus", false);

    // dragon hide armor (full set: Fire Resistance)
    public static final RegistryObject<Item> DRAGON_HIDE_HELMET = armor("dragon_hide_helmet", ModArmorMaterials.DRAGON_HIDE,
            ArmorItem.Type.HELMET, () -> MobEffects.FIRE_RESISTANCE, "item.samuraidragons.dragon_hide_set_bonus", true);
    public static final RegistryObject<Item> DRAGON_HIDE_CHESTPLATE = armor("dragon_hide_chestplate", ModArmorMaterials.DRAGON_HIDE,
            ArmorItem.Type.CHESTPLATE, () -> MobEffects.FIRE_RESISTANCE, "item.samuraidragons.dragon_hide_set_bonus", true);
    public static final RegistryObject<Item> DRAGON_HIDE_LEGGINGS = armor("dragon_hide_leggings", ModArmorMaterials.DRAGON_HIDE,
            ArmorItem.Type.LEGGINGS, () -> MobEffects.FIRE_RESISTANCE, "item.samuraidragons.dragon_hide_set_bonus", true);
    public static final RegistryObject<Item> DRAGON_HIDE_BOOTS = armor("dragon_hide_boots", ModArmorMaterials.DRAGON_HIDE,
            ArmorItem.Type.BOOTS, () -> MobEffects.FIRE_RESISTANCE, "item.samuraidragons.dragon_hide_set_bonus", true);

    private static RegistryObject<Item> armor(String name, ArmorMaterial material, ArmorItem.Type type,
                                              Supplier<MobEffect> effect, String bonusKey, boolean fireproof) {
        return ITEMS.register(name, () -> {
            Item.Properties props = new Item.Properties();
            if (fireproof) props = props.fireResistant();
            return new SetBonusArmorItem(material, type, props, effect, bonusKey);
        });
    }

    public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(DRAGON_KATANA.get());
            event.accept(ONI_MASK.get());
            event.accept(RONIN_SCROLL.get());
            event.accept(DRAGON_WHISTLE.get());
            event.accept(SAMURAI_HELMET.get());
            event.accept(SAMURAI_CHESTPLATE.get());
            event.accept(SAMURAI_LEGGINGS.get());
            event.accept(SAMURAI_BOOTS.get());
            event.accept(DRAGON_HIDE_HELMET.get());
            event.accept(DRAGON_HIDE_CHESTPLATE.get());
            event.accept(DRAGON_HIDE_LEGGINGS.get());
            event.accept(DRAGON_HIDE_BOOTS.get());
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(DRAGON_SCALE.get());
            event.accept(DRAGON_LEATHER.get());
        } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ONI_SHOGUN_SPAWN_EGG.get());
            event.accept(SHADOW_RONIN_SPAWN_EGG.get());
            event.accept(DRAGON_SPAWN_EGG.get());
        }
    }
}
