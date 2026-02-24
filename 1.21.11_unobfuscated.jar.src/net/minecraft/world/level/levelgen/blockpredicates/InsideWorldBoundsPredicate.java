/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class InsideWorldBoundsPredicate implements BlockPredicate {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.offsetCodec(16).optionalFieldOf("offset", BlockPos.ZERO).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, InsideWorldBoundsPredicate::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<InsideWorldBoundsPredicate> CODEC;
/*    */   private final Vec3i offset;
/*    */   
/*    */   public InsideWorldBoundsPredicate(Vec3i offset) {
/* 17 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos) {
/* 22 */     return !worldGenLevel.isOutsideBuildHeight(blockPos.offset(this.offset));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 27 */     return BlockPredicateType.INSIDE_WORLD_BOUNDS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/InsideWorldBoundsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */