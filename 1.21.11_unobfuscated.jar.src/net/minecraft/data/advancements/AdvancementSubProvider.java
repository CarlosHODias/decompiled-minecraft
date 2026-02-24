/*    */ package net.minecraft.data.advancements;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface AdvancementSubProvider
/*    */ {
/*    */   void generate(HolderLookup.Provider paramProvider, Consumer<AdvancementHolder> paramConsumer);
/*    */   
/*    */   static AdvancementHolder createPlaceholder(String id) {
/* 14 */     return Advancement.Builder.advancement().build(Identifier.parse(id));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/advancements/AdvancementSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */