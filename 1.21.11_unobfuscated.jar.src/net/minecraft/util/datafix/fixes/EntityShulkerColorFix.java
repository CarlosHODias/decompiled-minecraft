/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityShulkerColorFix extends NamedEntityFix {
/*    */   public EntityShulkerColorFix(Schema outputSchema, boolean changesType) {
/* 10 */     super(outputSchema, changesType, "EntityShulkerColorFix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     if (input.get("Color").map(Dynamic::asNumber).result().isEmpty()) {
/* 15 */       return input.set("Color", input.createByte((byte)10));
/*    */     }
/* 17 */     return input;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 22 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityShulkerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */