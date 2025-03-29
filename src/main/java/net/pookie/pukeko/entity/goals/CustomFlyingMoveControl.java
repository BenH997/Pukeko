package net.pookie.pukeko.entity.goals;

import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.phys.Vec3;
import net.pookie.pukeko.entity.custom.PukekoEntity;

public class CustomFlyingMoveControl extends FlyingMoveControl {
    private final PukekoEntity entity;

    public CustomFlyingMoveControl(PukekoEntity entity) {
        super(entity, 10, true);
        this.entity = entity;
    }

    @Override
    public void tick() {
        super.tick();

        if (entity.isFlyingEnabled() && entity.getMoveControl() instanceof FlyingMoveControl) {
            FlyingMoveControl moveControl = (FlyingMoveControl) entity.getMoveControl();

            double targetX = moveControl.getWantedX();
            double targetY = moveControl.getWantedY();
            double targetZ = moveControl.getWantedZ();

            Vec3 motion = entity.getDeltaMovement();
            double acceleration = 0.001;

            // Move towards the target position
            entity.setDeltaMovement(motion.add(
                    (targetX - entity.getX()) * acceleration,
                    (targetY - entity.getY()) * acceleration,
                    (targetZ - entity.getZ()) * acceleration
            ));

            // Reduce air drag to maintain smooth movement
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.00, 1.0, 1.00));
        }
    }
}
