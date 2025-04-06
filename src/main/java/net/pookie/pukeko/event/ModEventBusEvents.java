package net.pookie.pukeko.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.ModEntities;
import net.pookie.pukeko.entity.client.french.FrenchModel;
import net.pookie.pukeko.entity.client.kiwi.KiwiModel;
import net.pookie.pukeko.entity.client.pukeko.PukekoModel;
import net.pookie.pukeko.entity.custom.FrenchEntity;
import net.pookie.pukeko.entity.custom.KiwiEntity;
import net.pookie.pukeko.entity.custom.PukekoEntity;

@EventBusSubscriber(modid = Pukeko.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PukekoModel.LAYER_LOCATION, PukekoModel::createBodyLayer);
        event.registerLayerDefinition(FrenchModel.LAYER_LOCATION, FrenchModel::createBodyModel);
        event.registerLayerDefinition(KiwiModel.LAYER_LOCATION, KiwiModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.PUKEKO.get(), PukekoEntity.createAttributes().build());
        event.put(ModEntities.FRENCH.get(), FrenchEntity.createAttributes().build());
        event.put(ModEntities.KIWI.get(), KiwiEntity.createAttributes().build());
    }
}
