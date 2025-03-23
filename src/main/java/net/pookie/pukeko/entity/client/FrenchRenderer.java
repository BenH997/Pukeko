package net.pookie.pukeko.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.custom.FrenchEntity;

public class FrenchRenderer extends MobRenderer<FrenchEntity, FrenchModel<FrenchEntity>> {

    public FrenchRenderer(EntityRendererProvider.Context context) {
        super(context, new FrenchModel<>(context.bakeLayer(FrenchModel.LAYER_LOCATION)), 0.5f);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(FrenchEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Pukeko.MODID, "textures/entity/french/french.png");
    }

    @Override
    public void render(FrenchEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
