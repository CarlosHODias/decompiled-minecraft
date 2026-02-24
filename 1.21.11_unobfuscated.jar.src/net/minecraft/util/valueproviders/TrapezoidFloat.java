/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class TrapezoidFloat
/*    */   extends FloatProvider {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("min").forGetter(()), (App)Codec.FLOAT.fieldOf("max").forGetter(()), (App)Codec.FLOAT.fieldOf("plateau").forGetter(())).apply((Applicative)i, TrapezoidFloat::new)).validate(c -> (c.max < c.min) ? DataResult.error(()) : ((c.plateau > c.max - c.min) ? DataResult.error(()) : DataResult.success(c)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final MapCodec<TrapezoidFloat> CODEC;
/*    */   
/*    */   private final float min;
/*    */   
/*    */   private final float max;
/*    */   
/*    */   private final float plateau;
/*    */ 
/*    */   
/*    */   public static TrapezoidFloat of(float min, float max, float plateau) {
/* 30 */     return new TrapezoidFloat(min, max, plateau);
/*    */   }
/*    */   
/*    */   private TrapezoidFloat(float min, float max, float plateau) {
/* 34 */     this.min = min;
/* 35 */     this.max = max;
/* 36 */     this.plateau = plateau;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource random) {
/* 41 */     float range = this.max - this.min;
/* 42 */     float plateauStart = (range - this.plateau) / 2.0F;
/* 43 */     float plateauEnd = range - plateauStart;
/*    */     
/* 45 */     return this.min + random.nextFloat() * plateauEnd + random.nextFloat() * plateauStart;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMinValue() {
/* 50 */     return this.min;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMaxValue() {
/* 55 */     return this.max;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatProviderType<?> getType() {
/* 60 */     return FloatProviderType.TRAPEZOID;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "trapezoid(" + this.plateau + ") in [" + this.min + "-" + this.max + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/TrapezoidFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */