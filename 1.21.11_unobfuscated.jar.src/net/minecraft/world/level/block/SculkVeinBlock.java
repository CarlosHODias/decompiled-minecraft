/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class SculkVeinBlock extends MultifaceSpreadeableBlock implements SculkBehaviour {
/*  22 */   public static final MapCodec<SculkVeinBlock> CODEC = simpleCodec(SculkVeinBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<SculkVeinBlock> codec() {
/*  26 */     return CODEC;
/*     */   }
/*     */   
/*  29 */   private final MultifaceSpreader veinSpreader = new MultifaceSpreader(new SculkVeinSpreaderConfig(this, MultifaceSpreader.DEFAULT_SPREAD_ORDER));
/*     */ 
/*     */   
/*  32 */   private final MultifaceSpreader sameSpaceSpreader = new MultifaceSpreader(new SculkVeinSpreaderConfig(this, new MultifaceSpreader.SpreadType[] { MultifaceSpreader.SpreadType.SAME_POSITION }));
/*     */   
/*     */   public SculkVeinBlock(BlockBehaviour.Properties properties) {
/*  35 */     super(properties);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultifaceSpreader getSpreader() {
/*  40 */     return this.veinSpreader;
/*     */   }
/*     */   
/*     */   public MultifaceSpreader getSameSpaceSpreader() {
/*  44 */     return this.sameSpaceSpreader;
/*     */   }
/*     */   
/*     */   public static boolean regrow(LevelAccessor level, BlockPos pos, BlockState existing, Collection<Direction> faces) {
/*     */     boolean hasAtLeastOneFace = false;
/*  49 */     BlockState newState = Blocks.SCULK_VEIN.defaultBlockState();
/*     */     
/*  51 */     for (Direction face : faces) {
/*  52 */       if (canAttachTo((BlockGetter)level, pos, face)) {
/*  53 */         newState = (BlockState)newState.setValue((Property)getFaceProperty(face), true);
/*  54 */         hasAtLeastOneFace = true;
/*     */       } 
/*     */     } 
/*     */     
/*  58 */     if (!hasAtLeastOneFace) {
/*  59 */       return false;
/*     */     }
/*     */     
/*  62 */     if (!existing.getFluidState().isEmpty()) {
/*  63 */       newState = (BlockState)newState.setValue((Property)MultifaceBlock.WATERLOGGED, true);
/*     */     }
/*  65 */     level.setBlock(pos, newState, 3);
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDischarged(LevelAccessor level, BlockState state, BlockPos pos, RandomSource random) {
/*  71 */     if (!state.is(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     for (Direction dir : DIRECTIONS) {
/*  76 */       BooleanProperty sideProperty = getFaceProperty(dir);
/*  77 */       if ((Boolean)state.getValue((Property)sideProperty) && level.getBlockState(pos.relative(dir)).is(Blocks.SCULK)) {
/*  78 */         state = (BlockState)state.setValue((Property)sideProperty, false);
/*     */       }
/*     */     } 
/*  81 */     if (!hasAnyFace(state)) {
/*  82 */       FluidState fluidState = level.getFluidState(pos);
/*  83 */       state = (fluidState.isEmpty() ? Blocks.AIR : Blocks.WATER).defaultBlockState();
/*     */     } 
/*  85 */     level.setBlock(pos, state, 3);
/*  86 */     super.onDischarged(level, state, pos, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public int attemptUseCharge(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins) {
/*  91 */     if (spreadVeins && attemptPlaceSculk(spreader, level, cursor.getPos(), random)) {
/*  92 */       return cursor.getCharge() - 1;
/*     */     }
/*     */     
/*  95 */     return (random.nextInt(spreader.chargeDecayRate()) == 0) ? Mth.floor(cursor.getCharge() * 0.5F) : cursor.getCharge();
/*     */   }
/*     */   
/*     */   private boolean attemptPlaceSculk(SculkSpreader spreader, LevelAccessor level, BlockPos pos, RandomSource random) {
/*  99 */     BlockState state = level.getBlockState(pos);
/* 100 */     TagKey<Block> replaceTag = spreader.replaceableBlocks();
/* 101 */     for (Direction support : (Iterable<Direction>)Direction.allShuffled(random)) {
/* 102 */       if (!hasFace(state, support)) {
/*     */         continue;
/*     */       }
/*     */       
/* 106 */       BlockPos supportPos = pos.relative(support);
/* 107 */       BlockState supportState = level.getBlockState(supportPos);
/* 108 */       if (!supportState.is(replaceTag)) {
/*     */         continue;
/*     */       }
/*     */       
/* 112 */       BlockState defaultSculk = Blocks.SCULK.defaultBlockState();
/* 113 */       level.setBlock(supportPos, defaultSculk, 3);
/* 114 */       Block.pushEntitiesUp(supportState, defaultSculk, level, supportPos);
/* 115 */       level.playSound(null, supportPos, SoundEvents.SCULK_BLOCK_SPREAD, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */ 
/*     */       
/* 118 */       this.veinSpreader.spreadAll(defaultSculk, level, supportPos, spreader.isWorldGeneration());
/*     */ 
/*     */       
/* 121 */       Direction skip = support.getOpposite();
/* 122 */       for (Direction veinBlocks : DIRECTIONS) {
/* 123 */         if (veinBlocks != skip) {
/*     */ 
/*     */ 
/*     */           
/* 127 */           BlockPos veinPos = supportPos.relative(veinBlocks);
/* 128 */           BlockState possibleVeinBlock = level.getBlockState(veinPos);
/*     */           
/* 130 */           if (possibleVeinBlock.is(this))
/* 131 */             onDischarged(level, possibleVeinBlock, veinPos, random); 
/*     */         } 
/*     */       } 
/* 134 */       return true;
/*     */     } 
/* 136 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean hasSubstrateAccess(LevelAccessor level, BlockState state, BlockPos pos) {
/* 140 */     if (!state.is(Blocks.SCULK_VEIN)) {
/* 141 */       return false;
/*     */     }
/*     */     
/* 144 */     for (Direction direction : DIRECTIONS) {
/* 145 */       if (hasFace(state, direction) && level.getBlockState(pos.relative(direction)).is(BlockTags.SCULK_REPLACEABLE)) {
/* 146 */         return true;
/*     */       }
/*     */     } 
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   private class SculkVeinSpreaderConfig extends MultifaceSpreader.DefaultSpreaderConfig {
/*     */     private final MultifaceSpreader.SpreadType[] spreadTypes;
/*     */     
/*     */     public SculkVeinSpreaderConfig(SculkVeinBlock this$0, MultifaceSpreader.SpreadType... spreadTypes) {
/* 156 */       super(this$0);
/* 157 */       this.spreadTypes = spreadTypes;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean stateCanBeReplaced(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState) {
/* 162 */       BlockState againstState = level.getBlockState(placementPos.relative(placementDirection));
/*     */ 
/*     */ 
/*     */       
/* 166 */       if (againstState.is(Blocks.SCULK) || againstState.is(Blocks.SCULK_CATALYST) || againstState.is(Blocks.MOVING_PISTON)) {
/* 167 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 172 */       if (sourcePos.distManhattan((Vec3i)placementPos) == 2) {
/* 173 */         BlockPos neighourPos = sourcePos.relative(placementDirection.getOpposite());
/* 174 */         if (level.getBlockState(neighourPos).isFaceSturdy(level, neighourPos, placementDirection)) {
/* 175 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 179 */       FluidState fluidState = existingState.getFluidState();
/* 180 */       if (!fluidState.isEmpty() && !fluidState.is((net.minecraft.world.level.material.Fluid)net.minecraft.world.level.material.Fluids.WATER)) {
/* 181 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 185 */       if (existingState.is(BlockTags.FIRE)) {
/* 186 */         return false;
/*     */       }
/*     */       
/* 189 */       return (existingState.canBeReplaced() || super.stateCanBeReplaced(level, sourcePos, placementPos, placementDirection, existingState));
/*     */     }
/*     */ 
/*     */     
/*     */     public MultifaceSpreader.SpreadType[] getSpreadTypes() {
/* 194 */       return this.spreadTypes;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOtherBlockValidAsSource(BlockState state) {
/* 199 */       return !state.is(Blocks.SCULK_VEIN);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SculkVeinBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */