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
/*    */ 
/*    */ public class V3689
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3689(int versionKey, Schema parent) {
/* 16 */     super(versionKey, parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 21 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 22 */     schema.registerSimple(map, "minecraft:breeze");
/* 23 */     schema.registerSimple(map, "minecraft:wind_charge");
/* 24 */     schema.registerSimple(map, "minecraft:breeze_wind_charge");
/* 25 */     return map;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 30 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 31 */     schema.register(map, "minecraft:trial_spawner", () -> DSL.optionalFields("spawn_potentials", DSL.list(DSL.fields("data", DSL.fields("entity", References.ENTITY_TREE.in(schema)))), "spawn_data", DSL.fields("entity", References.ENTITY_TREE.in(schema))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V3689.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */