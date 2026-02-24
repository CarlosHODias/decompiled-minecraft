/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsSetGraphicsPresetToCustomFix extends com.mojang.datafixers.DataFix {
/*    */   public OptionsSetGraphicsPresetToCustomFix(Schema outputSchema) {
/* 10 */     super(outputSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("graphicsPreset set to \"custom\"", getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/OptionsSetGraphicsPresetToCustomFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */