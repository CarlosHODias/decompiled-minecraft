/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FilteredSignsFix extends NamedEntityWriteReadFix {
/*    */   public FilteredSignsFix(Schema outputSchema) {
/*  8 */     super(outputSchema, false, "Remove filtered text from signs", References.BLOCK_ENTITY, "minecraft:sign");
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 13 */     return input.remove("FilteredText1").remove("FilteredText2").remove("FilteredText3").remove("FilteredText4");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/FilteredSignsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */