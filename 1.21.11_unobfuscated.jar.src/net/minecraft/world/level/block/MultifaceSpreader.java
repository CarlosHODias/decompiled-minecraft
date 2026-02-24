/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class MultifaceSpreader
/*     */ {
/*  15 */   public static final SpreadType[] DEFAULT_SPREAD_ORDER = new SpreadType[] { SpreadType.SAME_POSITION, SpreadType.SAME_PLANE, SpreadType.WRAP_AROUND };
/*     */ 
/*     */ 
/*     */   
/*     */   private final SpreadConfig config;
/*     */ 
/*     */ 
/*     */   
/*     */   public MultifaceSpreader(MultifaceBlock multifaceBlock) {
/*  24 */     this(new DefaultSpreaderConfig(multifaceBlock));
/*     */   }
/*     */   
/*     */   public MultifaceSpreader(SpreadConfig config) {
/*  28 */     this.config = config;
/*     */   }
/*     */   
/*     */   public boolean canSpreadInAnyDirection(BlockState state, BlockGetter level, BlockPos pos, Direction startingFace) {
/*  32 */     return Direction.stream().anyMatch(spreadDirection -> {
/*     */           Objects.requireNonNull(this.config);
/*     */           return getSpreadFromFaceTowardDirection(state, state, level, pos, startingFace, this.config::canSpreadInto).isPresent();
/*     */         }); } public Optional<SpreadPos> spreadFromRandomFaceTowardRandomDirection(BlockState state, LevelAccessor level, BlockPos pos, RandomSource random) {
/*  36 */     return Direction.allShuffled(random).stream()
/*  37 */       .filter(faceDirection -> this.config.canSpreadFrom(state, state))
/*  38 */       .map(faceDirection -> spreadFromFaceTowardRandomDirection(state, state, level, random, pos, false))
/*  39 */       .filter(Optional::isPresent)
/*  40 */       .findFirst()
/*  41 */       .orElse(Optional.empty());
/*     */   }
/*     */   
/*     */   public long spreadAll(BlockState state, LevelAccessor level, BlockPos pos, boolean postProcess) {
/*  45 */     return (Long)Direction.stream()
/*  46 */       .filter(faceDirection -> this.config.canSpreadFrom(state, state))
/*  47 */       .map(faceDirection -> spreadFromFaceTowardAllDirections(state, state, level, postProcess, pos))
/*  48 */       .reduce(0L, Long::sum);
/*     */   }
/*     */   
/*     */   public Optional<SpreadPos> spreadFromFaceTowardRandomDirection(BlockState state, LevelAccessor level, BlockPos pos, Direction startingFace, RandomSource random, boolean postProcess) {
/*  52 */     return Direction.allShuffled(random).stream()
/*  53 */       .map(spreadDirection -> spreadFromFaceTowardDirection(state, state, level, pos, postProcess, startingFace))
/*  54 */       .filter(Optional::isPresent)
/*  55 */       .findFirst()
/*  56 */       .orElse(Optional.empty());
/*     */   }
/*     */   
/*     */   private long spreadFromFaceTowardAllDirections(BlockState state, LevelAccessor level, BlockPos pos, Direction startingFace, boolean postProcess) {
/*  60 */     return Direction.stream()
/*  61 */       .map(spreadDirection -> spreadFromFaceTowardDirection(state, state, level, pos, postProcess, startingFace))
/*  62 */       .filter(Optional::isPresent).count();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Optional<SpreadPos> spreadFromFaceTowardDirection(BlockState state, LevelAccessor level, BlockPos pos, Direction fromFace, Direction spreadDirection, boolean postProcess) {
/*  67 */     Objects.requireNonNull(this.config); return getSpreadFromFaceTowardDirection(state, (BlockGetter)level, pos, fromFace, spreadDirection, this.config::canSpreadInto)
/*  68 */       .flatMap(spreadPos -> spreadToFace(level, postProcess, level));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SpreadPos> getSpreadFromFaceTowardDirection(BlockState state, BlockGetter level, BlockPos pos, Direction startingFace, Direction spreadDirection, SpreadPredicate canSpreadInto) {
/*  73 */     if (spreadDirection.getAxis() == startingFace.getAxis()) {
/*  74 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (!this.config.isOtherBlockValidAsSource(state) && (!this.config.hasFace(state, startingFace) || this.config.hasFace(state, spreadDirection))) {
/*  79 */       return Optional.empty();
/*     */     }
/*  81 */     for (SpreadType type : this.config.getSpreadTypes()) {
/*  82 */       SpreadPos spreadPos = type.getSpreadPos(pos, spreadDirection, startingFace);
/*  83 */       if (canSpreadInto.test(level, pos, spreadPos)) {
/*  84 */         return Optional.of(spreadPos);
/*     */       }
/*     */     } 
/*  87 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<SpreadPos> spreadToFace(LevelAccessor level, SpreadPos spreadPos, boolean postProcess) {
/*  91 */     BlockState oldState = level.getBlockState(spreadPos.pos());
/*  92 */     if (this.config.placeBlock(level, spreadPos, oldState, postProcess)) {
/*  93 */       return Optional.of(spreadPos);
/*     */     }
/*  95 */     return Optional.empty();
/*     */   } public static final class SpreadPos extends Record {
/*     */     private final BlockPos pos; private final Direction face;
/*  98 */     public SpreadPos(BlockPos pos, Direction face) { this.pos = pos; this.face = face; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;
/*  98 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; } public Direction face() { return this.face; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface SpreadConfig
/*     */   {
/*     */     BlockState getStateForPlacement(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, Direction param1Direction);
/*     */ 
/*     */     
/*     */     boolean canSpreadInto(BlockGetter param1BlockGetter, BlockPos param1BlockPos, MultifaceSpreader.SpreadPos param1SpreadPos);
/*     */     
/*     */     default MultifaceSpreader.SpreadType[] getSpreadTypes() {
/* 111 */       return MultifaceSpreader.DEFAULT_SPREAD_ORDER;
/*     */     }
/*     */     
/*     */     default boolean hasFace(BlockState state, Direction face) {
/* 115 */       return MultifaceBlock.hasFace(state, face);
/*     */     }
/*     */     
/*     */     default boolean isOtherBlockValidAsSource(BlockState state) {
/* 119 */       return false;
/*     */     }
/*     */     
/*     */     default boolean canSpreadFrom(BlockState state, Direction face) {
/* 123 */       return (isOtherBlockValidAsSource(state) || hasFace(state, face));
/*     */     }
/*     */     
/*     */     default boolean placeBlock(LevelAccessor level, MultifaceSpreader.SpreadPos spreadPos, BlockState oldState, boolean postProcess) {
/* 127 */       BlockState spreadState = getStateForPlacement(oldState, (BlockGetter)level, spreadPos.pos(), spreadPos.face());
/* 128 */       if (spreadState != null) {
/*     */         
/* 130 */         if (postProcess) {
/* 131 */           level.getChunk(spreadPos.pos()).markPosForPostprocessing(spreadPos.pos());
/*     */         }
/* 133 */         return level.setBlock(spreadPos.pos(), spreadState, 2);
/*     */       } 
/* 135 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DefaultSpreaderConfig implements SpreadConfig {
/*     */     protected MultifaceBlock block;
/*     */     
/*     */     public DefaultSpreaderConfig(MultifaceBlock block) {
/* 143 */       this.block = block;
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockState getStateForPlacement(BlockState oldState, BlockGetter level, BlockPos placementPos, Direction placementDirection) {
/* 148 */       return this.block.getStateForPlacement(oldState, level, placementPos, placementDirection);
/*     */     }
/*     */     
/*     */     protected boolean stateCanBeReplaced(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState) {
/* 152 */       return (existingState.isAir() || existingState.is(this.block) || (existingState.is(Blocks.WATER) && existingState.getFluidState().isSource()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canSpreadInto(BlockGetter level, BlockPos sourcePos, MultifaceSpreader.SpreadPos spreadPos) {
/* 157 */       BlockState existingState = level.getBlockState(spreadPos.pos());
/* 158 */       return (stateCanBeReplaced(level, sourcePos, spreadPos.pos(), spreadPos.face(), existingState) && this.block.isValidStateForPlacement(level, existingState, spreadPos.pos(), spreadPos.face()));
/*     */     }
/*     */   }
/*     */   
/*     */   public enum SpreadType {
/* 163 */     SAME_POSITION
/*     */     {
/*     */       public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) {
/* 166 */         return new MultifaceSpreader.SpreadPos(pos, spreadDirection);
/*     */       }
/*     */     },
/* 169 */     SAME_PLANE
/*     */     {
/*     */       public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) {
/* 172 */         return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection), fromFace);
/*     */       }
/*     */     },
/* 175 */     WRAP_AROUND
/*     */     {
/*     */       public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) {
/* 178 */         return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection).relative(fromFace), spreadDirection.getOpposite()); } }; public abstract MultifaceSpreader.SpreadPos getSpreadPos(BlockPos param1BlockPos, Direction param1Direction1, Direction param1Direction2); } enum null { public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) { return new MultifaceSpreader.SpreadPos(pos, spreadDirection); } } enum null { public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) { return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection), fromFace); } } enum null { public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) { return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection).relative(fromFace), spreadDirection.getOpposite()); }
/*     */      }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SpreadPredicate {
/*     */     boolean test(BlockGetter param1BlockGetter, BlockPos param1BlockPos, MultifaceSpreader.SpreadPos param1SpreadPos);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MultifaceSpreader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */