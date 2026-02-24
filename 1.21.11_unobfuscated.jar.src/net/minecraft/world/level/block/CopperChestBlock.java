/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CopperChestBlock extends ChestBlock {
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getState), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((Applicative)i, CopperChestBlock::new));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final com.mojang.serialization.MapCodec<CopperChestBlock> CODEC;
/*     */   
/*  32 */   private static final Map<Block, java.util.function.Supplier<Block>> COPPER_TO_COPPER_CHEST_MAPPING = Map.of(Blocks.COPPER_BLOCK, () -> Blocks.COPPER_CHEST, Blocks.EXPOSED_COPPER, () -> Blocks.EXPOSED_COPPER_CHEST, Blocks.WEATHERED_COPPER, () -> Blocks.WEATHERED_COPPER_CHEST, Blocks.OXIDIZED_COPPER, () -> Blocks.OXIDIZED_COPPER_CHEST, Blocks.WAXED_COPPER_BLOCK, () -> Blocks.COPPER_CHEST, Blocks.WAXED_EXPOSED_COPPER, () -> Blocks.EXPOSED_COPPER_CHEST, Blocks.WAXED_WEATHERED_COPPER, () -> Blocks.WEATHERED_COPPER_CHEST, Blocks.WAXED_OXIDIZED_COPPER, () -> Blocks.OXIDIZED_COPPER_CHEST);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeatheringCopper.WeatherState weatherState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.mojang.serialization.MapCodec<? extends CopperChestBlock> codec() {
/*  44 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
/*  50 */     super(() -> BlockEntityType.CHEST, openSound, closeSound, properties);
/*  51 */     this.weatherState = weatherState;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chestCanConnectTo(BlockState blockState) {
/*  56 */     return (blockState.is(BlockTags.COPPER_CHESTS) && blockState.hasProperty((Property)ChestBlock.TYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  61 */     BlockState state = super.getStateForPlacement(context);
/*  62 */     return getLeastOxidizedChestOfConnectedBlocks(state, context.getLevel(), context.getClickedPos());
/*     */   }
/*     */   
/*     */   private static BlockState getLeastOxidizedChestOfConnectedBlocks(BlockState state, Level level, BlockPos pos) {
/*  66 */     BlockState connectedState = level.getBlockState(pos.relative(getConnectedDirection(state)));
/*  67 */     if (!((ChestType)state.getValue((Property)ChestBlock.TYPE)).equals(ChestType.SINGLE)) { Block block = state.getBlock(); if (block instanceof CopperChestBlock) { CopperChestBlock copperChestBlock = (CopperChestBlock)block; block = connectedState.getBlock(); if (block instanceof CopperChestBlock) { CopperChestBlock connectedCopperChestBlock = (CopperChestBlock)block;
/*  68 */           BlockState updatedBlockState = state;
/*  69 */           BlockState connectedPredictedBlockState = connectedState;
/*     */           
/*  71 */           if (copperChestBlock.isWaxed() != connectedCopperChestBlock.isWaxed()) {
/*  72 */             updatedBlockState = unwaxBlock(copperChestBlock, state).orElse(updatedBlockState);
/*  73 */             connectedPredictedBlockState = unwaxBlock(connectedCopperChestBlock, connectedState).orElse(connectedPredictedBlockState);
/*     */           } 
/*     */           
/*  76 */           Block leastOxidizedBlock = (copperChestBlock.weatherState.ordinal() <= connectedCopperChestBlock.weatherState.ordinal()) ? updatedBlockState.getBlock() : connectedPredictedBlockState.getBlock();
/*  77 */           return leastOxidizedBlock.withPropertiesOf(updatedBlockState); }  }
/*     */        }
/*  79 */      return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  84 */     BlockState blockState = super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*  85 */     if (chestCanConnectTo(neighbourState)) {
/*  86 */       ChestType chestType = (ChestType)blockState.getValue((Property)ChestBlock.TYPE);
/*  87 */       if (!chestType.equals(ChestType.SINGLE) && getConnectedDirection(blockState) == directionToNeighbour) {
/*  88 */         return neighbourState.getBlock().withPropertiesOf(blockState);
/*     */       }
/*     */     } 
/*  91 */     return blockState;
/*     */   }
/*     */   
/*     */   private static Optional<BlockState> unwaxBlock(CopperChestBlock copperChestBlock, BlockState state) {
/*  95 */     if (!copperChestBlock.isWaxed()) {
/*  96 */       return Optional.of(state);
/*     */     }
/*  98 */     return Optional.<Block>ofNullable((Block)((com.google.common.collect.BiMap)net.minecraft.world.item.HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(state.getBlock())).map(b -> b.withPropertiesOf(state));
/*     */   }
/*     */   
/*     */   public WeatheringCopper.WeatherState getState() {
/* 102 */     return this.weatherState;
/*     */   }
/*     */   
/*     */   public static BlockState getFromCopperBlock(Block copperBlock, Direction facing, Level level, BlockPos pos) {
/* 106 */     java.util.Objects.requireNonNull(Blocks.COPPER_CHEST); CopperChestBlock block = ((java.util.function.Supplier<CopperChestBlock>)COPPER_TO_COPPER_CHEST_MAPPING.getOrDefault(copperBlock, Blocks.COPPER_CHEST::asBlock)).get();
/* 107 */     ChestType chestType = block.getChestType(level, pos, facing);
/* 108 */     BlockState state = (BlockState)((BlockState)block.defaultBlockState().setValue((Property)FACING, (Comparable)facing)).setValue((Property)TYPE, (Comparable)chestType);
/* 109 */     return getLeastOxidizedChestOfConnectedBlocks(state, level, pos);
/*     */   }
/*     */   
/*     */   public boolean isWaxed() {
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/* 118 */     return oldState.is(BlockTags.COPPER_CHESTS);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CopperChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */