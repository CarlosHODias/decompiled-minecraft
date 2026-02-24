/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class ConstantHeight extends HeightProvider {
/*  9 */   public static final ConstantHeight ZERO = new ConstantHeight(VerticalAnchor.absolute(0));
/*    */   
/* 11 */   public static final MapCodec<ConstantHeight> CODEC = VerticalAnchor.CODEC.fieldOf("value").xmap(ConstantHeight::new, ConstantHeight::getValue);
/*    */   
/*    */   private final VerticalAnchor value;
/*    */   
/*    */   public static ConstantHeight of(VerticalAnchor value) {
/* 16 */     return new ConstantHeight(value);
/*    */   }
/*    */   
/*    */   private ConstantHeight(VerticalAnchor value) {
/* 20 */     this.value = value;
/*    */   }
/*    */   
/*    */   public VerticalAnchor getValue() {
/* 24 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 29 */     return this.value.resolveY(context);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 34 */     return HeightProviderType.CONSTANT;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return this.value.toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/heightproviders/ConstantHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */