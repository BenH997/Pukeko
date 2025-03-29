package net.pookie.pukeko.entity.goals;


import net.minecraft.world.entity.ai.goal.Goal;
import net.pookie.pukeko.entity.custom.PukekoEntity;

import java.util.EnumSet;

public class RandomFlyGoal extends Goal {
    private PukekoEntity entity;

    public RandomFlyGoal(PukekoEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.isFlyingEnabled();
    }

    @Override
    public void tick() {
        if (entity.getRandom().nextInt(60) == 0) {
            double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 10;
            double y = entity.getY() + (entity.getRandom().nextDouble() - 0.5) * 10;
            double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 10;

            entity.getMoveControl().setWantedPosition(x, y, z, 0.25);
        }
    }
}