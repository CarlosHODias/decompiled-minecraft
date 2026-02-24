/*   */ package net.minecraft.client.gui.screens.worldselection;
/*   */ public final class DataPackReloadCookie extends Record {
/*   */   private final net.minecraft.world.level.levelgen.WorldGenSettings worldGenSettings;
/*   */   private final net.minecraft.world.level.WorldDataConfiguration dataConfiguration;
/*   */   
/* 6 */   public DataPackReloadCookie(net.minecraft.world.level.levelgen.WorldGenSettings worldGenSettings, net.minecraft.world.level.WorldDataConfiguration dataConfiguration) { this.worldGenSettings = worldGenSettings; this.dataConfiguration = dataConfiguration; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie; } public net.minecraft.world.level.levelgen.WorldGenSettings worldGenSettings() { return this.worldGenSettings; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/DataPackReloadCookie;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.WorldDataConfiguration dataConfiguration() { return this.dataConfiguration; }
/*   */ 
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/DataPackReloadCookie.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */