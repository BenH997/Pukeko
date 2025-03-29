package net.pookie.pukeko.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.custom.PukekoEntity;

public class PukekoModel<T extends PukekoEntity> extends HierarchicalModel<T> {

    private boolean canFly;

    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Pukeko.MODID, "pukeko"), "main");

    private final ModelPart WholePukeko;
    private final ModelPart Foot;
    private final ModelPart LeftToe;
    private final ModelPart RightToe;
    private final ModelPart Toe4hidden;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart TailHidden;
    private final ModelPart Propeller;

	public PukekoModel(ModelPart root) {
            this.WholePukeko = root.getChild("WholePukeko");
            this.Foot = this.WholePukeko.getChild("Foot");
            this.LeftToe = this.Foot.getChild("LeftToe");
            this.RightToe = this.Foot.getChild("RightToe");
            this.Toe4hidden = this.Foot.getChild("Toe4hidden");
            this.Body = this.WholePukeko.getChild("Body");
            this.Head = this.WholePukeko.getChild("Head");
            this.TailHidden = this.WholePukeko.getChild("TailHidden");
            this.Propeller = this.TailHidden.getChild("Propeller");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition WholePukeko = partdefinition.addOrReplaceChild("WholePukeko", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

            PartDefinition Foot = WholePukeko.addOrReplaceChild("Foot", CubeListBuilder.create().texOffs(28, 11).addBox(-7.0F, 7.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(24, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

            PartDefinition LeftToe = Foot.addOrReplaceChild("LeftToe", CubeListBuilder.create().texOffs(4, 27).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, -1.0F));

            PartDefinition RightToe = Foot.addOrReplaceChild("RightToe", CubeListBuilder.create().texOffs(4, 15).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, -7.0F));

            PartDefinition Toe4hidden = Foot.addOrReplaceChild("Toe4hidden", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));

            PartDefinition Toe4hidden_r1 = Toe4hidden.addOrReplaceChild("Toe4hidden_r1", CubeListBuilder.create().texOffs(28, 11).addBox(-6.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

            PartDefinition Body = WholePukeko.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(6, 2).addBox(-5.0F, -4.0F, -2.0F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

            PartDefinition Head = WholePukeko.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -5.0F));

            PartDefinition Nose_r1 = Head.addOrReplaceChild("Nose_r1", CubeListBuilder.create().texOffs(24, 31).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -3.0F, 0.0F, -1.5708F, 0.0F));

            PartDefinition Head_r1 = Head.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(24, 27).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

            PartDefinition TailHidden = WholePukeko.addOrReplaceChild("TailHidden", CubeListBuilder.create().texOffs(9, 5).addBox(-5.0F, -2.0F, 0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -10.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

            PartDefinition Propeller = TailHidden.addOrReplaceChild("Propeller", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, -1.0F, 1.5F, 0.0F, 0.0F, 0.7854F));

            PartDefinition cube_r1 = Propeller.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 14).addBox(-0.1F, -1.5F, -0.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F, 3.1416F));

            PartDefinition cube_r2 = Propeller.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 14).addBox(1.4F, -3.5F, 0.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.0F, 1.5F, 0.0F, 1.5708F, 0.0F));

            return LayerDefinition.create(meshdefinition, 64, 64);

    }

    @Override
    public void setupAnim(PukekoEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.canFly = entity.isFlyingEnabled();

        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        if (!canFly) {
            this.animateWalk(PukekoAnimations.ANIM_PUKEKO_WALK, limbSwing, limbSwingAmount, 1f, 2.5f);
        }

        this.animate(entity.transformAnimationState, PukekoAnimations.ANIM_PUKEKO_TRANSFORM, ageInTicks, 1f);
        this.animate(entity.flyingAnimationState, PukekoAnimations.ANIM_PUKEKO_FLYING, ageInTicks, 1f);

    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.Head.yRot = headYaw * ((float)Math.PI / 180f);
        this.Head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        WholePukeko.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return WholePukeko;
    }
}
