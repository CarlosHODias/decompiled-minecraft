/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ @Deprecated
/*    */ public class SolidPredicate extends StateTestingPredicate {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).apply((com.mojang.datafixers.kinds.Applicative)i, SolidPredicate::new));
/*    */   } public static final com.mojang.serialization.MapCodec<SolidPredicate> CODEC;
/*    */   public SolidPredicate(Vec3i offset) {
/* 13 */     super(offset);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 18 */     return state.isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 23 */     return BlockPredicateType.SOLID;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/SolidPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */