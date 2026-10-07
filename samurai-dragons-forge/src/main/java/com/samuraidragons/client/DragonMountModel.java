package com.samuraidragons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.samuraidragons.SamuraiDragons;
import com.samuraidragons.entity.DragonMountEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** Simple hand-made dragon model: body, neck, head, tail and two flapping wings. */
public class DragonMountModel extends EntityModel<DragonMountEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(SamuraiDragons.MOD_ID, "dragon_mount"), "main");

    private final ModelPart root;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart head;
    private final ModelPart tail;

    public DragonMountModel(ModelPart root) {
        this.root = root;
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition p = mesh.getRoot();
        p.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0f, -2.0f, -8.0f, 6.0f, 5.0f, 16.0f), PartPose.ZERO);
        p.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 22).addBox(-2.5f, -2.5f, -6.0f, 5.0f, 5.0f, 6.0f), PartPose.offset(0.0f, 0.0f, -8.0f));
        p.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(0, 40).addBox(-1.5f, -1.5f, 0.0f, 3.0f, 3.0f, 14.0f), PartPose.offset(0.0f, 0.5f, 8.0f));
        p.addOrReplaceChild("left_wing", CubeListBuilder.create()
                .texOffs(22, 22).addBox(0.0f, -0.5f, -4.0f, 16.0f, 1.0f, 10.0f), PartPose.offset(3.0f, -1.0f, -2.0f));
        p.addOrReplaceChild("right_wing", CubeListBuilder.create()
                .texOffs(22, 22).mirror().addBox(-16.0f, -0.5f, -4.0f, 16.0f, 1.0f, 10.0f), PartPose.offset(-3.0f, -1.0f, -2.0f));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(DragonMountEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float flap = Mth.sin(ageInTicks * 0.3f) * 0.6f;
        this.leftWing.zRot = flap;
        this.rightWing.zRot = -flap;
        this.tail.xRot = Mth.sin(ageInTicks * 0.15f) * 0.1f;
        this.head.xRot = headPitch * ((float) Math.PI / 180f) * 0.5f;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int light, int overlay,
                               float r, float g, float b, float a) {
        this.root.render(poseStack, buffer, light, overlay, r, g, b, a);
    }
}
