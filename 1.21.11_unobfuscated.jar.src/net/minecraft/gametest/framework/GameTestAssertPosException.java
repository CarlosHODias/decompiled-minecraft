/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertPosException
/*    */   extends GameTestAssertException {
/*    */   private final BlockPos absolutePos;
/*    */   private final BlockPos relativePos;
/*    */   
/*    */   public GameTestAssertPosException(Component baseMessage, BlockPos absolutePos, BlockPos relativePos, int tick) {
/* 12 */     super(baseMessage, tick);
/* 13 */     this.absolutePos = absolutePos;
/* 14 */     this.relativePos = relativePos;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 19 */     return (Component)Component.translatable("test.error.position", new Object[] { this.message, this.absolutePos.getX(), this.absolutePos.getY(), this.absolutePos.getZ(), this.relativePos.getX(), this.relativePos.getY(), this.relativePos.getZ(), this.tick });
/*    */   }
/*    */   
/*    */   public Component getMessageToShowAtBlock() {
/* 23 */     return this.message;
/*    */   }
/*    */   
/*    */   public BlockPos getRelativePos() {
/* 27 */     return this.relativePos;
/*    */   }
/*    */   
/*    */   public BlockPos getAbsolutePos() {
/* 31 */     return this.absolutePos;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestAssertPosException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */