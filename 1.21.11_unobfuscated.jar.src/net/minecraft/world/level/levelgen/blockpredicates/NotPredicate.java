/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ class NotPredicate implements BlockPredicate {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPredicate.CODEC.fieldOf("predicate").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, NotPredicate::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<NotPredicate> CODEC;
/*    */   private final BlockPredicate predicate;
/*    */   
/*    */   public NotPredicate(BlockPredicate predicate) {
/* 16 */     this.predicate = predicate;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 21 */     return !this.predicate.test(level, origin);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 26 */     return BlockPredicateType.NOT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/NotPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */