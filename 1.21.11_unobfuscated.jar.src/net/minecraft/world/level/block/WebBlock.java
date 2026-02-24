/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WebBlock extends Block {
/* 14 */   public static final MapCodec<WebBlock> CODEC = simpleCodec(WebBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<WebBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WebBlock(BlockBehaviour.Properties properties) {
/* 22 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 27 */     Vec3 speedMultiplier = new Vec3(0.25D, 0.05000000074505806D, 0.25D);
/* 28 */     if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity; if (livingEntity.hasEffect(MobEffects.WEAVING))
/* 29 */         speedMultiplier = new Vec3(0.5D, 0.25D, 0.5D);  }
/*    */     
/* 31 */     entity.makeStuckInBlock(state, speedMultiplier);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WebBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */