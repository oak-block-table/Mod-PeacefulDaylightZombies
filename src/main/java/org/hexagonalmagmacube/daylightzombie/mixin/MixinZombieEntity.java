package org.hexagonalmagmacube.daylightzombie.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;

import org.hexagonalmagmacube.daylightzombie.PeacefulDaylightZombiesMod;


@Mixin(ZombieEntity.class)
public abstract class MixinZombieEntity extends HostileEntity {
    // Assume an entity is above ground if it is above this block level
    private static double GroundLevelThreshold = 60.0;
    // Hostile Entity will become peaceful in a brightness above this level (Range = 0.0 to 1.0 ?)
    // When measured within a ring of torches, the brightness level was 0.41.
    // Tested a few reasonably lit rooms and found brightness as low as 0.27 in corners.
    // We will assume a torch-lit room is at least 0.25f
    private static float CalmingBrightnessThreshold = 0.25f;


    protected MixinZombieEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Overwrite
    protected boolean burnsInDaylight() {
        return false;
    }

    @Inject(method = "initCustomGoals()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V",
            ordinal = 4), cancellable = true)
    protected void onInitCustomGoals(CallbackInfo ci) {
        // Mixin-Maintenance: This method was last updated for version 1.18.2 where the vanilla source was:
        //  this.goalSelector.add(2, new ZombieAttackGoal( (ZombieEntity)this, 1.0D, false));
        //  this.goalSelector.add(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
        //  this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        //  this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[]{ZombifiedPiglinEntity.class}));
        // Mixin-Maintenance: Injection parameter 'ordinal = 4' means that we expect the above lines execute from
        // the original method before this callback will interrupt and cancel that original method
        this.targetSelector.add(2, new MixinZombieEntity.TargetGoal(this, PlayerEntity.class));
        this.targetSelector.add(3, new MixinZombieEntity.TargetGoal(this, MerchantEntity.class));
        this.targetSelector.add(3, new MixinZombieEntity.TargetGoal(this, IronGolemEntity.class));
        this.targetSelector.add(5, new ActiveTargetGoal(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));

        ci.cancel();
    }

    private static class TargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
        public TargetGoal(MixinZombieEntity zombie, Class<T> targetEntityClass) {
            super(zombie, targetEntityClass, true);
        }

        private int i = 0;
        public boolean canStart() {
            // First evaluate
            if (!super.canStart())
            {
                return false;
            }

            // TODO: Should we try to use this.mob.getEyeLocation().getBlock().getType() != Cave Air instead
            boolean isAboveGround = (this.mob.getY() > GroundLevelThreshold);
            if (!isAboveGround) { return true; }
            boolean isBrightEnough = (this.mob.getBrightnessAtEyes() > CalmingBrightnessThreshold);

//            PeacefulDaylightZombiesMod.LOGGER.info(String.format("Y-level: %.2f; Brightness: %.2f; bright Enough: %b; (%s)",
//                    this.mob.getY(), this.mob.getBrightnessAtEyes(), isBrightEnough,
//                    this.mob.getUuidAsString().substring(0,7)));

            if (isBrightEnough) {
                this.targetEntity = null;
                return false;
            }
            if (this.targetEntity != null) {  // null check, just in case
                PeacefulDaylightZombiesMod.LOGGER.info(String.format(
                        "Zombie %s targets a %s; [X,Y,Z]: [%.1f, %.1f, %.1f]; Brightness: %.3f",
                        this.mob.getUuidAsString().substring(0,7),
                        this.targetEntity.getClass().getTypeName(),
                        this.mob.getX(), this.mob.getY(), this.mob.getZ(),
                        this.mob.getBrightnessAtEyes()));
            }
            return true;
        }
    }
}
