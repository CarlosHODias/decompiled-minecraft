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
/*    */ public class V1470
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1470(int versionKey, Schema parent) {
/* 15 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/* 19 */     schema.registerSimple(map, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 24 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/*    */ 
/*    */     
/* 27 */     registerMob(schema, map, "minecraft:turtle");
/* 28 */     registerMob(schema, map, "minecraft:cod_mob");
/* 29 */     registerMob(schema, map, "minecraft:tropical_fish");
/* 30 */     registerMob(schema, map, "minecraft:salmon_mob");
/* 31 */     registerMob(schema, map, "minecraft:puffer_fish");
/* 32 */     registerMob(schema, map, "minecraft:phantom");
/* 33 */     registerMob(schema, map, "minecraft:dolphin");
/* 34 */     registerMob(schema, map, "minecraft:drowned");
/*    */     
/* 36 */     schema.register(map, "minecraft:trident", name -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema), "Trident", References.ITEM_STACK.in(schema)));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V1470.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */