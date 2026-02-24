/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class WriteAndReadFix extends DataFix {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public WriteAndReadFix(Schema outputSchema, String name, DSL.TypeReference type) {
/* 13 */     super(outputSchema, true);
/* 14 */     this.name = name;
/* 15 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     return writeAndRead(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/WriteAndReadFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */