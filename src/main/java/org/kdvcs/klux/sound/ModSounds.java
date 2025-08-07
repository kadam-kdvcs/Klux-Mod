package org.kdvcs.klux.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.kdvcs.klux.Klux;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Klux.MODID);

    public static final RegistryObject<SoundEvent> DETECTOR_FOUND_ORE = registerSoundEvents("detector_found_ore");

    public static final RegistryObject<SoundEvent> SOUND_BLOCK_TOUCH = registerSoundEvents("sound_block_touch");

    //SOUND BLOCK
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_BREAK = registerSoundEvents("sound_block_break");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_STEP = registerSoundEvents("sound_block_step");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_FALL = registerSoundEvents("sound_block_fall");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_PLACE = registerSoundEvents("sound_block_place");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_HIT = registerSoundEvents("sound_block_hit");

    //MUSIC
    public static final RegistryObject<SoundEvent> RUMBLE = registerSoundEvents("rumble");
    public static final RegistryObject<SoundEvent> BIG_MUSIC = registerSoundEvents("big_music");

    //COMPRESSOR
    public static final RegistryObject<SoundEvent> COMPRESSOR_WORKING = registerSoundEvents("compressor_working");
    public static final RegistryObject<SoundEvent> FLUID_ASSEMBLER_WORKING = registerSoundEvents("fluid_assembler_working");
    public static final RegistryObject<SoundEvent> DEHYDRATOR_WORKING = registerSoundEvents("dehydrator_working");
    public static final RegistryObject<SoundEvent> EXTRACTOR_WORKING = registerSoundEvents("extractor_working");
    public static final RegistryObject<SoundEvent> FLUID_EXTRACTOR_WORKING = registerSoundEvents("fluid_extractor_working");
    public static final RegistryObject<SoundEvent> LIQUID_REACTOR_WORKING = registerSoundEvents("liquid_reactor_working");

    public static final RegistryObject<SoundEvent> FLUX_SYNTHESIZER_WORKING = registerSoundEvents("flux_synthesizer_working");
    public static final RegistryObject<SoundEvent> LIQUID_FILTER_WORKING = registerSoundEvents("liquid_filter_working");

    public static final ForgeSoundType SOUND_BLOCK_SOUNDS = new ForgeSoundType(1f,1f,
            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_PLACE,
            ModSounds.SOUND_BLOCK_HIT, ModSounds.SOUND_BLOCK_FALL);


    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Klux.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
