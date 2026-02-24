/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class TippedArrowPotionToItemFix
/*    */   extends NamedEntityWriteReadFix {
/*    */   public TippedArrowPotionToItemFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false, "TippedArrowPotionToItemFix", References.ENTITY, "minecraft:arrow");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 15 */     Optional<Dynamic<T>> potion = input.get("Potion").result();
/* 16 */     Optional<Dynamic<T>> customPotionEffects = input.get("custom_potion_effects").result();
/* 17 */     Optional<Dynamic<T>> color = input.get("Color").result();
/* 18 */     if (potion.isEmpty() && customPotionEffects.isEmpty() && color.isEmpty()) {
/* 19 */       return input;
/*    */     }
/*    */     
/* 22 */     return 
/* 23 */       input.remove("Potion")
/* 24 */       .remove("custom_potion_effects")
/* 25 */       .remove("Color")
/* 26 */       .update("item", itemStack -> {
/*    */           Dynamic<?> tag = itemStack.get("tag").orElseEmptyMap();
/*    */           if (potion.isPresent())
/*    */             tag = tag.set("Potion", potion.get()); 
/*    */           if (customPotionEffects.isPresent())
/*    */             tag = tag.set("custom_potion_effects", customPotionEffects.get()); 
/*    */           if (color.isPresent())
/*    */             tag = tag.set("CustomPotionColor", color.get()); 
/*    */           return itemStack.set("tag", tag);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/TippedArrowPotionToItemFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */