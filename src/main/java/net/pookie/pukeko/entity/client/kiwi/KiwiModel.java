package net.pookie.pukeko.entity.client.kiwi;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.client.pukeko.PukekoAnimations;
import net.pookie.pukeko.entity.custom.KiwiEntity;

public class KiwiModel<T extends KiwiEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Pukeko.MODID, "kiwi"), "main");
    private final ModelPart Kiwi;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart Topbeaker;
    private final ModelPart Bottombeaker;
    private final ModelPart LeftFoot;
    private final ModelPart foot;
    private final ModelPart RightFoot;
    private final ModelPart foot2;

    public KiwiModel(ModelPart root) {
        this.Kiwi = root.getChild("Kiwi");
        this.Body = this.Kiwi.getChild("Body");
        this.Head = this.Body.getChild("Head");
        this.Topbeaker = this.Head.getChild("Topbeaker");
        this.Bottombeaker = this.Head.getChild("Bottombeaker");
        this.LeftFoot = this.Kiwi.getChild("LeftFoot");
        this.foot = this.LeftFoot.getChild("foot");
        this.RightFoot = this.Kiwi.getChild("RightFoot");
        this.foot2 = this.RightFoot.getChild("foot2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Kiwi = partdefinition.addOrReplaceChild("Kiwi", CubeListBuilder.create(), PartPose.offset(1.0F, 17.0F, -4.0F));

        PartDefinition Body = Kiwi.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -3.0F, -6.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 2.0F, 5.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(16, 14).addBox(-2.0F, -3.0F, -4.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -2.0F, -5.0F));

        PartDefinition Topbeaker = Head.addOrReplaceChild("Topbeaker", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -1.0F, -4.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition Beak_r1 = Topbeaker.addOrReplaceChild("Beak_r1", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition Bottombeaker = Head.addOrReplaceChild("Bottombeaker", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, -4.0F));

        PartDefinition LeftFoot = Kiwi.addOrReplaceChild("LeftFoot", CubeListBuilder.create().texOffs(20, 22).addBox(0.0F, -0.06F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 22).addBox(0.0F, -2.06F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 5.0F, 4.5F));

        PartDefinition foot = LeftFoot.addOrReplaceChild("foot", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -0.05F, -2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.5F));

        PartDefinition RightFoot = Kiwi.addOrReplaceChild("RightFoot", CubeListBuilder.create().texOffs(16, 22).addBox(0.0F, -0.06F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 22).addBox(0.0F, -2.06F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.0F, 4.5F));

        PartDefinition foot2 = RightFoot.addOrReplaceChild("foot2", CubeListBuilder.create().texOffs(8, 22).addBox(-0.5F, -0.05F, -2.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(KiwiEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (!entity.isRoarAnimationPlaying()) {
            this.applyHeadRotation(netHeadYaw, headPitch);
        }
        this.animateWalk(KiwiAnimations.ANIM_KIWI_WALK, limbSwing, limbSwingAmount, 1f, 2.5f);
        this.animate(entity.idleAnimationState, KiwiAnimations.ANIM_KIWI_DANCE, ageInTicks,1f);
        this.animate(entity.roarAnimationState, KiwiAnimations.ANIM_KIWI_ROAR, ageInTicks, 1.2f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.Head.yRot = headYaw * ((float)Math.PI / 180f);
        this.Head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        Kiwi.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return Kiwi;
    }
}
