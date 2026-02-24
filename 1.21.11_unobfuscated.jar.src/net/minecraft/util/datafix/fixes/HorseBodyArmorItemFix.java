/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class HorseBodyArmorItemFix
/*    */   extends NamedEntityWriteReadFix {
/*    */   private final String previousBodyArmorTag;
/*    */   private final boolean clearArmorItems;
/*    */   
/*    */   public HorseBodyArmorItemFix(Schema outputSchema, String entityName, String previousBodyArmorTag, boolean clearArmorItems) {
/* 14 */     super(outputSchema, true, "Horse armor fix for " + entityName, References.ENTITY, entityName);
/* 15 */     this.previousBodyArmorTag = previousBodyArmorTag;
/* 16 */     this.clearArmorItems = clearArmorItems;
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 21 */     Optional<? extends Dynamic<?>> previousBodyArmor = input.get(this.previousBodyArmorTag).result();
/* 22 */     if (previousBodyArmor.isPresent()) {
/* 23 */       Dynamic<?> bodyArmorItem = previousBodyArmor.get();
/* 24 */       Dynamic<T> output = input.remove(this.previousBodyArmorTag);
/* 25 */       if (this.clearArmorItems) {
/* 26 */         output = output.update("ArmorItems", armorItems -> armorItems.createList(Streams.mapWithIndex(armorItems.asStream(), ())));
/*    */ 
/*    */         
/* 29 */         output = output.update("ArmorDropChances", armorDropChances -> armorDropChances.createList(Streams.mapWithIndex(armorDropChances.asStream(), ())));
/*    */       } 
/*    */ 
/*    */       
/* 33 */       output = output.set("body_armor_item", bodyArmorItem);
/* 34 */       output = output.set("body_armor_drop_chance", input.createFloat(2.0F));
/* 35 */       return output;
/*    */     } 
/* 37 */     return input;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/HorseBodyArmorItemFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */