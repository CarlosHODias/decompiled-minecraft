/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class ReplaceablePredicate extends StateTestingPredicate {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).apply((com.mojang.datafixers.kinds.Applicative)i, ReplaceablePredicate::new));
/*    */   } public static final com.mojang.serialization.MapCodec<ReplaceablePredicate> CODEC;
/*    */   public ReplaceablePredicate(Vec3i offset) {
/* 12 */     super(offset);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 17 */     return state.canBeReplaced();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 22 */     return BlockPredicateType.REPLACEABLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/ReplaceablePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */