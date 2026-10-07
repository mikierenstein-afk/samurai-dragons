package com.samuraidragons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.samuraidragons.SamuraiDragons;
import com.samuraidragons.entity.DragonMountEntity;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Reuses the vanilla phantom model (wings + tail), scaled up, with a dragon texture. */
public class DragonMountRenderer extends MobRenderer<DragonMountEntity, PhantomModel<DragonMountEntity>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SamuraiDragons.MOD_ID, "textures/entity/dragon_mount.png");
    private static final float SCALE = 2.4f;

    public DragonMountRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PhantomModel<>(ctx.bakeLayer(ModelLayers.PHANTOM)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(DragonMountEntity entity) { return TEXTURE; }

    @Override
    protected void scale(DragonMountEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.translate(0.0f, 1.3125f, 0.1875f);
    }

    @Override
    protected void setupRotations(DragonMountEntity entity, PoseStack poseStack, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
    }
}
