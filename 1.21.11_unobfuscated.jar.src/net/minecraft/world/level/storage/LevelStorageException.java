/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class LevelStorageException extends RuntimeException {
/*    */   private final Component messageComponent;
/*    */   
/*    */   public LevelStorageException(Component message) {
/*  9 */     super(message.getString());
/* 10 */     this.messageComponent = message;
/*    */   }
/*    */   
/*    */   public Component getMessageComponent() {
/* 14 */     return this.messageComponent;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/LevelStorageException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */