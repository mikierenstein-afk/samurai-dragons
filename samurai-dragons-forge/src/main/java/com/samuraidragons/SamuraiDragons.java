package com.samuraidragons;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SamuraiDragons.MOD_ID)
public class SamuraiDragons {
    public static final String MOD_ID = "samuraidragons";

    public SamuraiDragons() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        bus.addListener(ModEntities::registerAttributes);
        bus.addListener(ModItems::addToCreativeTabs);
    }
}
