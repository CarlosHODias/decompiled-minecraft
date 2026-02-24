/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsAmbientOcclusionFix extends com.mojang.datafixers.DataFix {
/*    */   public OptionsAmbientOcclusionFix(Schema outputSchema) {
/* 11 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsAmbientOcclusionFix", getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static String updateValue(String value) {
/* 22 */     switch (value) { case "0": case "1": case "2": default: break; }  return 
/*    */ 
/*    */       
/* 25 */       value;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/OptionsAmbientOcclusionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */