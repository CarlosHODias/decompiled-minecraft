/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EmptyItemInVillagerTradeFix extends DataFix {
/*    */   public EmptyItemInVillagerTradeFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 17 */     Type<?> tradeType = getInputSchema().getType(References.VILLAGER_TRADE);
/* 18 */     return writeFixAndRead("EmptyItemInVillagerTradeFix", tradeType, tradeType, input -> {
/*    */           Dynamic<?> buyB = input.get("buyB").orElseEmptyMap();
/*    */           String id = NamespacedSchema.ensureNamespaced(buyB.get("id").asString("minecraft:air"));
/*    */           int count = buyB.get("count").asInt(0);
/* 22 */           return (id.equals("minecraft:air") || count == 0) ? input.remove("buyB") : input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EmptyItemInVillagerTradeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */