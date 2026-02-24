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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V2842
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2842(int versionKey, Schema parent) {
/* 25 */     super(versionKey, parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 30 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/*    */     
/* 32 */     schema.registerType(false, References.CHUNK, () -> DSL.optionalFields("entities", DSL.list(References.ENTITY_TREE.in(schema)), "block_entities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "block_ticks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "sections", DSL.list(DSL.optionalFields("biomes", DSL.optionalFields("palette", DSL.list(References.BIOME.in(schema))), "block_states", DSL.optionalFields("palette", DSL.list(References.BLOCK_STATE.in(schema))))), "structures", DSL.optionalFields("starts", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)))));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V2842.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */