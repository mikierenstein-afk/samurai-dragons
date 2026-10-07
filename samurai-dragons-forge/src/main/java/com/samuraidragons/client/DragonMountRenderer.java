package com.samuraidragons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.samuraidragons.SamuraiDragons;
import com.samuraidragons.entity.DragonMountEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DragonMountRenderer extends MobRenderer<DragonMountEntity, DragonModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SamuraiDragons.MOD_ID, "textures/entity/dragon_mount.png");
    private static final float SCALE = 1.8f;

    public DragonMountRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DragonModel(ctx.bakeLayer(DragonModel.LAYER)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(DragonMountEntity entity) { return TEXTURE; }

    @Override
    protected void scale(DragonMountEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.translate(0.0f, 0.4f, 0.0f);
    }

    @Override
    protected void setupRotations(DragonMountEntity entity, PoseStack poseStack, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
    }
}
