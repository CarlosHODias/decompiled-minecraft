/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.SequencedMap;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V3818_3
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3818_3(int versionKey, Schema parent) {
/* 25 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema schema) {
/* 29 */     SequencedMap<String, Supplier<TypeTemplate>> components = new LinkedHashMap<>();
/* 30 */     components.put("minecraft:bees", () -> DSL.list(DSL.optionalFields("entity_data", References.ENTITY_TREE.in(schema))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     components.put("minecraft:block_entity_data", () -> References.BLOCK_ENTITY.in(schema));
/* 36 */     components.put("minecraft:bundle_contents", () -> DSL.list(References.ITEM_STACK.in(schema)));
/* 37 */     components.put("minecraft:can_break", () -> DSL.optionalFields("predicates", DSL.list(DSL.optionalFields("blocks", DSL.or(References.BLOCK_NAME.in(schema), DSL.list(References.BLOCK_NAME.in(schema)))))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     components.put("minecraft:can_place_on", () -> DSL.optionalFields("predicates", DSL.list(DSL.optionalFields("blocks", DSL.or(References.BLOCK_NAME.in(schema), DSL.list(References.BLOCK_NAME.in(schema)))))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     components.put("minecraft:charged_projectiles", () -> DSL.list(References.ITEM_STACK.in(schema)));
/* 48 */     components.put("minecraft:container", () -> DSL.list(DSL.optionalFields("item", References.ITEM_STACK.in(schema))));
/*    */ 
/*    */     
/* 51 */     components.put("minecraft:entity_data", () -> References.ENTITY_TREE.in(schema));
/* 52 */     components.put("minecraft:pot_decorations", () -> DSL.list(References.ITEM_NAME.in(schema)));
/* 53 */     components.put("minecraft:food", () -> DSL.optionalFields("using_converts_to", References.ITEM_STACK.in(schema)));
/*    */ 
/*    */     
/* 56 */     components.put("minecraft:custom_name", () -> References.TEXT_COMPONENT.in(schema));
/* 57 */     components.put("minecraft:item_name", () -> References.TEXT_COMPONENT.in(schema));
/* 58 */     components.put("minecraft:lore", () -> DSL.list(References.TEXT_COMPONENT.in(schema)));
/* 59 */     components.put("minecraft:written_book_content", () -> DSL.optionalFields("pages", DSL.list(DSL.or(DSL.optionalFields("raw", References.TEXT_COMPONENT.in(schema), "filtered", References.TEXT_COMPONENT.in(schema)), References.TEXT_COMPONENT.in(schema)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 70 */     return components;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 75 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 76 */     schema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components(schema)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V3818_3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */