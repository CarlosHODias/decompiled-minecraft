/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChorusFlowerBlock extends Block {
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("plant").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ChorusFlowerBlock::new));
/*     */   }
/*     */   
/*     */   public static final com.mojang.serialization.MapCodec<ChorusFlowerBlock> CODEC;
/*     */   public static final int DEAD_AGE = 5;
/*     */   
/*     */   public com.mojang.serialization.MapCodec<ChorusFlowerBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  36 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_5;
/*     */   
/*  38 */   private static final VoxelShape SHAPE_BLOCK_SUPPORT = Block.column(14.0D, 0.0D, 15.0D);
/*     */   
/*     */   private final Block plant;
/*     */   
/*     */   protected ChorusFlowerBlock(Block plant, BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     this.plant = plant;
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  50 */     if (!state.canSurvive((LevelReader)level, pos)) {
/*  51 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  57 */     return ((Integer)state.getValue((Property)AGE) < 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  62 */     return SHAPE_BLOCK_SUPPORT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*     */     // Byte code:
/*     */     //   0: aload_3
/*     */     //   1: invokevirtual above : ()Lnet/minecraft/core/BlockPos;
/*     */     //   4: astore #5
/*     */     //   6: aload_2
/*     */     //   7: aload #5
/*     */     //   9: invokevirtual isEmptyBlock : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   12: ifeq -> 27
/*     */     //   15: aload #5
/*     */     //   17: invokevirtual getY : ()I
/*     */     //   20: aload_2
/*     */     //   21: invokevirtual getMaxY : ()I
/*     */     //   24: if_icmple -> 28
/*     */     //   27: return
/*     */     //   28: aload_1
/*     */     //   29: getstatic net/minecraft/world/level/block/ChorusFlowerBlock.AGE : Lnet/minecraft/world/level/block/state/properties/IntegerProperty;
/*     */     //   32: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   35: checkcast java/lang/Integer
/*     */     //   38: invokevirtual intValue : ()I
/*     */     //   41: istore #6
/*     */     //   43: iload #6
/*     */     //   45: iconst_5
/*     */     //   46: if_icmplt -> 50
/*     */     //   49: return
/*     */     //   50: iconst_0
/*     */     //   51: istore #7
/*     */     //   53: iconst_0
/*     */     //   54: istore #8
/*     */     //   56: aload_2
/*     */     //   57: aload_3
/*     */     //   58: invokevirtual below : ()Lnet/minecraft/core/BlockPos;
/*     */     //   61: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   64: astore #9
/*     */     //   66: aload #9
/*     */     //   68: getstatic net/minecraft/world/level/block/Blocks.END_STONE : Lnet/minecraft/world/level/block/Block;
/*     */     //   71: invokevirtual is : (Lnet/minecraft/world/level/block/Block;)Z
/*     */     //   74: ifeq -> 83
/*     */     //   77: iconst_1
/*     */     //   78: istore #7
/*     */     //   80: goto -> 207
/*     */     //   83: aload #9
/*     */     //   85: aload_0
/*     */     //   86: getfield plant : Lnet/minecraft/world/level/block/Block;
/*     */     //   89: invokevirtual is : (Lnet/minecraft/world/level/block/Block;)Z
/*     */     //   92: ifeq -> 196
/*     */     //   95: iconst_1
/*     */     //   96: istore #10
/*     */     //   98: iconst_0
/*     */     //   99: istore #11
/*     */     //   101: iload #11
/*     */     //   103: iconst_4
/*     */     //   104: if_icmpge -> 162
/*     */     //   107: aload_2
/*     */     //   108: aload_3
/*     */     //   109: iload #10
/*     */     //   111: iconst_1
/*     */     //   112: iadd
/*     */     //   113: invokevirtual below : (I)Lnet/minecraft/core/BlockPos;
/*     */     //   116: invokevirtual getBlockState : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   119: astore #12
/*     */     //   121: aload #12
/*     */     //   123: aload_0
/*     */     //   124: getfield plant : Lnet/minecraft/world/level/block/Block;
/*     */     //   127: invokevirtual is : (Lnet/minecraft/world/level/block/Block;)Z
/*     */     //   130: ifeq -> 139
/*     */     //   133: iinc #10, 1
/*     */     //   136: goto -> 156
/*     */     //   139: aload #12
/*     */     //   141: getstatic net/minecraft/world/level/block/Blocks.END_STONE : Lnet/minecraft/world/level/block/Block;
/*     */     //   144: invokevirtual is : (Lnet/minecraft/world/level/block/Block;)Z
/*     */     //   147: ifeq -> 162
/*     */     //   150: iconst_1
/*     */     //   151: istore #8
/*     */     //   153: goto -> 162
/*     */     //   156: iinc #11, 1
/*     */     //   159: goto -> 101
/*     */     //   162: iload #10
/*     */     //   164: iconst_2
/*     */     //   165: if_icmplt -> 190
/*     */     //   168: iload #10
/*     */     //   170: aload #4
/*     */     //   172: iload #8
/*     */     //   174: ifeq -> 181
/*     */     //   177: iconst_5
/*     */     //   178: goto -> 182
/*     */     //   181: iconst_4
/*     */     //   182: invokeinterface nextInt : (I)I
/*     */     //   187: if_icmpgt -> 193
/*     */     //   190: iconst_1
/*     */     //   191: istore #7
/*     */     //   193: goto -> 207
/*     */     //   196: aload #9
/*     */     //   198: invokevirtual isAir : ()Z
/*     */     //   201: ifeq -> 207
/*     */     //   204: iconst_1
/*     */     //   205: istore #7
/*     */     //   207: iload #7
/*     */     //   209: ifeq -> 265
/*     */     //   212: aload_2
/*     */     //   213: aload #5
/*     */     //   215: aconst_null
/*     */     //   216: invokestatic allNeighborsEmpty : (Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z
/*     */     //   219: ifeq -> 265
/*     */     //   222: aload_2
/*     */     //   223: aload_3
/*     */     //   224: iconst_2
/*     */     //   225: invokevirtual above : (I)Lnet/minecraft/core/BlockPos;
/*     */     //   228: invokevirtual isEmptyBlock : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   231: ifeq -> 265
/*     */     //   234: aload_2
/*     */     //   235: aload_3
/*     */     //   236: aload_2
/*     */     //   237: aload_3
/*     */     //   238: aload_0
/*     */     //   239: getfield plant : Lnet/minecraft/world/level/block/Block;
/*     */     //   242: invokevirtual defaultBlockState : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   245: invokestatic getStateWithConnections : (Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   248: iconst_2
/*     */     //   249: invokevirtual setBlock : (Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
/*     */     //   252: pop
/*     */     //   253: aload_0
/*     */     //   254: aload_2
/*     */     //   255: aload #5
/*     */     //   257: iload #6
/*     */     //   259: invokevirtual placeGrownFlower : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;I)V
/*     */     //   262: goto -> 417
/*     */     //   265: iload #6
/*     */     //   267: iconst_4
/*     */     //   268: if_icmpge -> 411
/*     */     //   271: aload #4
/*     */     //   273: iconst_4
/*     */     //   274: invokeinterface nextInt : (I)I
/*     */     //   279: istore #10
/*     */     //   281: iload #8
/*     */     //   283: ifeq -> 289
/*     */     //   286: iinc #10, 1
/*     */     //   289: iconst_0
/*     */     //   290: istore #11
/*     */     //   292: iconst_0
/*     */     //   293: istore #12
/*     */     //   295: iload #12
/*     */     //   297: iload #10
/*     */     //   299: if_icmpge -> 375
/*     */     //   302: getstatic net/minecraft/core/Direction$Plane.HORIZONTAL : Lnet/minecraft/core/Direction$Plane;
/*     */     //   305: aload #4
/*     */     //   307: invokevirtual getRandomDirection : (Lnet/minecraft/util/RandomSource;)Lnet/minecraft/core/Direction;
/*     */     //   310: astore #13
/*     */     //   312: aload_3
/*     */     //   313: aload #13
/*     */     //   315: invokevirtual relative : (Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;
/*     */     //   318: astore #14
/*     */     //   320: aload_2
/*     */     //   321: aload #14
/*     */     //   323: invokevirtual isEmptyBlock : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   326: ifeq -> 369
/*     */     //   329: aload_2
/*     */     //   330: aload #14
/*     */     //   332: invokevirtual below : ()Lnet/minecraft/core/BlockPos;
/*     */     //   335: invokevirtual isEmptyBlock : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   338: ifeq -> 369
/*     */     //   341: aload_2
/*     */     //   342: aload #14
/*     */     //   344: aload #13
/*     */     //   346: invokevirtual getOpposite : ()Lnet/minecraft/core/Direction;
/*     */     //   349: invokestatic allNeighborsEmpty : (Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z
/*     */     //   352: ifeq -> 369
/*     */     //   355: aload_0
/*     */     //   356: aload_2
/*     */     //   357: aload #14
/*     */     //   359: iload #6
/*     */     //   361: iconst_1
/*     */     //   362: iadd
/*     */     //   363: invokevirtual placeGrownFlower : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;I)V
/*     */     //   366: iconst_1
/*     */     //   367: istore #11
/*     */     //   369: iinc #12, 1
/*     */     //   372: goto -> 295
/*     */     //   375: iload #11
/*     */     //   377: ifeq -> 402
/*     */     //   380: aload_2
/*     */     //   381: aload_3
/*     */     //   382: aload_2
/*     */     //   383: aload_3
/*     */     //   384: aload_0
/*     */     //   385: getfield plant : Lnet/minecraft/world/level/block/Block;
/*     */     //   388: invokevirtual defaultBlockState : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   391: invokestatic getStateWithConnections : (Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   394: iconst_2
/*     */     //   395: invokevirtual setBlock : (Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
/*     */     //   398: pop
/*     */     //   399: goto -> 408
/*     */     //   402: aload_0
/*     */     //   403: aload_2
/*     */     //   404: aload_3
/*     */     //   405: invokevirtual placeDeadFlower : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   408: goto -> 417
/*     */     //   411: aload_0
/*     */     //   412: aload_2
/*     */     //   413: aload_3
/*     */     //   414: invokevirtual placeDeadFlower : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   417: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #68	-> 0
/*     */     //   #69	-> 6
/*     */     //   #70	-> 27
/*     */     //   #73	-> 28
/*     */     //   #74	-> 43
/*     */     //   #75	-> 49
/*     */     //   #78	-> 50
/*     */     //   #79	-> 53
/*     */     //   #81	-> 56
/*     */     //   #82	-> 66
/*     */     //   #83	-> 77
/*     */     //   #84	-> 83
/*     */     //   #85	-> 95
/*     */     //   #86	-> 98
/*     */     //   #87	-> 107
/*     */     //   #88	-> 121
/*     */     //   #89	-> 133
/*     */     //   #91	-> 139
/*     */     //   #92	-> 150
/*     */     //   #86	-> 156
/*     */     //   #97	-> 162
/*     */     //   #98	-> 190
/*     */     //   #100	-> 193
/*     */     //   #101	-> 204
/*     */     //   #104	-> 207
/*     */     //   #105	-> 234
/*     */     //   #106	-> 253
/*     */     //   #107	-> 265
/*     */     //   #108	-> 271
/*     */     //   #109	-> 281
/*     */     //   #110	-> 286
/*     */     //   #113	-> 289
/*     */     //   #114	-> 292
/*     */     //   #115	-> 302
/*     */     //   #116	-> 312
/*     */     //   #117	-> 320
/*     */     //   #118	-> 355
/*     */     //   #119	-> 366
/*     */     //   #114	-> 369
/*     */     //   #123	-> 375
/*     */     //   #124	-> 380
/*     */     //   #126	-> 402
/*     */     //   #128	-> 408
/*     */     //   #129	-> 411
/*     */     //   #131	-> 417
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   121	35	12	testState	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   101	61	11	i	I
/*     */     //   98	95	10	height	I
/*     */     //   312	57	13	direction	Lnet/minecraft/core/Direction;
/*     */     //   320	49	14	target	Lnet/minecraft/core/BlockPos;
/*     */     //   295	80	12	i	I
/*     */     //   281	127	10	numBranchAttempts	I
/*     */     //   292	116	11	createdBranch	Z
/*     */     //   0	418	0	this	Lnet/minecraft/world/level/block/ChorusFlowerBlock;
/*     */     //   0	418	1	state	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   0	418	2	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	418	3	pos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	418	4	random	Lnet/minecraft/util/RandomSource;
/*     */     //   6	412	5	above	Lnet/minecraft/core/BlockPos;
/*     */     //   43	375	6	currentAge	I
/*     */     //   53	365	7	growUpwards	Z
/*     */     //   56	362	8	pillarOnEndStone	Z
/*     */     //   66	352	9	belowState	Lnet/minecraft/world/level/block/state/BlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void placeGrownFlower(Level level, BlockPos pos, int age) {
/* 134 */     level.setBlock(pos, (BlockState)defaultBlockState().setValue((Property)AGE, age), 2);
/* 135 */     level.levelEvent(1033, pos, 0);
/*     */   }
/*     */   
/*     */   private void placeDeadFlower(Level level, BlockPos pos) {
/* 139 */     level.setBlock(pos, (BlockState)defaultBlockState().setValue((Property)AGE, 5), 2);
/* 140 */     level.levelEvent(1034, pos, 0);
/*     */   }
/*     */   
/*     */   private static boolean allNeighborsEmpty(LevelReader level, BlockPos pos, Direction ignore) {
/* 144 */     for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 145 */       if (direction != ignore && !level.isEmptyBlock(pos.relative(direction))) {
/* 146 */         return false;
/*     */       }
/*     */     } 
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 154 */     if (directionToNeighbour != Direction.UP && !state.canSurvive(level, pos)) {
/* 155 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*     */     
/* 158 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 163 */     BlockState belowState = level.getBlockState(pos.below());
/* 164 */     if (belowState.is(this.plant) || belowState.is(Blocks.END_STONE)) {
/* 165 */       return true;
/*     */     }
/* 167 */     if (!belowState.isAir()) {
/* 168 */       return false;
/*     */     }
/*     */     
/*     */     boolean oneNeighbor = false;
/* 172 */     for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 173 */       BlockState neighbor = level.getBlockState(pos.relative(direction));
/* 174 */       if (neighbor.is(this.plant)) {
/* 175 */         if (oneNeighbor) {
/* 176 */           return false;
/*     */         }
/* 178 */         oneNeighbor = true; continue;
/* 179 */       }  if (!neighbor.isAir()) {
/* 180 */         return false;
/*     */       }
/*     */     } 
/* 183 */     return oneNeighbor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 188 */     builder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   public static void generatePlant(LevelAccessor level, BlockPos target, RandomSource random, int maxHorizontalSpread) {
/* 192 */     level.setBlock(target, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target, Blocks.CHORUS_PLANT.defaultBlockState()), 2);
/* 193 */     growTreeRecursive(level, target, random, target, maxHorizontalSpread, 0);
/*     */   }
/*     */   
/*     */   private static void growTreeRecursive(LevelAccessor level, BlockPos current, RandomSource random, BlockPos startPos, int maxHorizontalSpread, int depth) {
/* 197 */     Block chorus = Blocks.CHORUS_PLANT;
/*     */     
/* 199 */     int height = random.nextInt(4) + 1;
/* 200 */     if (depth == 0) {
/* 201 */       height++;
/*     */     }
/*     */     
/* 204 */     for (int i = 0; i < height; i++) {
/* 205 */       BlockPos target = current.above(i + 1);
/* 206 */       if (!allNeighborsEmpty((LevelReader)level, target, null)) {
/*     */         return;
/*     */       }
/*     */       
/* 210 */       level.setBlock(target, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target, chorus.defaultBlockState()), 2);
/* 211 */       level.setBlock(target.below(), ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target.below(), chorus.defaultBlockState()), 2);
/*     */     } 
/*     */     
/*     */     boolean placedStem = false;
/* 215 */     if (depth < 4) {
/* 216 */       int stems = random.nextInt(4);
/* 217 */       if (depth == 0) {
/* 218 */         stems++;
/*     */       }
/* 220 */       for (int j = 0; j < stems; j++) {
/* 221 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 222 */         BlockPos target = current.above(height).relative(direction);
/* 223 */         if (Math.abs(target.getX() - startPos.getX()) < maxHorizontalSpread && Math.abs(target.getZ() - startPos.getZ()) < maxHorizontalSpread)
/*     */         {
/*     */           
/* 226 */           if (level.isEmptyBlock(target) && level.isEmptyBlock(target.below()) && allNeighborsEmpty((LevelReader)level, target, direction.getOpposite())) {
/* 227 */             placedStem = true;
/* 228 */             level.setBlock(target, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target, chorus.defaultBlockState()), 2);
/* 229 */             level.setBlock(target.relative(direction.getOpposite()), ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target.relative(direction.getOpposite()), chorus.defaultBlockState()), 2);
/* 230 */             growTreeRecursive(level, target, random, startPos, maxHorizontalSpread, depth + 1);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 235 */     if (!placedStem) {
/* 236 */       level.setBlock(current.above(height), (BlockState)Blocks.CHORUS_FLOWER.defaultBlockState().setValue((Property)AGE, 5), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 242 */     BlockPos pos = blockHit.getBlockPos();
/* 243 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (projectile.mayInteract(serverLevel, pos) && projectile.mayBreak(serverLevel))
/* 244 */         level.destroyBlock(pos, true, (net.minecraft.world.entity.Entity)projectile);  }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ChorusFlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */