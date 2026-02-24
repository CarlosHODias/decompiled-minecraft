/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class FlatLayerInfo {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(FlatLayerInfo::getHeight), (App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").orElse(Blocks.AIR).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, FlatLayerInfo::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<FlatLayerInfo> CODEC;
/*    */   private final Block block;
/*    */   private final int height;
/*    */   
/*    */   public FlatLayerInfo(int height, Block block) {
/* 21 */     this.height = height;
/* 22 */     this.block = block;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 26 */     return this.height;
/*    */   }
/*    */   
/*    */   public net.minecraft.world.level.block.state.BlockState getBlockState() {
/* 30 */     return this.block.defaultBlockState();
/*    */   }
/*    */   
/*    */   public FlatLayerInfo heightLimited(int maxHeight) {
/* 34 */     if (this.height > maxHeight) {
/* 35 */       return new FlatLayerInfo(maxHeight, this.block);
/*    */     }
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return ((this.height != 1) ? ("" + this.height + "*") : "") + ((this.height != 1) ? ("" + this.height + "*") : "");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/flat/FlatLayerInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */