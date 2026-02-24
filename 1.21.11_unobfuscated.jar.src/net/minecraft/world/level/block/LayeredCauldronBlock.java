/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.cauldron.CauldronInteraction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LayeredCauldronBlock extends AbstractCauldronBlock {
/*     */   static {
/*  28 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter(()), (App)CauldronInteraction.CODEC.fieldOf("interactions").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, LayeredCauldronBlock::new));
/*     */   }
/*     */   
/*     */   public static final MapCodec<LayeredCauldronBlock> CODEC;
/*     */   public static final int MIN_FILL_LEVEL = 1;
/*     */   public static final int MAX_FILL_LEVEL = 3;
/*     */   
/*     */   public MapCodec<LayeredCauldronBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
/*     */   
/*     */   private static final int BASE_CONTENT_HEIGHT = 6;
/*     */   
/*     */   private static final double HEIGHT_PER_LEVEL = 3.0D;
/*  46 */   private static final VoxelShape[] FILLED_SHAPES = (VoxelShape[])Util.make(() -> Block.boxes(2, ()));
/*     */   
/*     */   private final Biome.Precipitation precipitationType;
/*     */   
/*     */   public LayeredCauldronBlock(Biome.Precipitation precipitationType, CauldronInteraction.InteractionMap interactionMap, BlockBehaviour.Properties properties) {
/*  51 */     super(properties, interactionMap);
/*  52 */     this.precipitationType = precipitationType;
/*  53 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFull(BlockState state) {
/*  58 */     return ((Integer)state.getValue((Property)LEVEL) == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReceiveStalactiteDrip(Fluid fluid) {
/*  63 */     return (fluid == net.minecraft.world.level.material.Fluids.WATER && this.precipitationType == Biome.Precipitation.RAIN);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getContentHeight(BlockState state) {
/*  68 */     return getPixelContentHeight((Integer)state.getValue((Property)LEVEL)) / 16.0D;
/*     */   }
/*     */   
/*     */   private static double getPixelContentHeight(int level) {
/*  72 */     return 6.0D + level * 3.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, Entity entity) {
/*  77 */     return FILLED_SHAPES[(Integer)state.getValue((Property)LEVEL) - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  82 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  83 */       BlockPos blockPos = pos.immutable();
/*  84 */       effectApplier.runBefore(InsideBlockEffectType.EXTINGUISH, e -> {
/*     */             if (state.isOnFire() && state.mayInteract(serverLevel, serverLevel)) {
/*     */               handleEntityOnFireInside(blockPos, state, serverLevel);
/*     */             }
/*     */           }); }
/*     */     
/*  90 */     effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
/*     */   }
/*     */   
/*     */   private void handleEntityOnFireInside(BlockState state, Level level, BlockPos pos) {
/*  94 */     if (this.precipitationType == Biome.Precipitation.SNOW) {
/*  95 */       lowerFillLevel((BlockState)Blocks.WATER_CAULDRON.defaultBlockState().setValue((Property)LEVEL, state.getValue((Property)LEVEL)), level, pos);
/*     */     } else {
/*  97 */       lowerFillLevel(state, level, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void lowerFillLevel(BlockState state, Level level, BlockPos pos) {
/* 102 */     int newLevel = (Integer)state.getValue((Property)LEVEL) - 1;
/* 103 */     BlockState newState = (newLevel == 0) ? Blocks.CAULDRON.defaultBlockState() : (BlockState)state.setValue((Property)LEVEL, newLevel);
/* 104 */     level.setBlockAndUpdate(pos, newState);
/* 105 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
/* 110 */     if (!CauldronBlock.shouldHandlePrecipitation(level, precipitation) || (Integer)state.getValue((Property)LEVEL) == 3 || precipitation != this.precipitationType) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     BlockState newState = (BlockState)state.cycle((Property)LEVEL);
/* 115 */     level.setBlockAndUpdate(pos, newState);
/* 116 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, net.minecraft.core.Direction direction) {
/* 121 */     return (Integer)state.getValue((Property)LEVEL);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 126 */     builder.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
/* 131 */     if (isFull(state)) {
/*     */       return;
/*     */     }
/* 134 */     BlockState newState = (BlockState)state.setValue((Property)LEVEL, (Integer)state.getValue((Property)LEVEL) + 1);
/* 135 */     level.setBlockAndUpdate(pos, newState);
/* 136 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/* 137 */     level.levelEvent(1047, pos, 0);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/LayeredCauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */