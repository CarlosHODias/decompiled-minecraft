/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3078
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3078(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/* 18 */     schema.registerSimple(map, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 23 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 24 */     registerMob(schema, map, "minecraft:frog");
/* 25 */     registerMob(schema, map, "minecraft:tadpole");
/* 26 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 31 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 32 */     schema.register(map, "minecraft:sculk_shrieker", () -> DSL.optionalFields("listener", DSL.optionalFields("event", DSL.optionalFields("game_event", References.GAME_EVENT_NAME.in(schema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 39 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V3078.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */