/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RuleProcessor extends StructureProcessor {
/*    */   public static final com.mojang.serialization.MapCodec<RuleProcessor> CODEC;
/*    */   
/*    */   static {
/* 15 */     CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(RuleProcessor::new, p -> p.rules);
/*    */   }
/*    */   private final ImmutableList<ProcessorRule> rules;
/*    */   
/*    */   public RuleProcessor(List<? extends ProcessorRule> rules) {
/* 20 */     this.rules = ImmutableList.copyOf(rules);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 25 */     RandomSource random = RandomSource.create(Mth.getSeed((net.minecraft.core.Vec3i)processedBlockInfo.pos()));
/* 26 */     BlockState locState = level.getBlockState(processedBlockInfo.pos());
/* 27 */     for (UnmodifiableIterator<ProcessorRule> unmodifiableIterator = this.rules.iterator(); unmodifiableIterator.hasNext(); ) { ProcessorRule rule = unmodifiableIterator.next();
/* 28 */       if (rule.test(processedBlockInfo.state(), locState, originalBlockInfo.pos(), processedBlockInfo.pos(), referencePos, random)) {
/* 29 */         return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), rule.getOutputState(), rule.getOutputTag(random, processedBlockInfo.nbt()));
/*    */       } }
/*    */     
/* 32 */     return processedBlockInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 37 */     return StructureProcessorType.RULE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/RuleProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */