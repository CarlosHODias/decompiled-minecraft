/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SlabBlock;
/*    */ import net.minecraft.world.level.block.StairBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlackstoneReplaceProcessor
/*    */   extends StructureProcessor
/*    */ {
/* 20 */   public static final MapCodec<BlackstoneReplaceProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 22 */   public static final BlackstoneReplaceProcessor INSTANCE = new BlackstoneReplaceProcessor();
/*    */   
/*    */   private final Map<Block, Block> replacements;
/*    */   
/*    */   private BlackstoneReplaceProcessor() {
/* 27 */     this.replacements = (Map<Block, Block>)Util.make(Maps.newHashMap(), map -> {
/*    */           map.put(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
/*    */           map.put(Blocks.STONE, Blocks.POLISHED_BLACKSTONE);
/*    */           map.put(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           map.put(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           map.put(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           map.put(Blocks.STONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
/*    */           map.put(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           map.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           map.put(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           map.put(Blocks.SMOOTH_STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           map.put(Blocks.STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           map.put(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           map.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           map.put(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           map.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           map.put(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           map.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
/*    */           map.put(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*    */           map.put(Blocks.IRON_BARS, Blocks.IRON_CHAIN);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 67 */     Block newBlock = this.replacements.get(processedBlockInfo.state().getBlock());
/* 68 */     if (newBlock == null) {
/* 69 */       return processedBlockInfo;
/*    */     }
/* 71 */     BlockState oldState = processedBlockInfo.state();
/* 72 */     BlockState newState = newBlock.defaultBlockState();
/* 73 */     if (oldState.hasProperty((Property)StairBlock.FACING)) {
/* 74 */       newState = (BlockState)newState.setValue((Property)StairBlock.FACING, oldState.getValue((Property)StairBlock.FACING));
/*    */     }
/* 76 */     if (oldState.hasProperty((Property)StairBlock.HALF)) {
/* 77 */       newState = (BlockState)newState.setValue((Property)StairBlock.HALF, oldState.getValue((Property)StairBlock.HALF));
/*    */     }
/* 79 */     if (oldState.hasProperty((Property)SlabBlock.TYPE)) {
/* 80 */       newState = (BlockState)newState.setValue((Property)SlabBlock.TYPE, oldState.getValue((Property)SlabBlock.TYPE));
/*    */     }
/* 82 */     return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), newState, processedBlockInfo.nbt());
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 87 */     return StructureProcessorType.BLACKSTONE_REPLACE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/BlackstoneReplaceProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */