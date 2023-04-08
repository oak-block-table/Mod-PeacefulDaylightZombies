package org.hexagonalmagmacube.daylightzombie.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.hexagonalmagmacube.daylightzombie.PeacefulDaylightZombiesMod;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkeletonEntity.class)
public abstract class MixinSkeletonEntity extends AbstractSkeletonEntity {

    public MixinSkeletonEntity(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

//    @Overwrite
//    public boolean isShaking() { return true; }

//    @Inject(method = "initEquipment(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/LocalDifficulty;)V", at = @At("HEAD")) //, cancellable = true)
//    protected void afterInitEquipment() {
//    @Overwrite
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        int randomInt = random.nextBetween(1, 4);
        if (randomInt <= 2) { // now, a 2 out of 4 chance of special event
            PeacefulDaylightZombiesMod.LOGGER.info("Trade the bow for a sword.");
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
            this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        } else {
            PeacefulDaylightZombiesMod.LOGGER.info(String.format("Random case #%1d Standard skeleton.",randomInt));
        }
    }
}
