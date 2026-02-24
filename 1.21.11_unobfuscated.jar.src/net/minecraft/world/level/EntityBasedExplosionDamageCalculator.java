/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityBasedExplosionDamageCalculator
/*    */   extends ExplosionDamageCalculator {
/*    */   private final Entity source;
/*    */   
/*    */   public EntityBasedExplosionDamageCalculator(Entity source) {
/* 14 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/* 19 */     return super.getBlockExplosionResistance(explosion, level, pos, block, fluid).map(resistance -> this.source.getBlockExplosionResistance(explosion, explosion, level, pos, block, fluid));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/* 24 */     return this.source.shouldBlockExplode(explosion, level, pos, state, power);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/EntityBasedExplosionDamageCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */