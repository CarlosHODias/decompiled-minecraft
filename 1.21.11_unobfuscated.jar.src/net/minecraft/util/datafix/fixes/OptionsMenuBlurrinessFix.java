/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsMenuBlurrinessFix extends com.mojang.datafixers.DataFix {
/*    */   public OptionsMenuBlurrinessFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("OptionsMenuBlurrinessFix", 
/* 16 */         getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int convertToIntRange(String floatBlurriness) {
/*    */     try {
/* 28 */       return Math.round(Float.parseFloat(floatBlurriness) * 10.0F);
/* 29 */     } catch (NumberFormatException e) {
/* 30 */       return 5;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/OptionsMenuBlurrinessFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */