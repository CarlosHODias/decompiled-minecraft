/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MatchingBlockTagPredicate extends StateTestingPredicate {
/*    */   final TagKey<Block> tag;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and((App)TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, MatchingBlockTagPredicate::new));
/*    */   }
/*    */   public static final com.mojang.serialization.MapCodec<MatchingBlockTagPredicate> CODEC;
/*    */   
/*    */   protected MatchingBlockTagPredicate(Vec3i offset, TagKey<Block> tag) {
/* 19 */     super(offset);
/* 20 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 25 */     return state.is(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 30 */     return BlockPredicateType.MATCHING_BLOCK_TAG;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/MatchingBlockTagPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */