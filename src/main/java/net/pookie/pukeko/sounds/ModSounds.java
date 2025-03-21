package net.pookie.pukeko.sounds;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.pookie.pukeko.Pukeko;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Pukeko.MODID);

    // Sound list
    public static final Supplier<SoundEvent> PUKEKO_AMBIENT_1 = registerSoundEvent("pukeko_ambient_1");
    public static final Supplier<SoundEvent> PUKEKO_AMBIENT_2 = registerSoundEvent("pukeko_ambient_2");
    public static final Supplier<SoundEvent> PUKEKO_AMBIENT_3 = registerSoundEvent("pukeko_ambient_3");
    public static final Supplier<SoundEvent> PUKEKO_DEATH = registerSoundEvent("pukeko_death");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Pukeko.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
