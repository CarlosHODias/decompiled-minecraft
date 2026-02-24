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
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LavaFluid
/*     */   extends FlowingFluid
/*     */ {
/*     */   public static final float MIN_LEVEL_CUTOFF = 0.44444445F;
/*     */   
/*     */   public Fluid getFlowing() {
/*  41 */     return Fluids.FLOWING_LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid getSource() {
/*  46 */     return Fluids.LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBucket() {
/*  51 */     return Items.LAVA_BUCKET;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(Level level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  56 */     BlockPos above = pos.above();
/*  57 */     if (level.getBlockState(above).isAir() && !level.getBlockState(above).isSolidRender()) {
/*  58 */       if (random.nextInt(100) == 0) {
/*  59 */         double xx = pos.getX() + random.nextDouble();
/*     */         
/*  61 */         double yy = pos.getY() + 1.0D;
/*  62 */         double zz = pos.getZ() + random.nextDouble();
/*  63 */         level.addParticle((ParticleOptions)ParticleTypes.LAVA, xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*  64 */         level.playLocalSound(xx, yy, zz, SoundEvents.LAVA_POP, SoundSource.AMBIENT, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
/*     */       } 
/*  66 */       if (random.nextInt(200) == 0) {
/*  67 */         level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.AMBIENT, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(ServerLevel level, BlockPos pos, FluidState fluidState, RandomSource random) {
/*  74 */     if (!level.canSpreadFireAround(pos)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  79 */     int passes = random.nextInt(3);
/*  80 */     if (passes > 0) {
/*  81 */       BlockPos testPos = pos;
/*     */       
/*  83 */       for (int pass = 0; pass < passes; pass++) {
/*  84 */         testPos = testPos.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
/*  85 */         if (!level.isLoaded(testPos)) {
/*     */           return;
/*     */         }
/*  88 */         BlockState blockState = level.getBlockState(testPos);
/*  89 */         if (blockState.isAir()) {
/*  90 */           if (hasFlammableNeighbours((LevelReader)level, testPos)) {
/*  91 */             level.setBlockAndUpdate(testPos, BaseFireBlock.getState((BlockGetter)level, testPos));
/*     */             return;
/*     */           } 
/*  94 */         } else if (blockState.blocksMotion()) {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  99 */       for (int i = 0; i < 3; i++) {
/* 100 */         BlockPos testPos = pos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
/* 101 */         if (!level.isLoaded(testPos)) {
/*     */           return;
/*     */         }
/* 104 */         if (level.isEmptyBlock(testPos.above()) && isFlammable((LevelReader)level, testPos)) {
/* 105 */           level.setBlockAndUpdate(testPos.above(), BaseFireBlock.getState((BlockGetter)level, testPos));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
/* 113 */     effectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
/* 114 */     effectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
/* 115 */     effectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
/*     */   }
/*     */   
/*     */   private boolean hasFlammableNeighbours(LevelReader level, BlockPos pos) {
/* 119 */     for (Direction direction : Direction.values()) {
/* 120 */       if (isFlammable(level, pos.relative(direction))) {
/* 121 */         return true;
/*     */       }
/*     */     } 
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isFlammable(LevelReader level, BlockPos pos) {
/* 128 */     if (level.isInsideBuildHeight(pos.getY()) && !level.hasChunkAt(pos)) {
/* 129 */       return false;
/*     */     }
/* 131 */     return level.getBlockState(pos).ignitedByLava();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleOptions getDripParticle() {
/* 136 */     return (ParticleOptions)ParticleTypes.DRIPPING_LAVA;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
/* 141 */     fizz(level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlopeFindDistance(LevelReader level) {
/* 146 */     return isFastLava(level) ? 4 : 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState createLegacyBlock(FluidState fluidState) {
/* 151 */     return (BlockState)Blocks.LAVA.defaultBlockState().setValue((Property)LiquidBlock.LEVEL, getLegacyLevel(fluidState));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSame(Fluid other) {
/* 156 */     return (other == Fluids.LAVA || other == Fluids.FLOWING_LAVA);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDropOff(LevelReader level) {
/* 161 */     return isFastLava(level) ? 1 : 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
/* 166 */     return (state.getHeight(level, pos) >= 0.44444445F && other.is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTickDelay(LevelReader level) {
/* 171 */     return isFastLava(level) ? 10 : 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpreadDelay(Level level, BlockPos pos, FluidState oldFluidState, FluidState newFluidState) {
/* 176 */     int result = getTickDelay((LevelReader)level);
/*     */     
/* 178 */     if (!oldFluidState.isEmpty() && !newFluidState.isEmpty() && !((Boolean)oldFluidState.getValue((Property)FALLING)) && !((Boolean)newFluidState.getValue((Property)FALLING)) && newFluidState.getHeight((BlockGetter)level, pos) > oldFluidState.getHeight((BlockGetter)level, pos) && level.getRandom().nextInt(4) != 0) {
/* 179 */       result *= 4;
/*     */     }
/* 181 */     return result;
/*     */   }
/*     */   
/*     */   private void fizz(LevelAccessor level, BlockPos pos) {
/* 185 */     level.levelEvent(1501, pos, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canConvertToSource(ServerLevel level) {
/* 190 */     return (Boolean)level.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState target) {
/* 195 */     if (direction == Direction.DOWN) {
/* 196 */       FluidState fluidState = level.getFluidState(pos);
/* 197 */       if (is(FluidTags.LAVA) && fluidState.is(FluidTags.WATER)) {
/* 198 */         if (state.getBlock() instanceof LiquidBlock) {
/* 199 */           level.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
/*     */         }
/* 201 */         fizz(level, pos);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 206 */     super.spreadTo(level, pos, state, direction, target);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking() {
/* 211 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getExplosionResistance() {
/* 216 */     return 100.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 221 */     return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
/*     */   }
/*     */   
/*     */   private static boolean isFastLava(LevelReader level) {
/* 225 */     return (Boolean)level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA);
/*     */   }
/*     */   
/*     */   public static class Source
/*     */     extends LavaFluid {
/*     */     public int getAmount(FluidState fluidState) {
/* 231 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState fluidState) {
/* 236 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Flowing
/*     */     extends LavaFluid {
/*     */     protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
/* 243 */       super.createFluidStateDefinition(builder);
/* 244 */       builder.add(new Property[] { (Property)LEVEL });
/*     */     }
/*     */ 
/*     */     
/*     */     public int getAmount(FluidState fluidState) {
/* 249 */       return (Integer)fluidState.getValue((Property)LEVEL);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSource(FluidState fluidState) {
/* 254 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/material/LavaFluid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */