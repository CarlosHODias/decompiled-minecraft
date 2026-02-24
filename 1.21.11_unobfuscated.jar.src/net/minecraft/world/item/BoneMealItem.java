/*     */ package net.minecraft.world.item;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.BaseCoralWallFanBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BonemealableBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ 
/*     */ public class BoneMealItem extends Item {
/*     */   public static final int GRASS_SPREAD_WIDTH = 3;
/*     */   
/*     */   public BoneMealItem(Item.Properties properties) {
/*  34 */     super(properties);
/*     */   }
/*     */   public static final int GRASS_SPREAD_HEIGHT = 1; public static final int GRASS_COUNT_MULTIPLIER = 3;
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  39 */     Level level = context.getLevel();
/*  40 */     BlockPos pos = context.getClickedPos();
/*  41 */     BlockPos relative = pos.relative(context.getClickedFace());
/*     */ 
/*     */     
/*  44 */     ItemStack boneMealStack = context.getItemInHand();
/*  45 */     if (growCrop(boneMealStack, level, pos)) {
/*  46 */       if (!level.isClientSide()) {
/*  47 */         boneMealStack.causeUseVibration((Entity)context.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
/*  48 */         level.levelEvent(1505, pos, 15);
/*     */       } 
/*  50 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/*  54 */     BlockState clickedState = level.getBlockState(pos);
/*  55 */     boolean solidBlockFace = clickedState.isFaceSturdy((BlockGetter)level, pos, context.getClickedFace());
/*  56 */     if (solidBlockFace && 
/*  57 */       growWaterPlant(boneMealStack, level, relative, context.getClickedFace())) {
/*  58 */       if (!level.isClientSide()) {
/*  59 */         boneMealStack.causeUseVibration((Entity)context.getPlayer(), GameEvent.ITEM_INTERACT_FINISH);
/*  60 */         level.levelEvent(1505, relative, 15);
/*     */       } 
/*  62 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/*  66 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static boolean growCrop(ItemStack itemStack, Level level, BlockPos pos) {
/*  70 */     BlockState state = level.getBlockState(pos);
/*     */     
/*  72 */     Block block = state.getBlock(); if (block instanceof BonemealableBlock) { BonemealableBlock bonemealableBlock = (BonemealableBlock)block;
/*     */       
/*  74 */       if (bonemealableBlock.isValidBonemealTarget((LevelReader)level, pos, state)) {
/*  75 */         if (level instanceof ServerLevel) {
/*  76 */           if (bonemealableBlock.isBonemealSuccess(level, level.random, pos, state)) {
/*  77 */             bonemealableBlock.performBonemeal((ServerLevel)level, level.random, pos, state);
/*     */           }
/*  79 */           itemStack.shrink(1);
/*     */         } 
/*  81 */         return true;
/*     */       }  }
/*     */     
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean growWaterPlant(ItemStack itemStack, Level level, BlockPos pos, Direction clickedFace) {
/*  88 */     if (!level.getBlockState(pos).is(Blocks.WATER) || level.getFluidState(pos).getAmount() != 8) {
/*  89 */       return false;
/*     */     }
/*     */     
/*  92 */     if (!(level instanceof ServerLevel)) {
/*  93 */       return true;
/*     */     }
/*     */     
/*  96 */     RandomSource random = level.getRandom();
/*     */     
/*     */     int j;
/*  99 */     label49: for (j = 0; j < 128; j++) {
/* 100 */       BlockPos testPos = pos;
/* 101 */       BlockState stateToGrow = Blocks.SEAGRASS.defaultBlockState();
/*     */       
/* 103 */       for (int i = 0; i < j / 16; i++) {
/* 104 */         testPos = testPos.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
/*     */         
/* 106 */         if (level.getBlockState(testPos).isCollisionShapeFullBlock((BlockGetter)level, testPos)) {
/*     */           continue label49;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 112 */       Holder<Biome> testBiome = level.getBiome(testPos);
/* 113 */       if (testBiome.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
/* 114 */         if (j == 0 && clickedFace != null && clickedFace.getAxis().isHorizontal()) {
/*     */           
/* 116 */           stateToGrow = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.WALL_CORALS, level.random).map(h -> ((Block)h.value()).defaultBlockState()).orElse(stateToGrow);
/* 117 */           if (stateToGrow.hasProperty((Property)BaseCoralWallFanBlock.FACING)) {
/* 118 */             stateToGrow = (BlockState)stateToGrow.setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)clickedFace);
/*     */           }
/* 120 */         } else if (random.nextInt(4) == 0) {
/* 121 */           stateToGrow = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.UNDERWATER_BONEMEALS, level.random).map(h -> ((Block)h.value()).defaultBlockState()).orElse(stateToGrow);
/*     */         } 
/*     */       }
/*     */       
/* 125 */       if (stateToGrow.is(BlockTags.WALL_CORALS, s -> s.hasProperty((Property)BaseCoralWallFanBlock.FACING))) {
/* 126 */         int d = 0;
/* 127 */         while (!stateToGrow.canSurvive((LevelReader)level, testPos) && d < 4) {
/* 128 */           stateToGrow = (BlockState)stateToGrow.setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)Direction.Plane.HORIZONTAL.getRandomDirection(random));
/* 129 */           d++;
/*     */         } 
/*     */       } 
/*     */       
/* 133 */       if (stateToGrow.canSurvive((LevelReader)level, testPos)) {
/*     */ 
/*     */ 
/*     */         
/* 137 */         BlockState testState = level.getBlockState(testPos);
/* 138 */         if (testState.is(Blocks.WATER) && level.getFluidState(testPos).getAmount() == 8) {
/* 139 */           level.setBlock(testPos, stateToGrow, 3);
/*     */         
/*     */         }
/* 142 */         else if (testState.is(Blocks.SEAGRASS) && ((BonemealableBlock)Blocks.SEAGRASS).isValidBonemealTarget((LevelReader)level, testPos, testState) && random.nextInt(10) == 0) {
/* 143 */           ((BonemealableBlock)Blocks.SEAGRASS).performBonemeal((ServerLevel)level, random, testPos, testState);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     itemStack.shrink(1);
/* 149 */     return true;
/*     */   }
/*     */   
/*     */   public static void addGrowthParticles(LevelAccessor level, BlockPos pos, int count) {
/* 153 */     BlockState blockState = level.getBlockState(pos);
/* 154 */     Block block = blockState.getBlock(); if (block instanceof BonemealableBlock) { BonemealableBlock bonemealableBlock = (BonemealableBlock)block;
/* 155 */       BlockPos particlePos = bonemealableBlock.getParticlePos(pos);
/* 156 */       switch (bonemealableBlock.getType()) {
/*     */         case NEIGHBOR_SPREADER:
/* 158 */           ParticleUtils.spawnParticles(level, particlePos, count * 3, 3.0D, 1.0D, false, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER); break;
/*     */         case GROWER:
/* 160 */           ParticleUtils.spawnParticleInBlock(level, particlePos, count, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER); break;
/*     */       }  }
/* 162 */     else if (blockState.is(Blocks.WATER))
/* 163 */     { ParticleUtils.spawnParticles(level, pos, count * 3, 3.0D, 1.0D, false, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/BoneMealItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */