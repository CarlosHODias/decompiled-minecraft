/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ 
/*    */ public class DecoratedPotFieldRenameFix
/*    */   extends DataFix {
/*    */   private static final String DECORATED_POT_ID = "minecraft:decorated_pot";
/*    */   
/*    */   public DecoratedPotFieldRenameFix(Schema outputSchema) {
/* 13 */     super(outputSchema, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     Type<?> oldDecoratedPot = getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:decorated_pot");
/* 19 */     Type<?> newDecoratedPot = getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:decorated_pot");
/*    */     
/* 21 */     return convertUnchecked("DecoratedPotFieldRenameFix", oldDecoratedPot, newDecoratedPot);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/DecoratedPotFieldRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */