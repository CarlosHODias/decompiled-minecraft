/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class BlockPropertyRenameAndFix
/*    */   extends AbstractBlockPropertyFix {
/*    */   private final String blockId;
/*    */   private final String oldPropertyName;
/*    */   private final String newPropertyName;
/*    */   private final UnaryOperator<String> valueFixer;
/*    */   
/*    */   public BlockPropertyRenameAndFix(Schema outputSchema, String name, String blockId, String oldPropertyName, String newPropertyName, UnaryOperator<String> valueFixer) {
/* 15 */     super(outputSchema, name);
/* 16 */     this.blockId = blockId;
/* 17 */     this.oldPropertyName = oldPropertyName;
/* 18 */     this.newPropertyName = newPropertyName;
/* 19 */     this.valueFixer = valueFixer;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldFix(String blockId) {
/* 24 */     return blockId.equals(this.blockId);
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fixProperties(String blockId, Dynamic<T> properties) {
/* 29 */     return properties.renameAndFixField(this.oldPropertyName, this.newPropertyName, dynamic -> dynamic.createString(this.valueFixer.apply(dynamic.asString(""))));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BlockPropertyRenameAndFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */