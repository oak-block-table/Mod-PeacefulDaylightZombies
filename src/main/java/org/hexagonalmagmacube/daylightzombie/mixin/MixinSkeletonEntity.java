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
        PeacefulDaylightZombiesMod.LOGGER.info("Trade the bow for a sword.");
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
    }
}
