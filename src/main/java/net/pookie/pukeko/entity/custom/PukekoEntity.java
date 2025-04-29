package net.pookie.pukeko.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.Mod;
import net.pookie.pukeko.entity.ModEntities;
import net.pookie.pukeko.entity.goals.PukekoFlyingMoveControl;
import net.pookie.pukeko.entity.goals.PukekoRandomFlyGoal;
import net.pookie.pukeko.items.ModItems;
import net.pookie.pukeko.sounds.ModSounds;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.item.Items;

import java.util.Random;

public class PukekoEntity extends Animal implements Saddleable {

    // Transform Animation
    public final AnimationState transformAnimationState = new AnimationState();
    private int transformAnimationTicks = 0;
    private boolean transformAnimationPlaying = false;

    // Untransform Animation
    public final AnimationState untransformAnimationState = new AnimationState();
    private int untransformAnimationTicks = 0;
    private boolean untransformAnimationPlaying = false;

    // Attack Animation
    public final AnimationState attackAnimationSate = new AnimationState();
    private int attackAnimationTicks = 0;
    private boolean attackAnimationPlaying = false;

    // Duplication Animation
    public final AnimationState duplicationAnimationState = new AnimationState();
    private final int duplicationSpawnCooldown = 100;
    private boolean duplicationAnimationPlaying = false;

    // Flying Animation
    public final AnimationState flyingAnimationState = new AnimationState();

    // Annihilation Animation
    public final AnimationState annihilationAnimationState = new AnimationState();

    private String entityName = null;

    // Different state booleans
    private boolean canFly = false;
    private boolean annihilationActive = false;
    private boolean duplicationActive = false;
    private boolean locomotionActive = false;

    private int duplicationTimer = 0;

    public PukekoEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new MoveControl(this);
        this.navigation = new GroundPathNavigation(this, this.level());
    }

    // Registers goals of the pukeko
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.15, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, KiwiEntity.class, false));
        this.goalSelector.addGoal(1, new PanicGoal(this, 3.0));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, FrenchEntity.class, 6.0F, 5.0, 5.2));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, Ingredient.of(ModItems.KIWI), false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isSaddleable() {
        return this.locomotionActive;
    }

    @Override
    public void equipSaddle(ItemStack itemStack, @Nullable SoundSource soundSource) {

    }

    @Override
    public boolean isSaddled() {
        return false;
    }

    public class PukekoAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public PukekoAttackGoal(PukekoEntity pukeko) {
            super(pukeko, LivingEntity.class, 0, true, true,
                    entity -> !(entity instanceof PukekoEntity));
        }

        @Override
        public boolean canUse() {
            return ((PukekoEntity) this.mob).annihilationActive && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            this.mob.setNoActionTime(0);

            PukekoEntity.this.startAttackAnimation();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20d)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.KIWI);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.PUKEKO.get().create(level);
    }

    @Override
    public void tick() {
        super.tick();

        // Duplication
        if (this.duplicationActive && this.duplicationTimer <= 0) {
            Level level = this.level();
            Vec3 position = this.position();

            EntityType<PukekoEntity> entityType = ModEntities.PUKEKO.get();
            PukekoEntity newEntity = entityType.create(level);
            if (newEntity != null) {
                newEntity.moveTo(position.x(), position.y() + 0.8, position.z(), this.getYRot(), this.getXRot());
                newEntity.setYRot(this.getYRot());
                newEntity.setXRot(this.getXRot());
                newEntity.setYHeadRot(this.getYRot());

                newEntity.setNoAi(true);
                level.addFreshEntity(newEntity);
                newEntity.setYRot(this.getYRot());
                newEntity.setXRot(this.getXRot());
                newEntity.setNoAi(false);

                this.duplicationAnimationPlaying = true;
            }

            this.duplicationTimer = duplicationSpawnCooldown;
        } else {
            this.duplicationTimer--;
        }

        // Animations
        if (this.isTransformAnimationPlaying() && this.transformAnimationTicks <= 0) {
            this.transformAnimationTicks = -1;
            this.transformAnimationPlaying = false;
        } else if (this.isTransformAnimationPlaying() && this.transformAnimationTicks >= 1) {
            this.transformAnimationTicks--;
        }

        if (this.isUntransformAnimationPlaying() && this.untransformAnimationTicks <= 0) {
            this.untransformAnimationTicks = -1;
            this.untransformAnimationPlaying = false;
        } else if (this.isUntransformAnimationPlaying() && this.untransformAnimationTicks >= 1) {
            this.untransformAnimationTicks--;
        }

        if (this.isUntransformAnimationPlaying() && this.attackAnimationTicks <= 0) {
            this.attackAnimationTicks = -1;
            this.attackAnimationPlaying = false;
        } else if (this.isUntransformAnimationPlaying() && this.attackAnimationTicks >= 1) {
            this.attackAnimationTicks--;
        }

        // Animation conditions
        if (this.level().isClientSide()) {
            this.transformAnimationState.animateWhen(this.isTransformAnimationPlaying(), this.tickCount);
            this.flyingAnimationState.animateWhen(!this.isTransformAnimationPlaying() && this.canFly, this.tickCount);
            this.untransformAnimationState.animateWhen(this.isUntransformAnimationPlaying(), this.tickCount);
            this.attackAnimationSate.animateWhen(this.attackAnimationPlaying, this.tickCount);
            this.duplicationAnimationState.animateWhen(this.duplicationAnimationPlaying, this.tickCount);
            this.annihilationAnimationState.animateWhen(this.annihilationActive, this.tickCount);
        }

        // Get Entity name
        if (this.hasCustomName()) {
            entityName = this.getCustomName().getString();
        }

        // Flying enabling name
        if (this.hasCustomName() && !this.canFly && entityName.equals("AVIATION")) {
            this.canFly = true;
            this.moveControl = new PukekoFlyingMoveControl(this);
            this.navigation = new FlyingPathNavigation(this, this.level());

            this.goalSelector.removeGoal(new WaterAvoidingRandomStrollGoal(this, 1.0));
            this.goalSelector.removeGoal(new PanicGoal(this, 2.0));
            this.goalSelector.addGoal(1, new PukekoRandomFlyGoal(this));

            this.startTransformationAnimation();

            this.getMoveControl().setWantedPosition(this.getX(), this.getY() + 1, this.getZ(), 0.05);
            this.setNoGravity(true);

            disableNonActiveAbilities("AVIATION");
        }

        // Annihilation enabling name
        if (this.hasCustomName() && !this.annihilationActive && this.entityName.equals("ANNIHILATION")) {
            this.annihilationActive = true;
            this.goalSelector.removeGoal(new PanicGoal(this, 3.0F));
            this.targetSelector.addGoal(1, new PukekoAttackGoal(this));
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2D, true));

            disableNonActiveAbilities("ANNIHILATION");
        }

        // Duplication enable
        if (this.hasCustomName() && !this.duplicationActive && this.entityName.equals("DUPLICATION")) {
            this.duplicationActive = true;
            this.duplicationTimer = duplicationSpawnCooldown;
            this.duplicationAnimationPlaying = true;

            disableNonActiveAbilities("DUPLICATION");
        }

        if (this.hasCustomName() && !this.locomotionActive && this.entityName.equals("LOCOMOTION")) {
            this.locomotionActive = true;

            disableNonActiveAbilities("LOCOMOTION");
        }

        // Reset pukeko after rename
        // Reset flight
        if (canFly && !this.entityName.equals("AVIATION")) {
            this.canFly = false;
            this.moveControl = new MoveControl(this);
            this.navigation = new GroundPathNavigation(this, this.level());

            this.goalSelector.removeGoal(new PukekoRandomFlyGoal(this));

            this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
            this.goalSelector.addGoal(1, new PanicGoal(this, 3.0));

            this.startUntransformationAnimation();

            this.setNoGravity(false);
            this.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 1));
        }

        // Reset Annihilation
        if (annihilationActive && !this.entityName.equals("ANNIHILATION")) {
            this.annihilationActive = false;
            this.targetSelector.removeGoal(new PukekoAttackGoal(this));
            this.goalSelector.removeGoal(new MeleeAttackGoal(this, 2D, true));
            this.goalSelector.addGoal(1, new PanicGoal(this, 3.0F));
            this.setTarget(null);
        }

        // Reset duplication
        if (duplicationActive && !this.entityName.equals("DUPLICATION")) {
            this.duplicationActive = false;
            this.duplicationAnimationPlaying = false;
        }

        if (locomotionActive && !this.entityName.equals("LOCOMOTION")) {
            this.locomotionActive = false;
        }
    }

    private void disableNonActiveAbilities(String currentlyActive) {
        if (!currentlyActive.equals("AVIATION") && this.canFly) {
            this.canFly = false;
            this.moveControl = new MoveControl(this);
            this.navigation = new GroundPathNavigation(this, this.level());
            this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
            this.goalSelector.addGoal(1, new PanicGoal(this, 3.0));
            this.goalSelector.removeGoal(new PukekoRandomFlyGoal(this));

            this.startUntransformationAnimation();

            this.setNoGravity(false);
            this.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 1));
        }

        if (!currentlyActive.equals("ANNIHILATION") && this.annihilationActive) {
            annihilationActive = false;
            this.targetSelector.removeGoal(new PukekoAttackGoal(this));
            this.goalSelector.removeGoal(new MeleeAttackGoal(this, 2.0D, true));
            this.goalSelector.addGoal(1, new PanicGoal(this, 3.0F));
            this.setTarget(null);
        }

        if (!currentlyActive.equals("DUPLICATION") && this.duplicationActive) {
            this.duplicationActive = false;
            this.duplicationAnimationPlaying = false;
        }

        if (!currentlyActive.equals("LOCOMOTION") && this.locomotionActive) {
            this.locomotionActive = false;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        super.mobInteract(player, hand);
        if (this.locomotionActive) {
            ItemStack item = player.getItemInHand(hand);
            if (!this.level().isClientSide && !item.is(ModItems.KIWI)) {
                player.startRiding(this);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            InteractionResult interactionresult = super.mobInteract(player, hand);
            return interactionresult;
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.locomotionActive && this.getFirstPassenger() instanceof Player player) {
            this.setXRot(player.getXRot() * 0.5f);
            this.setYRot(player.getYRot());

            this.setSpeed((float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.25f));

            float forward = player.zza;
            float strafe = player.xxa;

            super.travel(new Vec3(strafe, travelVector.y, forward));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public float maxUpStep() {
        if (this.locomotionActive) {
            return 2.0f;
        } else {
            return 0.6f;
        }
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 2.0f);
    }

    // Sounds
    @Override
    protected @Nullable SoundEvent getAmbientSound() {

        if (this.isBaby()) {
            return ModSounds.PUKEKO_AMBIENT_3.get();
        }

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
        return ModSounds.PUKEKO_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.PUKEKO_DEATH.get();
    }

    // More Attributes
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return (!this.isFlyingEnabled()) && super.causeFallDamage(fallDistance, multiplier, source);
    }

    public boolean isFlyingEnabled() {
        return this.canFly;
    }

    // Transform Animation
    public void startTransformationAnimation() {
        if (!transformAnimationPlaying) {
            this.transformAnimationPlaying = true;
            this.transformAnimationTicks = 36;
        }
    }

    public boolean isTransformAnimationPlaying() {
        return this.transformAnimationPlaying;
    }

    // Untransform Animation
    public void startUntransformationAnimation() {
        if (!untransformAnimationPlaying) {
            this.untransformAnimationPlaying = true;
            this.untransformAnimationTicks = 36;
        }
    }

    public boolean isUntransformAnimationPlaying() {
        return this.untransformAnimationPlaying;
    }

    // Attack Animation
    public void startAttackAnimation() {
        if (!attackAnimationPlaying) {
            this.attackAnimationPlaying = true;
            this.attackAnimationTicks = 10;
        }
    }

    // Duplication getter
    public boolean isDuplicationActive() {
        return this.duplicationActive;
    }
}
