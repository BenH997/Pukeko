package net.pookie.pukeko.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.ModEntities;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Pukeko.MODID);

    public static final DeferredItem<Item> PUKEKO_SPAWN_EGG = ITEMS.register("pukeko_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PUKEKO, 0x5e5e5e, 0xe08c9c,
                    new Item.Properties()));

    public static final DeferredItem<Item> FRENCH_SPAWN_EGG = ITEMS.register("french_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.FRENCH, 0x000000, 0xFFFFFF, // Placeholder colors
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}


