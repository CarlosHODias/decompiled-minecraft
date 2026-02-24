/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class TwoLayersFeatureSize extends FeatureSize {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.intRange(0, 81).fieldOf("limit").orElse(1).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter(()), (App)minClippedHeightCodec()).apply((Applicative)i, TwoLayersFeatureSize::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<TwoLayersFeatureSize> CODEC;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private final int lowerSize;
/*    */   
/*    */   private final int upperSize;
/*    */   
/*    */   public TwoLayersFeatureSize(int limit, int lowerSize, int upperSize) {
/* 25 */     this(limit, lowerSize, upperSize, OptionalInt.empty());
/*    */   }
/*    */   
/*    */   public TwoLayersFeatureSize(int limit, int lowerSize, int upperSize, OptionalInt minClippedHeight) {
/* 29 */     super(minClippedHeight);
/* 30 */     this.limit = limit;
/* 31 */     this.lowerSize = lowerSize;
/* 32 */     this.upperSize = upperSize;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FeatureSizeType<?> type() {
/* 37 */     return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSizeAtHeight(int treeHeight, int yo) {
/* 42 */     return (yo < this.limit) ? this.lowerSize : this.upperSize;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */