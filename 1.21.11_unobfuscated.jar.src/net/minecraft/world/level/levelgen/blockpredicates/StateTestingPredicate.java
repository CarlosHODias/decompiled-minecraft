/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class StateTestingPredicate
/*    */   implements BlockPredicate {
/*    */   protected static <P extends StateTestingPredicate> Products.P1<RecordCodecBuilder.Mu<P>, Vec3i> stateTestingCodec(RecordCodecBuilder.Instance<P> instance) {
/* 14 */     return instance.group(
/* 15 */         (App)Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(c -> c.offset));
/*    */   }
/*    */   protected final Vec3i offset;
/*    */   
/*    */   protected StateTestingPredicate(Vec3i offset) {
/* 20 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean test(WorldGenLevel level, BlockPos origin) {
/* 25 */     return test(level.getBlockState(origin.offset(this.offset)));
/*    */   }
/*    */   
/*    */   protected abstract boolean test(BlockState paramBlockState);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/StateTestingPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */