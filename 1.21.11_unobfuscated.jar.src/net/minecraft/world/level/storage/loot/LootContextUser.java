/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ 
/*    */ public interface LootContextUser
/*    */ {
/*    */   default Set<ContextKey<?>> getReferencedContextParams() {
/*  9 */     return Set.of();
/*    */   }
/*    */   
/*    */   default void validate(ValidationContext context) {
/* 13 */     context.validateContextUsage(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/LootContextUser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */