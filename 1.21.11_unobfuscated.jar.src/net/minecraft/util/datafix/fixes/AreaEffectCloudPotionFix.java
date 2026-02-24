/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class AreaEffectCloudPotionFix
/*    */   extends NamedEntityFix {
/*    */   public AreaEffectCloudPotionFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false, "AreaEffectCloudPotionFix", References.ENTITY, "minecraft:area_effect_cloud");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 17 */     return entity.update(DSL.remainderFinder(), this::fix);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fix(Dynamic<T> entity) {
/* 21 */     Optional<Dynamic<T>> color = entity.get("Color").result();
/* 22 */     Optional<Dynamic<T>> effects = entity.get("effects").result();
/* 23 */     Optional<Dynamic<T>> potion = entity.get("Potion").result();
/* 24 */     entity = entity.remove("Color").remove("effects").remove("Potion");
/*    */     
/* 26 */     if (color.isEmpty() && effects.isEmpty() && potion.isEmpty()) {
/* 27 */       return entity;
/*    */     }
/*    */     
/* 30 */     Dynamic<T> potionContents = entity.emptyMap();
/* 31 */     if (color.isPresent()) {
/* 32 */       potionContents = potionContents.set("custom_color", color.get());
/*    */     }
/* 34 */     if (effects.isPresent()) {
/* 35 */       potionContents = potionContents.set("custom_effects", effects.get());
/*    */     }
/* 37 */     if (potion.isPresent()) {
/* 38 */       potionContents = potionContents.set("potion", potion.get());
/*    */     }
/*    */     
/* 41 */     return entity.set("potion_contents", potionContents);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/AreaEffectCloudPotionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */