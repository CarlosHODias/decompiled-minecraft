/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class TridentAnimationFix
/*    */   extends DataComponentRemainderFix
/*    */ {
/*    */   public TridentAnimationFix(Schema outputSchema) {
/* 10 */     super(outputSchema, "TridentAnimationFix", "minecraft:consumable");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) {
/* 15 */     return input.update("animation", animation -> {
/*    */           String optional = animation.asString().result().orElse("");
/*    */           return "spear".equals(optional) ? animation.createString("trident") : animation;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/TridentAnimationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */