package net.pookie.pukeko.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.ModEntities;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Pukeko.MODID);

    // Spawn Eggs

    public static final DeferredItem<Item> PUKEKO_SPAWN_EGG = ITEMS.register("pukeko_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PUKEKO, 0x5e5e5e, 0xe08c9c,
                    new Item.Properties()));

    public static final DeferredItem<Item> FRENCH_SPAWN_EGG = ITEMS.register("french_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.FRENCH, 0x000091, 0xE1000F, // Placeholder colors
                    new Item.Properties()));

    public static final DeferredItem<Item> KIWI_SPAWN_EGG = ITEMS.register("kiwi_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.KIWI, 0x513424, 0xffa44a,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}


