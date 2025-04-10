package net.pookie.pukeko.entity.custom;

import net.minecraft.server.level.ServerLevel;
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
import org.jetbrains.annotations.Nullable;

public class KiwiEntity extends Animal {

    public KiwiEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Basic Animation stuff
    public final AnimationState idleAnimationState = new AnimationState();
    public int idleCooldown = this.random.nextInt(3000) + 3000;
    private boolean idleAnimationPlaying = false;
    private int idleAnimationTicks = 40;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PukekoEntity.class, 10, 1.25,1.5));
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
            this.idleCooldown = this.random.nextInt(3000) + 3000;
        } else {
            this.idleCooldown--;
        }

        if (idleAnimationPlaying && idleAnimationTicks <= 0) {
            idleAnimationTicks = 40;
            idleAnimationPlaying = false;
        } else if (idleAnimationPlaying) {
            idleAnimationTicks--;
        }

        if (this.level().isClientSide()) {
            this.idleAnimationState.animateWhen(idleAnimationPlaying, tickCount);
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
}
