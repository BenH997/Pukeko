package net.pookie.pukeko;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.pookie.pukeko.entity.ModEntities;
import net.pookie.pukeko.entity.client.french.FrenchRenderer;
import net.pookie.pukeko.entity.client.kiwi.KiwiRenderer;
import net.pookie.pukeko.entity.client.pukeko.PukekoRenderer;
import net.pookie.pukeko.sounds.ModSounds;
import net.pookie.pukeko.items.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// Build: ./gradlew build

// TODO

// * Fix kiwi spawning in water
// * Fix global kiwi roar
// * Make tameable
    // * Eats Kiwis
    // * pukekos spawn at pukeko temples
    // * Drops kiwi (hard to tell if kiwi (bird) or fruit)
// * Pukeko morphing 😈
// * Make crab
// * Give pukeko armor
// * Add Drops
// * Finish french villager

@Mod(Pukeko.MODID)
public class Pukeko {
    public static final String MODID = "pukekomod";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Pukeko(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        /* REGISTRY */

        ModSounds.register(modEventBus); // Register sounds
        ModItems.register(modEventBus); // Register items
        ModEntities.register(modEventBus); // Register entities

        /* REGISTRY */

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.SPAWN_EGGS)) {
            event.accept(ModItems.PUKEKO_SPAWN_EGG);
            event.accept(ModItems.FRENCH_SPAWN_EGG);
            event.accept(ModItems.KIWI_SPAWN_EGG);
        }

        if (event.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            event.accept(ModItems.KIWI);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.PUKEKO.get(), PukekoRenderer::new);
            EntityRenderers.register(ModEntities.FRENCH.get(), FrenchRenderer::new);
            EntityRenderers.register(ModEntities.KIWI.get(), KiwiRenderer::new);
        }
    }
}