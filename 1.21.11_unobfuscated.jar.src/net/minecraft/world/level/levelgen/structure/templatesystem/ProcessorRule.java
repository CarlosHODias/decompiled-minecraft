/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
/*    */ 
/*    */ public class ProcessorRule
/*    */ {
/* 17 */   public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RuleTest.CODEC.fieldOf("input_predicate").forGetter(()), (App)RuleTest.CODEC.fieldOf("location_predicate").forGetter(()), (App)PosRuleTest.CODEC.lenientOptionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(()), (App)BlockState.CODEC.fieldOf("output_state").forGetter(()), (App)RuleBlockEntityModifier.CODEC.lenientOptionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(())).apply((Applicative)i, ProcessorRule::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<ProcessorRule> CODEC;
/*    */ 
/*    */   
/*    */   private final RuleTest inputPredicate;
/*    */   
/*    */   private final RuleTest locPredicate;
/*    */   
/*    */   private final PosRuleTest posPredicate;
/*    */   
/*    */   private final BlockState outputState;
/*    */   
/*    */   private final RuleBlockEntityModifier blockEntityModifier;
/*    */ 
/*    */   
/*    */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, BlockState outputState) {
/* 39 */     this(inputPredicate, locPredicate, PosAlwaysTrueTest.INSTANCE, outputState);
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState) {
/* 43 */     this(inputPredicate, locPredicate, posPredicate, outputState, (RuleBlockEntityModifier)DEFAULT_BLOCK_ENTITY_MODIFIER);
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState, RuleBlockEntityModifier blockEntityModifier) {
/* 47 */     this.inputPredicate = inputPredicate;
/* 48 */     this.locPredicate = locPredicate;
/* 49 */     this.posPredicate = posPredicate;
/* 50 */     this.outputState = outputState;
/* 51 */     this.blockEntityModifier = blockEntityModifier;
/*    */   }
/*    */   
/*    */   public boolean test(BlockState inputState, BlockState locState, BlockPos inTemplatePos, BlockPos worldPos, BlockPos reference, RandomSource random) {
/* 55 */     return (this.inputPredicate.test(inputState, random) && this.locPredicate.test(locState, random) && this.posPredicate.test(inTemplatePos, worldPos, reference, random));
/*    */   }
/*    */   
/*    */   public BlockState getOutputState() {
/* 59 */     return this.outputState;
/*    */   }
/*    */   
/*    */   public CompoundTag getOutputTag(RandomSource random, CompoundTag existingTag) {
/* 63 */     return this.blockEntityModifier.apply(random, existingTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/ProcessorRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */