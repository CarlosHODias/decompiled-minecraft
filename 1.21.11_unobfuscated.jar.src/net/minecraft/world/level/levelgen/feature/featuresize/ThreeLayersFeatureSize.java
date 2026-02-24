/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class ThreeLayersFeatureSize extends FeatureSize {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.intRange(0, 80).fieldOf("limit").orElse(1).forGetter(()), (App)Codec.intRange(0, 80).fieldOf("upper_limit").orElse(1).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("middle_size").orElse(1).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter(()), (App)minClippedHeightCodec()).apply((Applicative)i, ThreeLayersFeatureSize::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<ThreeLayersFeatureSize> CODEC;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private final int upperLimit;
/*    */   
/*    */   private final int lowerSize;
/*    */   
/*    */   private final int middleSize;
/*    */   private final int upperSize;
/*    */   
/*    */   public ThreeLayersFeatureSize(int limit, int upperLimit, int lowerSize, int middleSize, int upperSize, OptionalInt minClippedHeight) {
/* 27 */     super(minClippedHeight);
/* 28 */     this.limit = limit;
/* 29 */     this.upperLimit = upperLimit;
/* 30 */     this.lowerSize = lowerSize;
/* 31 */     this.middleSize = middleSize;
/* 32 */     this.upperSize = upperSize;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FeatureSizeType<?> type() {
/* 37 */     return FeatureSizeType.THREE_LAYERS_FEATURE_SIZE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSizeAtHeight(int treeHeight, int yo) {
/* 42 */     if (yo < this.limit) {
/* 43 */       return this.lowerSize;
/*    */     }
/* 45 */     if (yo >= treeHeight - this.upperLimit) {
/* 46 */       return this.upperSize;
/*    */     }
/* 48 */     return this.middleSize;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/featuresize/ThreeLayersFeatureSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */