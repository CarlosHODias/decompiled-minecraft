/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1928
/*    */   extends NamespacedSchema {
/*    */   public V1928(int versionKey, Schema parent) {
/* 11 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/* 15 */     schema.registerSimple(map, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */     
/* 22 */     map.remove("minecraft:illager_beast");
/* 23 */     registerMob(schema, map, "minecraft:ravager");
/*    */     
/* 25 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V1928.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */