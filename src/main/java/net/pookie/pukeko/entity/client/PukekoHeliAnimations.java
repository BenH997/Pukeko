package net.pookie.pukeko.entity.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class PukekoHeliAnimations {
    public static final AnimationDefinition ANIM_PUKEKO_FLY = AnimationDefinition.Builder.withLength(1f)
            .addAnimation("Propeller",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 3600f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("Foot",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 3600f, 0f),
                                    AnimationChannel.Interpolations.LINEAR))).build();
}
