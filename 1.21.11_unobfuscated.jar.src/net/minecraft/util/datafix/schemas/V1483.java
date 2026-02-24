/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1483
/*    */   extends NamespacedSchema {
/*    */   public V1483(int versionKey, Schema parent) {
/* 11 */     super(versionKey, parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 18 */     map.put("minecraft:pufferfish", map.remove("minecraft:puffer_fish"));
/*    */     
/* 20 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V1483.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */