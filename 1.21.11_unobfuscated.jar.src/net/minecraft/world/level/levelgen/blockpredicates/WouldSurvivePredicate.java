/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WouldSurvivePredicate implements BlockPredicate {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, WouldSurvivePredicate::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<WouldSurvivePredicate> CODEC;
/*    */   private final Vec3i offset;
/*    */   private final BlockState state;
/*    */   
/*    */   protected WouldSurvivePredicate(Vec3i offset, BlockState state) {
/* 20 */     this.offset = offset;
/* 21 */     this.state = state;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 26 */     return this.state.canSurvive((net.minecraft.world.level.LevelReader)level, origin.offset(this.offset));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.WOULD_SURVIVE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/WouldSurvivePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */