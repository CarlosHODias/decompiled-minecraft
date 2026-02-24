/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ abstract class CombiningPredicate
/*    */   implements BlockPredicate {
/*    */   protected CombiningPredicate(List<BlockPredicate> predicates) {
/* 13 */     this.predicates = predicates;
/*    */   }
/*    */   protected final List<BlockPredicate> predicates;
/*    */   public static <T extends CombiningPredicate> MapCodec<T> codec(Function<List<BlockPredicate>, T> constructor) {
/* 17 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPredicate.CODEC.listOf().fieldOf("predicates").forGetter(())).apply((Applicative)i, constructor));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/CombiningPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */