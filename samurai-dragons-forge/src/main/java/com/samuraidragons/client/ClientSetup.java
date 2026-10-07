package com.samuraidragons.client;

import com.samuraidragons.ModEntities;
import com.samuraidragons.SamuraiDragons;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SamuraiDragons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ONI_SHOGUN.get(),
                ctx -> new SamuraiBossRenderer<>(ctx, "oni_shogun", 1.7f));
        event.registerEntityRenderer(ModEntities.SHADOW_RONIN.get(),
                ctx -> new SamuraiBossRenderer<>(ctx, "shadow_ronin", 1.0f));
        event.registerEntityRenderer(ModEntities.DRAGON.get(), DragonMountRenderer::new);
    }
}
