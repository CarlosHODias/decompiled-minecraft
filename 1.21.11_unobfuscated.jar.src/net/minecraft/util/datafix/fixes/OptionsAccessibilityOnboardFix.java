/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsAccessibilityOnboardFix extends com.mojang.datafixers.DataFix {
/*    */   public OptionsAccessibilityOnboardFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("OptionsAccessibilityOnboardFix", 
/* 16 */         getInputSchema().getType(References.OPTIONS), typed -> typed.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/OptionsAccessibilityOnboardFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */