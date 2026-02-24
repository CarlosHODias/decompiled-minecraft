/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WitherRoseBlock extends FlowerBlock {
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects), (App)propertiesCodec()).apply((Applicative)i, WitherRoseBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WitherRoseBlock> CODEC;
/*    */   
/*    */   public MapCodec<WitherRoseBlock> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WitherRoseBlock(Holder<MobEffect> mobEffect, float effectSeconds, BlockBehaviour.Properties properties) {
/* 38 */     this(makeEffectList(mobEffect, effectSeconds), properties);
/*    */   }
/*    */   
/*    */   public WitherRoseBlock(SuspiciousStewEffects suspiciousStewEffects, BlockBehaviour.Properties properties) {
/* 42 */     super(suspiciousStewEffects, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 47 */     return (super.mayPlaceOn(state, level, pos) || state.is(Blocks.NETHERRACK) || state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL));
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 52 */     VoxelShape shape = getShape(state, (BlockGetter)level, pos, net.minecraft.world.phys.shapes.CollisionContext.empty());
/* 53 */     Vec3 shapeCenter = shape.bounds().getCenter();
/* 54 */     double x = pos.getX() + shapeCenter.x;
/* 55 */     double z = pos.getZ() + shapeCenter.z;
/* 56 */     for (int i = 0; i < 3; i++) {
/* 57 */       if (random.nextBoolean()) {
/* 58 */         level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x + random.nextDouble() / 5.0D, pos.getY() + 0.5D - random.nextDouble(), z + random.nextDouble() / 5.0D, 0.0D, 0.0D, 0.0D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 65 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 66 */       if (level.getDifficulty() != net.minecraft.world.Difficulty.PEACEFUL && entity instanceof LivingEntity) {
/* 67 */         LivingEntity livingEntity = (LivingEntity)entity;
/* 68 */         if (!livingEntity.isInvulnerableTo(serverLevel, level.damageSources().wither()))
/* 69 */           livingEntity.addEffect(getBeeInteractionEffect()); 
/*    */       }  }
/*    */   
/*    */   }
/*    */   
/*    */   public MobEffectInstance getBeeInteractionEffect() {
/* 75 */     return new MobEffectInstance(net.minecraft.world.effect.MobEffects.WITHER, 40);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WitherRoseBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */