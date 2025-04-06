package net.pookie.pukeko.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pookie.pukeko.Pukeko;
import net.pookie.pukeko.entity.custom.FrenchEntity;
import net.pookie.pukeko.entity.custom.KiwiEntity;
import net.pookie.pukeko.entity.custom.PukekoEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Pukeko.MODID);

    public static final Supplier<EntityType<PukekoEntity>> PUKEKO =
            ENTITY_TYPES.register("pukeko", () -> EntityType.Builder.of(PukekoEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 1.0f).build("pukeko"));

    public static final Supplier<EntityType<FrenchEntity>> FRENCH =
            ENTITY_TYPES.register("french", () -> EntityType.Builder.of(FrenchEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 2.0f).build("french"));

    public static final Supplier<EntityType<KiwiEntity>> KIWI =
            ENTITY_TYPES.register("kiwi", () -> EntityType.Builder.of(KiwiEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 0.6f).build("kiwi"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
