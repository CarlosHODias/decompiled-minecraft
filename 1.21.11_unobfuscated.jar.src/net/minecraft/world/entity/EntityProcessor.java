/*   */ package net.minecraft.world.entity;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface EntityProcessor
/*   */ {
/*   */   static {
/* 7 */     NOP = (input -> input);
/*   */   }
/*   */   
/*   */   public static final EntityProcessor NOP;
/*   */   
/*   */   Entity process(Entity paramEntity);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EntityProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */