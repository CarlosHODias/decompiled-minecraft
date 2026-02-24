/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityShulkerRotationFix extends NamedEntityFix {
/*    */   public EntityShulkerRotationFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false, "EntityShulkerRotationFix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 16 */     List<Double> rotation = input.get("Rotation").asList(d -> d.asDouble(180.0D));
/* 17 */     if (!rotation.isEmpty()) {
/* 18 */       rotation.set(0, (Double)rotation.get(0) - 180.0D);
/* 19 */       Objects.requireNonNull(input); return input.set("Rotation", input.createList(rotation.stream().map(input::createDouble)));
/*    */     } 
/* 21 */     return input;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 26 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityShulkerRotationFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */