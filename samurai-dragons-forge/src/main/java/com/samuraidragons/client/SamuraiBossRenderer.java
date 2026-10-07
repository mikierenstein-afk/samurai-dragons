package com.samuraidragons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.samuraidragons.SamuraiDragons;
import com.samuraidragons.entity.SamuraiBossEntity;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

/** Reuses the vanilla humanoid model with a custom texture and scale. */
public class SamuraiBossRenderer<T extends SamuraiBossEntity> extends MobRenderer<T, ZombieModel<T>> {
    private final ResourceLocation texture;
    private final float scale;

    public SamuraiBossRenderer(EntityRendererProvider.Context ctx, String textureName, float scale) {
        super(ctx, new ZombieModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f * scale);
        this.texture = new ResourceLocation(SamuraiDragons.MOD_ID, "textures/entity/" + textureName + ".png");
        this.scale = scale;
        this.addLayer(new ItemInHandLayer<>(this, ctx.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) { return texture; }

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(scale, scale, scale);
    }
}
