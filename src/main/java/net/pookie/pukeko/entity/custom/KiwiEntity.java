package net.pookie.pukeko.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.pookie.pukeko.entity.ModEntities;
import net.pookie.pukeko.items.ModItems;
import net.pookie.pukeko.sounds.ModSounds;
import org.jetbrains.annotations.Nullable;

public class KiwiEntity extends Animal {

    public KiwiEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Idle/dance animation
    public final AnimationState idleAnimationState = new AnimationState();
    public int idleCooldown = this.random.nextInt(3000) + 3000;
    private boolean idleAnimationPlaying = false;
    private int idleAnimationTicks = 80;

    // Roar animation
    public final AnimationState roarAnimationState = new AnimationState();
    public int roarCooldown = this.random.nextInt(8400) + 8400;
    private boolean roarAnimationPlaying = false;
    private int roarAnimationTicks = 105;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PukekoEntity.class, 5, 1.15,1.07));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.25));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, Ingredient.of(Items.COD), false));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, Ingredient.of(Items.SALMON), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.idleCooldown <= 0) {
            this.idleAnimationPlaying = true;
            this.idleAnimationTicks = 80;
            this.idleCooldown = this.random.nextInt(3000) + 3000;
        } else {
            this.idleCooldown--;
        }

        if (idleAnimationPlaying && idleAnimationTicks <= 0) {
            idleAnimationTicks = 80;
            idleAnimationPlaying = false;
        } else if (idleAnimationPlaying) {
            idleAnimationTicks--;
        }

        if (this.roarCooldown <= 0) {
            this.roarAnimationPlaying = true;
            this.roarAnimationTicks = 105;
            this.roarCooldown = this.random.nextInt(8400) + 8400;
        } else {
            this.roarCooldown--;
        }

        if (roarAnimationPlaying) {
            if (this.level().isClientSide() && this.roarAnimationTicks == 85) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.KIWI_ROAR.get(), SoundSource.HOSTILE, 1.0F, 1.0F, false);
            }

            this.roarAnimationTicks--;

            if (this.roarAnimationTicks <= 0) {
                this.roarAnimationTicks = 105;
                this.roarAnimationPlaying = false;
            }
        }

        // Intersection handling
        if (roarAnimationPlaying && idleAnimationPlaying) {
            idleAnimationPlaying = false;
            idleAnimationTicks = 80;
            idleCooldown = this.random.nextInt(1000) + 1000;
        }

        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(idleAnimationPlaying, tickCount);
            this.roarAnimationState.animateWhen(roarAnimationPlaying, tickCount);
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.KIWI.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        int randomInt = this.random.nextInt(3);

        if (randomInt == 0) {
            return ModSounds.KIWI_AMBIENT_1.get();
        } else if (randomInt == 1) {
            return ModSounds.KIWI_AMBIENT_2.get();
        } else {
            return ModSounds.KIWI_AMBIENT_3.get();
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        int randomDrops = this.random.nextInt(3);

        for (int i = 0; i < randomDrops; i++) {
            this.spawnAtLocation(ModItems.KIWI);
        }
    }

    public boolean isRoarAnimationPlaying() {
        return roarAnimationPlaying;
    }
}
