/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.mojang.math.Quadrant;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface VariantMutator
/*    */   extends UnaryOperator<Variant> {
/*    */   @FunctionalInterface
/*    */   public static interface VariantProperty<T> {
/*    */     Variant apply(Variant param1Variant, T param1T);
/*    */     
/*    */     default VariantMutator withValue(T value) {
/* 15 */       return variant -> apply(value, (T)value);
/*    */     }
/*    */   }
/*    */   
/* 19 */   public static final VariantProperty<Quadrant> X_ROT = Variant::withXRot;
/* 20 */   public static final VariantProperty<Quadrant> Y_ROT = Variant::withYRot;
/* 21 */   public static final VariantProperty<Quadrant> Z_ROT = Variant::withZRot;
/* 22 */   public static final VariantProperty<Identifier> MODEL = Variant::withModel;
/* 23 */   public static final VariantProperty<Boolean> UV_LOCK = Variant::withUvLock;
/*    */   
/*    */   default VariantMutator then(VariantMutator other) {
/* 26 */     return variant -> other.apply(apply(other));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/VariantMutator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */