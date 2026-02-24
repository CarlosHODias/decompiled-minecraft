/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*    */ 
/*    */ public class HeightRangePlacement extends PlacementModifier {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)HeightProvider.CODEC.fieldOf("height").forGetter(())).apply((Applicative)i, HeightRangePlacement::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<HeightRangePlacement> CODEC;
/*    */   private final HeightProvider height;
/*    */   
/*    */   private HeightRangePlacement(HeightProvider height) {
/* 25 */     this.height = height;
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement of(HeightProvider height) {
/* 29 */     return new HeightRangePlacement(height);
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement uniform(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 33 */     return of((HeightProvider)UniformHeight.of(minInclusive, maxInclusive));
/*    */   }
/*    */   
/*    */   public static HeightRangePlacement triangle(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 37 */     return of((HeightProvider)TrapezoidHeight.of(minInclusive, maxInclusive));
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 42 */     return Stream.of(origin.atY(this.height.sample(random, context)));
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 47 */     return PlacementModifierType.HEIGHT_RANGE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/HeightRangePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */