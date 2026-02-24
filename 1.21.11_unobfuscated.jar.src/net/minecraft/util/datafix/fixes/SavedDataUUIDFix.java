/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SavedDataUUIDFix extends AbstractUUIDFix {
/* 10 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public SavedDataUUIDFix(Schema outputSchema) {
/* 13 */     super(outputSchema, References.SAVED_DATA_RAIDS);
/*    */   }
/*    */ 
/*    */   
/*    */   protected com.mojang.datafixers.TypeRewriteRule makeRule() {
/* 18 */     return fixTypeEverywhereTyped("SavedDataUUIDFix", getInputSchema().getType(this.typeReference), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/SavedDataUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */