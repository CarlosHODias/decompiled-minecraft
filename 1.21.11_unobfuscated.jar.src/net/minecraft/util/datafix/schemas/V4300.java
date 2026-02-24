/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ public class V4300
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V4300(int versionKey, Schema parent) {
/* 15 */     super(versionKey, parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 20 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 21 */     schema.register(map, "minecraft:llama", name -> entityWithInventory(schema));
/* 22 */     schema.register(map, "minecraft:trader_llama", name -> entityWithInventory(schema));
/* 23 */     schema.register(map, "minecraft:donkey", name -> entityWithInventory(schema));
/* 24 */     schema.register(map, "minecraft:mule", name -> entityWithInventory(schema));
/* 25 */     schema.registerSimple(map, "minecraft:horse");
/* 26 */     schema.registerSimple(map, "minecraft:skeleton_horse");
/* 27 */     schema.registerSimple(map, "minecraft:zombie_horse");
/*    */     
/* 29 */     return map;
/*    */   }
/*    */   
/*    */   private static TypeTemplate entityWithInventory(Schema schema) {
/* 33 */     return DSL.optionalFields("Items", 
/* 34 */         DSL.list(References.ITEM_STACK.in(schema)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V4300.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */