/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ public class LoadingDotsText {
/*  4 */   private static final String[] FRAMES = new String[] { "O o o", "o O o", "o o O", "o O o" };
/*    */ 
/*    */ 
/*    */   
/*    */   private static final long INTERVAL_MS = 300L;
/*    */ 
/*    */ 
/*    */   
/*    */   public static String get(long timeMs) {
/* 13 */     int index = (int)(timeMs / 300L % FRAMES.length);
/* 14 */     return FRAMES[index];
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/LoadingDotsText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */