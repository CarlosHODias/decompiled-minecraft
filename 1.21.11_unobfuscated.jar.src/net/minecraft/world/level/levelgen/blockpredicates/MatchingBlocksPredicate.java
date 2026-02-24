/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class MatchingBlocksPredicate extends StateTestingPredicate {
/*    */   private final HolderSet<Block> blocks;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and((App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, MatchingBlocksPredicate::new));
/*    */   }
/*    */   public static final com.mojang.serialization.MapCodec<MatchingBlocksPredicate> CODEC;
/*    */   
/*    */   public MatchingBlocksPredicate(Vec3i offset, HolderSet<Block> blocks) {
/* 20 */     super(offset);
/* 21 */     this.blocks = blocks;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 26 */     return state.is(this.blocks);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.MATCHING_BLOCKS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/MatchingBlocksPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */