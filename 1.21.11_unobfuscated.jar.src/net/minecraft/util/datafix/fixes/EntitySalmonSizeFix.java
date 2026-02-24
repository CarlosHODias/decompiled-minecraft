/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntitySalmonSizeFix extends NamedEntityFix {
/*    */   public EntitySalmonSizeFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false, "EntitySalmonSizeFix", References.ENTITY, "minecraft:salmon");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), tag -> {
/*    */           String type = tag.get("type").asString("medium");
/*    */           return type.equals("large") ? tag : tag.set("type", tag.createString("medium"));
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntitySalmonSizeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */