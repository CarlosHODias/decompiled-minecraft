/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TurtleEggBlock
/*     */   extends Block {
/*  36 */   public static final MapCodec<TurtleEggBlock> CODEC = simpleCodec(TurtleEggBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<TurtleEggBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
/*  44 */   public static final IntegerProperty EGGS = BlockStateProperties.EGGS;
/*     */   
/*     */   public static final int MAX_HATCH_LEVEL = 2;
/*     */   
/*     */   public static final int MIN_EGGS = 1;
/*     */   public static final int MAX_EGGS = 4;
/*  50 */   private static final VoxelShape SHAPE_SINGLE = Block.box(3.0D, 0.0D, 3.0D, 12.0D, 7.0D, 12.0D);
/*  51 */   private static final VoxelShape SHAPE_MULTIPLE = Block.column(14.0D, 0.0D, 7.0D);
/*     */   
/*     */   public TurtleEggBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HATCH, 0)).setValue((Property)EGGS, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/*  60 */     if (!entity.isSteppingCarefully()) {
/*  61 */       destroyEgg(level, onState, pos, entity, 100);
/*     */     }
/*  63 */     super.stepOn(level, pos, onState, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/*  68 */     if (!(entity instanceof net.minecraft.world.entity.monster.zombie.Zombie)) {
/*  69 */       destroyEgg(level, state, pos, entity, 3);
/*     */     }
/*     */     
/*  72 */     super.fallOn(level, state, pos, entity, fallDistance);
/*     */   }
/*     */   
/*     */   private void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity, int randomness) {
/*  76 */     if (state.is(Blocks.TURTLE_EGG) && level instanceof ServerLevel) {
/*  77 */       ServerLevel serverLevel = (ServerLevel)level;
/*  78 */       if (canDestroyEgg(serverLevel, entity) && 
/*  79 */         level.random.nextInt(randomness) == 0)
/*     */       {
/*  81 */         decreaseEggs((Level)serverLevel, pos, state); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void decreaseEggs(Level level, BlockPos pos, BlockState state) {
/*  86 */     level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
/*  87 */     int numberOfEggs = (Integer)state.getValue((Property)EGGS);
/*  88 */     if (numberOfEggs <= 1) {
/*     */       
/*  90 */       level.destroyBlock(pos, false);
/*     */     } else {
/*     */       
/*  93 */       level.setBlock(pos, (BlockState)state.setValue((Property)EGGS, numberOfEggs - 1), 2);
/*  94 */       level.gameEvent((Holder)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
/*  95 */       level.levelEvent(2001, pos, Block.getId(state));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 101 */     if (shouldUpdateHatchLevel((Level)level, pos) && onSand((BlockGetter)level, pos)) {
/* 102 */       int hatch = (Integer)state.getValue((Property)HATCH);
/* 103 */       if (hatch < 2) {
/* 104 */         level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
/* 105 */         level.setBlock(pos, (BlockState)state.setValue((Property)HATCH, hatch + 1), 2);
/* 106 */         level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
/*     */       } else {
/*     */         
/* 109 */         level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
/* 110 */         level.removeBlock(pos, false);
/* 111 */         level.gameEvent((Holder)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
/*     */         
/* 113 */         for (int i = 0; i < (Integer)state.getValue((Property)EGGS); i++) {
/* 114 */           level.levelEvent(2001, pos, Block.getId(state));
/* 115 */           Turtle turtle = (Turtle)EntityType.TURTLE.create((Level)level, EntitySpawnReason.BREEDING);
/* 116 */           if (turtle != null) {
/* 117 */             turtle.setAge(-24000);
/* 118 */             turtle.setHomePos(pos);
/* 119 */             turtle.snapTo(pos.getX() + 0.3D + i * 0.2D, pos.getY(), pos.getZ() + 0.3D, 0.0F, 0.0F);
/* 120 */             level.addFreshEntity((Entity)turtle);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean onSand(BlockGetter level, BlockPos pos) {
/* 128 */     return isSand(level, pos.below());
/*     */   }
/*     */   
/*     */   public static boolean isSand(BlockGetter level, BlockPos pos) {
/* 132 */     return level.getBlockState(pos).is(BlockTags.SAND);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 137 */     if (onSand((BlockGetter)level, pos) && !level.isClientSide()) {
/* 138 */       level.levelEvent(2012, pos, 15);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldUpdateHatchLevel(Level level, BlockPos pos) {
/* 143 */     float chance = (Float)level.environmentAttributes().getValue(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, pos);
/* 144 */     return (chance > 0.0F && level.random.nextFloat() < chance);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith) {
/* 149 */     super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
/*     */     
/* 151 */     decreaseEggs(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 156 */     if (!context.isSecondaryUseActive() && context.getItemInHand().is(asItem()) && (Integer)state.getValue((Property)EGGS) < 4) {
/* 157 */       return true;
/*     */     }
/* 159 */     return super.canBeReplaced(state, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 164 */     BlockState state = context.getLevel().getBlockState(context.getClickedPos());
/* 165 */     if (state.is(this)) {
/* 166 */       return (BlockState)state.setValue((Property)EGGS, Math.min(4, (Integer)state.getValue((Property)EGGS) + 1));
/*     */     }
/*     */     
/* 169 */     return super.getStateForPlacement(context);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 174 */     return ((Integer)state.getValue((Property)EGGS) == 1) ? SHAPE_SINGLE : SHAPE_MULTIPLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 179 */     builder.add(new Property[] { (Property)HATCH, (Property)EGGS });
/*     */   }
/*     */   
/*     */   private boolean canDestroyEgg(ServerLevel level, Entity entity) {
/* 183 */     if (entity instanceof Turtle || entity instanceof net.minecraft.world.entity.ambient.Bat) {
/* 184 */       return false;
/*     */     }
/*     */     
/* 187 */     if (entity instanceof net.minecraft.world.entity.LivingEntity) {
/* 188 */       return (entity instanceof Player || (Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING));
/*     */     }
/*     */     
/* 191 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TurtleEggBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */