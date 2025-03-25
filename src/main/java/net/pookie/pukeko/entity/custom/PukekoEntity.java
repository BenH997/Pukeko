package net.pookie.pukeko.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.pookie.pukeko.entity.ModEntities;
import net.pookie.pukeko.items.ModItems;
import net.pookie.pukeko.sounds.ModSounds;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.monster.Ghast;

import java.util.Random;

public class PukekoEntity extends Animal {

    // Basic Animation stuff
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    // Egg lay counter
    public int eggTime = this.random.nextInt(6000) + 6000;

    // If modified by name
    public boolean nameModified = false;

    public PukekoEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Registers goals of the pukeko
    @Override
    protected void registerGoals() {

        // Keep head above water
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 3.0));

        // Breeding/bb stuff
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.BAMBOO), false)); // Sketchy
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));

        // Random looking and movement
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Avoid the french
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, FrenchEntity.class/*Villager.class*/, 6.0F, 5.0, 5.2));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20d)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.BAMBOO); // Could be broken
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.PUKEKO.get().create(level);
    }

 // Uncomment if you want an idle animation
    private void setupAnimationsStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationsStates();
        }
    }

    // Sounds

    @Override
    protected @Nullable SoundEvent getAmbientSound() {

        if (this.isBaby()) {
            return ModSounds.PUKEKO_AMBIENT_3.get();
        }

        // Play random ambient sound
        Random rand = new Random();
        int randomNumber = rand.nextInt(3);

        if (randomNumber == 0) {
            return ModSounds.PUKEKO_AMBIENT_1.get();
        } else if (randomNumber == 1) {
            return ModSounds.PUKEKO_AMBIENT_2.get();
        } else {
            return ModSounds.PUKEKO_AMBIENT_3.get();
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        Random random = new Random();
        int randomNumber = random.nextInt(3);

        if (randomNumber == 0) {
            return ModSounds.PUKEKO_HURT_1.get();
        } else if (randomNumber == 1) {
            return ModSounds.PUKEKO_HURT_2.get();
        } else {
            return ModSounds.PUKEKO_HURT_3.get();
        }
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.PUKEKO_DEATH.get();
    }

    // Pukeko updates/events
    @Override
    public void aiStep() {
        super.aiStep();

        // Naming events
        if (this.hasCustomName() && this.getCustomName().getString().equalsIgnoreCase("LOBOTOMIZATION")) {
            this.goalSelector.getAvailableGoals().clear();
            nameModified = true;
        }

        // Reset goals after rename
        if (nameModified && !this.getCustomName().getString().equalsIgnoreCase("LOBOTOMIZATION")) {
            this.goalSelector.getAvailableGoals().clear();
            registerGoals();
            nameModified = false;
        }

        // Egg lay
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.eggTime <= 0) {
            this.playSound(ModSounds.PUKEKO_DEATH.get()); // Change later
            this.spawnAtLocation(ModItems.PUKEKO_SPAWN_EGG); // Change later
            this.gameEvent(GameEvent.ENTITY_PLACE); // Allows to be detected by skulks?!?
            eggTime = this.random.nextInt(5000) + 5000;
        }
    }

    // Transformation/interaction prototype

//    @Override
//    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
//        ItemStack Itemstack = player.getItemInHand(hand);
//
//        if (Itemstack.is(Items.IRON_BARS)) {
//            this.goalSelector.getAvailableGoals().clear();
//
//            this.goalSelector.addGoal(0, new FloatGoal(this));
//            this.goalSelector.addGoal(1, new SwellGoal());
//        }
//
//        return super.interactAt(player, vec, hand);
//    }
}
