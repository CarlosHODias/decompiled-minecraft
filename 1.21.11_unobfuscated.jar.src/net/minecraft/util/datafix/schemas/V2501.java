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
/*    */ public class V2501
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V2501(int versionKey, Schema parent) {
/* 20 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   private static void registerFurnace(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/* 24 */     schema.register(map, name, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "CustomName", References.TEXT_COMPONENT.in(schema), "RecipesUsed", DSL.compoundList(References.RECIPE.in(schema), DSL.constType(DSL.intType()))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 33 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 34 */     registerFurnace(schema, map, "minecraft:furnace");
/* 35 */     registerFurnace(schema, map, "minecraft:smoker");
/* 36 */     registerFurnace(schema, map, "minecraft:blast_furnace");
/* 37 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V2501.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */