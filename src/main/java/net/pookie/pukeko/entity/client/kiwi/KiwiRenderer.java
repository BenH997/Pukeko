package net.pookie.pukeko.entity.client.kiwi;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.client.pukeko.PukekoModel;
import net.pookie.pukeko.entity.custom.KiwiEntity;
import net.pookie.pukeko.entity.custom.PukekoEntity;

public class KiwiRenderer extends MobRenderer<KiwiEntity, KiwiModel<KiwiEntity>> {

    public KiwiRenderer(EntityRendererProvider.Context context) {
        super(context, new KiwiModel<>(context.bakeLayer(KiwiModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(KiwiEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Pukeko.MODID, "textures/entity/kiwi/kiwi.png");
    }

    @Override
    public void render(KiwiEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        if (entity.isBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
