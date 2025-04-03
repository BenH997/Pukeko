package net.pookie.pukeko.entity.goals;

import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.phys.Vec3;
import net.pookie.pukeko.entity.custom.PukekoEntity;

public class PukekoFlyingMoveControl extends FlyingMoveControl {
    private final PukekoEntity entity;

    public PukekoFlyingMoveControl(PukekoEntity entity) {
        super(entity, 20, true);
        this.entity = entity;
    }

    @Override
    public void tick() {
        super.tick();

        if (entity.isFlyingEnabled() && entity.getMoveControl() instanceof FlyingMoveControl) {
            FlyingMoveControl moveControl = (FlyingMoveControl) entity.getMoveControl();

            Vec3 targetPos = new Vec3(moveControl.getWantedX(), moveControl.getWantedY(), moveControl.getWantedZ());
            Vec3 currentPos = entity.position();

            Vec3 direction = targetPos.subtract(currentPos);
            double distance = direction.length();

            if (distance > 0.01) { // Avoid division by zero
                Vec3 movement = direction.normalize().scale(0.01); // Adjust speed dynamically
                entity.setDeltaMovement(entity.getDeltaMovement().add(movement));
            }

            // Clamp movement speed to prevent extreme acceleration at large coordinates
            double maxSpeed = 0.02;
            Vec3 velocity = entity.getDeltaMovement();
            double clampedX = Math.max(-maxSpeed, Math.min(velocity.x, maxSpeed));
            double clampedY = Math.max(-maxSpeed, Math.min(velocity.y, maxSpeed));
            double clampedZ = Math.max(-maxSpeed, Math.min(velocity.z, maxSpeed));

            entity.setDeltaMovement(new Vec3(clampedX, clampedY, clampedZ));
        }
    }
}
