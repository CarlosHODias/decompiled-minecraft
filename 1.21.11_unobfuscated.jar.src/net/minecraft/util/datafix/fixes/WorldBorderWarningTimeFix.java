/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class WorldBorderWarningTimeFix extends DataFix {
/*    */   public WorldBorderWarningTimeFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return writeFixAndRead("WorldBorderWarningTimeFix", getInputSchema().getType(References.SAVED_DATA_WORLD_BORDER), getOutputSchema().getType(References.SAVED_DATA_WORLD_BORDER), input -> input.update("data", ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/WorldBorderWarningTimeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */