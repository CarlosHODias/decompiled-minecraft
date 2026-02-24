/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class FilteredBooksFix extends ItemStackTagFix {
/*    */   public FilteredBooksFix(Schema outputSchema) {
/*  9 */     super(outputSchema, "Remove filtered text from books", id -> (id.equals("minecraft:writable_book") || id.equals("minecraft:written_book")));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> tag) {
/* 14 */     return Util.writeAndReadTypedOrThrow(tag, tag.getType(), dynamic -> dynamic.remove("filtered_title").remove("filtered_pages"));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/FilteredBooksFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */