/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class DebugProfileOverlayReferenceFix extends com.mojang.datafixers.DataFix {
/*    */   public DebugProfileOverlayReferenceFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected com.mojang.datafixers.TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("DebugProfileOverlayReferenceFix", 
/* 16 */         getInputSchema().getType(References.DEBUG_PROFILE), typed -> typed.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/DebugProfileOverlayReferenceFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */