/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1488
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1488(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 19 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 20 */     schema.register(map, "minecraft:command_block", () -> DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(schema), "LastOutput", References.TEXT_COMPONENT.in(schema)));
/*    */ 
/*    */ 
/*    */     
/* 24 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V1488.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */