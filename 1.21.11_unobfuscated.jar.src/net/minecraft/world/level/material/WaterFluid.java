/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public abstract class WaterFluid
/*     */   extends FlowingFluid
/*     */ {
/*     */   public Fluid getFlowing() {
/*  36 */     return Fluids.FLOWING_WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid getSource() {
/*  41 */     return Fluids.WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBucket() {
/*  46 */     return Items.WATER_BUCKET;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  51 */     if (!fluidState.isSource() && !((Boolean)fluidState.getValue((Property)FALLING))) {
/*  52 */       if (random.nextInt(64) == 0) {
/*  53 */         level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.AMBIENT, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
/*     */       }
/*  55 */     } else if (random.nextInt(10) == 0) {
/*  56 */       level.addParticle((ParticleOptions)ParticleTypes.UNDERWATER, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/*  62 */     return (ParticleOptions)ParticleTypes.DRIPPING_WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canConvertToSource(ServerLevel level) {
/*  67 */     return (Boolean)level.getGameRules().get(GameRules.WATER_SOURCE_CONVERSION);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
/*  72 */     BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*  73 */     Block.dropResources(state, level, pos, blockEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
/*  78 */     effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlopeFindDistance(LevelReader level) {
/*  83 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState createLegacyBlock(FluidState fluidState) {
/*  88 */     return (BlockState)Blocks.WATER.defaultBlockState().setValue((Property)LiquidBlock.LEVEL, getLegacyLevel(fluidState));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSame(Fluid other) {
/*  93 */     return (other == Fluids.WATER || other == Fluids.FLOWING_WATER);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDropOff(LevelReader level) {
/*  98 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDelay(LevelReader level) {
/* 103 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
/* 108 */     return (direction == Direction.DOWN && !other.is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getExplosionResistance() {
/* 113 */     return 100.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 118 */     return Optional.of(SoundEvents.BUCKET_FILL);
/*     */   }
/*     */   
/*     */   public static class Source
/*     */     extends WaterFluid {
/*     */     public int getAmount(FluidState fluidState) {
/* 124 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState fluidState) {
/* 129 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends WaterFluid {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
/* 136 */       super.createFluidStateDefinition(builder);
/* 137 */       builder.add(new Property[] { (Property)LEVEL });
/*     */     }
/*     */ 
/*     */     
/*     */     public int getAmount(FluidState fluidState) {
/* 142 */       return (Integer)fluidState.getValue((Property)LEVEL);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState fluidState) {
/* 147 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/material/WaterFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */