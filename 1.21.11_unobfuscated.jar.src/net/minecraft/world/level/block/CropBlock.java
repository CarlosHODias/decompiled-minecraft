/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CropBlock extends VegetationBlock implements BonemealableBlock {
/*  26 */   public static final MapCodec<CropBlock> CODEC = simpleCodec(CropBlock::new);
/*     */   public static final int MAX_AGE = 7;
/*     */   
/*     */   public MapCodec<? extends CropBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  34 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7; private static final VoxelShape[] SHAPES;
/*     */   static {
/*  36 */     SHAPES = Block.boxes(7, age -> Block.column(16.0D, 0.0D, (2 + age * 2)));
/*     */   }
/*     */   protected CropBlock(BlockBehaviour.Properties properties) {
/*  39 */     super(properties);
/*  40 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)getAgeProperty(), 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  45 */     return SHAPES[getAge(state)];
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  50 */     return state.is(Blocks.FARMLAND);
/*     */   }
/*     */   
/*     */   protected IntegerProperty getAgeProperty() {
/*  54 */     return AGE;
/*     */   }
/*     */   
/*     */   public int getMaxAge() {
/*  58 */     return 7;
/*     */   }
/*     */   
/*     */   public int getAge(BlockState state) {
/*  62 */     return (Integer)state.getValue((Property)getAgeProperty());
/*     */   }
/*     */   
/*     */   public BlockState getStateForAge(int age) {
/*  66 */     return (BlockState)defaultBlockState().setValue((Property)getAgeProperty(), age);
/*     */   }
/*     */   
/*     */   public final boolean isMaxAge(BlockState state) {
/*  70 */     return (getAge(state) >= getMaxAge());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  75 */     return !isMaxAge(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  80 */     if (level.getRawBrightness(pos, 0) >= 9) {
/*  81 */       int age = getAge(state);
/*  82 */       if (age < getMaxAge()) {
/*  83 */         float growthSpeed = getGrowthSpeed(this, (BlockGetter)level, pos);
/*     */         
/*  85 */         if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
/*  86 */           level.setBlock(pos, getStateForAge(age + 1), 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void growCrops(Level level, BlockPos pos, BlockState state) {
/*  93 */     int age = Math.min(getMaxAge(), getAge(state) + getBonemealAgeIncrease(level));
/*  94 */     level.setBlock(pos, getStateForAge(age), 2);
/*     */   }
/*     */   
/*     */   protected int getBonemealAgeIncrease(Level level) {
/*  98 */     return Mth.nextInt(level.random, 2, 5);
/*     */   }
/*     */   
/*     */   protected static float getGrowthSpeed(Block type, BlockGetter level, BlockPos pos) {
/* 102 */     float speed = 1.0F;
/*     */     
/* 104 */     BlockPos below = pos.below();
/* 105 */     for (int xx = -1; xx <= 1; xx++) {
/* 106 */       for (int zz = -1; zz <= 1; zz++) {
/* 107 */         float blockSpeed = 0.0F;
/*     */         
/* 109 */         BlockState blockState = level.getBlockState(below.offset(xx, 0, zz));
/* 110 */         if (blockState.is(Blocks.FARMLAND)) {
/* 111 */           blockSpeed = 1.0F;
/* 112 */           if ((Integer)blockState.getValue((Property)FarmBlock.MOISTURE) > 0) {
/* 113 */             blockSpeed = 3.0F;
/*     */           }
/*     */         } 
/*     */         
/* 117 */         if (xx != 0 || zz != 0) {
/* 118 */           blockSpeed /= 4.0F;
/*     */         }
/*     */         
/* 121 */         speed += blockSpeed;
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     BlockPos north = pos.north();
/* 126 */     BlockPos south = pos.south();
/* 127 */     BlockPos west = pos.west();
/* 128 */     BlockPos east = pos.east();
/*     */     
/* 130 */     boolean horizontal = (level.getBlockState(west).is(type) || level.getBlockState(east).is(type));
/* 131 */     boolean vertical = (level.getBlockState(north).is(type) || level.getBlockState(south).is(type));
/*     */     
/* 133 */     if (horizontal && vertical) {
/* 134 */       speed /= 2.0F;
/*     */     } else {
/*     */       
/* 137 */       boolean diagonal = (level.getBlockState(west.north()).is(type) || 
/* 138 */         level.getBlockState(east.north()).is(type) || 
/* 139 */         level.getBlockState(east.south()).is(type) || 
/* 140 */         level.getBlockState(west.south()).is(type));
/*     */       
/* 142 */       if (diagonal) {
/* 143 */         speed /= 2.0F;
/*     */       }
/*     */     } 
/*     */     
/* 147 */     return speed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 152 */     return (hasSufficientLight(level, pos) && super.canSurvive(state, level, pos));
/*     */   }
/*     */   
/*     */   protected static boolean hasSufficientLight(LevelReader level, BlockPos pos) {
/* 156 */     return (level.getRawBrightness(pos, 0) >= 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 161 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (entity instanceof net.minecraft.world.entity.monster.Ravager && (Boolean)serverLevel.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.MOB_GRIEFING))
/* 162 */         serverLevel.destroyBlock(pos, true, entity);  }
/*     */     
/* 164 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemLike getBaseSeedId() {
/* 169 */     return (ItemLike)Items.WHEAT_SEEDS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 174 */     return new ItemStack(getBaseSeedId());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 179 */     return !isMaxAge(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 184 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 189 */     growCrops((Level)level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 194 */     builder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CropBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */