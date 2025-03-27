package net.pookie.pukeko.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.custom.PukekoEntity;

public class PukekoRenderer extends MobRenderer<PukekoEntity, PukekoModel<PukekoEntity>> {
    public PukekoRenderer(EntityRendererProvider.Context context) {
        super(context, new PukekoModel<>(context.bakeLayer(PukekoModel.LAYER_LOCATION)), 0.55f);
    }

    @Override
    public ResourceLocation getTextureLocation(PukekoEntity entity) {
        if (!entity.isFlyingEnabled()) {
            return ResourceLocation.fromNamespaceAndPath(Pukeko.MODID, "textures/entity/pukeko/pukeko.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(Pukeko.MODID, "textures/entity/pukekoHeli/pukekoHeli.png");
        }

    }

    @Override
    public void render(PukekoEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isFlyingEnabled()) {
            PukekoModel.render(stack, buffer, packedLight, entity, partialTicks, entityYaw);
        } else {
            PukekoHeliModel.render(stack, buffer, packedLight, entity, partialTicks, entityYaw);
        }

        if (entity.isBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
