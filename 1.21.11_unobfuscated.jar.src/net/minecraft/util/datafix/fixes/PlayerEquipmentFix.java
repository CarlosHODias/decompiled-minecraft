/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class PlayerEquipmentFix
/*    */   extends DataFix {
/*    */   public PlayerEquipmentFix(Schema outputSchema) {
/* 14 */     super(outputSchema, true);
/*    */   }
/*    */   
/* 17 */   private static final Map<Integer, String> SLOT_TRANSLATIONS = Map.of(100, 
/* 18 */       "feet", 101, 
/* 19 */       "legs", 102, 
/* 20 */       "chest", 103, 
/* 21 */       "head", -106, 
/* 22 */       "offhand");
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> oldPlayerType = getInputSchema().getTypeRaw(References.PLAYER);
/* 28 */     Type<?> newPlayerType = getOutputSchema().getTypeRaw(References.PLAYER);
/*    */     
/* 30 */     return writeFixAndRead("Player Equipment Fix", oldPlayerType, newPlayerType, tag -> {
/*    */           Map<Dynamic<?>, Dynamic<?>> equipment = new HashMap<>();
/*    */           tag = tag.update("Inventory", ());
/*    */           return tag.set("equipment", tag.createMap(equipment));
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/PlayerEquipmentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */