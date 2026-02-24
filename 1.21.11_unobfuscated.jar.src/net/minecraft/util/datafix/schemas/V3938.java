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
/*    */ public class V3938
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V3938(int versionKey, Schema parent) {
/* 16 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   protected static TypeTemplate abstractArrow(Schema schema) {
/* 20 */     return DSL.optionalFields("inBlockState", 
/* 21 */         References.BLOCK_STATE.in(schema), "item", 
/* 22 */         References.ITEM_STACK.in(schema), "weapon", 
/* 23 */         References.ITEM_STACK.in(schema));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 29 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 30 */     schema.register(map, "minecraft:spectral_arrow", () -> abstractArrow(schema));
/* 31 */     schema.register(map, "minecraft:arrow", () -> abstractArrow(schema));
/* 32 */     return map;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V3938.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */